package bc.core.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import quickfix.ConfigError;
import sun.misc.BASE64Encoder;


public class MsgHandler implements MessageListener, CallbackInf, InitializingBean {

	private static Log log = LogFactory.getLog(MsgHandler.class);
	
    private Connection connection;
    private MessageProducer producer;
    private Session session;
    private int count;
    private long start;
    private Topic topic_pub;
    private Topic topic_sub;
//	private String topicSendShpoints;
//	private String topicRecvShpoints;
//	private String topicSendWxpay;
//	private String topicRecvWxpay;
	private String topicSendShpoints;
	private String topicRecvShpoints;
	
	public String getTopicSendShpoints() {
		return topicSendShpoints;
	}
	public void setTopicSendShpoints(String topicSendShpoints) {
		this.topicSendShpoints = topicSendShpoints;
	}
	public String getTopicRecvShpoints() {
		return topicRecvShpoints;
	}
	public void setTopicRecvShpoints(String topicRecvShpoints) {
		this.topicRecvShpoints = topicRecvShpoints;
	}

	private String url ;
    
	public void setUrl(String url) {
		this.url = url;
	}
    public String getUrl() {
    	return url;
    }

	private ExecutorService executor;
	
	private int maxThreads ;

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	private String username="admin";
	private String passwd="admin";
	
	public void afterPropertiesSet() throws ConfigError {

    	try {
    		executor = Executors.newFixedThreadPool(maxThreads);
    		run();
    	} catch (Exception e){
    		log.error(e,e);
    	}
    	
    }
	
//	public String getTopicSendShpointsPay() {
//		return topicSendShpointsPay;
//	}
//	public void setTopicSendShpointsPay(String topicSendShpointsPay) {
//		this.topicSendShpointsPay = topicSendShpointsPay;
//	}
//	public String getTopicRecvShpointsPay() {
//		return topicRecvShpointsPay;
//	}
//	public void setTopicRecvShpointsPay(String topicRecvShpointsPay) {
//		this.topicRecvShpointsPay = topicRecvShpointsPay;
//	}
	public void run() throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
        
        connection = factory.createConnection(username, passwd);
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        topic_pub = session.createTopic(topicSendShpoints);
        topic_sub = session.createTopic(topicRecvShpoints);

        MessageConsumer consumer = session.createConsumer(topic_sub);
        consumer.setMessageListener(this);
        connection.start();

        producer = session.createProducer(topic_pub);
        log.info("Waiting for messages...");
    }

    private static boolean checkText(Message m, String s) {
        try {
            return m instanceof TextMessage && ((TextMessage)m).getText().equals(s);
        } catch (JMSException e) {
            e.printStackTrace(System.out);
            return false;
        }
    }

    public void onMessage(Message message) {
        if (checkText(message, "SHUTDOWN")) {

            try {
                connection.close();
            } catch (Exception e) {
                log.error(e,e);
            }

        } else if (checkText(message, "REPORT")) {
            try {
                long time = System.currentTimeMillis() - start;
                String msg = "Received " + count + " in " + time + "ms";
                producer.send(session.createTextMessage(msg));
            } catch (Exception e) {
                log.error(e,e);
            }
            count = 0;

        } else {
        	   String reqMsg = getTextMsg(message).toString();
        	   log.info("接收前端数据  OnMsg:" + reqMsg);
               TradingEngine TE = new TradingEngine(reqMsg);

			   TE.setCbInf(this);
			   Future<String> future = executor.submit(TE);
			   

        }
    }
    public String pressTest(String reqMsg) {

		String msgType = BCMessageUtil.GetValue(reqMsg, "MTYPE");
		if (msgType.equals("DPREQ")) {
			String retMsg = reqMsg.replace("MTYPE=DPREQ", "MTYPE=DPRESP");
			return retMsg.replace("|EOM=|", "")+ "|RETCODE=0000000|RETCOMMENT=交易成功|EOM=|";
		}
		return reqMsg;
	}
    Object getTextMsg(Message m) {
        try {
            return ((TextMessage)m).getText();
        } catch (JMSException e) {
            log.error(e,e);
            return e.toString();
        }
    }
   
    public void sendRetMessage(String respMsg, String sessionID) {
	      
    	try {
			if (null != respMsg && "" != respMsg ) {
				
				 StringBuilder sb = new StringBuilder();
				 sb.append("|FIXSESSIONID=");
				 sb.append(sessionID);
				 sb.append(respMsg);
	             producer.send(session.createTextMessage(sb.toString()));
				 log.info("返回前端数据 Send ret msg:" + cutPassword(sb.toString()));
			} else {
	
				log.error("TE return null message.");
			}
    	} catch (Exception e){
			
			log.error(e,e);
			log.error("error message from :"+ cutPassword(respMsg));
		}
	
	
	}
    
    private String cutPassword(String message){
    	
		String outMsg = message.toString();
		outMsg = outMsg.replaceAll("PANPASSWD=.*[|]SECONDTRACK=.", "PANPASSWD=|SECONDTRACK=");
		
		//不显式打印二磁道信息
		String secondTrack = BCMessageUtil.GetValue(outMsg, "SECONDTRACK");
		if(!"".equals(secondTrack)) {
			outMsg = outMsg.replaceAll(secondTrack, new BASE64Encoder().encode(secondTrack.getBytes()));
		}
		return outMsg;
	}


}

