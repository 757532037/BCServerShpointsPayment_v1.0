package bc.core.server;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import bc.core.utils.Constant;
import bc.core.utils.ShdbUtil;

public class BCRecData {
	
	public BCRecData(DataSource dataSource) {
		this.datasource = dataSource;
	}
	
	private static final Logger logger = Logger.getLogger(BCRecData.class);
	private DataSource datasource;
	private String testConnSQL;
	private Connection conn = null;

	public String getTestConnSQL() {
		return testConnSQL;
	}

	public void setTestConnSQL(String testConnSQL) {
		this.testConnSQL = testConnSQL;
	}

	private HashMap<String, String> errcode_Map; //错误编码对应错误描述
	private HashMap<String, String> bc_product_map;  //伯乔活动名称 <活动id，活动名称>
	private HashMap<String, Map<String,String>> bcproductrealMap;  //伯乔活动名称 <活动id，活动名称>
	private String insertPaymentSQL;
	private String insertCancelSQL;
	private String updatePaymetStatusSql;
	private String updateCancelStatusSP;

	private String selectPayingSql;
	
	private String selectErrCodeSql;
	private String selectBcProdSql;
	private String selectParaSQL;
	private String getUsedNumSP;
	private String selectPaymentSuccessSQL;
	private String selectPaymentCITISQL;
	private String selectPaymentSuccessByOrderidSQL;
	private String selectPbcRightSendSql;
	/**根据NOTE1和BATCHNO和活动编号查询成功的交易次数*/
	private String countCitipaymentByNote1ByBatchno;
	/**根据NOTE1和BATCHNO和活动编号查询成功的交易金额*/
	private String sumCitipaymentByNote1ByBatchno;
	/**根据NOTE1和BATCHNO和活动编号查询成功的交易金额*/
	public String getSumCitipaymentByNote1ByBatchno() {
		return sumCitipaymentByNote1ByBatchno;
	}
	/**根据NOTE1和BATCHNO和活动编号查询成功的交易金额*/
	public void setSumCitipaymentByNote1ByBatchno(
			String sumCitipaymentByNote1ByBatchno) {
		this.sumCitipaymentByNote1ByBatchno = sumCitipaymentByNote1ByBatchno;
	}

	/**根据NOTE1和BATCHNO和活动编号查询成功的交易次数*/
	public String getCountCitipaymentByNote1ByBatchno() {
		return countCitipaymentByNote1ByBatchno;
	}
	/**根据NOTE1和BATCHNO和活动编号查询成功的交易次数*/
	public void setCountCitipaymentByNote1ByBatchno(
			String countCitipaymentByNote1ByBatchno) {
		this.countCitipaymentByNote1ByBatchno = countCitipaymentByNote1ByBatchno;
	}
	/**退货撤销结算数据*/
	private String deleteFromTinfoDdtClearAccount;
	/**退货撤销结算数据*/
	public String getDeleteFromTinfoDdtClearAccount() {
		return deleteFromTinfoDdtClearAccount;
	}
	/**退货撤销结算数据*/
	public void setDeleteFromTinfoDdtClearAccount(
			String deleteFromTinfoDdtClearAccount) {
		this.deleteFromTinfoDdtClearAccount = deleteFromTinfoDdtClearAccount;
	}

	/**根据ORDERID更改状态*/
	private String updateShpointsStatusByOrderid;
	/**根据ORDERID更改状态*/
	public String getUpdateShpointsStatusByOrderid() {
		return updateShpointsStatusByOrderid;
	}
	/**根据ORDERID更改状态*/
	public void setUpdateShpointsStatusByOrderid(
			String updateShpointsStatusByOrderid) {
		this.updateShpointsStatusByOrderid = updateShpointsStatusByOrderid;
	}

	private String getTSNSP;
	/**冲正撤销更新状态*/
	private String updatecitiStatusSql;
	/**冲正撤销更新状态*/
	public String getUpdatecitiStatusSql() {
		return updatecitiStatusSql;
	}
	/**冲正撤销更新状态*/
	public void setUpdatecitiStatusSql(String updatecitiStatusSql) {
		this.updatecitiStatusSql = updatecitiStatusSql;
	}

	/**查询上海银行积分表*/
	private String selectShpointsByOrderid;
	/**插入上海银行积分表*/
	private String insertShpoints;
	/**更新上海银行积分表*/
	private String updateShpointsByOrderid;
	/**根据物品代码查看物品管理表*/
	private String selectShpointsprorealByItem;
	
	/**查询商品关联
	 * 
	 * ord：
	 * select rea.cataloguecode,rea.itemcode,rea.bcproid,rea.comm,rea.fasttrack,
				rea.pointsnum,rea.start_date,rea.end_date,rea.fat_trade_num_c,rea.fat_trade_num_b,
				rea.bq_benefit,rea.activitday,rea.daypointsnum from bccore.shpoints_bqsh_proreal rea where 1=1
	*new:
	*select rea.cataloguecode,rea.itemcode,rea.bcproid,rea.comm,rea.fasttrack,
        rea.pointsnum,tip.tip_start_date as start_date,tip.tip_end_date as end_date,tim.fat_rate_c as fat_trade_num_c,
        tim.fat_rate_b as fat_trade_num_b,
        tim.fat_bank_price as bq_benefit,rea.activitday,rea.daypointsnum from bccore.shpoints_bqsh_proreal rea 
        ,bcmis.t_info_mode tim ,bcmis.t_info_product tip where tip.tip_mode_id=tim.fat_id and rea.bcproid = tip.tip_product_id
	 * */
	private String selectAllShpointsproreal;
	/**查询当前所有210积分+7元兑换爱西西里交易*/
	private String selectAllShpoints210Trade;
	/**查询今天210积分+7元兑换爱西西里交易*/
	private String selectTodayShpoints210Trade;
	/**查询本月上海银行210积分+7元兑换爱西西里CUSTID信息*/
	private String selectShbkCustInfo;
	/**根据和custid查询citi中当天出现次数*/
	private String selectThisPanShbk210TradesInfo;
	/**根据和custid查询citi中当天出现次数*/
	public String getSelectThisPanShbk210TradesInfo() {
		return selectThisPanShbk210TradesInfo;
	}
	/**根据和custid查询citi中当天出现次数*/
	public void setSelectThisPanShbk210TradesInfo(
			String selectThisPanShbk210TradesInfo) {
		this.selectThisPanShbk210TradesInfo = selectThisPanShbk210TradesInfo;
	}

	/**上海银行权益CUSTID信息插入*/
	private String insertShbkCustInfo;
	
	/**查询上海银行积分表*/
	public String getSelectShpointsByOrderid() {
		return selectShpointsByOrderid;
	}
	/**查询上海银行积分表*/
	public void setSelectShpointsByOrderid(String selectShpointsByOrderid) {
		this.selectShpointsByOrderid = selectShpointsByOrderid;
	}
	/**插入上海银行积分表*/
	public String getInsertShpoints() {
		return insertShpoints;
	}
	/**插入上海银行积分表*/
	public void setInsertShpoints(String insertShpoints) {
		this.insertShpoints = insertShpoints;
	}
	/**更新上海银行积分表*/
	public String getUpdateShpointsByOrderid() {
		return updateShpointsByOrderid;
	}
	/**更新上海银行积分表*/
	public void setUpdateShpointsByOrderid(String updateShpointsByOrderid) {
		this.updateShpointsByOrderid = updateShpointsByOrderid;
	}
	/**根据物品代码查看物品管理表*/
	public String getSelectShpointsprorealByItem() {
		return selectShpointsprorealByItem;
	}
	/**根据物品代码查看物品管理表*/
	public void setSelectShpointsprorealByItem(String selectShpointsprorealByItem) {
		this.selectShpointsprorealByItem = selectShpointsprorealByItem;
	}
	/**查询商品关联*/
	public String getSelectAllShpointsproreal() {
		return selectAllShpointsproreal;
	}
	/**查询商品关联*/
	public void setSelectAllShpointsproreal(String selectAllShpointsproreal) {
		this.selectAllShpointsproreal = selectAllShpointsproreal;
	}
	/**查询当前所有210积分+7元兑换爱西西里交易*/
	public String getSelectAllShpoints210Trade() {
		return selectAllShpoints210Trade;
	}
	/**查询当前所有210积分+7元兑换爱西西里交易*/
	public void setSelectAllShpoints210Trade(String selectAllShpoints210Trade) {
		this.selectAllShpoints210Trade = selectAllShpoints210Trade;
	}
	/**查询本月上海银行210积分+7元兑换爱西西里CUSTID信息*/
	public String getSelectShbkCustInfo() {
		return selectShbkCustInfo;
	}
	/**查询本月上海银行210积分+7元兑换爱西西里CUSTID信息*/
	public void setSelectShbkCustInfo(String selectShbkCustInfo) {
		this.selectShbkCustInfo = selectShbkCustInfo;
	}
	/**查询今天210积分+7元兑换爱西西里交易*/
	public String getSelectTodayShpoints210Trade() {
		return selectTodayShpoints210Trade;
	}
	/**查询今天210积分+7元兑换爱西西里交易*/
	public void setSelectTodayShpoints210Trade(String selectTodayShpoints210Trade) {
		this.selectTodayShpoints210Trade = selectTodayShpoints210Trade;
	}

	/**上海银行权益CUSTID信息插入*/
	public String getInsertShbkCustInfo() {
		return insertShbkCustInfo;
	}
	/**上海银行权益CUSTID信息插入*/
	public void setInsertShbkCustInfo(String insertShbkCustInfo) {
		this.insertShbkCustInfo = insertShbkCustInfo;
	}

	public String getSelectPaymentCITISQL() {
		return selectPaymentCITISQL;
	}

	public void setSelectPaymentCITISQL(String selectPaymentCITISQL) {
		this.selectPaymentCITISQL = selectPaymentCITISQL;
	}

	private int tsnBlockMin = -1;
	private int tsnBlockMax = -1;
	private int tsnBlockSize;
	
	
	public int getTsnBlockSize() {
		return tsnBlockSize;
	}

	public void setTsnBlockSize(int tsnBlockSize) {
		this.tsnBlockSize = tsnBlockSize;
	}
	
	public String getGetTSNSP() {
		return getTSNSP;
	}

	public void setGetTSNSP(String getTSNSP) {
		this.getTSNSP = getTSNSP;
	}

	public String getInsertPaymentSQL() {
		return insertPaymentSQL;
	}

	public void setInsertPaymentSQL(String insertPaymentSQL) {
		this.insertPaymentSQL = insertPaymentSQL;
	}

	public String getInsertCancelSQL() {
		return insertCancelSQL;
	}

	public void setInsertCancelSQL(String insertCancelSQL) {
		this.insertCancelSQL = insertCancelSQL;
	}

	public String getUpdateCancelStatusSP() {
		return updateCancelStatusSP;
	}

	public void setUpdateCancelStatusSP(String updateCancelStatusSP) {
		this.updateCancelStatusSP = updateCancelStatusSP;
	}

	public String getUpdatePaymetStatusSql() {
		return updatePaymetStatusSql;
	}
	
	public void setUpdatePaymetStatusSql(String updatePaymetStatusSql) {
		this.updatePaymetStatusSql = updatePaymetStatusSql;
	}
	
	public String getSelectPaymentSuccessByOrderidSQL() {
		return selectPaymentSuccessByOrderidSQL;
	}

	public void setSelectPaymentSuccessByOrderidSQL(
			String selectPaymentSuccessByOrderidSQL) {
		this.selectPaymentSuccessByOrderidSQL = selectPaymentSuccessByOrderidSQL;
	}

	public String getSelectErrCodeSql() {
		return selectErrCodeSql;
	}

	public void setSelectErrCodeSql(String selectErrCodeSql) {
		this.selectErrCodeSql = selectErrCodeSql;
	}
	
	public HashMap<String, String> getErrcode_Map() {
		return errcode_Map;
	}
	public void setErrcode_Map(HashMap<String, String> errcodeMap) {
		errcode_Map = errcodeMap;
	}
	public HashMap<String, String> getBc_product_map(){
		return bc_product_map;
	}
	public String getSelectBcProdSql() {
		return selectBcProdSql;
	}
	public void setSelectBcProdSql(String selectBcProdSql) {
		this.selectBcProdSql = selectBcProdSql;
	}
	
	public HashMap<String, Map<String,String>> getBcproductrealMap() {
		return bcproductrealMap;
	}

	public void setBcproductrealMap(HashMap<String, Map<String,String>> bcproductrealMap) {
		this.bcproductrealMap = bcproductrealMap;
	}

	public String getGetUsedNumSP() {
		return getUsedNumSP;
	}

	public void setGetUsedNumSP(String getUsedNumSP) {
		this.getUsedNumSP = getUsedNumSP;
	}
	public String getSelectParaSQL() {
		return selectParaSQL;
	}

	public void setSelectParaSQL(String selectParaSQL) {
		this.selectParaSQL = selectParaSQL;
	}
	public String getSelectPaymentSuccessSQL() {
		return selectPaymentSuccessSQL;
	}

	public void setSelectPaymentSuccessSQL(String selectPaymentSuccessSQL) {
		this.selectPaymentSuccessSQL = selectPaymentSuccessSQL;
	}
	public String getSelectPbcRightSendSql() {
		return selectPbcRightSendSql;
	}

	public void setSelectPbcRightSendSql(String selectPbcRightSendSql) {
		this.selectPbcRightSendSql = selectPbcRightSendSql;
	}
	
	public String getSelectPayingSql() {
		return selectPayingSql;
	}

	public void setSelectPayingSql(String selectPayingSql) {
		this.selectPayingSql = selectPayingSql;
	}

	/**
	 * 交易发送到引擎，先插入交易表，再根据返回，更新该条交易
	 */
	public int insertBeforePayment(Map<String,String> msgMap) throws Exception {
		
		int iRet = 0;
		PreparedStatement pstmt = null;
		logger.info("-----insertSbBeforePayment start-----");
		try {
			checkConnect();
			
			pstmt = conn.prepareStatement(insertPaymentSQL);
			
			int i = 0;
	
			//pstmt.setString(1, t.getOrderID());
			pstmt.setString(++i, msgMap.get(Constant.ORDERID));
			pstmt.setString(++i, msgMap.get(Constant.BATCHNO));
			pstmt.setString(++i, msgMap.get(Constant.SERIALNO));
			pstmt.setInt(++i, 0);
			pstmt.setString(++i, msgMap.get(Constant.MERID) == null ? "Na" : msgMap.get(Constant.MERID));
			pstmt.setDouble(++i, Double.valueOf(transAmt(msgMap.get(Constant.TRANSAMT))));
			pstmt.setString(++i, msgMap.get(Constant.TERMINALID));
			pstmt.setString(++i, msgMap.get(Constant.POSTIME));
			pstmt.setString(++i, formatNumberString(msgMap.get(Constant.POSID), 6));
			pstmt.setString(++i, msgMap.get(Constant.PAN));//10
			pstmt.setString(++i, msgMap.get(Constant.PRODTYPE));
			pstmt.setInt(++i, 1);
			pstmt.setString(++i, msgMap.get(Constant.TRANSDATE));
			pstmt.setString(++i, msgMap.get(Constant.TRANSTIME));
			pstmt.setString(++i, null);
			pstmt.setString(++i, msgMap.get(Constant.TRANSDATE));
			pstmt.setString(++i, msgMap.get(Constant.TRANSTIME));
			pstmt.setString(++i, null);
			pstmt.setString(++i,null);
			pstmt.setString(++i, msgMap.get(Constant.BANKORDID));//10
	
			pstmt.setString(++i, msgMap.get(Constant.CUSTID));
			pstmt.setString(++i, msgMap.get(Constant.CASHIERID));
			pstmt.setString(++i, msgMap.get(Constant.STOREID));
			pstmt.setString(++i, msgMap.get(Constant.BANKNAME));
			pstmt.setString(++i, msgMap.get(Constant.MERNAME));
			pstmt.setString(++i, msgMap.get(Constant.PRODUCTID));
			pstmt.setString(++i, msgMap.get(Constant.ACTTYPE));
			pstmt.setString(++i, msgMap.get(Constant.CUSTOMERID));//这里NOTE1存放上海银行店面积分兑换CustomerId
			pstmt.setString(++i, transAmt(msgMap.get(Constant.TRANSCASH)));
			//商户流水号
			pstmt.setString(++i, msgMap.get(Constant.NOTE4));//NOTE4存放210积分CustomerId
			pstmt.setQueryTimeout(60);
			int res = pstmt.executeUpdate();
	
			if (1 == res)
				logger.info("Successfully insert before payment.orderId : [" + msgMap.get(Constant.ORDERID) + "]");
			else {
				logger.error("Failed to insert before payment.orderId : [" + msgMap.get(Constant.ORDERID) + "]");
				iRet = -9999;
			}
		}catch (SQLException e) {
			logger.error(e, e);
			iRet = -9999;
		} finally {
			if(pstmt != null)
				pstmt.close();
		}
		return iRet;
	}
	
	/**
	 * 格式化金额.000
	 * @param tramsAmt
	 * @return
	 */
	public static String transAmt(String tramsAmt){
		if(tramsAmt==null) return "0.000";
		return new DecimalFormat("0.000").format(BigDecimal.valueOf(Double.valueOf(tramsAmt)));
	}
	public int updatePayment(int inStatus, Map<String,String> msgMap) throws Exception {
		int iRet = 0;
		PreparedStatement pstmt = null;
		try {
			
			checkConnect();
			pstmt = conn.prepareStatement(updatePaymetStatusSql);
			int i = 0;
			pstmt.setInt(++i, inStatus);
			pstmt.setString(++i, msgMap.get(Constant.BANKORDID));
			pstmt.setString(++i, msgMap.get(Constant.RETCODE));
			pstmt.setString(++i, msgMap.get(Constant.RETCOMMENT));
			pstmt.setString(++i, transAmt(msgMap.get(Constant.CUSTOMER_CON)));
			pstmt.setString(++i, transAmt(msgMap.get(Constant.BANK_PRICE)));
			pstmt.setString(++i, transAmt(msgMap.get(Constant.CHANNEL_PRICE)));
			pstmt.setString(++i, transAmt(msgMap.get(Constant.BQ_BENEFIT)));
			pstmt.setString(++i, transAmt(msgMap.get(Constant.BQ_GET)));
			pstmt.setString(++i, transAmt(msgMap.get(Constant.CHANNEL_PAY)));
			pstmt.setString(++i, transAmt(msgMap.get(Constant.TRANS_CASH)));
			pstmt.setString(++i, transAmt(msgMap.get(Constant.CUSTOMER_CON)));
			pstmt.setString(++i, msgMap.get(Constant.ORDERID));
			pstmt.setString(++i, msgMap.get(Constant.BATCHNO));
			pstmt.setString(++i, msgMap.get(Constant.SERIALNO));
			
			pstmt.setQueryTimeout(60);
			int res = pstmt.executeUpdate();
			
			if (1 == res)
				logger.info("Successfully update payment orderId : [" + msgMap.get(Constant.ORDERID) + "]"
						+ " batchNo : [" + msgMap.get(Constant.BATCHNO) + "] serialNo : [" + msgMap.get(Constant.SERIALNO) + "]");
			else {
				logger.error("Failed to update payment orderId : [" + msgMap.get(Constant.ORDERID) + "]"
						+ " batchNo : [" + msgMap.get(Constant.BATCHNO) + "] serialNo : [" + msgMap.get(Constant.SERIALNO) + "]");
				iRet = -9999;
			}
		}
		catch (SQLException e) {
			logger.error(e, e);
			iRet = -9999;
		} finally {
			if(pstmt != null)
				pstmt.close();
		}
		return iRet;
	}
	
	/***
	 * 根据序列号和序号更新伯乔退款交易记录-2撤销退款
	 * @param msgMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertCancel(int status,Map<String,String> msgMap) throws Exception
	{
		boolean ret = true;
		try {

			checkConnect();
			
			int res = updateStatus(status, msgMap.get(Constant.BATCHNO), msgMap.get(Constant.SERIALNO));
			if (res == 1) {
				logger.info("Successfully persist the cancel record, Got SQL result ["
						+ 1 + "]");

			} else {
				logger.error("Failed to persist the cancel record, Got SQL result ["
						+ res + "]");
				ret = false;
			}

		} catch (SQLException e) {
			logger.error(e, e);
			ret = false;
		}
		return ret;

	}
	
	/**
	 * 插入上海银行积分表
	 * @param msgMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertShpoints(Map<String,String> msgMap) throws Exception
	{
		logger.info("insert shpoints map params" + msgMap);
		PreparedStatement pstmt = null;
		boolean ret = true;
		try {
			checkConnect();
			pstmt = conn.prepareStatement(insertShpoints);
			int i = 0;
			
			pstmt.setString(++i, msgMap.get("mti"));//   MESSAGETYPEIDENTIFIER, recvMap.get("1"));//
			pstmt.setString(++i, msgMap.get("2"));//           ACCOUNTNUMBER, recvMap.get("2"));//
			pstmt.setString(++i, msgMap.get("3"));//          PROCESSINGCODE, recvMap.get("3"));//
			pstmt.setString(++i, msgMap.get("4"));//       TRANSACTIONAMOUNT, recvMap.get("4"));//
			pstmt.setString(++i, msgMap.get("7"));//  TRANSACTIONDATEANDTIME, recvMap.get("7"));//
			pstmt.setString(++i, msgMap.get("11"));//  SYSTEMTRACEAUDITNUMBER, recvMap.get("11"));//
			pstmt.setString(++i, msgMap.get("12"));//"   LOCALTRANSACTIONTIME, recvMap.get("12"));//"
			pstmt.setString(++i, msgMap.get("13"));//    LOCALTRANSACTIONDATE, recvMap.get("13"));// 
			pstmt.setString(++i, msgMap.get("15"));//             CAPTUREDATE, recvMap.get("15"));//
			pstmt.setString(++i, msgMap.get("22"));//               ENTRYMODE, recvMap.get("22"));//
			pstmt.setString(++i, msgMap.get("25"));//          POINTOFSERVICE, recvMap.get("25"));//
			pstmt.setString(++i, msgMap.get("32"));//     ACQUIRINGINSTITUTE, recvMap.get("32"));//
			pstmt.setString(++i, msgMap.get("33"));//     FORWARDINGINSTITUTE, recvMap.get("33"));//
			pstmt.setString(++i, msgMap.get("37"));//RETRIEVALREFERENCENUMBER, recvMap.get("37"));//
			pstmt.setString(++i, msgMap.get("38"));//            APPROVALCODE, recvMap.get("38"));//
			pstmt.setString(++i, msgMap.get("39"));//            RESPONSECODE, result.getErr_code());//上海积分返回码39
			pstmt.setString(++i, msgMap.get("41"));//  TERMINALIDENTIFICATION, recvMap.get("41"));//
			pstmt.setString(++i, msgMap.get("42"));//BCTERMINALIDENTIFICATION, recvMap.get("42"));//
			pstmt.setString(++i, msgMap.get("43"));//       BCNAMEANDLOCATION, recvMap.get("43"));//
			pstmt.setString(++i, msgMap.get("48"));//          ADDITIONALDATA, recvMap.get("48"));//
			pstmt.setString(++i, msgMap.get("49"));// TRANSACTIONCURRENCYCODE, recvMap.get("49"));//
			pstmt.setString(++i, msgMap.get("90"));//     ORIGINALDATAELEMENT, recvMap.get("90"));//
			pstmt.setString(++i, msgMap.get("48.1"));//           CATALOGUECODE, recvMap.get("48.1"));//
			pstmt.setString(++i, msgMap.get("48.7"));//         REFERENCENUMBER, recvMap.get("48.7"));//
			pstmt.setString(++i, msgMap.get("48.2"));//                ITEMCODE, recvMap.get("48.2"));//
			pstmt.setString(++i, msgMap.get("48.3"));//                QUANTITY, recvMap.get("48.3"));//
			pstmt.setString(++i, msgMap.get("48.4"));//      FASTTRACKINDICATOR, recvMap.get("48.4"));//
			pstmt.setString(++i, msgMap.get("48.5"));//          POINTSREDEEMED, recvMap.get("48.5"));//
			pstmt.setString(++i, msgMap.get("48.6"));//         FASTTRACKAMOUNT, recvMap.get("48.6"));//
			pstmt.setString(++i, msgMap.get(ShdbUtil.RESPONSEMSG));//
			pstmt.setString(++i, msgMap.get(ShdbUtil.ORDERID));//
			
			pstmt.setQueryTimeout(60);
			int res = pstmt.executeUpdate();
			
			if (1 == res) {
				logger.info("Successfully persist the cancel record for shpoints, Got SQL result ["
						+ res + "]");
			} else {
				logger.error("Failed to persist the cancel record for shpoints, Got SQL result ["
						+ res + "]");
				ret = false;
			}
			
		} catch (SQLException e) {
			logger.error(e, e);
			ret = false;
		} finally {
			if(pstmt != null)
				pstmt.close();
		}
		return ret;
		
	}
	
	/**
	 * 根据订单号更新上海银行积分记录
	 * @param shpointsMap
	 * @return
	 * @throws SQLException
	 */
	public int updateShpointsByOrderid(Map<String,String> shpointsMap) throws SQLException{
		int iRet = 0;
		PreparedStatement pstmt = null;
		try {
			checkConnect();
			pstmt = conn.prepareStatement(updateShpointsByOrderid);
			int i = 0;
			pstmt.setString(++i, shpointsMap.get("mti"));   //1MESSAGETYPEIDENTIFIER
			pstmt.setString(++i, shpointsMap.get("2"));           //2ACCOUNTNUMBER
			pstmt.setString(++i, shpointsMap.get("3"));          //3PROCESSINGCODE
//			pstmt.setString(++i, shpointsMap.get("4"));       //4TRANSACTIONAMOUNT
			pstmt.setString(++i, shpointsMap.get("7"));  //7TRANSACTIONDATEANDTIME
			pstmt.setString(++i, shpointsMap.get("11"));  //11SYSTEMTRACEAUDITNUMBER
			pstmt.setString(++i, shpointsMap.get("12"));    //12LOCALTRANSACTIONTIME
			pstmt.setString(++i, shpointsMap.get("13"));    //13LOCALTRANSACTIONDATE
			pstmt.setString(++i, shpointsMap.get("15"));             //15CAPTUREDATE
			pstmt.setString(++i, shpointsMap.get("22"));             	 //22ENTRYMODE
			pstmt.setString(++i, shpointsMap.get("25"));          //25POINTOFSERVICE
			pstmt.setString(++i, shpointsMap.get("32"));      //32ACQUIRINGINSTITUTE
			pstmt.setString(++i, shpointsMap.get("33"));     //33FORWARDINGINSTITUTE
			pstmt.setString(++i, shpointsMap.get("37"));//37RETRIEVALREFERENCENUMBER
			pstmt.setString(++i, shpointsMap.get("38"));            //38APPROVALCODE
			pstmt.setString(++i, shpointsMap.get("39"));            //39RESPONSECODE
			pstmt.setString(++i, shpointsMap.get("41"));  //41TERMINALIDENTIFICATION
			pstmt.setString(++i, shpointsMap.get("42"));//42BCTERMINALIDENTIFICATION
			pstmt.setString(++i, shpointsMap.get("43"));       //43BCNAMEANDLOCATION
//			pstmt.setString(++i, shpointsMap.get("48"));          //48ADDITIONALDATA
			pstmt.setString(++i, shpointsMap.get("49")); //49TRANSACTIONCURRENCYCODE
			pstmt.setString(++i, shpointsMap.get("90"));     //90ORIGINALDATAELEMENT
			pstmt.setString(++i, shpointsMap.get(ShdbUtil.RESPONSEMSG));
			pstmt.setString(++i, shpointsMap.get(ShdbUtil.ORDERID));             	 //ORDERID
			
			pstmt.setQueryTimeout(60);
			iRet = pstmt.executeUpdate();
		}catch (SQLException e) {
			logger.error(e, e);
			iRet = 0;
		} finally {
			if(pstmt != null)
				pstmt.close();
		}
		return iRet;
	}
	
	
	/**
	 * 根据订单序号查询上海银行积分记录
	 * @param orderid
	 * @return
	 * @throws SQLException
	 */
	public Map<String,String> selectShpointsByOrderid(String orderid) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String,String> result = null;
		try {
			checkConnect();
			pstmt = conn.prepareStatement(selectShpointsByOrderid);
			int i = 0;
			pstmt.setString(++i, orderid);
			rs = pstmt.executeQuery();
			pstmt.setQueryTimeout(60);
			if (rs.next()) {
				result = new HashMap<String, String>();
				result.put(ShdbUtil.MESSAGETYPEIDENTIFIER, rs.getString(1));
				result.put(ShdbUtil.ACCOUNTNUMBER, rs.getString(2));
				result.put(ShdbUtil.PROCESSINGCODE, rs.getString(3));
				result.put(ShdbUtil.TRANSACTIONAMOUNT, rs.getString(4));
				result.put(ShdbUtil.TRANSACTIONDATEANDTIME, rs.getString(5));
				result.put(ShdbUtil.SYSTEMTRACEAUDITNUMBER, rs.getString(6));
				result.put(ShdbUtil.LOCALTRANSACTIONTIME, rs.getString(7));
				result.put(ShdbUtil.LOCALTRANSACTIONDATE, rs.getString(8));
				result.put(ShdbUtil.CAPTUREDATE, rs.getString(9));
				result.put(ShdbUtil.ENTRYMODE, rs.getString(10));
				result.put(ShdbUtil.POINTOFSERVICE, rs.getString(11));
				result.put(ShdbUtil.ACQUIRINGINSTITUTE, rs.getString(12));
				result.put(ShdbUtil.FORWARDINGINSTITUTE, rs.getString(13));
				result.put(ShdbUtil.RETRIEVALREFERENCENUMBER, rs.getString(14));
				result.put(ShdbUtil.APPROVALCODE, rs.getString(15));
				result.put(ShdbUtil.RESPONSECODE, rs.getString(16));
				result.put(ShdbUtil.TERMINALIDENTIFICATION, rs.getString(17));
				result.put(ShdbUtil.BCTERMINALIDENTIFICATION, rs.getString(18));
				result.put(ShdbUtil.BCNAMEANDLOCATION, rs.getString(19));
				result.put(ShdbUtil.ADDITIONALDATA, rs.getString(20));
				result.put(ShdbUtil.TRANSACTIONCURRENCYCODE, rs.getString(21));
				result.put(ShdbUtil.ORIGINALDATAELEMENT, rs.getString(22));
				result.put(ShdbUtil.CATALOGUECODE, rs.getString(23));
				result.put(ShdbUtil.REFERENCENUMBER, rs.getString(24));
				result.put(ShdbUtil.ITEMCODE, rs.getString(25));
				result.put(ShdbUtil.QUANTITY, rs.getString(26));
				result.put(ShdbUtil.FASTTRACKINDICATOR, rs.getString(27));
				result.put(ShdbUtil.POINTSREDEEMED, rs.getString(28));
				result.put(ShdbUtil.FASTTRACKAMOUNT, rs.getString(29));
			}
			
		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			if(pstmt != null)
				pstmt.close();
			if (rs != null) {
				rs.close();
			}
		}
		return result;
	}
	
	/**
	 * 根据物品代码查看物品管理表
	 * @return
	 * @throws SQLException 
	 */
	public Map<String,String> selectShpointsprorealByItem(String item) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String,String> result = null;
		try {
			checkConnect();
			pstmt = conn.prepareStatement(selectShpointsprorealByItem);
			int i = 0;
			pstmt.setString(++i, item);
			rs = pstmt.executeQuery();
			pstmt.setQueryTimeout(60);
			if (rs.next()) {
				result = new HashMap<String, String>();
				result.put(ShdbUtil.CATALOGUECODE1, rs.getString(1));
				result.put(ShdbUtil.ITEMCODE1, rs.getString(2));
				result.put(ShdbUtil.BCPROID, rs.getString(3));
				result.put(ShdbUtil.COMM, rs.getString(4));
				result.put(ShdbUtil.FASTTRACK, rs.getString(5));
				result.put(ShdbUtil.POINTSNUM, rs.getString(6));
				result.put(ShdbUtil.STARTDATE, rs.getString(7));
				result.put(ShdbUtil.ENDDATE, rs.getString(8));
				result.put(ShdbUtil.FATTRADENUMC, rs.getString(9));
				result.put(ShdbUtil.FATTRADENUMB, rs.getString(10));
			}
			
		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			if(pstmt != null)
				pstmt.close();
			if (rs != null) {
				rs.close();
			}
		}
		return result;
	}
	
	public int selectPaying(Map<String,String> msgMap) throws Exception
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			
			checkConnect();
			
			pstmt = conn.prepareStatement(selectPayingSql);
			
			int i = 0;
			
			pstmt.setString(++i, msgMap.get(Constant.ORDERID));
			pstmt.setString(++i, msgMap.get(Constant.BATCHNO));
			pstmt.setString(++i, msgMap.get(Constant.SERIALNO));
			rs = pstmt.executeQuery();
			pstmt.setQueryTimeout(60);
			if (rs.next()) {
				count = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			if(pstmt != null)
				pstmt.close();
			if (rs != null) {
				rs.close();
			}
		}
		return count;
		
	}
	
	/**
	 * 根据序列号和订单号更新订单状态
	 * @param status
	 * @param batchno
	 * @param serialno
	 * @return
	 * @throws Exception
	 */
	private int updateStatus(int status, String batchno, String serialno) throws Exception {
		
		CallableStatement cstmt = null;
		int ret = 0;
		try {
			checkConnect();
			cstmt = conn.prepareCall(updateCancelStatusSP);

			cstmt.setInt(1, status);
			cstmt.setString(2, batchno);
			cstmt.setString(3, serialno);

			ret = cstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			if(cstmt!=null)
				cstmt.close();
		}

		return ret;
	}

	public boolean init() {
		errcode_Map = new HashMap<String, String>();
		bc_product_map = new HashMap<String, String>();
		bcproductrealMap = new HashMap<String, Map<String,String>>();
		try {
			getErrCodeMap();
			initBcProductMap();
			initbcproductrealMap();
		} catch ( Exception e){
			logger.error(e,e);
		}

		return true;
	}
	
	/**
	 * 初始化商品关联信息
	 * @throws SQLException
	 */
	public void initbcproductrealMap() throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			checkConnect();
			pstmt = conn.prepareStatement(selectAllShpointsproreal);
			rs = pstmt.executeQuery();
			pstmt.setQueryTimeout(60);
			while (rs.next()) {
				Map<String,String> result = new HashMap<String, String>();
				result.put(ShdbUtil.CATALOGUECODE1, rs.getString(1));
				result.put(ShdbUtil.ITEMCODE1, rs.getString(2));
				String bcproid = rs.getString(3);
				result.put(ShdbUtil.BCPROID, bcproid);
				result.put(ShdbUtil.COMM, rs.getString(4));
				result.put(ShdbUtil.FASTTRACK, rs.getString(5));
				result.put(ShdbUtil.POINTSNUM, rs.getString(6));
				result.put(ShdbUtil.STARTDATE, rs.getString(7));
				result.put(ShdbUtil.ENDDATE, rs.getString(8));
				result.put(ShdbUtil.FATTRADENUMC, rs.getString(9));
				result.put(ShdbUtil.FATTRADENUMB, rs.getString(10));
				result.put(ShdbUtil.BQBENEFIT, rs.getString(11));
				result.put(ShdbUtil.ACTIVITDAY, rs.getString(12));
				result.put(ShdbUtil.DAYPOINTSNUM, rs.getString(13));
				bcproductrealMap.put(bcproid, result);
			}
			
		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			if(pstmt != null)
				pstmt.close();
			if (rs != null) {
				rs.close();
			}
		}
	}
	
	public boolean reset(){
		return init();
	}
	
	private void checkConnect() throws SQLException {
		if (conn == null || conn.isClosed() ){
			conn = datasource.getConnection();
			checkConnect();
		}
		PreparedStatement pstmt = null ;
		ResultSet rs = null;
		try{
			if(testConnSQL == null || testConnSQL == "")
				testConnSQL = "select 1 from dual";
			pstmt = conn.prepareStatement(testConnSQL);
			rs = pstmt.executeQuery();
		}catch(Exception e){
			logger.error("sql connect is not running,now is autoReConnet");
			logger.error(e,e);
			conn = datasource.getConnection();
		}
		finally{
			if(rs != null)
				rs.close();
			if(pstmt != null)
				pstmt.close();
		}
		
	}
	
	private boolean getErrCodeMap() throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean ret = false;
		try {
			checkConnect();
			/*if (conn == null || conn.isClosed() )
				conn = datasource.getConnection();*/
			pstmt = conn.prepareStatement(selectErrCodeSql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				String id = rs.getString(1);
				String name = rs.getString(2);
				logger.info("Got errcode [" + id + "], comments[" + name
						+ "]");
				errcode_Map.put(id, name);
			}
			

		} catch (SQLException e) {
			logger.error(e, e);
			ret = false;
		} finally {
			if(rs != null)
				rs.close();
			if(pstmt != null)
				pstmt.close();
			
		}
		return ret;
	}

	/**
	 * 加载活动信息
	 * ID，名称
	 */
	private boolean initBcProductMap()throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean ret = false;
		try {
			checkConnect();
			/*if (conn == null || conn.isClosed() )
				conn = datasource.getConnection();*/
			pstmt = conn.prepareStatement(selectBcProdSql);
	
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				bc_product_map.put(rs.getString(1), rs.getString(2));
			}
	
		} catch (SQLException e) {
			logger.error(e, e);
			ret = false;
		} finally {
			if(rs != null)
				rs.close();
			if(pstmt != null)
				pstmt.close();
			
		}
		return ret;
	}
	/**
	 * 查询当前所有210积分+7元兑换爱西西里成功交易
	 * @return
	 * @throws SQLException
	 */
	public synchronized int selectAllShpoints210Trade() throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int totalNum = 0;
		try {
			checkConnect();
			pstmt = conn.prepareStatement(selectAllShpoints210Trade);
			rs = pstmt.executeQuery();
			pstmt.setQueryTimeout(60);
			if (rs.next()) {
				totalNum = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			if(pstmt != null)
				pstmt.close();
			if (rs != null) {
				rs.close();
			}
		}
		return totalNum;
	}
	/**
	 * 查询今天210积分+7元兑换爱西西里成功交易
	 * 单日限额查询
	 * @return
	 * @throws SQLException
	 */
	public synchronized int selectTodayShpoints210Trade() throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int totalNum = 0;
		try {
			checkConnect();
			pstmt = conn.prepareStatement(selectTodayShpoints210Trade);
			rs = pstmt.executeQuery();
			pstmt.setQueryTimeout(60);
			if (rs.next()) {
				totalNum = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			if(pstmt != null)
				pstmt.close();
			if (rs != null) {
				rs.close();
			}
		}
		return totalNum;
	}
	/**
	 * @param pan
	 * 查询本月上海银行210积分+7元兑换爱西西里CUSTID信息
	 * @return
	 * 根据卡号查询本月上海银行权益表的权益出现次数0表示本月没出现过
	 * @throws SQLException
	 */
	public int selectShbkCustInfo(String pan) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int custNum = 0;
		try {
			checkConnect();
			pstmt = conn.prepareStatement(selectShbkCustInfo);
			pstmt.setString(1, pan);
			rs = pstmt.executeQuery();
			pstmt.setQueryTimeout(60);
			if (rs.next()) {
				custNum = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			if(pstmt != null)
				pstmt.close();
			if (rs != null) {
				rs.close();
			}
		}
		return custNum;
	}
	
	/**
	 * @param note4
	 * 				客户号
	 * @return
	 * 根据客户号查询当天交易表(citi)出现交易成功次数
	 * @throws SQLException
	 */
	public int selectThisPanShbk210TradesInfo(String note4) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int custNum = 0;
		try {
			checkConnect();
			pstmt = conn.prepareStatement(selectThisPanShbk210TradesInfo);
			pstmt.setString(1, note4);
			rs = pstmt.executeQuery();
			pstmt.setQueryTimeout(60);
			if (rs.next()) {
				custNum = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			if(pstmt != null)
				pstmt.close();
			if (rs != null) {
				rs.close();
			}
		}
		return custNum;
	}
	
	
	/**上海银行权益CUSTID信息插入
	 * @throws SQLException */
	public int insertShbkCustInfo(String card_no,String user_id) throws SQLException{
		logger.info("insert insertShbkCustInfo map params" );
		PreparedStatement pstmt = null;
		try {
			checkConnect();
			pstmt = conn.prepareStatement(insertShbkCustInfo);
			int i = 0;
			pstmt.setString(++i, card_no.substring(0, 6));
			pstmt.setString(++i, card_no);
			pstmt.setString(++i, user_id);
			pstmt.setQueryTimeout(60);
			return pstmt.executeUpdate();
			
		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			if(pstmt != null)
				pstmt.close();
		}
		return 0;
	}
	
	/**
	 * 查询交易
	 * @param batchno
	 * @param serialno
	 * @param custid
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,String> selectPaymentCITISQL(String batchno, String serialno ,String custid) throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			checkConnect();
			/*if (conn == null || conn.isClosed() )
				conn = datasource.getConnection();*/
			pstmt = conn.prepareStatement(selectPaymentCITISQL);

			pstmt.setString(1, batchno);
			pstmt.setString(2, serialno);
			pstmt.setString(3, custid);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				HashMap<String,String> orderInfo = new HashMap<String,String>(); 
				orderInfo.put(Constant.ORDERID, rs.getString(1));
				orderInfo.put(Constant.PAN, rs.getString(2));
				orderInfo.put(Constant.ACTTYPE, rs.getString(3));
				orderInfo.put(Constant.PRODTYPE, rs.getString(4));
				orderInfo.put(Constant.STATUS, rs.getString(5));
				orderInfo.put(Constant.AUTHORIZECODE, rs.getString(6));
				orderInfo.put(Constant.AUTHDATE, rs.getString(7));
				orderInfo.put(Constant.AUTHTIME, rs.getString(8));
				orderInfo.put(Constant.TRANSAMT, rs.getString(9));
				orderInfo.put(Constant.TRANSCASH, rs.getString(10));
				orderInfo.put(Constant.BANKORDID, rs.getString(11));
				orderInfo.put(Constant.BATCHNO, rs.getString(12));
				orderInfo.put(Constant.SERIALNO, rs.getString(13));
				orderInfo.put(Constant.RETCODE, rs.getString(14));
				logger.info("Got orderInfo [" + orderInfo + "]");
				return orderInfo;
			}
			
		} catch (SQLException e) {
			logger.error(e.toString());
		} finally {
			if(rs != null)
				rs.close();
			if(pstmt != null)
				pstmt.close();
		}
		return null;
	}
	
	/**
	 * 根据批次号+流水号唯一确认一笔交易（现脚本 没有加status 为成功？）
	 */
	public HashMap<String,String> queryPaymentTradePan(String batchno, String serialno ,String custid ,String pan) throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			checkConnect();
			/*if (conn == null || conn.isClosed() )
				conn = datasource.getConnection();*/
			pstmt = conn.prepareStatement(selectPaymentSuccessSQL);

			pstmt.setString(1, batchno);
			pstmt.setString(2, serialno);
			pstmt.setString(3, custid);
			pstmt.setString(4, pan);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				HashMap<String,String> orderInfo = new HashMap<String,String>(); 
				orderInfo.put(Constant.ORDERID, rs.getString(1));
				orderInfo.put(Constant.PAN, rs.getString(2));
				orderInfo.put(Constant.ACTTYPE, rs.getString(3));
				orderInfo.put(Constant.PRODTYPE, rs.getString(4));
				orderInfo.put(Constant.STATUS, rs.getString(5));
				orderInfo.put(Constant.AUTHORIZECODE, rs.getString(6));
				orderInfo.put(Constant.AUTHDATE, rs.getString(7));
				orderInfo.put(Constant.AUTHTIME, rs.getString(8));
				orderInfo.put(Constant.TRANSAMT, rs.getString(9));
				orderInfo.put(Constant.TRANSCASH, rs.getString(10));
				orderInfo.put(Constant.BANKORDID, rs.getString(11));
				orderInfo.put(Constant.BATCHNO, rs.getString(12));
				orderInfo.put(Constant.SERIALNO, rs.getString(13));
				orderInfo.put(Constant.RETCODE, rs.getString(14));
				logger.info("Got orderInfo [" + orderInfo + "]");
				return orderInfo;
			}
			
		} catch (SQLException e) {
			logger.error(e.toString());
		} finally {
			if(rs != null)
				rs.close();
			if(pstmt != null)
				pstmt.close();
		}
		return null;
	}
	
	/**
	 * 根据流水号获取当天唯一的伯乔成功交易
	 */
	public HashMap<String,String> queryPaymentSuccessPan(String orderid, String terminalId, String storeId) throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			checkConnect();
			/*if (conn == null || conn.isClosed() )
				conn = datasource.getConnection();*/
			pstmt = conn.prepareStatement(selectPaymentSuccessByOrderidSQL);

			pstmt.setString(1, orderid);
			pstmt.setString(2, terminalId);
			pstmt.setString(3, storeId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				HashMap<String,String> orderInfo = new HashMap<String,String>();
				orderInfo.put(Constant.ORDERID, rs.getString(1));
				orderInfo.put(Constant.PAN, rs.getString(2));
				orderInfo.put(Constant.ACTTYPE, rs.getString(3));
				orderInfo.put(Constant.PRODTYPE, rs.getString(4));
				orderInfo.put("STATUS", rs.getString(5));
				orderInfo.put("authorizecode", rs.getString(6));
				orderInfo.put("authdate", rs.getString(7));
				orderInfo.put("authtime", rs.getString(8));
				orderInfo.put(Constant.TRANSAMT, rs.getString(9));
				orderInfo.put(Constant.TRANSCASH, rs.getString(10));
				orderInfo.put(Constant.BANKORDID, rs.getString(11));
				orderInfo.put(Constant.BATCHNO, rs.getString(12));
				orderInfo.put(Constant.SERIALNO, rs.getString(13));
				orderInfo.put(Constant.RETCODE, rs.getString(14));
				logger.info("Got orderInfo [" + orderInfo + "]");
				return orderInfo;
			}
			
		} catch (SQLException e) {
			logger.error(e.toString());
		} finally {
			if(rs != null)
				rs.close();
			if(pstmt != null)
				pstmt.close();
		}
		return null;
	}
	
	
	/**
	 * 获取序列号
	 * @return
	 */
	public long getTSN() {

		try {
			if (tsnBlockMin < tsnBlockMax) {
				tsnBlockMin++;
				return tsnBlockMin;
			} else if (retrieveTsnBlock()) {
				return tsnBlockMin;
			} else
				return -1;
		} catch (Exception e) {
			logger.error(e, e);
			return -1;
		}

	}

	private boolean retrieveTsnBlock() throws Exception {

		CallableStatement cstmt = null;
		boolean ret = true;
		try {
			checkConnect();
			/*
			 * if (conn == null || conn.isClosed() ) conn =
			 * datasource.getConnection();
			 */
			cstmt = conn.prepareCall(getTSNSP);

			cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
			cstmt.setInt(2, tsnBlockSize);

			cstmt.execute();
			tsnBlockMax = cstmt.getInt(1);
			tsnBlockMin = tsnBlockMax - tsnBlockSize + 1;
			logger.info("Got TSN block max [" + tsnBlockMax + "], blockSize=["
					+ tsnBlockSize + "]");

		} catch (SQLException e) {
			logger.error(e, e);
			ret = false;
		} finally {

			cstmt.close();
		}

		return ret;
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
	
	/**
	 * 根据ORDERID批次号流水号更新订单信息
	 * @param status
	 * @param orderid
	 * @param batchno
	 * @param serialno
	 * @return
	 * @throws SQLException
	 */
	public int updatecitiStatusSql(int status, String orderid, String batchno, String serialno) throws SQLException{
		int iRet = 0;
		PreparedStatement pstmt = null;
		try {
			
			checkConnect();
			pstmt = conn.prepareStatement(updatecitiStatusSql);
			int i = 0;
			pstmt.setInt(++i, status);
			pstmt.setString(++i, orderid);
			pstmt.setString(++i, batchno);
			pstmt.setString(++i, serialno);
			
			pstmt.setQueryTimeout(60);
			int res = pstmt.executeUpdate();
			iRet = res;
			if (1 == res)
				logger.info("Successfully update payment orderId : [" + orderid + "]"
						+ " batchNo : [" + batchno + "] serialNo : [" + serialno + "]");
			else {
				logger.error("Failed to update payment orderId : [" + orderid + "]"
						+ " batchNo : [" + batchno + "] serialNo : [" + serialno + "]");
				iRet = 0;
			}
		}
		catch (SQLException e) {
			logger.error(e, e);
			iRet = -9999;
		} finally {
			if(pstmt != null)
				pstmt.close();
		}
		return iRet;
	}
	/**
	 * 根据ORDERID批次号流水号更新订单信息
	 update bccore.SHPOINTSPAYMENT sh set 
					  RESPONSEMSG = ?,
					  where orderid = ?
	 */
	public int updateShpointsStatusByOrderid(String respMsg, String orderid) throws SQLException{
		int iRet = 0;
		PreparedStatement pstmt = null;
		try {
			
			checkConnect();
			pstmt = conn.prepareStatement(updateShpointsStatusByOrderid);
			int i = 0;
			pstmt.setString(++i, respMsg);
			pstmt.setString(++i, orderid);
			
			pstmt.setQueryTimeout(60);
			int res = pstmt.executeUpdate();
			iRet = res;
			if (1 == res)
				logger.info("Successfully update payment orderId : [" + orderid + "]"
						+ " respMsg : [" + respMsg + "]");
			else {
				logger.error("Failed to update payment orderId : [" + orderid + "]"
						+ " respMsg : [" + respMsg + "]");
				iRet = 0;
			}
		}
		catch (SQLException e) {
			logger.error(e, e);
			iRet = 0;
		} finally {
			if(pstmt != null)
				pstmt.close();
		}
		return iRet;
	}
	/**
	 * 退货撤销结算数据
	 */
	public int deleteFromTinfoDdtClearAccount(String custid, String pan
			, String seril) throws SQLException{
		int iRet = 0;
		PreparedStatement pstmt = null;
		try {
			checkConnect();
			pstmt = conn.prepareStatement(deleteFromTinfoDdtClearAccount);
			int i = 0;
			pstmt.setString(++i, custid);
			pstmt.setString(++i, pan);
			pstmt.setString(++i, seril);
			
			pstmt.setQueryTimeout(60);
			int res = pstmt.executeUpdate();
			iRet = res;
			if (1 == res)
				logger.info("Successfully update payment orderId : [" + pan + "]"
						+ " respMsg : [" + seril + "]");
			else {
				logger.error("Failed to update payment orderId : [" + pan + "]"
						+ " respMsg : [" + seril + "]");
				iRet = 0;
			}
		}
		catch (SQLException e) {
			logger.error(e, e);
			iRet = 0;
		} finally {
			if(pstmt != null)
				pstmt.close();
		}
		return iRet;
	}
	
	/**
	 * 
	 * 暂未用到--201600616
	 * 根据NOTE1和BATCHNO和活动编号查询成功的交易次数
	 * @param note1
	 * 				Constant.CUSTOMERID_+bean.getCustomerId()
	 * @param batchno
	 * 				当天批次号
	 * @param productid
	 * 				活动编号
	 * @return
	 * @throws SQLException
	 */
	public int countCitipaymentByNote1ByBatchno(String note1,String batchno,String productid) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int custNum = 0;
		try {
			checkConnect();
			pstmt = conn.prepareStatement(countCitipaymentByNote1ByBatchno);
			pstmt.setString(1, note1);
//			pstmt.setString(2, batchno);
			pstmt.setString(2, productid);
			rs = pstmt.executeQuery();
			pstmt.setQueryTimeout(60);
			if (rs.next()) {
				custNum = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			if(pstmt != null)
				pstmt.close();
			if (rs != null) {
				rs.close();
			}
		}
		return custNum;
	}
	/**
	 * 根据NOTE1和BATCHNO和活动编号查询成功的交易金额
	 * @param note1
	 * 				Constant.CUSTOMERID_+bean.getCustomerId()
	 * @param batchno
	 * 				当天批次号
	 * @param productid
	 * 				活动编号
	 * @return
	 * @throws SQLException
	 */
	public BigDecimal sumCitipaymentByNote1ByBatchno(String note1,String batchno,String productid) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BigDecimal custNum = new BigDecimal("0.000");
		try {
			checkConnect();
			pstmt = conn.prepareStatement(sumCitipaymentByNote1ByBatchno);
			pstmt.setString(1, note1);
//			pstmt.setString(2, batchno);
			pstmt.setString(2, productid);
			rs = pstmt.executeQuery();
			pstmt.setQueryTimeout(60);
			if (rs.next()) {
				custNum = rs.getBigDecimal(1);
			}
		} catch (SQLException e) {
			logger.error(e, e);
		} finally {
			if(pstmt != null)
				pstmt.close();
			if (rs != null) {
				rs.close();
			}
		}
		return custNum;
	}
}
