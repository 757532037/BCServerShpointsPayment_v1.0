package bc.core.server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import bc.core.utils.Constant;

public class BCMessageUtil {

	private static final Logger log = Logger.getLogger(BCMessageUtil.class);

	String createField(String name, String value) {
		return "|" + name + "=" + value;
	}

	String createOtherField(String name, String value) {
		return name + "=" + value;
	}

	public static String GetValue(String message, String tag) {
		int start = -1;
		int end = -1;

		try {
			if (0 == message.length()) {
				log.error("Message not defined, from fixengin is empty.");
				return "";
			}
			if (0 == tag.length()) {
				log.error("Tag not defined");
				return "";
			} else
				tag = "|" + tag + "=";
			start = message.indexOf(tag);
			if (-1 == start) {
				return "";
			}
			start += tag.length();
			end = message.indexOf("|", start);
			if (-1 == end) {
				log.error("Badly formated message encountered!");
				return "";
			}
		} catch (Exception e) {
			log.error(e, e);
			return "";
		}

		return message.substring(start, end).trim();
	}

	/**
	 * 得到当前的时间，时间格式yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public static String getCurrentDatetime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}
	
	public static String[] getItemList(String message, String delims) {
		String[] tokens;
		try {
			//String delims = "SITEM=";
			tokens = message.split(delims);
		} catch (Exception e) {
			log.error(e, e);
			return null;
		}

		return tokens;
	}

	String formatDate(Calendar cal) {

		String strdate = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		if (cal != null) {
			strdate = sdf.format(cal.getTime());
		}

		return strdate;

	}

	String getCurrentDate() {
		String s;
		DateFormat formatter;
		Date date = new Date();

		formatter = new SimpleDateFormat("yyyyMMdd");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		s = formatter.format(date);
		return s;
	}

	String getCurrentLocalDate() {
		String s;
		DateFormat formatter;
		Date date = new Date();

		formatter = new SimpleDateFormat("yyyyMMdd");
		s = formatter.format(date);
		return s;
	}

	String getCurrentTime() {

		String s;
		DateFormat formatter;
		Date date = new Date();

		// Time formate 14.36.33
		formatter = new SimpleDateFormat("HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		s = formatter.format(date);
		return s;
	}

	String getCurrentLocalTime() {

		String s;
		DateFormat formatter;
		Date date = new Date();

		// Time formate 14.36.33
		formatter = new SimpleDateFormat("HH:mm:ss");
		s = formatter.format(date);
		return s;
	}

	String getCurrentLocalTime2() {

		String s;
		DateFormat formatter;
		Date date = new Date();

		// Time formate 14.36.33
		formatter = new SimpleDateFormat("HHmmss");
		s = formatter.format(date);
		return s;
	}

	public String createNACKMessage(String error) {
		/*
		 * "|SOH=|VERS=1.00|MTYP=TRDENTRY|DATE=19960903|TIME=16:20:03|DLR=DLRX|
		 * "USER=DLRXTE|PGRP=TRSY|TRDTYP=OUTRIGHT|TRDDT=19960903|DLRTNUM=1|EOH=|"
		 */
		StringBuilder str = new StringBuilder();

		str.append("|SOH=");
		str.append(createField("VERS", "1.00"));
		str.append(createField("MTYPE", "NACK"));
		str.append(createField("DATE", getCurrentDate()));
		str.append(createField("TIME", getCurrentTime()));

		str.append(createField("DLVRES", error));

		str.append("|EOH=|EOM=");

		return str.toString();
	}

	String createHeader(String mtyp, String otherHeaderFields) {

		StringBuilder str = new StringBuilder();

		str.append("|SOH=");
		str.append(createField("VERS", "1.00"));
		str.append(createField("MTYPE", mtyp));

		str.append(otherHeaderFields);
		str.append("|EOH=");

		return str.toString();
	}
	public String createCFRESP_BC(String posMsg){
		HashMap<String,String> msgMap = BCMessageUtil.parseBcpayMsg2Map(posMsg);
		String otherHeaderFields = createField(
				Constant.TRMID,msgMap.get(Constant.TRMID))
				+ createField(Constant.STOREID, msgMap.get(Constant.STOREID))
				+ createField(Constant.CUSTID, msgMap.get(Constant.CUSTID))
				+ createField(Constant.POSID,msgMap.get(Constant.POSID))
				+ createField(Constant.POSTIME, msgMap.get(Constant.POSTIME))
				+ createField(Constant.MERID, msgMap.get(Constant.MERID))
				+ createField(Constant.MERNAME, msgMap.get(Constant.MERNAME));
		StringBuilder sb = new StringBuilder();
		sb.append(createHeader("CARDINFORESP", otherHeaderFields));

		// hard code thess two values now
		sb.append(createField(Constant.SEQ, msgMap.get(Constant.SEQ)));
		sb.append(createField(Constant.TIMEOUT, "2"));

		sb.append(createField(Constant.CONFIRMTYPE, ""));
		sb.append(createField(Constant.INFO, ""));
//		List<com.citiccard.xml.confirminfo.Message.Info> L = xml.getInfo();
//		com.citiccard.xml.confirminfo.Message.Info info = L.get(0);
		sb.append(createField(Constant.POINTAMT, "0"));
//		sb.append(createField(Constant.CHANGFLAG, ""));
//		sb.append(createField(Constant.COUNTNUM, ""));
//		sb.append(createField(Constant.OVERDATE, ""));
//
//		sb.append(createField(Constant.TRANSDATE, xml.getTransDate()));
//		sb.append(createField(Constant.TRANSTIME, xml.getTransTime()));
		sb.append(createField(Constant.RETCODE, "0000000"));
		sb.append(createField(Constant.RETCOMMENT, "交易成功"));
		sb.append(createField(Constant.BANKNAME, msgMap.get(Constant.BANKNAME)));
		sb.append(createField(Constant.EXCHANGERATE, "0"));

		sb.append("|EOM=|");
		
		return sb.toString();
	}

	public static int GetInt(String msg, String tag) {
		// TODO Auto-generated method stub
		int ret = -9999;
		try {
			String val = GetValue(msg, tag);
			ret = Integer.parseInt(val);
		} catch ( Exception e){
			log.error(e,e);
		}
		return ret ;
	}
	
	 public static final int MIN_LENGTH = 24;
	  /** The random number generator. */
	
	  protected static char[] goodChar = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
	      'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
	      'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
	      'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
	      '2', '3', '4', '5', '6', '7', '8', '9', '+', '-', '@', };

	  /* Generate a Password object with a random password. */
	  public static String getNewPassword() {
	    StringBuffer sb = new StringBuffer();
	    java.util.Random r = new java.util.Random();

	    for (int i = 0; i < MIN_LENGTH; i++) {
	      sb.append(goodChar[r.nextInt(goodChar.length)]);
	    }
	    return sb.toString();
	  }
	  
	  
	  public static String enCode(String stringToEncrypt, String encryptionKey)
		{
			try {
				//String encryptionKey = "123456789012345678901234567890";
				//String encryptionScheme = StringEncrypter.DESEDE_ENCRYPTION_SCHEME;
				
				//String stringToEncrypt = "test123";
				//String encryptionKey = "test1234test1234test1234";
				
				String encryptionScheme = StringEncrypter.DESEDE_ENCRYPTION_SCHEME;
		
				StringEncrypter encrypter = new StringEncrypter( encryptionScheme, encryptionKey );
				String encryptedString = encrypter.encrypt( stringToEncrypt );
		
				return encryptedString;
				
			} catch ( Exception e){
				log.info(e,e);
				return "";
			}
		}
		
		public static String deCode(String encryptedString, String encryptionKey)
		{
			try {
				
				String encryptionScheme = StringEncrypter.DESEDE_ENCRYPTION_SCHEME;
		
				StringEncrypter encrypter = new StringEncrypter( encryptionScheme, encryptionKey );
				String stringToEncrypt = encrypter.decrypt(encryptedString);
		
				return stringToEncrypt;
				
			} catch ( Exception e){
				log.info(e,e);
				return "";
			}
		}

		public static double getDouble(String msg, String tag) {
			double ret = 0;
			try {
				String val = GetValue(msg, tag);
				ret = Double.parseDouble(val);
			} catch ( Exception e){
				log.error(e,e);
			}
			return ret ;
		}


		/**
		 * add the message tag,if the tag is exist,then replace the oldData with newData. 
		 * @param message
		 * @param tag
		 * @param newData
		 * @return
		 */
		public static String addMsgTag(String message,String tag, String newData){

			int start = -1;
			int end = -1;

			try {
				tag = "|" + tag + "=";
				start = message.indexOf(tag);
				if (-1 == start) {
					message = message.replace("|EOM=|", "");
					message = message + tag + newData;
					message = message + "|EOM=|";
				}else{
					start += tag.length();
					end = message.indexOf("|", start);
					String data = message.substring(start, end).trim();
					message = message.replace(tag+data, tag + newData);
					//message = message + tag + newData;	
					//log.info("oldData="+tag+data+" newData="+ tag + newData);
				}
				
			} catch (Exception e) {
				log.error(e,e);
				return "";
			}
			return message;
		}

		/*
		 * 将bcpay报文解成map形式 key-value
		 */
		public static HashMap<String,String> parseBcpayMsg2Map(String msg){
			HashMap<String,String> returnMap = new HashMap<String,String>();
			StringTokenizer msgStoken = new StringTokenizer(msg,"|");
			String fieldArray[] = null;
			String str;
			boolean flag = false;
			String key = null;
			String value = null;
			while(msgStoken.hasMoreTokens()){
				str = msgStoken.nextToken();
				if(str.indexOf("SOH") != -1 || str.indexOf("EOH") != -1){
					continue;
				}else if(str.indexOf("EOM") != -1){
					flag = true;
					break;
				}
				fieldArray = str.split("=", 2);
				if(fieldArray.length >= 2 && fieldArray[1].length() > 0){
					key = fieldArray[0];
					value = fieldArray[1];
					returnMap.put(key, value);
				}else{
					key = fieldArray[0];
					value = null;
					returnMap.put(key, value);
				}
			}
			if(flag){
				log.debug("msgMap::" + returnMap);
				log.info("解包完成!");
			}else{
				log.info("解包未完成!");
			}
			return returnMap;
		}
		
}
