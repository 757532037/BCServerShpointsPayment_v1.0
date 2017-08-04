package com.shpoints.jt;
import com.shpoints.client.SimpleClient;
import com.shpoints.exception.Simple8583Exception;
import com.shpoints.factory.XmlReader;
import com.shpoints.key.SimpleConstants;
import com.shpoints.util.DateUtil;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * size+message type +bitmap +报文请求数据（域字段）
 * <p>测试类.</p>
 *
 */
public class Simple8583Test{

//    public static void main(String[] args) throws Exception {
//        Map<String,String> requestMap = new HashMap<String,String>();
//        requestMap.put(SimpleConstants.MTI,"0430");
//        requestMap.put("15","SP99515061500104387");
//        requestMap.put("13","张清源");
//        requestMap.put("14","156");
//        requestMap.put("12","pay");
//        requestMap.put("3","12M01041");
//        requestMap.put("2","470000");
//        requestMap.put("7","492500");
//        requestMap.put("6","6228210250013865710");
//        requestMap.put("5","04");
//        requestMap.put("4","01041");
//        requestMap.put("9","012");
//        requestMap.put("8","SP99515061500104387");
//        String ip = "localhost";
//        int port = 8999;
//        int timeout = 15000;//15s超时
//
//        String macKey = "helloSimple8583";
//        SimpleClient simpleClient = new SimpleClient(ip,port,timeout);
//        simpleClient.setMacKey(macKey);
//        XmlReader xmlReader = new XmlReader("simple8583.xml");
//        Map<String,String> resultMap = simpleClient.sendToBank(requestMap,xmlReader);
//        System.out.println(resultMap);
//    }
//    public static void main(String[] args) throws Exception {
//    	Map<String,String> requestMap = new HashMap<String,String>();
//    	requestMap.put(SimpleConstants.MTI,"0430");
//    	
//    	requestMap.put("1","0000");
//    	requestMap.put("2","470000");
//    	requestMap.put("3","12M01041");
//    	requestMap.put("4","010410104101041");
//    	requestMap.put("5","03");
//    	requestMap.put("6","6228210250013865710");
//    	requestMap.put("7","4925004925000");
//    	requestMap.put("8","SP995150615001043870");
//    	requestMap.put("9","012");
//    	requestMap.put("10","SP995150615001043870");
//    	requestMap.put("11","470000");
//    	requestMap.put("12","4700000012");
//    	requestMap.put("13","张清源");
//    	requestMap.put("14","156");
//    	requestMap.put("15","SP99515061500104387");
//    	requestMap.put("16","SP9951506150010438721");
//    	requestMap.put("17","SP995150615001");
//    	requestMap.put("18","SP995150615001SP99515061500132");
//    	String ip = "218.106.61.146";
//    	int port = 19945;
//    	int timeout = 15000;//15s超时
//    	
//    	String macKey = "helloSimple8583";
//    	SimpleClient simpleClient = new SimpleClient(ip,port,timeout);
//    	simpleClient.setMacKey(macKey);
//    	XmlReader xmlReader = new XmlReader("simple8583.xml");
//    	Map<String,String> resultMap = simpleClient.sendToBank(requestMap,xmlReader);
//    	System.out.println(resultMap);
//    }

    public static void main(String[] args) throws Exception {
    	Map<String,String> requestMap = new HashMap<String,String>();
    	requestMap.put(SimpleConstants.MTI,"0320");
    	System.out.println("30333330f23a04810a81800000000000000000003136343032363734303534343336353739373339303030303030303030303030303030303230313630333331313230343135313331353137353730323231303232313031323530303034323043313230343135313339393132333435363738303030202020".length());
    	requestMap.put("2","6259532009180693");
    	requestMap.put("3","390000");
    	requestMap.put("4","000000000000");
    	System.out.println(DateUtil.getDateyyyyMMddHH());//25(50)-32(04012900)-42(bcpos)-49()
    	requestMap.put("7",DateUtil.getDateyyyyMMddHH());//交易日期和时间10
    	requestMap.put("11","041513");//系统跟踪审计号(当天唯一流水号)--6
    	requestMap.put("12",DateUtil.getDateHHmmss());//当地交易时间6
    	requestMap.put("13",DateUtil.getDateMMDD());//当地交易日期4
    	requestMap.put("15",DateUtil.getDateMMDD());//清算日期4
    	requestMap.put("22","012");//服务点输入模式3
    	requestMap.put("25","50");//POS代码(默认50)2
    	requestMap.put("32","04012900");//收单机构识别代码04012900右补充空格
    	requestMap.put("37","420C12041513");//调单参考号(交易唯一)默认和交易时间保持一致
    	requestMap.put("41","12345678");//终端标识（pos后8位）
    	requestMap.put("42","bcpos");//收卡方终端标识(默认bcpos)
    	requestMap.put("43","INTERNET");//收卡方名称和地点(有上送没有上送空)
    	requestMap.put("49","");//交易货币代码默认上送空
    	String ip = "218.106.61.146";//
    	int port = 19945;
    	int timeout = 15000;//15s超时
    	
    	SimpleClient simpleClient = new SimpleClient(ip,port,timeout);
//    	simpleClient.setMacKey(macKey);
    	XmlReader xmlReader = new XmlReader("simple8583.xml");
    	try {
			Map<String,String> resultMap = simpleClient.sendToBank(requestMap,xmlReader);
			for(int i=0;i<5;i++){
				System.out.println(resultMap.get("48").substring(i*8, (i+1)*8));
			}
			System.out.println("resultMap"+resultMap.toString());
		} catch (Exception e) {
			throw new Simple8583Exception("交易异常进入冲正", e);
		}
    }
	

}
