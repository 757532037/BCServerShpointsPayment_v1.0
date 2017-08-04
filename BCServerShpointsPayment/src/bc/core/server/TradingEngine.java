package bc.core.server;

import java.util.HashMap;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bc.core.utils.Constant;

public class TradingEngine implements Callable<String>

{

	private String msg;
	private PaymentServicesImp payment;
	
	private static Log log = LogFactory.getLog(TradingEngine.class);

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;

	}

	private enum MTYP {
		/**210积分+7元兑换爱西西里周三活动*/
		DPREQAXXL,
		/**1积分兑换*/
		DPREQ,
		/**2积分兑换撤销*/
		CANCELREQ,
		/**3积分兑换冲正*/
		AUTOREVERSEREQ,
		/**4积分兑换撤销冲正*/
		CANCELREQREVERSEREQ,
		/**5积分余额查询*/
		CARDINFO,
		/**6积分退货申请*/
		SALESRETURN,
		/**7退货查询*/
		QUERYSALESRETURN
	};

	public PaymentServicesImp getBosPayment() {
		return payment;
	}

	public void setBosPayment(PaymentServicesImp bosPayment) {
		this.payment = bosPayment;
	}

	public TradingEngine(String inMsg) {
		this.msg = inMsg;
		Init();
	}

	public void Init() {
		try{
			if (Main.getAppContext() != null) {
				this.payment = (PaymentServicesImp) Main.getAppContext()
						.getBean("paymentImp");
			}
		}catch(Exception e){
			log.error(e,e);
		}
	}

	private CallbackInf cbInf;
	private String sessionID;

	

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public CallbackInf getCbInf() {
		return cbInf;
	}

	public void setCbInf(CallbackInf cbInf) {
		this.cbInf = cbInf;
	}
	

	public String call()  {

		String msgType = BCMessageUtil.GetValue(msg, "MTYPE");
		String ret = "";
		String sessionID = BCMessageUtil.GetValue(msg, "FIXSESSIONID");
		
		try {

		MTYP typ = MTYP.valueOf(msgType);
		String batchno;
		String serialno;
		String custid;
		String pan;
		HashMap<String,String> orderInfo;
		switch (typ) {
//		-1异常，0未交易，1积分兑换成功，2，积分撤销，3积分兑换冲正，4积分撤销冲正，5积分申请退货
		case DPREQ://1积分兑换
			ret = payment.shpointsPay(msg);
			break;
		case DPREQAXXL://210积分+7元兑换爱西西里周三活动
			ret = payment.axxlShpointsAcitvity(msg);
			break;
		case CANCELREQ://2积分兑换撤销
			batchno = BCMessageUtil.GetValue(msg, Constant.BATCHNO);
			serialno = BCMessageUtil.GetValue(msg, Constant.SERIALNO);
			custid = BCMessageUtil.GetValue(msg, Constant.CUSTID);
			orderInfo = payment.selectPaymentCITISQL(batchno, serialno ,custid);
			if(orderInfo == null){//未找到交易记录
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE, "-2011");
				ret = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT, "未找到原交易");
				break;
			}
			if("-1".equals(orderInfo.get("STATUS"))){//原交易异常，不可撤下
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "-1");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易异常，不可撤下");
				break;
			}else if("0".equals(orderInfo.get("STATUS"))){//原交易不存在可撤销状态
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "-2011");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易不存在可撤销状态");
				break;
			}else if("2".equals(orderInfo.get("STATUS"))){//积分撤销
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "2");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已申请撤销");
				break;
			}else if("5".equals(orderInfo.get("STATUS"))){//积分兑换冲正
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "5");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已申请兑换冲正");
				break;
			}else if("5".equals(orderInfo.get("STATUS"))){//积分撤销冲正
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "5");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已申请撤销冲正");
				break;
			}else if("4".equals(orderInfo.get("STATUS"))){//积分申请退货
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "4");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已申请退货");
				break;
			}else if("6".equals(orderInfo.get("STATUS"))){
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "6");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已结算");
				break;
			}else if("1".equals(orderInfo.get("STATUS"))){//可以撤销
				ret = payment.shpointsRefund(msg,orderInfo);
				break;
			}
		case AUTOREVERSEREQ://3积分兑换冲正
			batchno = BCMessageUtil.GetValue(msg, Constant.BATCHNO);
			serialno = BCMessageUtil.GetValue(msg, Constant.SERIALNO);
			custid = BCMessageUtil.GetValue(msg, Constant.CUSTID);
			orderInfo = payment.selectPaymentCITISQL(batchno, serialno ,custid);
			if(orderInfo == null){//未找到交易记录
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE, "-2011");
				ret = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT, "未找到原交易");
				break;
			}
			if("-1".equals(orderInfo.get("STATUS"))){//原交易异常，不可撤下
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "-1");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易异常，不可撤下");
				break;
			}else if("0".equals(orderInfo.get("STATUS"))){//原交易不存在可撤销状态
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "-2011");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易不存在可撤销状态");
				break;
			}else if("2".equals(orderInfo.get("STATUS"))){//积分撤销
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "2");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已申请撤销");
				break;
			}else if("5".equals(orderInfo.get("STATUS"))){//积分兑换冲正
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "5");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已申请兑换冲正");
				break;
			}else if("5".equals(orderInfo.get("STATUS"))){//积分撤销冲正
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "5");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已申请撤销冲正");
				break;
			}else if("4".equals(orderInfo.get("STATUS"))){//积分申请退货
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "4");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已申请退货");
				break;
			}else if("6".equals(orderInfo.get("STATUS"))){
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "6");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已结算");
				break;
			}else if("1".equals(orderInfo.get("STATUS"))){
				ret = payment.shpointsDpreqReverse(msg,orderInfo);
				break;
			}
		case CANCELREQREVERSEREQ://4积分兑换撤销冲正
			batchno = BCMessageUtil.GetValue(msg, Constant.BATCHNO);
			serialno = BCMessageUtil.GetValue(msg, Constant.SERIALNO);
			custid = BCMessageUtil.GetValue(msg, Constant.CUSTID);
			orderInfo = payment.selectPaymentCITISQL(batchno, serialno ,custid);
			if(orderInfo == null){//未找到交易记录
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE, "-2011");
				ret = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT, "未找到原交易");
				break;
			}
			if("-1".equals(orderInfo.get("STATUS"))){//原交易异常，不可撤下
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "-1");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易异常，不可撤下");
				break;
			}else if("0".equals(orderInfo.get("STATUS"))){//原交易不存在可撤销状态
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "-2011");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易不存在可撤销状态");
				break;
			}else if("2".equals(orderInfo.get("STATUS"))){//积分撤销
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "2");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已申请撤销");
				break;
			}else if("5".equals(orderInfo.get("STATUS"))){//积分兑换冲正
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "5");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已申请兑换冲正");
				break;
			}else if("5".equals(orderInfo.get("STATUS"))){//积分撤销冲正
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "5");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已申请撤销冲正");
				break;
			}else if("4".equals(orderInfo.get("STATUS"))){//积分申请退货
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "4");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已申请退货");
				break;
			}else if("6".equals(orderInfo.get("STATUS"))){
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "6");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已结算");
				break;
			}else if("1".equals(orderInfo.get("STATUS"))){
				ret= payment.shpointsCancelReverse(msg,orderInfo);
				break;
			}
		case CARDINFO://5积分余额查询
			ret= payment.shpointsAccountQue(msg);
			break;
		case QUERYSALESRETURN://7退货查询
			batchno = BCMessageUtil.GetValue(msg, Constant.BATCHNO);
			serialno = BCMessageUtil.GetValue(msg, Constant.SERIALNO);
			custid = BCMessageUtil.GetValue(msg, Constant.CUSTID);
			pan = BCMessageUtil.GetValue(msg, Constant.PAN);
			orderInfo = payment.queryPaymentTradePan(batchno, serialno ,custid ,pan);
			if(orderInfo==null){
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE, "-2011");
				ret = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT, "未找到原交易");
				break;
			}
			ret= payment.queryShpointsSalesReturn(msg, orderInfo);
			break;
		case SALESRETURN://6积分退货申请
			batchno = BCMessageUtil.GetValue(msg, Constant.BATCHNO);
			serialno = BCMessageUtil.GetValue(msg, Constant.SERIALNO);
			custid = BCMessageUtil.GetValue(msg, Constant.CUSTID);
			orderInfo = payment.selectPaymentCITISQL(batchno, serialno ,custid);
			if(orderInfo == null){//未找到交易记录
				ret = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT, "未找到原交易");
				break;
			}
			if("-1".equals(orderInfo.get("STATUS"))){//原交易异常，不可撤下
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "0");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易异常，不可撤下");
				break;
			}else if("0".equals(orderInfo.get("STATUS"))){//原交易不存在可撤销状态
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "0");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易不存在可撤销状态");
				break;
			}else if("2".equals(orderInfo.get("STATUS"))){//积分撤销
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "0");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已申请撤销");
				break;
			}else if("5".equals(orderInfo.get("STATUS"))){//积分兑换冲正
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "0");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已申请兑换冲正");
				break;
			}else if("5".equals(orderInfo.get("STATUS"))){//积分撤销冲正
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "0");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已申请撤销冲正");
				break;
			}else if("4".equals(orderInfo.get("STATUS"))){//积分申请退货
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "0");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "原交易已申请退货");
				break;
			}else if("6".equals(orderInfo.get("STATUS"))){
				ret = msg.replaceAll("REFUNDREQ", "REFUNDRESP");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "0");
				ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "交易已结算,请联系伯乔");
				break;
			}else if("1".equals(orderInfo.get("STATUS"))){
				ret= payment.shpointsSalesReturn(msg,orderInfo);
				break;
			}
		default:
			ret = payment.resetCash(msg);
			break;
		}
		ret = BCMessageUtil.addMsgTag(ret, Constant.BANKNAME, Constant.SHPOINTSBANKNAME);
		cbInf.sendRetMessage(ret, sessionID);
		
		} catch ( Exception e) {
			log.error("Invalid message type:" + msgType);
			log.error(e,e);
			ret = BCMessageUtil.addMsgTag(ret, Constant.RETCODE, "99");
			ret = BCMessageUtil.addMsgTag(ret, Constant.RETCOMMENT, "系统网络错误");
			return ret;
		}
		return ret;
		
	}

}
