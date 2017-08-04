package bc.core.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import bc.core.server.BCRecData;
import bc.core.server.Main;
import bc.core.server.ParaBean;

public class BCUtils {
	private static long currentTime = 0;
	private static long seqNo = 0;
	static {
		currentTime = Long.parseLong(getTime("HHmmss"));
	}

	public static String getCurrentDate() {
		return getTime("yyyy-MM-dd");
	}
	public static String getCurrentTime() {
		return getTime("HH:mm:ss");
	}
	public static String getCurDate(){
		return getTime("yyyyMMdd");
	}
	public static String getCurTime(){
		return getTime("HHmmss");
	}
	

	public synchronized static String getSerialNo() {
		String serialNod = String.valueOf(currentTime++);
		if (serialNod.length() < 6) {
			int length = serialNod.length();
			for (int i = 0; i < 6 - length; i++) {
				serialNod = "0" + serialNod;
			}
		}
		return serialNod;
	}

	public synchronized static String getSEQNo() {
		String strSeq = Long.toString(++seqNo);
		return strSeq;
	}

	public static String getMerchantID() {
		return "GYS018";
	}

	public static String getMerchantName() {
		return "伯乔科技";
	}

	public static String getProductType() {
		return "Z94001";
	}

	public static String getTime(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(Calendar.getInstance().getTime());
	}
	
	public static String formatTime(Date time, String pattern) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			
			String reTime =formatter.format(time); 
			return reTime;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T xmlToBean(String xml, Class<T> clazz){
		if (xml == null || "".equals(xml)) {
			return null;
		}
		InputStream is = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz);
			Unmarshaller mar = jc.createUnmarshaller();
			is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			return (T) mar.unmarshal(is);
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static <T> String beanToXml(T bean){
		JAXBContext context;
		StringWriter sw = new StringWriter();
		try {
			context = JAXBContext.newInstance(bean.getClass());
			Marshaller m = context.createMarshaller();
			m.marshal(bean,sw);
			return sw.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		} finally {
			try {
				sw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
