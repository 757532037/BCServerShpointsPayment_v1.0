package bc.core.server;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.core.checkpoints.BosGetUserInfoBean;
import bc.core.checkpoints.BosInterfaceUtil;
import bc.core.checkpoints.ShpointsTimeOutJob;
import bc.core.checkpoints.ShpointsTimeOutJobBean;
import bc.core.utils.BCUtils;
import bc.core.utils.Constant;
import bc.core.utils.ShdbUtil;
import bc.core.utils.ShpointsFunction;

import com.shpoints.client.SimpleClient;
import com.shpoints.enm.Mti;
import com.shpoints.enm.ResultCode;
import com.shpoints.factory.XmlReader;
import com.shpoints.key.SimpleConstants;
import com.shpoints.util.DateUtil;
import com.shpoints.util.ShpointsContainer;

public class PaymentServicesImp {
	/**收发解析报文*/
	private SimpleClient simpleClient;
	/**收发解析报文*/
	public SimpleClient getSimpleClient() {
		return simpleClient;
	}
	/**收发解析报文*/
	public void setSimpleClient(SimpleClient simpleClient) {
		this.simpleClient = simpleClient;
	}
	/**冲正队列*/
	private ShpointsTimeOutJob job;
	/**冲正队列*/
	public ShpointsTimeOutJob getJob() {
		return job;
	}
	/**冲正队列*/
	public void setJob(ShpointsTimeOutJob job) {
		this.job = job;
	}
	/**初始化模板*/
	private XmlReader xmlReader;
	/**初始化模板*/
	public XmlReader getXmlReader() {
		return xmlReader;
	}
	/**初始化模板*/
	public void setXmlReader(XmlReader xmlReader) {
		this.xmlReader = xmlReader;
	}
	/**上海银行积分查看接口*/
	private BosInterfaceUtil bosInterfaceUtil;
	/**上海银行积分查看接口*/
	public BosInterfaceUtil getBosInterfaceUtil() {
		return bosInterfaceUtil;
	}
	/**上海银行积分查看接口*/
	public void setBosInterfaceUtil(BosInterfaceUtil bosInterfaceUtil) {
		this.bosInterfaceUtil = bosInterfaceUtil;
	}

	private Logger log = Logger.getLogger(PaymentServicesImp.class);

	private BCRecData BCDB;
 
	private float bqPar=0.00003f;
	
	public float getBqPar() {
		return bqPar;
	}
	public void setBqPar(String bqPar) {
		
		this.bqPar = Float.parseFloat(bqPar);
	}
	BCMessageUtil BCMsgUtil = new BCMessageUtil();

	public synchronized BCRecData getBCDB() {
		return BCDB;
	}

	public void setBCDB(BCRecData bCDB) {
		BCDB = bCDB;
	}
	
	
	/**
	 * 1积分兑换
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String shpointsPay(String msg) throws Exception{
		try {
			String tranAmount =transAmt(BCMessageUtil.GetValue(msg, Constant.TRANSCASH)).replace(".", "");
			Map<String,String> realPro = BCDB.getBcproductrealMap().get(""+Integer.parseInt(BCMessageUtil.GetValue(msg, Constant.PRODTYPE).substring(4)));
			String pointsper = realPro.get(ShdbUtil.POINTSNUM);
			log.info(realPro.toString()+"---<-----MAP-----------------------------店面积分兑换交易金额:----->---"+tranAmount);
			msg = BCMessageUtil.addMsgTag(msg, Constant.TRANSAMT, ""+(Integer.parseInt(tranAmount)*Integer.parseInt(pointsper)));
//			HashMap<String,String> msgMapre = BCMessageUtil.parseBcpayMsg2Map(msg);
			/**爱西西里店面积分暂时不上*/
			if(BCMessageUtil.GetValue(msg, Constant.CUSTID).equals("1012")){
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"14");
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"店面积分暂未开通O(∩_∩)O~~");
				return msg;
			}
			log.info("tradePay function"+msg);
			long tsn = BCDB.getTSN();
			String yyyyMMdd = BCUtils.getTime("yyyyMMdd");
			String HHmmss = BCUtils.getTime("HHmmss");
			/**批次号**/
			String batchNo = BCUtils.getCurrentDate().replaceAll("-", "").substring(2, 8);
			/**序列号*/
			String serialNo = formatNumberString(tsn+"", 6);
			String prodid = ""+Integer.parseInt(BCMessageUtil.GetValue(msg, Constant.PRODTYPE).substring(4));
			msg = BCMessageUtil.addMsgTag(msg, Constant.PRODUCTID,prodid);
			msg = BCMessageUtil.addMsgTag(msg, Constant.BATCHNO,batchNo);
			msg = BCMessageUtil.addMsgTag(msg, Constant.SERIALNO,serialNo);
			msg = BCMessageUtil.addMsgTag(msg, Constant.TRANSDATE,yyyyMMdd);
			msg = BCMessageUtil.addMsgTag(msg, Constant.TRANSTIME,HHmmss);
			msg = BCMessageUtil.addMsgTag(msg, Constant.DISCNTAMT,"0");
			msg = BCMessageUtil.addMsgTag(msg, Constant.ACTIVITY,"上海银行积分支付");
			msg = BCMessageUtil.addMsgTag(msg, Constant.BANKNAME,"上海银行");
			msg = BCMessageUtil.addMsgTag(msg, Constant.GOODSINFO,null);
			HashMap<String,String> msgMap = BCMessageUtil.parseBcpayMsg2Map(msg);
			if(!isNotCardNo(msgMap.get(Constant.PAN))){
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"14");
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"卡号位数须16位!");
				return msg;
			}
			if(msgMap.get(Constant.TRANSCASH)==null||msgMap.get(Constant.TRANSCASH).equals("0.00")){
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"金额输入有误!");
				return msg;
			}
			if(!ShpointsFunction.isAlive(realPro.get("STARTDATE"), realPro.get("ENDDATE"), yyyyMMdd)){
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"不在活动日期");
				return msg;
			}
			BosGetUserInfoBean bean = bosInterfaceUtil.getUserId(msgMap.get(Constant.PAN), serialNo, 210);
			if (!bean.isSuccess()){
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"没有找到该卡片优惠信息");
				return msg;
			}
			msgMap.put(Constant.CUSTOMERID, bean.getCustomerId());//将客户号放入note1字段
			BigDecimal tradeCash = BCDB.sumCitipaymentByNote1ByBatchno(msgMap.get(Constant.CUSTOMERID), batchNo, prodid);
			if(tradeCash==null){
				tradeCash = new BigDecimal("0.00");
			}
			String checkValue = checkTrade(tradeCash, msg, msgMap, tranAmount,new BigDecimal(realPro.get(ShdbUtil.DAYPOINTSNUM)));
			if(checkValue!=null){
				return checkValue;
			}
			//先插入伯桥交易
			log.info("insert trade");
			BCDB.insertBeforePayment(msgMap);
			//上海银行积分交易
			int[] result0240_48 = new int[]{8,8,3,1,8,9,12};
			Map<String,String> sendMap = new HashMap<String,String>();
			sendMap.put(SimpleConstants.MTI,Mti.TRADE.getMsg_type_snd());//积分兑换
			sendMap.put("2",msgMap.get(Constant.PAN));
			String getDateyyyyMMddHH = DateUtil.getDateyyyyMMddHH();
			String getDateHHmmss = DateUtil.getDateHHmmss();
			String getDateMMDD = DateUtil.getDateMMDD();
			String getDateMddHHmmssSS = DateUtil.getDateMddHHmmssSS();
			sendMap.put("7",getDateyyyyMMddHH);//交易日期和时间10
			sendMap.put("11",getDateHHmmss);//系统跟踪审计号(当天唯一流水号)--6--前端获取
			sendMap.put("12",getDateHHmmss);//当地交易时间6
			sendMap.put("13",getDateMMDD);//当地交易日期4
			sendMap.put("15",getDateMMDD);//清算日期4
			sendMap.put("37",getDateMddHHmmssSS);//调单参考号(交易唯一)
			sendMap.put("41",ShpointsFunction.formatLeft(8,msgMap.get(Constant.POSID),"0"));//终端标识（pos后8位）--前端获取
			if(msgMap.get(Constant.FASTTRACKINDICATOR).equals(ShpointsFunction.FASTTRACK_B)){//兑换现金
				Map<String ,String > pointsMap = shpointsAccountQuery(msg);//判断积分是否充足
				if(pointsMap.get("errorCode")!=null){//查询失败
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,pointsMap.get("errorMsg"));
					return msg;
				}
				if(!ShpointsFunction.pointsIsEnough(pointsMap.get("48.5"), tranAmount,Integer.parseInt(realPro.get("POINTSNUM")))){//兑换积分不足
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"卡号不正确或兑换积分不足");
					return msg;
				}
				sendMap.put("4",ShpointsFunction.formatLeft(12, tranAmount, "0"));//格式化金额
				sendMap.put("48", ShpointsFunction.formatRight(8, realPro.get(Constant.CATALOGUECODE), " ")//放类别
						+ShpointsFunction.formatRight(8, realPro.get(Constant.ITEMCODE), " ")//放物品
						+ShpointsFunction.formatRight(3, realPro.get(Constant.QUANTITY), " ")//放数量
						+ShpointsFunction.FASTTRACK_B+Constant.s200);//goodsNO+proNo+num+type+temp
			}else if(msgMap.get(Constant.FASTTRACKINDICATOR).equals(ShpointsFunction.FASTTRACK_N)){//兑换物品//这里需要调整
				sendMap.put("4","000000000000");//金额默认上送0
				sendMap.put("48", ShpointsFunction.formatRight(8, msgMap.get(Constant.CATALOGUECODE), " ")
						+ShpointsFunction.formatRight(8, msgMap.get(Constant.ITEMCODE), " ")
						+ShpointsFunction.formatRight(3, msgMap.get(Constant.QUANTITY), " ")
						+ShpointsFunction.FASTTRACK_N+Constant.s200);
			}
			Map<String,String> recvMap = simpleClient.sendToBank(sendMap,xmlReader);
			log.info("积分兑换recv_Msg:"+recvMap.toString());
			String st39 = recvMap.get("39");
			ResultCode result= ResultCode.getErrorCode(st39);//执行交易结果（包含成功和失败）
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE, "99");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT, result.getErr_msg());
			msgMap.put(ShdbUtil.RESPONSEMSG, result.getErr_msg());//上海积分交易状态信息
			msgMap.put(ShdbUtil.ORDERID, msgMap.get(Constant.ORDERID));//
			msgMap.put(Constant.RECEIPTAMOUNT, msgMap.get(Constant.TRANSCASH));//商户实收金额
			msgMap.put(Constant.RETCODE, "99");
			msgMap.put(Constant.RETCOMMENT, result.getErr_msg());
			msgMap.put(Constant.BANKORDID, null);
			String cfat = realPro.get(ShdbUtil.FATTRADENUMC);
			String bfat = realPro.get(ShdbUtil.FATTRADENUMB);
			msgMap.put(Constant.RECEIPTAMOUNT, msgMap.get(Constant.TRANSCASH));//客户支付金额
			String bamt = fatAmt(bfat, msgMap.get(Constant.TRANSCASH));
			String camt = fatAmt(cfat, msgMap.get(Constant.TRANSCASH));
			msgMap.put(Constant.BANK_PRICE, bamt);
			msgMap.put(Constant.CHANNEL_PRICE, camt);
			msgMap.put(Constant.BQ_BENEFIT, bqbenefit(bamt, camt));
			msgMap.put(Constant.BQ_GET, fatAmt(bfat, msgMap.get(Constant.TRANSCASH)));
			msgMap.put(Constant.CHANNEL_PAY, "0.00");
			msgMap.put(Constant.TRANS_CASH, msgMap.get(Constant.TRANSCASH));
			msgMap.put(Constant.CUSTOMER_CON, msgMap.get(Constant.TRANSCASH));
			msgMap.put(Constant.RETCOMMENT, result.getErr_msg());
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,result.getErr_msg());
			if(result.getErr_code().equals(Constant.SUCCESS)){//交易成功
				int thisIndex = 0;
				for(int i=0;i<result0240_48.length;i++){
					recvMap.put("48."+(i+1), recvMap.get("48").substring(thisIndex, thisIndex+=result0240_48[i]));
				}
				msgMap.put(Constant.RETCODE, "00");
				BCDB.updatePayment(1,msgMap);//更新伯桥交易成功
				recvMap.put(ShdbUtil.RESPONSEMSG, "交易成功");
				recvMap.put(ShdbUtil.ORDERID, msgMap.get(Constant.ORDERID));
				BCDB.insertShpoints(recvMap);//这里插入伯桥交易表
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"0000000");
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"交易成功");
				msg = BCMessageUtil.addMsgTag(msg, Constant.ATTENTION,"兑换成功；兑换金额为" + msgMap.get(Constant.TRANSCASH) + "元,消费积分"+Integer.parseInt(tranAmount.trim())*Integer.parseInt(realPro.get("POINTSNUM")));
				return msg;
			}else{//交易失败
				BCDB.insertShpoints(recvMap);//这里插入伯桥交易表
				BCDB.updatePayment(-1,msgMap);//更新伯桥交易失败
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,result.getErr_msg());
				return msg;
			}
		} catch (Exception e) {
			log.error(e,e);
			ResultCode result= ResultCode.getErrorCode("99");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,result.getErr_code());
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,result.getErr_msg());
			msg = BCMessageUtil.addMsgTag(msg, Constant.ATTENTION,"");
			ShpointsTimeOutJobBean jobBean = new ShpointsTimeOutJobBean(System.currentTimeMillis() + 70000,Mti.TRADE.getMsg_type_snd(),msg,this);
			job.correctionQueue.add(jobBean);
			return msg;
		}
	}
	
	/**
	 * 210积分+7元兑换爱西西里全场活动
	 * @param msg
	 * @return
	 * @throws ParseException 
	 * @throws SQLException 
	 */
	public String axxlShpointsAcitvity(String msg) throws SQLException{
		try {
			Map<String,String> realPro = BCDB.getBcproductrealMap().get(""+Integer.parseInt(BCMessageUtil.GetValue(msg, Constant.PRODTYPE).substring(4)));//这里需要调整
			String transAmt = realPro.get(ShdbUtil.POINTSNUM);
			msg = BCMessageUtil.addMsgTag(msg, Constant.TRANSAMT, transAmt);
			HashMap<String,String> msgMap = BCMessageUtil.parseBcpayMsg2Map(msg);
			if(realPro.get(ShdbUtil.ACTIVITDAY).contains(""+DateUtil.getThisDay())){//爱西西里活动日
				/**今天可交易量*/
//				int totalPointsNum = DateUtil.getTotalPointsNum(realPro.get(ShdbUtil.DAYPOINTSNUM), realPro.get(ShdbUtil.ACTIVITDAY), realPro.get(ShdbUtil.STARTDATE), realPro.get(ShdbUtil.ENDDATE));//剩余顺延用这个
//				int hasTradeNum = BCDB.selectAllShpoints210Trade();//剩余顺延用这个
				int todayTradeNum = BCDB.selectTodayShpoints210Trade();//单日限额2300用这个
//				if(hasTradeNum>=totalPointsNum){//剩余顺延用这个
				if(todayTradeNum>=Integer.parseInt(realPro.get(ShdbUtil.DAYPOINTSNUM))){//单日限额2300用这个
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"今天活动名额已上限");
				}else{//交易没上限，可以进行210积分交易
					Map<String,String> pointsAccount = shpointsAccountQuery(msg);//查积分
					if(pointsAccount.get("errorCode")==null){//查询成功
						if(Integer.parseInt(pointsAccount.get("48.5"))>=Integer.parseInt(realPro.get(ShdbUtil.POINTSNUM))){//单个产品兑换所需积分、积分兑换210充足
							//查看权益
							long tsn = BCDB.getTSN();
							String yyyyMMdd = BCUtils.getTime("yyyyMMdd");
							String HHmmss = BCUtils.getTime("HHmmss");
							/**批次号**/
							String batchNo = BCUtils.getCurrentDate().replaceAll("-", "").substring(2, 8);
							/**序列号*/
							String serialNo = formatNumberString(tsn+"", 6);
							BosGetUserInfoBean bean = bosInterfaceUtil.getUserId(msgMap.get(Constant.PAN), serialNo, 210);
							if (bean.isSuccess()) {//查询CUSTID成功bean.isSuccess()
								log.info("insert custInfo");
								int countnum = BCDB.selectShbkCustInfo(bean.getCustomerId());
								if(countnum>1){//本月权益已用完
									msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
									msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"当月权益已使用完");
									return msg;
								}else{//这里可以进行交易
									//(Integer.parseInt(realPro.get(ShdbUtil.POINTSNUM))/5)+"";这里的5表示5积分兑换1分钱
//									String tranAmount = (Integer.parseInt(realPro.get(ShdbUtil.POINTSNUM))/5)+"";
									//查看今天是否有交易记录
									int thisDayTradeNum = BCDB.selectThisPanShbk210TradesInfo(bean.getCustomerId());
									if(thisDayTradeNum>0){
										msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
										msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"当天权益已使用完");
										return msg;
									}
									String tranAmount = "1";
									msg = shpointsPay210(msg, serialNo, yyyyMMdd, HHmmss, batchNo,tranAmount,realPro,bean.getCustomerId());
									msgMap = BCMessageUtil.parseBcpayMsg2Map(msg);
									if(msgMap.get(Constant.RETCODE).equals("0000000")){//交易成功插入CUSTID表
										BCDB.insertShbkCustInfo(msgMap.get(Constant.PAN), bean.getCustomerId());
										msg =  BCMessageUtil.addMsgTag(msg, Constant.TRANSCASH, "7.00");;
										return msg;
									}
								}
								log.info("make trade");
							}else{//查询失败
								msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
								msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"没有找到该卡片优惠信息");
							}
						}else{//积分不够
							msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
							msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"兑换积分不足"+realPro.get(ShdbUtil.POINTSNUM)+"分");
						}
					}else{//积分查询错误
						msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
						msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,pointsAccount.get("errorMsg"));
					}
				}
			}else{//不在活动日期
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"不在活动日期");
			}
			return msg;
		} catch (Exception e) {
			log.error(e,e);
			ResultCode result= ResultCode.getErrorCode("99");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,result.getErr_code());
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,result.getErr_msg());
			msg = BCMessageUtil.addMsgTag(msg, Constant.ATTENTION,"");
			ShpointsTimeOutJobBean jobBean = new ShpointsTimeOutJobBean(System.currentTimeMillis() + 70000,Mti.TRADE.getMsg_type_snd(),msg,this);
			job.correctionQueue.add(jobBean);
			return msg;
		}
	}
	/**
	 * 上海银行210积分+7元活动
	 * @param msg
	 * 			原交易信息
	 * @param serialNo
	 * 			流水号
	 * @param yyyyMMdd
	 * 				交易日期
	 * @param HHmmss
	 * 				交易时间
	 * @param batchNo
	 * 				批次号
	 * @param tranAmount
	 * 					兑换金额
	 * @param Map<String,String> realPro
	 * 						活动信息
	 * @return
	 */
	public String shpointsPay210(String msg,String serialNo,String yyyyMMdd,String HHmmss,String batchNo,String tranAmount,Map<String,String> realPro,String customerId){
		try {
			log.info("axxl 210+7 tradePay function"+msg);
			msg = BCMessageUtil.addMsgTag(msg, Constant.PRODUCTID,"553");
			msg = BCMessageUtil.addMsgTag(msg, Constant.BATCHNO,batchNo);
			msg = BCMessageUtil.addMsgTag(msg, Constant.SERIALNO,serialNo);
			msg = BCMessageUtil.addMsgTag(msg, Constant.TRANSDATE,yyyyMMdd);
			msg = BCMessageUtil.addMsgTag(msg, Constant.TRANSTIME,HHmmss);
			msg = BCMessageUtil.addMsgTag(msg, Constant.DISCNTAMT,"0");
			msg = BCMessageUtil.addMsgTag(msg, Constant.ACTIVITY,"21积分+7元限量兑换");
			msg = BCMessageUtil.addMsgTag(msg, Constant.BANKNAME,"上海银行");
			msg = BCMessageUtil.addMsgTag(msg, Constant.GOODSINFO,null);
			HashMap<String,String> msgMap = BCMessageUtil.parseBcpayMsg2Map(msg);
			//先插入伯桥交易
			log.info("insert trade");
			msgMap.put(Constant.TRANSCASH, "7.00");//7.00
			msgMap.put(Constant.BANK_PRICE, "0.90");
			msgMap.put(Constant.CHANNEL_PRICE, "0.00");
			msgMap.put(Constant.BQ_GET, "0.90");
			msgMap.put(Constant.TRANS_CASH, "7.90");
			msgMap.put(Constant.CUSTOMER_CON, "0.00");
			msgMap.put(Constant.CUSTOMERPAY, "7.00");
			msgMap.put(Constant.NOTE4, customerId);
			BCDB.insertBeforePayment(msgMap);
			//上海银行积分交易
			int[] result0240_48 = new int[]{8,8,3,1,8,9,12};
			Map<String,String> sendMap = new HashMap<String,String>();
			sendMap.put(SimpleConstants.MTI,Mti.TRADE.getMsg_type_snd());//积分兑换
			sendMap.put("2",msgMap.get(Constant.PAN));
			String getDateyyyyMMddHH = DateUtil.getDateyyyyMMddHH();
			String getDateHHmmss = DateUtil.getDateHHmmss();
			String getDateMMDD = DateUtil.getDateMMDD();
			String getDateMddHHmmssSS = DateUtil.getDateMddHHmmssSS();
			sendMap.put("7",getDateyyyyMMddHH);//交易日期和时间10
			sendMap.put("11",getDateHHmmss);//系统跟踪审计号(当天唯一流水号)--6--前端获取
			sendMap.put("12",getDateHHmmss);//当地交易时间6
			sendMap.put("13",getDateMMDD);//当地交易日期4
			sendMap.put("15",getDateMMDD);//清算日期4
			sendMap.put("37",getDateMddHHmmssSS);//调单参考号(交易唯一)
			sendMap.put("41",ShpointsFunction.formatLeft(8,msgMap.get(Constant.POSID),"0"));//终端标识（pos后8位）--前端获取
			sendMap.put("4",ShpointsFunction.formatLeft(12, tranAmount, "0"));//格式化金额
			sendMap.put("48", ShpointsFunction.formatRight(8, realPro.get(Constant.CATALOGUECODE), " ")//放类别
					+ShpointsFunction.formatRight(8, realPro.get(Constant.ITEMCODE), " ")//放物品
					+ShpointsFunction.formatRight(3, realPro.get(Constant.QUANTITY), " ")//放数量
					+ShpointsFunction.FASTTRACK_B+Constant.s200);//goodsNO+proNo+num+type+temp
			Map<String,String> recvMap = simpleClient.sendToBank(sendMap,xmlReader);
			log.info("上海银行21积分+7元活动recv_Msg:"+recvMap.toString());
			ResultCode result= ResultCode.getErrorCode(recvMap.get("39"));//执行交易结果（包含成功和失败）
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE, "99");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT, result.getErr_msg());
			msgMap.put(ShdbUtil.RESPONSEMSG, result.getErr_msg());//上海积分交易状态信息
			msgMap.put(ShdbUtil.ORDERID, msgMap.get(Constant.ORDERID));//
			msgMap.put(Constant.RECEIPTAMOUNT, realPro.get(ShdbUtil.FATTRADENUMC));//商户实收金额
			msgMap.put(Constant.RETCODE, "99");
			msgMap.put(Constant.RETCOMMENT, result.getErr_msg());
			msgMap.put(Constant.BANKORDID, null);
			//这里需要调整这里需要调整******************************************/
			msgMap.put(Constant.RECEIPTAMOUNT, realPro.get(ShdbUtil.FATTRADENUMB));//客户支付金额
			msgMap.put(Constant.BANK_PRICE, realPro.get(ShdbUtil.BQBENEFIT));
			msgMap.put(Constant.CHANNEL_PRICE, "0.00");
			msgMap.put(Constant.BQ_BENEFIT, realPro.get(ShdbUtil.BQBENEFIT));
			msgMap.put(Constant.BQ_GET, realPro.get(ShdbUtil.BQBENEFIT));
			msgMap.put(Constant.CHANNEL_PAY, "0.00");
			msgMap.put(Constant.TRANS_CASH, "7.00");
			msgMap.put(Constant.CUSTOMERPAY, "7.00");
			msgMap.put(Constant.CUSTOMER_CON, msgMap.get(Constant.TRANSCASH));
			msgMap.put(Constant.RETCOMMENT, result.getErr_msg());
			//这里需要调整这里需要调整******************************************/
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,result.getErr_code());
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,result.getErr_msg());
			if(result.getErr_code().equals(Constant.SUCCESS)){//交易成功
				int thisIndex = 0;
				for(int i=0;i<result0240_48.length;i++){
					recvMap.put("48."+(i+1), recvMap.get("48").substring(thisIndex, thisIndex+=result0240_48[i]));
				}
				msgMap.put(Constant.RETCODE, "0000000");
				BCDB.updatePayment(1,msgMap);//更新伯桥交易成功
				recvMap.put(ShdbUtil.RESPONSEMSG, "交易成功");
				recvMap.put(ShdbUtil.ORDERID, msgMap.get(Constant.ORDERID));
				msg = BCMessageUtil.addMsgTag(msg, Constant.TRANSCASH,realPro.get(ShdbUtil.FATTRADENUMC));
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"0000000");
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"交易成功");
				msg = BCMessageUtil.addMsgTag(msg, Constant.ATTENTION,"兑换成功；消费积分为:"+realPro.get(ShdbUtil.POINTSNUM));
				BCDB.insertShpoints(recvMap);//这里插入伯桥交易表
			}else{//交易失败
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,result.getErr_msg());
				BCDB.updatePayment(-1,msgMap);//更新伯桥交易失败
			}
			return msg;
		} catch (Exception e) {
			log.error(e,e);
			ResultCode result= ResultCode.getErrorCode("99");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,result.getErr_code());
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,result.getErr_msg());
			msg = BCMessageUtil.addMsgTag(msg, Constant.ATTENTION,"");
			ShpointsTimeOutJobBean jobBean = new ShpointsTimeOutJobBean(System.currentTimeMillis() + 70000,Mti.TRADE.getMsg_type_snd(),msg,this);
			job.correctionQueue.add(jobBean);
			return msg;
		}
	}
	
	/**
	 * 费率计算小数点后两位四舍五入
	 * @param fat
	 * @param amt
	 * @return
	 */
	public String fatAmt(String fat,String amt){
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(BigDecimal.valueOf(Double.valueOf(amt)).multiply(new BigDecimal(fat)));//str.substring(0, str.indexOf('.')+3);
	}
	/**
	 * 伯乔收益小数点后两位四舍五入
	 * @param bamt
	 * @param camt
	 * @return
	 */
	public String bqbenefit(String bamt,String camt){
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(BigDecimal.valueOf(Double.valueOf(bamt)).subtract(new BigDecimal(camt)));
		
	}
	
	public String transAmt(String tramsAmt){
		return new DecimalFormat("0.00").format(BigDecimal.valueOf(Double.valueOf(tramsAmt)));
	}
	
	public static void main(String[] args) throws Exception {
//		String str = "|FIXSESSIONID=FIX.4.4:BCGW->1012|FIXSESSIONID=FIX.4.4:BCGW->1012|SOH=|VERS=1.00|MTYPE=DPREQ|POSTIME=20160413134803|SUBPOSID=8658|TRMID=axtest3|CUSTID=1012|CUSTNAME=爱茜茜里|CASHERID=axtest3|STOREID=axtest3|SEQ=160413134803506est3|EOH=|TIMEOUT=1500000|ORDERID=160413134803506est3|PAN=6259532009180693|SECONDTRACK=|TRANSAMT=0|TRANSCASH=10.00|PRODTYPE=10120090|ACTTYPE=010506|PRODQNTY=1|PAYMTD=03|DIVIDEDFREQ=1|POSINPUTTYP=031|POSCONDCODE=03|PHONE=|POSID=658728|BATCHNO=160413|SERIALNO=860466|TRANSDATE=20160413|TRANSTIME=134803|DISCNTAMT=0|ACTIVITY=上海银行积分支付|BANKNAME=上海银行|GOODSINFO=null|RETCODE=99|RETCOMMENT=系统错误|EOM=|";
//		PaymentServicesImp imp = new PaymentServicesImp();
//		imp.shpointsAccountQuery(str);
//		System.out.println(transAmt("12.015"));
//		System.out.println(Integer.parseInt((new DecimalFormat("0.00").format(new BigDecimal("1.0")))) );
	}
	
	/**
	 * @Title:2 shpointsRefund
	 * @Description: 积分兑奖撤销
	 * @param msg
	 * @param orderInfo
	 * @return
	 * String    返回类型
	 * @throws
	 * @param msg
	 * @param orderInfo
	 * @return
	 * @throws SQLException 
	 */
	public String shpointsRefund(String msg, HashMap<String, String> orderInfo) throws SQLException{
		log.info("cancel function");
		Map<String,String> msgMap = BCMessageUtil.parseBcpayMsg2Map(msg);
		//		-1异常，0未交易，1积分兑换成功，2，积分撤销，3积分兑换冲正，4积分撤销冲正，5积分申请退货
		Map<String, String> selectPoints = BCDB.selectShpointsByOrderid(orderInfo.get(Constant.ORDERID));
		if(selectPoints!=null){
			try {
				Map<String,String> requestMap = new HashMap<String,String>();
		    	requestMap.put(SimpleConstants.MTI,Mti.CANCEL.getMsg_type_snd());
		    	requestMap.put("2",orderInfo.get(Constant.PAN));
		    	requestMap.put("7",DateUtil.getDateyyyyMMddHH());//交易日期和时间10
		    	requestMap.put("11",DateUtil.getDateHHmmss());//系统跟踪审计号(当天唯一流水号)--6
		    	requestMap.put("12",DateUtil.getDateHHmmss());//当地交易时间6
		    	requestMap.put("13",DateUtil.getDateMMDD());//当地交易日期4
		    	requestMap.put("15",DateUtil.getDateMMDD());//清算日期4
		    	requestMap.put("37",DateUtil.getDateMddHHmmssSS());//调单参考号12默认空
		    	requestMap.put("41",msgMap.get(Constant.POSID));//终端标识（pos后8位）
		    	requestMap.put("4","000000000000");//25(50)-32(04012900)-42(bcpos)-49()
		    	requestMap.put("48", selectPoints.get(ShdbUtil.REFERENCENUMBER));//兑换参考号码()--10-11-12
				Map<String,String> resultMap = simpleClient.sendToBank(requestMap,xmlReader);
				log.info("积分兑奖撤销recv_Msg:"+resultMap.toString());
				resultMap.put(ShdbUtil.ORDERID, orderInfo.get(Constant.ORDERID));
				ResultCode resultCode = ResultCode.getErrorCode(resultMap.get("39"));
				if(resultCode.getErr_code().endsWith(Constant.SUCCESS)){//积分兑奖撤销成功
					resultMap.put(ShdbUtil.RESPONSEMSG, "兑换撤销成功");
					BCDB.insertCancel(2,msgMap);//插入伯桥撤销2撤销
					BCDB.updateShpointsByOrderid(resultMap);//根据订单号更新上海银行积分记录
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"0000000");
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,resultCode.getErr_msg());
					msg = BCMessageUtil.addMsgTag(msg, Constant.ATTENTION,resultCode.getErr_msg());

				}else{//积分兑奖撤销失败
//					BCDB.insertCancel(2,msgMap);//插入伯桥撤销2撤销
//					BCDB.updateShpointsByOrderid(resultMap);//根据订单号更新上海银行积分记录
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
	    			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,resultCode.getErr_msg());
				}
			} catch (Exception e) {
				log.error("insert refund trade error");
				log.error(e,e);
				ResultCode result= ResultCode.getErrorCode("99");
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,result.getErr_code());
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,result.getErr_msg());
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,result.getErr_msg());
				msg = BCMessageUtil.addMsgTag(msg, Constant.ATTENTION,"");
				ShpointsTimeOutJobBean jobBean = new ShpointsTimeOutJobBean(System.currentTimeMillis() + 70000,Mti.CANCEL.getMsg_type_snd(),msg,this);
				job.correctionQueue.add(jobBean);
			}
		}else{
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE, "99");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT, "上海银行积分交易表不存在该笔交易");
		}
		return msg;
	}
	
	/**
	 * 3积分兑换冲正
	 * @param reverseMap
	 * @return
	 * @throws Exception 
	 */
	public String shpointsDpreqReverse(String msg,Map<String,String> reverseMap) throws Exception{
		String getDateyyyyMMddHH = DateUtil.getDateyyyyMMddHH();
		String getDateHHmmss = DateUtil.getDateHHmmss();
		String getDateMMDD = DateUtil.getDateMMDD();
		String getDateMddHHmmssSS = DateUtil.getDateMddHHmmssSS();
		Map<String,String> pointsMap = BCDB.selectShpointsByOrderid(reverseMap.get(Constant.ORDERID));
		HashMap<String,String> msgMap = BCMessageUtil.parseBcpayMsg2Map(msg);
		Map<String,String> requestMap = new HashMap<String,String>();
		requestMap.put(SimpleConstants.MTI,Mti.REVERSE.getMsg_type_snd());
		requestMap.put("2",reverseMap.get(Constant.PAN));
		requestMap.put("7",getDateyyyyMMddHH);//交易日期和时间10
		requestMap.put("11",getDateHHmmss);//系统跟踪审计号(当天唯一流水号)--6
		requestMap.put("12",getDateHHmmss);//当地交易时间6
		requestMap.put("13",getDateMMDD);//当地交易日期4
		requestMap.put("15",getDateMMDD);//清算日期4
		requestMap.put("33",ShpointsContainer._33);//收单机构识别代码04012900右补充空格
		requestMap.put("37",getDateMddHHmmssSS);//调单参考号12默认空
		requestMap.put("41",msgMap.get(Constant.ORDERID));//终端标识（pos后8位）
		requestMap.put("4","000000000000");//25(50)-32(04012900)-42(bcpos)-49()
		String REFERENCENUMBER = pointsMap.get(ShdbUtil.REFERENCENUMBER);
		String TRANSACTIONDATEANDTIME = pointsMap.get(ShdbUtil.TRANSACTIONDATEANDTIME);
		
		requestMap.put("48", REFERENCENUMBER);//兑换参考号码()--10-11-12
		requestMap.put("90", "0240"+REFERENCENUMBER+TRANSACTIONDATEANDTIME+ShpointsContainer._32+ShpointsContainer._32);//Original Message Type 4  	原始消息类别
		Map<String,String> resultMap = simpleClient.sendToBank(requestMap,xmlReader);ResultCode resultCode = ResultCode.getErrorCode(resultMap.get("39"));
		log.info("积分兑换冲正recv_Msg:"+resultMap.toString());
		resultMap.put(ShdbUtil.ORDERID, reverseMap.get(Constant.ORDERID));
		if(resultCode.getErr_code().endsWith(Constant.SUCCESS)){//积分兑换冲正成功
			BCDB.insertCancel(5,msgMap);//插入到伯桥销表
			msgMap.put(ShdbUtil.RESPONSEMSG, "兑换冲正成功");
			BCDB.updateShpointsByOrderid(msgMap);//根据订单号更新上海银行积分记录
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"0000000");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,resultCode.getErr_msg());
			msg = BCMessageUtil.addMsgTag(msg, Constant.ATTENTION,resultCode.getErr_msg());
		} else {//交易失败
//			BCDB.insertCancel(1,msgMap);//插入伯桥2积分兑换冲正
//			BCDB.updateShpointsByOrderid(msgMap);//根据订单号更新上海银行积分记录
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,resultCode.getErr_msg());
		}
		return msg;
	}
	
	/**
	 * 4积分兑换撤销冲正
	 * @param msg
	 * @param reverseMap
	 * @return
	 * @throws Exception 
	 */
	public String shpointsCancelReverse(String msg,Map<String,String> reverseMap) throws Exception{
		String getDateyyyyMMddHH = DateUtil.getDateyyyyMMddHH();
		String getDateHHmmss = DateUtil.getDateHHmmss();
		String getDateMMDD = DateUtil.getDateMMDD();
		String getDateMddHHmmssSS = DateUtil.getDateMddHHmmssSS();
		Map<String,String> pointsMap = BCDB.selectShpointsByOrderid(reverseMap.get(Constant.ORDERID));
		HashMap<String,String> msgMap = BCMessageUtil.parseBcpayMsg2Map(msg);
		Map<String,String> requestMap = new HashMap<String,String>();
		requestMap.put(SimpleConstants.MTI,Mti.CANCELREVERSE.getMsg_type_snd());
		requestMap.put("2",reverseMap.get(Constant.PAN));
		requestMap.put("7",getDateyyyyMMddHH);//交易日期和时间10
		requestMap.put("11",getDateHHmmss);//系统跟踪审计号(当天唯一流水号)--6
		requestMap.put("12",getDateHHmmss);//当地交易时间6
		requestMap.put("13",getDateMMDD);//当地交易日期4
		requestMap.put("15",getDateMMDD);//清算日期4
		requestMap.put("33",ShpointsContainer._33);//收单机构识别代码04012900右补充空格
		requestMap.put("37",getDateMddHHmmssSS);//调单参考号12默认空
		requestMap.put("41",msgMap.get(Constant.POSID));//终端标识（pos后8位）
		requestMap.put("4","000000000000");//25(50)-32(04012900)-42(bcpos)-49()
		String REFERENCENUMBER = pointsMap.get(ShdbUtil.REFERENCENUMBER);
		String TRANSACTIONDATEANDTIME = pointsMap.get(ShdbUtil.TRANSACTIONDATEANDTIME);
		requestMap.put("48", REFERENCENUMBER);//兑换参考号码()--10-11-12
		requestMap.put("90", "0260"+REFERENCENUMBER+TRANSACTIONDATEANDTIME+ShpointsContainer._32+ShpointsContainer._32);//Original Message Type 4  	原始消息类别
		Map<String,String> resultMap = simpleClient.sendToBank(requestMap,xmlReader);
		log.info("积分兑换撤销冲正recv_Msg:"+resultMap.toString());
		resultMap.put(ShdbUtil.ORDERID, reverseMap.get(Constant.ORDERID));
		ResultCode resultCode = ResultCode.getErrorCode(resultMap.get("39"));
		if(resultCode.getErr_code().endsWith(Constant.SUCCESS)){//积分兑换冲正成功
//			BCDB.insertCancel(1,msgMap);//插入到伯桥销表
			msgMap.put(ShdbUtil.RESPONSEMSG, "兑换冲正成功");
			BCDB.updateShpointsByOrderid(msgMap);//根据订单号更新上海银行积分记录
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"0000000");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,resultCode.getErr_msg());
			msg = BCMessageUtil.addMsgTag(msg, Constant.ATTENTION,resultCode.getErr_msg());
		} else {//交易失败
//			BCDB.insertCancel(1,msgMap);//插入伯桥2积分兑换冲正
//			BCDB.updateShpointsByOrderid(msgMap);//根据订单号更新上海银行积分记录
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,resultCode.getErr_msg());
		}
		return msg;
	}
	
	/**
	 * 6积分退货申请
	 * @param msg
	 * @param reverseMap
	 * @return
	 * @throws Exception 
	 */
	public String shpointsSalesReturn(String msg,Map<String,String> reverseMap) throws Exception{
		String getDateyyyyMMddHH = DateUtil.getDateyyyyMMddHH();
		String getDateHHmmss = DateUtil.getDateHHmmss();
		String getDateMMDD = DateUtil.getDateMMDD();
		String getDateMddHHmmssSS = DateUtil.getDateMddHHmmssSS();
		Map<String,String> pointsMap = BCDB.selectShpointsByOrderid(reverseMap.get(Constant.ORDERID));
		if(ShpointsFunction.isToday(pointsMap.get(ShdbUtil.TRANSACTIONDATEANDTIME))){
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"0");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"该交易没有入库，请隔日做退款");
			return msg;
		}
		HashMap<String,String> msgMap = BCMessageUtil.parseBcpayMsg2Map(msg);
		Map<String,String> requestMap = new HashMap<String,String>();
    	requestMap.put(SimpleConstants.MTI,Mti.REFUND.getMsg_type_snd());
    	requestMap.put("2",pointsMap.get(ShdbUtil.ACCOUNTNUMBER));
    	requestMap.put("7",getDateyyyyMMddHH);//交易日期和时间10
    	requestMap.put("11",getDateHHmmss);//系统跟踪审计号(当天唯一流水号)--6
    	requestMap.put("12",getDateHHmmss);//当地交易时间6
    	requestMap.put("13",getDateMMDD);//当地交易日期4
    	requestMap.put("15",getDateMMDD);//清算日期4
    	requestMap.put("37",getDateMddHHmmssSS);//调单参考号12默认空
    	requestMap.put("41",Constant.POSID);//终端标识（pos后8位）
    	requestMap.put("4","000000000000");//25(50)-32(04012900)-42(bcpos)-49()
    	String REFERENCENUMBER = pointsMap.get(ShdbUtil.REFERENCENUMBER);
    	requestMap.put("48", REFERENCENUMBER);//兑换参考号码()--10-11-12-16
		Map<String,String> resultMap = simpleClient.sendToBank(requestMap,xmlReader);
		log.info("积分退货申请recv_Msg:"+resultMap.toString());
		resultMap.put(ShdbUtil.ORDERID, reverseMap.get(Constant.ORDERID));
		ResultCode resultCode = ResultCode.getErrorCode(resultMap.get("39"));
		if(resultCode.getErr_code().endsWith(Constant.SUCCESS)){//积分兑换冲正成功
			BCDB.insertCancel(2,msgMap);//插入到伯桥销表
			int delete = BCDB.deleteFromTinfoDdtClearAccount(reverseMap.get(Constant.CUSTID),
					reverseMap.get(Constant.PAN), reverseMap.get(Constant.SERIALNO));//删除伯桥结算表数据
			resultMap.put(ShdbUtil.RESPONSEMSG, "退货成功");
			int upstatus = BCDB.updateShpointsByOrderid(resultMap);//根据订单号更新上海银行积分记录
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"0");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,resultCode.getErr_msg());
			msg = BCMessageUtil.addMsgTag(msg, Constant.ATTENTION,resultCode.getErr_msg());
			log.info("delete:"+delete+"\tupstatus:"+upstatus);
		} else {//交易失败
//			BCDB.insertCancel(1,msgMap);//插入伯桥2积分兑换冲正
//			BCDB.updateShpointsByOrderid(msgMap);//根据订单号更新上海银行积分记录
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"0");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,resultCode.getErr_msg());
		}
		return msg;
	}
	
	/**
	 * 查询交易信息
	 * @param msg
	 * @param reverseMap
	 * @return
	 */
	public String queryShpointsSalesReturn(String msg,Map<String,String> reverseMap){
		if(reverseMap==null){
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"0");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"未找到原交易");
			return msg;
		}
		msg = BCMessageUtil.addMsgTag(msg, Constant.RETSTATUS,reverseMap.get(Constant.STATUS));
		msg = BCMessageUtil.addMsgTag(msg, Constant.BATCHNO,reverseMap.get(Constant.BATCHNO));
		msg = BCMessageUtil.addMsgTag(msg, Constant.SERIALNO,reverseMap.get(Constant.SERIALNO));
		msg = BCMessageUtil.addMsgTag(msg, Constant.TRANSAMT,reverseMap.get(Constant.TRANSAMT));
		msg = BCMessageUtil.addMsgTag(msg, "PRODUCTNUM","1");
		msg = BCMessageUtil.addMsgTag(msg, Constant.PRODTYPE,reverseMap.get(Constant.PRODTYPE));
		msg = BCMessageUtil.addMsgTag(msg, Constant.AUTHCODE,reverseMap.get(Constant.AUTHCODE));
		msg = BCMessageUtil.addMsgTag(msg, Constant.AUTHDATE,reverseMap.get(Constant.AUTHDATE));
		msg = BCMessageUtil.addMsgTag(msg, Constant.AUTHTIME,reverseMap.get(Constant.AUTHTIME));
		msg = BCMessageUtil.addMsgTag(msg, Constant.ORDERID,reverseMap.get(Constant.ORDERID));
		msg = BCMessageUtil.addMsgTag(msg, Constant.PAN,reverseMap.get(Constant.PAN));
		msg = BCMessageUtil.addMsgTag(msg, Constant.BANKNAME,reverseMap.get(Constant.BANKNAME));
		msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"0");
		msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"查询成功");
		return msg;
	}
	
	/***
	 * 5积分余额查询
	 * @param msg
	 * @return
	 */
	public String shpointsAccountQue(String msg){
		Map<String,String> pointsMap = shpointsAccountQuery(msg);
		if(pointsMap.get("39")==null||!pointsMap.get("39").equals("00")){
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE, "14");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT, "卡号或账户状态不正常");
			return msg;
		}
		if(pointsMap.get("errorCode")==null){//查询成功
		Map<String,String> realPro = BCDB.getBcproductrealMap().get(""+Integer.parseInt(BCMessageUtil.GetValue(msg, Constant.PRODTYPE).substring(4)));
		String transAmt = new DecimalFormat("0.00").format(new BigDecimal(pointsMap.get("48.5")).divide(new BigDecimal(realPro.get(ShdbUtil.POINTSNUM))));
		transAmt = new DecimalFormat("0.00").format(new BigDecimal(transAmt).divide(new BigDecimal("100")));
			msg = BCMessageUtil.addMsgTag(msg, Constant.TRANSCASH, transAmt);
			msg = BCMessageUtil.addMsgTag(msg, Constant.POINTAMT, pointsMap.get("48.5"));
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE, "0000000");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT, "交易成功");
			return msg;
		} else {//查询失败
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE, "99");
			msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT, pointsMap.get("errorMsg"));
			return msg;
		}
	}
	
	
	
	/**
	 *  查询积分
	 * @Title: shpointsAccountQuery
	 * @Description: 查询积分
	 * @param msg
	 * resultMap.get("errorCode");结果为空表示查询成功
	 * @return
	 * String    返回类型
	 * @param msg
	 * resultMap.get("errorMsg");结果为空表示查询成功
	 * 			48.5表示当月可用积分
	 * @return
	 * 
	 */
	public Map<String ,String> shpointsAccountQuery(String msg){
		log.info("query function");
		HashMap<String,String> msgMap = BCMessageUtil.parseBcpayMsg2Map(msg);
		int[] result0320_48 = new int[]{8,8,8,8,8};
    	Map<String,String> requestMap = new HashMap<String,String>();
    	requestMap.put(SimpleConstants.MTI,Mti.QUERY.getMsg_type_snd());
    	requestMap.put("2",msgMap.get(Constant.PAN));
    	requestMap.put("4","000000000000");//25(50)-32(04012900)-42(bcpos)-49()
    	requestMap.put("7",DateUtil.getDateyyyyMMddHH());//交易日期和时间10
    	requestMap.put("11",DateUtil.getDateHHmmss());//系统跟踪审计号(当天唯一流水号)--6--前端获取
    	requestMap.put("12",DateUtil.getDateHHmmss());//当地交易时间6
    	requestMap.put("13",DateUtil.getDateMMDD());//当地交易日期4
    	requestMap.put("15",DateUtil.getDateMMDD());//清算日期4
    	requestMap.put("37",DateUtil.getDateMddHHmmssSS());//调单参考号(交易唯一)
    	requestMap.put("41",msgMap.get(Constant.POSID));//终端标识（pos后8位）--前端获取
    	
    	try {
			Map<String,String> resultMap = simpleClient.sendToBank(requestMap,xmlReader);
			log.info("积分余额查询recv_Msg:"+resultMap.toString());
			int thisIndex = 0;
			if(resultMap.get("39").equals(Constant.SUCCESS)){
				for(int i=0;i<result0320_48.length;i++){
					resultMap.put("48."+(i+1), resultMap.get("48").substring(thisIndex, thisIndex+=result0320_48[i]));
				}
				return resultMap;
			}else{
				ResultCode resultCode = ResultCode.getErrorCode(resultMap.get("39"));
				resultMap.put("errorCode", "99");
				resultMap.put("errorMsg", resultCode.getErr_msg());
				return resultMap;
			}
		} catch (Exception e) {
			log.error(e,e);
			Map<String,String> resultMap = new HashMap<String,String>();
			ResultCode resultCode = ResultCode.getErrorCode(resultMap.get("39"));
			resultMap.put("errorCode", "99");
			resultMap.put("errorMsg", resultCode.getErr_msg());
			return resultMap;
		}
	}
	
	
	/**
	 * 根据批次号和序列查伯桥交易表
	 * @param batchno
	 * 				批次号
	 * @param serialno
	 * 				序列号
	 * @return
	 */
	public HashMap<String,String> queryPaymentTradePan(String batchno, String serialno ,String custid ,String pan){
		try {
			return BCDB.queryPaymentTradePan(batchno, serialno, custid ,pan);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 *根据ORDERID批次号流水号更新订单信息
	 * @param status
	 * @param orderid
	 * @param batchno
	 * @param serialno
	 * @return
	 * @throws SQLException
	 */
	public int updatecitiStatusSql(int status, String orderid, String batchno, String serialno){
		try {
			return BCDB.updatecitiStatusSql(status, orderid, batchno, serialno);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 *根据ORDERID更新状态信息
	 * @throws SQLException
	 */
	public int updateShpointsStatusByOrderid(String respMsg, String orderid){
		try {
			return BCDB.updateShpointsStatusByOrderid(respMsg, orderid);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 根据批次号和序列查伯桥交易表
	 * @param batchno
	 * 				批次号
	 * @param serialno
	 * 				序列号
	 * @return
	 */
	public HashMap<String,String> selectPaymentCITISQL(String batchno, String serialno ,String custid){
		try {
			return BCDB.selectPaymentCITISQL(batchno, serialno, custid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 先查询正常交易
	 * @param orderid
	 * @param terminalId
	 * @param storeId
	 * @return
	 */
	public HashMap<String,String> getOrderInfo(String orderid, String terminalId, String storeId){
		try {
			return BCDB.queryPaymentSuccessPan(orderid,terminalId,storeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 先查询正常交易
	 * @param orderid
	 * @param terminalId
	 * @param storeId
	 * @return
	 */
	public HashMap<String,String> OrderInfo(String orderid, String terminalId, String storeId){
		try {
			return BCDB.queryPaymentSuccessPan(orderid,terminalId,storeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String resetCash(String POSInMsg){
		String retMsg;
		retMsg = POSInMsg.replace("RESETCASHREQ", "RESETCASHRESP");
		log.info("ready to reset cash");
		BCDB.reset();
		log.info("reset finish");
		return retMsg;
	}
	
	public HashMap<String,String> getItemByBcItemId(String bcItemId){
		HashMap<String, String> map = new HashMap<String, String>();
		String bcItems[] = bcItemId.split("&");                        //由前端选取2D或者3D
		String bcItemAndQnty[];
		String itemId;
		String qnty;
		for(int i=0; i<bcItems.length; i++){
			if(bcItems[i] == null || "".equals(bcItems[i])){
				break;
			}
			bcItemAndQnty = bcItems[i].split("\\^");
			itemId = bcItemAndQnty[0];
			qnty = bcItemAndQnty[1];
			if(Integer.parseInt(qnty) > 0 ){
				map.put("itemId", itemId);
				map.put("qnty", qnty);
				break;
			}
		}
		return map;
	}
	private String formatNumberString(String getValue, int totalLen) {

		String value = getValue.replace(".", "");
		int length = value.length();
		if (length >= totalLen)
			return value;
		else {
			StringBuffer buffer = new StringBuffer(totalLen);
			for (int i = length; i < totalLen; i++) {
				buffer.append('0');
			}
			buffer.append(value);
			return buffer.toString();
		}
	}
	
	/**判断卡号是否为16位*/
	public static boolean isNotCardNo(String str) {  
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile( "^[0-9]{16}$");  
        java.util.regex.Matcher match = pattern.matcher(str.trim());  
        return match.matches();  
    }
	
	/**
	 * 检查是否权益用完
	 * @param tradeCash
	 * @param msg
	 * @param msgMap
	 * @param tranAmount
	 * @return
	 */
    public static String checkTrade(BigDecimal tradeCash,String msg,Map msgMap,String tranAmount,BigDecimal daypointsNum){
    	tradeCash = tradeCash.add(new BigDecimal((String)msgMap.get(Constant.TRANSCASH)));
		if(msgMap.get(Constant.CUSTID).equals("1073")){//唐纳滋30
			if(Integer.parseInt(tranAmount)!=3000){
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"支持单次兑换金额30元!");
				return msg;
			}
//					557	上海银行店面积分兑换-美仕唐纳滋
//					tradeCash = tradeCash.add(new BigDecimal(msgMap.get(Constant.TRANSCASH)));
				if(daypointsNum.compareTo(tradeCash)<0){ //超出单日限额60.000元	
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"您当天兑换金额限"+daypointsNum+"元！");
					return msg;
				}
		}else if(msgMap.get(Constant.CUSTID).equals("1013")){//西树泡芙-摩提集团18
			if(Integer.parseInt(tranAmount)!=1800){//交易金额不符
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
				msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"支持单次兑换金额18元!");
				return msg;
			}
//					558	上海银行店面积分兑换-西树泡芙
//					559	上海银行店面积分兑换-西树泡芙
//					BigDecimal tradeCash = BCDB.sumCitipaymentByNote1ByBatchno(msgMap.get(Constant.CUSTOMERID), batchNo, prodid);
//					tradeCash.add(new BigDecimal(msgMap.get(Constant.TRANSCASH)));
				if(daypointsNum.compareTo(tradeCash)<0){ //超出单日限额36.000元	
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"您当天兑换金额限"+daypointsNum+"元！");
					return msg;
				}
				
		}else if(msgMap.get(Constant.CUSTID).equals("1034")){//来伊份
//					555	上海银行店面积分兑换-来伊份
//					BigDecimal tradeCash = BCDB.sumCitipaymentByNote1ByBatchno(msgMap.get(Constant.CUSTOMERID), batchNo, prodid);
//					tradeCash.add(new BigDecimal(msgMap.get(Constant.TRANSCASH)));
				if(daypointsNum.compareTo(tradeCash)<0){ //超出单日限额100.000元	
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"您当天兑换金额限"+daypointsNum+"元！");
					return msg;
				}
		}else if(msgMap.get(Constant.CUSTID).equals("1063")){//元祖
//					556	上海银行店面积分兑换-元祖
//					BigDecimal tradeCash = BCDB.sumCitipaymentByNote1ByBatchno(msgMap.get(Constant.CUSTOMERID), batchNo, prodid);
//					tradeCash.add(new BigDecimal(msgMap.get(Constant.TRANSCASH)));
				if(daypointsNum.compareTo(tradeCash)<0){ //超出单日限额100.000元	
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCODE,"99");
					msg = BCMessageUtil.addMsgTag(msg, Constant.RETCOMMENT,"您当天兑换金额限"+daypointsNum+"元！");
					return msg;
				}
		}
    	return null;
    }
	
}
