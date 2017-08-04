package com.shpoints.jt;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.shpoints.client.SimpleClient;
import com.shpoints.exception.Simple8583Exception;
import com.shpoints.factory.XmlReader;
import com.shpoints.key.SimpleConstants;
import com.shpoints.util.DateUtil;


public class JT {

	String ip = "218.106.61.146";//
	int port = 19945;
	int timeout = 15000;//15s超时
	/**
	 * test积分余额查询
	 * @param args
	 * @throws Exception
	 * 
	 */
	
	public static void main(String[] args) throws Exception {
		new JT().test2();
	}
	@Test
    public void test1() throws Exception {
		int[] result0320_48 = new int[]{8,8,8,8,8};
    	Map<String,String> requestMap = new HashMap<String,String>();
    	requestMap.put(SimpleConstants.MTI,"0320");
    	System.out.println("30333330f23a04810a81800000000000000000003136343032363734303534343336353739373339303030303030303030303030303030303230313630333331313230343135313331353137353730323231303232313031323530303034323043313230343135313339393132333435363738303030202020".length());
    	requestMap.put("2","6250996650431128");
    	requestMap.put("3","390000");
    	requestMap.put("4","000000000000");//25(50)-32(04012900)-42(bcpos)-49()
    	requestMap.put("7",DateUtil.getDateyyyyMMddHH());//交易日期和时间10
    	requestMap.put("11",DateUtil.getDateHHmmss());//系统跟踪审计号(当天唯一流水号)--6--前端获取
    	requestMap.put("12",DateUtil.getDateHHmmss());//当地交易时间6
    	requestMap.put("13",DateUtil.getDateMMDD());//当地交易日期4
    	requestMap.put("15",DateUtil.getDateMMDD());//清算日期4
//    	requestMap.put("22","012");//服务点输入模式3
//    	requestMap.put("25","50");//POS代码(默认50)2
//    	requestMap.put("32","04012900");//收单机构识别代码04012900右补充空格
    	requestMap.put("37",DateUtil.getDateMddHHmmssSS());//调单参考号(交易唯一)
    	requestMap.put("41","12345678");//终端标识（pos后8位）--前端获取
//    	requestMap.put("42","bcpos");//收卡方终端标识(默认bcpos)
//    	requestMap.put("43","");//收卡方名称和地点(有上送没有上送空)--前端获取
//    	requestMap.put("49","");//交易货币代码默认上送空
    	
    	SimpleClient simpleClient = new SimpleClient(ip,port,timeout);
    	XmlReader xmlReader = new XmlReader("simple8583.xml");
    	try {
			Map<String,String> resultMap = simpleClient.sendToBank(requestMap,xmlReader);
			int thisIndex = 0;
			for(int i=0;i<result0320_48.length;i++){
//				System.out.println(resultMap.get("48").substring(thisIndex, thisIndex+=result0320_48[i]));
				resultMap.put("48."+(i+1), resultMap.get("48").substring(thisIndex, thisIndex+=result0320_48[i]));
			}
			System.out.println("resultMap"+resultMap.toString());
		} catch (Exception e) {
			throw new Simple8583Exception("交易异常进入冲正", e);
		}
    }

	/***
	 * test积分兑奖
	 * @throws IOException 
	 * @throws JAXBException 
	 */
	@Test
	public void test2() throws JAXBException, IOException{
		int[] result0240_48 = new int[]{8,8,3,1,8,9,12};
		Map<String,String> requestMap = new HashMap<String,String>();
    	requestMap.put(SimpleConstants.MTI,"0240");
    	requestMap.put("2","6223001420816726");
    	requestMap.put("3","390000");
    	requestMap.put("7",DateUtil.getDateyyyyMMddHH());//交易日期和时间10
    	requestMap.put("11",DateUtil.getDateHHmmss());//系统跟踪审计号(当天唯一流水号)--6
    	requestMap.put("12",DateUtil.getDateHHmmss());//当地交易时间6
    	requestMap.put("13",DateUtil.getDateMMDD());//当地交易日期4
    	requestMap.put("15",DateUtil.getDateMMDD());//清算日期4
    	requestMap.put("37",DateUtil.getDateMddHHmmssSS());//调单参考号12默认空
    	requestMap.put("41","12345678");//终端标识（pos后8位）
    	requestMap.put("43","");//收卡方名称和地点(有上送没有上送空)
    	String t200 = "                                                                                                                                                                                                        ";
    	//000001    003001（X物品N） 090001（兑换金额B）
    	requestMap.put("4","000000000000");//25(50)-32(04012900)-42(bcpos)-49()-----根据交易类型
    	requestMap.put("48", "000001  "+"BQ1001  "+"2  N"+t200);//goodsNO+proNo+num+type+temp
    	SimpleClient simpleClient = new SimpleClient(ip,port,timeout);
    	XmlReader xmlReader = new XmlReader("simple8583.xml");
    	try {
			Map<String,String> resultMap = simpleClient.sendToBank(requestMap,xmlReader);
			int thisIndex = 0;
			for(int i=0;i<result0240_48.length;i++){
//				System.out.println(resultMap.get("48").substring(thisIndex, thisIndex+=result0240_48[i]));
				resultMap.put("48."+(i+1), resultMap.get("48").substring(thisIndex, thisIndex+=result0240_48[i]));
			}
			System.out.println("resultMap"+resultMap.toString());
		} catch (Exception e) {
			throw new Simple8583Exception("交易异常进入冲正", e);
		}
	}
	
	/***
	 * test积分退货
	 * @throws IOException 
	 * @throws JAXBException 
	 */
	@Test
	public void test3() throws JAXBException, IOException{
		Map<String,String> requestMap = new HashMap<String,String>();
    	requestMap.put(SimpleConstants.MTI,"0340");
    	requestMap.put("2","6259532009180693");
    	requestMap.put("7",DateUtil.getDateyyyyMMddHH());//交易日期和时间10
    	requestMap.put("11",DateUtil.getDateHHmmss());//系统跟踪审计号(当天唯一流水号)--6
    	requestMap.put("12",DateUtil.getDateHHmmss());//当地交易时间6
    	requestMap.put("13",DateUtil.getDateMMDD());//当地交易日期4
    	requestMap.put("15",DateUtil.getDateMMDD());//清算日期4
    	requestMap.put("37",DateUtil.getDateMddHHmmssSS());//调单参考号12默认空
    	requestMap.put("41","12345678");//终端标识（pos后8位）
    	requestMap.put("43","");//收卡方名称和地点(有上送没有上送空)
    	requestMap.put("4","000000000000");//25(50)-32(04012900)-42(bcpos)-49()
    	requestMap.put("48", "231205-00001");//兑换参考号码()--10-11-12-16
    	SimpleClient simpleClient = new SimpleClient(ip,port,timeout);
    	XmlReader xmlReader = new XmlReader("simple8583.xml");
    	try {
			Map<String,String> resultMap = simpleClient.sendToBank(requestMap,xmlReader);
			System.out.println("resultMap"+resultMap.toString());
		} catch (Exception e) {
			throw new Simple8583Exception("交易异常进入冲正", e);
		}
    	
	}
	
	/***
	 * test积分兑奖撤销
	 * @throws IOException 
	 * @throws JAXBException 
	 */
	@Test
	public void test4() throws JAXBException, IOException{
		Map<String,String> requestMap = new HashMap<String,String>();
    	requestMap.put(SimpleConstants.MTI,"0260");
    	requestMap.put("2","6250996650431128");
    	requestMap.put("7",DateUtil.getDateyyyyMMddHH());//交易日期和时间10
    	requestMap.put("11",DateUtil.getDateHHmmss());//系统跟踪审计号(当天唯一流水号)--6
    	requestMap.put("12",DateUtil.getDateHHmmss());//当地交易时间6
    	requestMap.put("13",DateUtil.getDateMMDD());//当地交易日期4
    	requestMap.put("15",DateUtil.getDateMMDD());//清算日期4
    	requestMap.put("37",DateUtil.getDateMddHHmmssSS());//调单参考号12默认空
    	requestMap.put("41","12345678");//终端标识（pos后8位）
    	requestMap.put("43","");//收卡方名称和地点(有上送没有上送空)
    	requestMap.put("4","000000000000");//25(50)-32(04012900)-42(bcpos)-49()
    	requestMap.put("48", "231205-00004");//兑换参考号码()--10-11-12
    	SimpleClient simpleClient = new SimpleClient(ip,port,timeout);
    	XmlReader xmlReader = new XmlReader("simple8583.xml");
    	try {
			Map<String,String> resultMap = simpleClient.sendToBank(requestMap,xmlReader);
			System.out.println("resultMap"+resultMap.toString());
		} catch (Exception e) {
			throw new Simple8583Exception("交易异常进入冲正", e);
		}
    	
	}
	
	/***
	 * test积分撤消冲正
	 * @throws Exception 
	 */
	@Test
	public void test5() throws Exception{
		Map<String,String> requestMap = new HashMap<String,String>();
		requestMap.put(SimpleConstants.MTI,"0300");
		requestMap.put("2","6250996650431128");
		requestMap.put("7",DateUtil.getDateyyyyMMddHH());//交易日期和时间10
		requestMap.put("11",DateUtil.getDateHHmmss());//系统跟踪审计号(当天唯一流水号)--6
		requestMap.put("12",DateUtil.getDateHHmmss());//当地交易时间6
		requestMap.put("13",DateUtil.getDateMMDD());//当地交易日期4
		requestMap.put("15",DateUtil.getDateMMDD());//清算日期4
		requestMap.put("37",DateUtil.getDateMddHHmmssSS());//调单参考号12默认空
		requestMap.put("41","12345678");//终端标识（pos后8位）
		requestMap.put("43","");//收卡方名称和地点(有上送没有上送空)
		requestMap.put("4","000000000000");//25(50)-32(04012900)-42(bcpos)-49()
		requestMap.put("48", "231205-00004");//兑换参考号码()--10-11-12
		requestMap.put("90", "0260"+"231205-00004"+"0407180553"+""+"04012900"+"04012900");//Original Message Type 4  	原始消息类别
		//Original System Trace Number 6   	原始系统跟踪号
		//Original Transaction Date – Time 10  	原始交易日期-时间
		//Original Acquire Institute ID Code 11  	原始收单机构ID代码
		//Original Forwarding Institute ID Code 11  	原始转发机构ID代码
		SimpleClient simpleClient = new SimpleClient(ip,port,timeout);
		XmlReader xmlReader = new XmlReader("simple8583.xml");
		Map<String,String> resultMap = simpleClient.sendToBank(requestMap,xmlReader);
		System.out.println("resultMap"+resultMap.toString());
    	
	}
	/***
	 * test积分兑奖冲正
	 * @throws Exception 
	 */
	@Test
	public void test6() throws Exception{
    	Map<String,String> requestMap = new HashMap<String,String>();
		requestMap.put(SimpleConstants.MTI,"0280");
		requestMap.put("2","6250996650431128");
		requestMap.put("7",DateUtil.getDateyyyyMMddHH());//交易日期和时间10
		requestMap.put("11",DateUtil.getDateHHmmss());//系统跟踪审计号(当天唯一流水号)--6
		requestMap.put("12",DateUtil.getDateHHmmss());//当地交易时间6
		requestMap.put("13",DateUtil.getDateMMDD());//当地交易日期4
		requestMap.put("15",DateUtil.getDateMMDD());//清算日期4
		requestMap.put("37",DateUtil.getDateMddHHmmssSS());//调单参考号12默认空
		requestMap.put("41","12345678");//终端标识（pos后8位）
		requestMap.put("43","");//收卡方名称和地点(有上送没有上送空)
		requestMap.put("4","000000000000");//25(50)-32(04012900)-42(bcpos)-49()
		requestMap.put("48", "231205-00002");//兑换参考号码()--10-11-12
		requestMap.put("90", "0240"+"231205-00002"+"0407171036"+""+"04012900"+"04012900");//Original Message Type 4  	原始消息类别
		//Original System Trace Number 6   	原始系统跟踪号
		//Original Transaction Date – Time 10  	原始交易日期-时间
		//Original Acquire Institute ID Code 11  	原始收单机构ID代码
		//Original Forwarding Institute ID Code 11  	原始转发机构ID代码
		SimpleClient simpleClient = new SimpleClient(ip,port,timeout);
		XmlReader xmlReader = new XmlReader("simple8583.xml");
		Map<String,String> resultMap = simpleClient.sendToBank(requestMap,xmlReader);
		System.out.println("resultMap"+resultMap.toString());
		
	}
	
	
}
