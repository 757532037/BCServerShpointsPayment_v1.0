package bc.core.checkpoints;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.Security;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import bc.core.utils.BCUtils;

public class BosInterfaceUtil {
	private String url;
	private String trustPath;
	private String trustPsw;
	private int timeOut;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getTrustPath() {
		return trustPath;
	}
	public void setTrustPath(String trustPath) {
		this.trustPath = trustPath;
	}

	public String getTrustPsw() {
		return trustPsw;
	}
	public void setTrustPsw(String trustPsw) {
		this.trustPsw = trustPsw;
	}
	
	public int getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	private static final Logger logger = Logger.getLogger(BosInterfaceUtil.class);
	
	public void init(){
		System.setProperty("java.protocol.handler.pkgs",
				"com.sun.net.ssl.internal.www.protocol");
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		System.setProperty("javax.net.ssl.trustStore", trustPath);
		System.setProperty("javax.net.ssl.trustStorePassword", trustPsw);
		System.setProperty("javax.net.ssl.trustStoreType", "jks");
	}
	public static void main(String[] args) {
		BosInterfaceUtil bosInterfaceUtil = new BosInterfaceUtil();
		bosInterfaceUtil.init();
		BosGetUserInfoBean bean = bosInterfaceUtil.getUserId("6250996650431128", "100001", 21);
		System.out.println(bean.getPan());
		
	}
	
	/**
	 * 更卡号查询客户号ID
	 * @param cardNo
	 * 				卡号
	 * @param bcseq
	 * 				流水号
	 * @param money
	 * 				交易积分
	 * @return
	 */
	public BosGetUserInfoBean getUserId(final String cardNo, final String bcseq, final double money){
		
		ContentProducer cp = new ContentProducer() {

			@Override
			public void writeTo(OutputStream os) throws IOException {
				long time = System.currentTimeMillis();
				time = time / 10;
				BosGetUserInfoBean xmlBean = new BosGetUserInfoBean();
				xmlBean.setInfoType("0200");
				xmlBean.setBcseq(bcseq);
				xmlBean.setPan(cardNo);
//				xmlBean.setPan("6259533393673996");
				xmlBean.setMoney(new BigDecimal(2100));
				String xml = BCUtils.beanToXml(xmlBean);
				logger.info("bos req xml : " + xml);
				os.write(xml.getBytes("UTF-8"));
				os.flush();
			}//
		};
		HttpEntity entity = new EntityTemplate(cp);
		String resultXml = httpPost(entity);
		return BCUtils.xmlToBean(resultXml, BosGetUserInfoBean.class);
	}
	
	public boolean isSuccess(BosGetUserInfoBean bean){
		return bean == null ? false : "000000".equals(bean.getRetCode());
	}
	
	private String httpPost(HttpEntity entity){
		HttpClient httpclient = HttpClients.custom().build();
		HttpPost httppost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(timeOut).setConnectTimeout(timeOut)
				.setConnectionRequestTimeout(timeOut).build();// 设置请求和传输超时时间
		httppost.setConfig(requestConfig);
		httppost.setEntity(entity);
		try {
			HttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity result = response.getEntity();
				String res = EntityUtils.toString(result, "UTF-8");
				logger.info("bos resp xml : " + res);
				return res;
			} else {
				return null;
			}
		} catch (ClientProtocolException e) {
			logger.error(e,e);
		} catch (IOException e) {
			logger.error(e,e);
		}
		return null;
	}
}
