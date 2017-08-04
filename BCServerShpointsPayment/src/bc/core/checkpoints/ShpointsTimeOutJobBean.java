package bc.core.checkpoints;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.shpoints.enm.Mti;

import bc.core.server.BCMessageUtil;
import bc.core.server.PaymentServicesImp;
import bc.core.utils.Constant;

public class ShpointsTimeOutJobBean implements Runnable{
	private static final Logger logger = Logger.getLogger(ShpointsTimeOutJobBean.class);
	private long TradeTime;
	private String mti;
	private String msg;
	private PaymentServicesImp payment;
	/**冲正循环次数*/
	private Integer frequency;
	/**冲正间隔时间*/
	private Integer waitTime;
	
	public ShpointsTimeOutJobBean(long tradeTime,
			String mti, String msg,
			PaymentServicesImp payment) {
		super();
		TradeTime = tradeTime;
		this.mti = mti;
		this.msg = msg;
		this.payment = payment;
	}
	public long getTradeTime() {
		return TradeTime;
	}
	public void setTradeTime(long tradeTime) {
		TradeTime = tradeTime;
	}
	
	public String getMti() {
		return mti;
	}
	public void setMti(String mti) {
		this.mti = mti;
	}
	/**冲正循环次数*/
	public Integer getFrequency() {
		return frequency;
	}
	/**冲正循环次数*/
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	/**冲正间隔时间*/
	public Integer getWaitTime() {
		return waitTime;
	}
	/**冲正间隔时间*/
	public void setWaitTime(Integer waitTime) {
		this.waitTime = waitTime;
	}
	@Override
	public void run() {
		HashMap<String,String> msgMap = BCMessageUtil.parseBcpayMsg2Map(msg);
		logger.info("cancel thread job orderid : [" + msgMap.get(Constant.ORDERID) + "]，"
						+ "terminalId : [" + msgMap.get(Constant.TERMINALID) + "]，storeId : [" + msgMap.get(Constant.STOREID) + "]");
		msg = BCMessageUtil.addMsgTag(msg, "AUTO", "AUTO");
		for (int i = 0; i < frequency; i++) {
			
			HashMap<String,String> orderInfo = payment.getOrderInfo(msgMap.get(Constant.ORDERID),msgMap.get(Constant.TERMINALID)
					,msgMap.get(Constant.STOREID));
			if(orderInfo == null){
				logger.error("cancel or reverse thread job : [not found trade error!] msg : "+msg);
				return;
			}
			
			if(mti.equals(Mti.TRADE.getMsg_type_snd())){//3积分兑换冲正"0240"
				try {
					msg = payment.shpointsDpreqReverse(msg, orderInfo);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(mti.equals(Mti.CANCEL.getMsg_type_snd())){//积分兑奖撤销"0260"
				try {
					msg = payment.shpointsDpreqReverse(msg, orderInfo);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
//			HashMap<String,String> returnMap = BCMessageUtil.parseBcpayMsg2Map(msg);
			logger.info("cancel thread job ，  return msg : ["+msg+"]");
			if("0000000".equals(orderInfo.get(Constant.RETCODE))){//冲正成功
//				int returnMsg = payment.updatecitiStatusSql(0, orderInfo.get(Constant.ORDERID), orderInfo.get(Constant.BATCHNO), orderInfo.get(Constant.SERIALNO));
				int returnMsg = payment.updateShpointsStatusByOrderid("交易冲正成功", orderInfo.get(Constant.ORDERID));
				i = frequency;
				break;
			}else{
				try {
					Thread.sleep(waitTime*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
