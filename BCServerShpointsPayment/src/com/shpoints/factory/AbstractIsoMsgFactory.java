package com.shpoints.factory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shpoints.key.SimpleConstants;
import com.shpoints.model.BitMap;
import com.shpoints.model.IsoField;
import com.shpoints.model.IsoPackage;
import com.shpoints.util.EncodeUtil;
import com.shpoints.util.SimpleUtil;


/**
 * <p>报文组装抽象类.</p>
 *
 * @author Magic Joey
 * @version AbstractIsoMsgFactory.java 1.0 Created@2014-07-10 10:43 $
 */
public abstract class AbstractIsoMsgFactory {

	protected String macKey="5883F8DA898A31495DF792A8DA15E013";

	protected AbstractIsoMsgFactory() {

	}
	
	/** 入口和出口*/
	public byte[] pack(Map<String, String> dataMap,final IsoPackage pack)
            throws IOException, ClassNotFoundException {
        //深度拷贝，对拷贝后的对象进行操作，
		IsoPackage packClone = pack.deepClone();

		List<Integer> dataFieldList = new ArrayList<Integer>(dataMap.size());
		for (String key : dataMap.keySet()) {
			IsoField field = packClone.getIsoField(key);
			if (field == null) {
				continue;
			}
			field.setValue(dataMap.get(key));
			//数据域
			if (SimpleUtil.isNumeric(key)) {
				int val = Integer.valueOf(key);
				if(packClone.isBit64()&&val>64){
                    //设置位非64位图模式，即128模式
                    packClone.setBit64(false);
                    //将bitMap第一位置为1，表示这个数据域为128位长
					dataFieldList.add(1);
				}
				dataFieldList.add(val);
			}
		}
		// 根据参数判断BitMap长度
		BitMap bitMap = null;
		if(packClone.isBit64()){
            bitMap = new BitMap(64);
		}else{
            bitMap = new BitMap(128);
		}
		byte[] bitMapByte = bitMap.addBits(dataFieldList);//生成BitMap
		//设置BitMap的值位图
		packClone.getIsoField(SimpleConstants.BIT_MAP).setByteValue(bitMapByte);
		//将数组合并
		return merge(packClone);
	}

    /**
     * 将返回信息拆成Map返回
     * @param bts
     * @param pack
     * @return
     * @throws Exception
     */
	public Map<String, String> unpack(byte[] bts,final IsoPackage pack)
			throws Exception {
		if (pack == null || pack.size() == 0) {
			throw new IllegalArgumentException("配置为空，请检查IsoPackage是否为空");
		}
		Map<String, String> returnMap = new HashMap<String, String>();
		// 起判断的作用
		int offset = 0;
        //深度拷贝
		IsoPackage target =  pack.deepClone();
		// 获取到bitMap
		boolean hasBitMap = false;
		BitMap bitMap = null;
		for (IsoField field : target) {//对域进行遍历
			if (field.isAppData()) {//该域是否为1~64/1~128的数据域
				if (hasBitMap) {
					int index = Integer.valueOf(field.getId());
					if(index==1){
						continue;//第一位不处理，只是标志位
					}
					if (bitMap.getBit(index - 1) == 1) {//判断该位域是否有使用
						offset += subByte(bts, offset, field,pack.getMti());//offset为当前遍历的位置
						returnMap.put(field.getId(), field.getValue());
					}
				}
			} else {
				offset += subByte(bts, offset, field,pack.getMti());//offset为当前遍历的位置
				returnMap.put(field.getId(), field.getValue());
				if (field.getId().equalsIgnoreCase(SimpleConstants.BIT_MAP)) {
					hasBitMap = true;
					bitMap = BitMap.addBits(field.getByteValue());
				}
			}
		}
		return returnMap;
	}

	/**接收byte转Strnigvalue*/
	private int subByte(byte[] bts, int offset, IsoField field,String mit)
			throws UnsupportedEncodingException {
		byte[] val = null;
		int length = field.getLength();
		switch (field.getIsoType()) {
		case NUMERIC:
		case CHAR:
		case BINARY:
			val = new byte[field.getLength()];
			System.arraycopy(bts, offset, val, 0, length);
			break;
		case LLVAR_NUMERIC:
			byte[] llvarNumLen = new byte[1];
			llvarNumLen[0] = bts[offset];
			// 除以2的原因是LLVAR_NUMERIC前面的报文域长度是数字长度而非字节长度
			int firstNumLen = Integer.valueOf(EncodeUtil.hex(llvarNumLen)) / 2;
			val = new byte[firstNumLen];
			System.arraycopy(bts, offset + 1, val, 0, firstNumLen);
			length = 1 + firstNumLen;
			break;
		case LLVAR:
			if(field.getId().equals("2")||field.getId().equals("32")||field.getId().equals("33")){
				byte[] LLlenth = new byte[2];
				LLlenth[0] = bts[offset];
				LLlenth[1] = bts[offset+1];
				int firstLen = Integer.valueOf(new String(LLlenth));
				val = new byte[firstLen];
				System.arraycopy(bts, offset+2, val, 0, firstLen);
				length = 2+firstLen;
			}
			break;
		case LLLVAR:
				byte[] LLlenth = new byte[3];
				LLlenth[0] = bts[offset];
				LLlenth[1] = bts[offset+1];
				LLlenth[2] = bts[offset+2];
				int firstLen = Integer.valueOf(new String(LLlenth));
				val = new byte[firstLen];
				System.arraycopy(bts, offset+3, val, 0, firstLen);
				length = 3+firstLen;
//			}
			break;
		default:
			break;
		}
		field.setByteValue(val);//发送拼报
		return length;
	}
	

	/** Byte数组的合并，不同byte数组域将被整合为一个大的byte数组*/
	private byte[] merge(IsoPackage isoPackage) throws IOException {
		ByteArrayOutputStream byteOutPut = new ByteArrayOutputStream(100);
		for (IsoField field : isoPackage) {
			if (field.isChecked()) {
				switch (field.getIsoType()) {
				case LLVAR_NUMERIC:
					byte[] lengthByte0 = new byte[1];
					lengthByte0 = EncodeUtil.bcd(field.getValue().length(), 1);
					byteOutPut.write(lengthByte0);
					break;
				case LLVAR://加可变长度
					if((""+field.getByteValue().length).length()==1){//个位
						byteOutPut.write(("0"+field.getByteValue().length).getBytes());
					}else if((""+field.getByteValue().length).length()==2){
						byteOutPut.write((""+field.getByteValue().length).getBytes());
					}
					break;
				case LLLVAR:
					if((""+field.getByteValue().length).length()==1){//个位
						byteOutPut.write(("00"+field.getByteValue().length).getBytes());
					}else if((""+field.getByteValue().length).length()==2){//十位{
						byteOutPut.write(("0"+field.getByteValue().length).getBytes());
					}else if((""+field.getByteValue().length).length()==3){//百位{
						byteOutPut.write((""+field.getByteValue().length).getBytes());
					}
					break;
				default:
					break;
				}

				byteOutPut.write(field.getByteValue());
			}
		}
		byte[] beforeSend = byteOutPut.toByteArray();
		byte[] bts = new byte[beforeSend.length + 2];//加上size位
		byte[] lenArr = msgLength(beforeSend.length);
		System.arraycopy(lenArr, 0, bts, 0, 2);
		System.arraycopy(beforeSend, 0, bts, 2, beforeSend.length);//从第二位开始拷贝beforeSend拷贝beforeSend.length个长度
		
		return beforeSend;//不带size
	}
	
	//生成前两个字节的长度位
	/**根据约定不同需要对此方法进行重写-将10进制转16*/
	protected abstract byte[] msgLength(int length);
	
	//生成最后一位的MAC加密
	protected abstract byte[] mac(IsoPackage isoPackage) throws Exception;
	
	//对返回的数据进行MAC校验
	protected abstract void macValidate(IsoPackage isoPackage,Map<String,String> map);

    public void setMacKey(String macKey) {
        this.macKey = macKey;
    }
}
