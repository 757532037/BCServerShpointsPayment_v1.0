package com.shpoints.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.shpoints.enm.Mti;
import com.shpoints.exception.Simple8583Exception;
import com.shpoints.factory.IsoMsgFactory;
import com.shpoints.factory.XmlReader;
import com.shpoints.key.SimpleConstants;
import com.shpoints.model.IsoPackage;
import com.shpoints.util.ShpointsContainer;


/**
 * <p>发送客户端抽象类.</p>
 *
 */
public abstract class AbstractClient {
	
	private static Logger log = Logger.getLogger(AbstractClient.class);
    //默认18s超时
	protected int timeout=14000;
    //Ip地址
	protected String ip;
    //端口号
	protected int port;
	protected Socket socket;
    //加密串，用于MD5或者DES加密

	//设置访问的Ip地址，端口号和超时时间
	public AbstractClient(String ip, int port, int timeout) {
		this.ip = ip;
		this.port = port;
		this.timeout = timeout;
	}

	//发送接受报文的方法
	public Map<String, String> sendToBank(Map<String, String> dataMap,
			XmlReader xmlReader) throws Exception {
//		init();
		//单例
		IsoMsgFactory factory = IsoMsgFactory.getInstance();
		setDefultData(dataMap);
		String mti = dataMap.get("mti");
		IsoPackage pack = xmlReader.getIsoConfig().get(mti);
		byte[] buf = null;
		try {
			byte[] sendData = factory.pack(dataMap, pack);
			Socket socket = new Socket(this.ip, port);
				socket.setSoTimeout(timeout);
				// 发送的报文
				log.info(Mti.getMit(mti).getMsg()+"send_Msg：" + dataMap.toString());
				//发送数据
				socket.getOutputStream().write(sendData);
				socket.getOutputStream().flush();
				//两字节长度
				byte[] lenbuf = new byte[512];
				if(socket != null && socket.isConnected()){
					socket.getInputStream().read(lenbuf);
					buf = lenbuf;
				}
				
		} catch (Exception e) {
			e.printStackTrace();
			//抛出可能的异常，比如连接超时等
			throw new Simple8583Exception("网络通讯异常",e);
		}
		//将前面的MsgLength域剔除
		if(pack.getFirst().getId().equals("MsgLength")){
			pack.remove(0);
		}
		return factory.unpack(buf, pack);
	}
	
	/**通用默认*/
	public void setDefultData(Map<String, String> dataMap){
		dataMap.put("22", ShpointsContainer._22);//服务点输入模式3
		dataMap.put("25", ShpointsContainer._25);//POS代码(默认50)2
		dataMap.put("32", ShpointsContainer._32);//收单机构识别代码04012900右补充空格
		dataMap.put("42", ShpointsContainer._42);//收卡方终端标识(默认bcpos)
		if(dataMap.get("43")==null)//收卡方名称和地点(有上送没有上送空)
			dataMap.put("43",ShpointsContainer._43);//收卡方名称和地点(有上送没有上送空)
		Mti Mit = com.shpoints.enm.Mti.getMit(dataMap.get(SimpleConstants.MTI));
		dataMap.put("3", Mit.getCode_snd());
		dataMap.put("49", ShpointsContainer._49);//交易货币代码默认上送空
	}
	protected abstract int computeLength(byte[] lenBts);

}
