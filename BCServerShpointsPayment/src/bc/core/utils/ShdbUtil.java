package bc.core.utils;

/***
 * 数据库字段
 * 
 * @author Administrator
 * 
 */
public interface ShdbUtil {
	/**
	 * 1Message Type Identifier 消息类型标识 N 4 send M recM
	 */
	public static String MESSAGETYPEIDENTIFIER = "MESSAGETYPEIDENTIFIER";
	/**
	 * 2Account Number 帐户号 LL + N 19 send M recM
	 */
	public static String ACCOUNTNUMBER = "ACCOUNTNUMBER";
	/**
	 * 3Processing Code 处理代码 N 6 send M recM
	 */
	public static String PROCESSINGCODE = "PROCESSINGCODE";
	/**
	 * 4Transaction Amount 交易金额 N 12 M sendM(默认值为0 ) recM
	 */
	public static String TRANSACTIONAMOUNT = "TRANSACTIONAMOUNT";
	/**
	 * 7Transaction Date and Time 交易日期和时间 N 10 sendM rec M
	 */
	public static String TRANSACTIONDATEANDTIME = "TRANSACTIONDATEANDTIME";
	/**
	 * 11System Trace Audit Number 系统跟踪审计号(当天唯一) N 6 sendM recM
	 */
	public static String SYSTEMTRACEAUDITNUMBER = "SYSTEMTRACEAUDITNUMBER";
	/**
	 * 12 Approval Code 核准代码 AN 6 sendM recM
	 */
	public static String LOCALTRANSACTIONTIME = "LOCALTRANSACTIONTIME";
	/**
	 * 13Local Transaction Date 当地交易日期 N 4 sendM recM
	 */
	public static String LOCALTRANSACTIONDATE = "LOCALTRANSACTIONDATE";
	/**
	 * 15Capture Date 清算日期 N 4 sendM rec M
	 */
	public static String CAPTUREDATE = "CAPTUREDATE";
	/**
	 * 22Entry Mode 服务点输入模式 N 3 sendM(参考银联规范２２域 ) recM
	 */
	public static String ENTRYMODE = "ENTRYMODE";
	/**
	 * 25Point of Service Condition Code POS代码 N 2 sendM (ATM – “02” IVR – “08”
	 * CDM – “12” Internet – “42” Branch – “04” Multimedia Kiosk – “05” Others –
	 * “06” defaulte-50) recM
	 */
	public static String POINTOFSERVICE = "POINTOFSERVICE";
	/**
	 * 32Acquiring Institute Identification Code 收单机构识别代码 LL + N 11 sendM rec M
	 */
	public static String ACQUIRINGINSTITUTE = "ACQUIRINGINSTITUTE";
	/**
	 * 33Forwarding Institute Identification Code 转发机构识别代码(同32) LL + N11 sendM
	 * rec M
	 */
	public static String FORWARDINGINSTITUTE = "FORWARDINGINSTITUTE";
	/** 37 Retrieval Reference Number 调单参考号码 AN 12 sendM rec M */
	public static String RETRIEVALREFERENCENUMBER = "RETRIEVALREFERENCENUMBER";
	/**
	 * 38 Approval Code 核准代码 AN 6 sendN recM
	 */
	public static String APPROVALCODE = "APPROVALCODE";
	/**
	 * 39 Response Code 应答代码 AN2 sendN recM
	 */
	public static String RESPONSECODE = "RESPONSECODE";
	/**
	 * 41 Terminal Identification 终端识别号码 AN 8 send M rec M
	 */
	public static String TERMINALIDENTIFICATION = "TERMINALIDENTIFICATION";
	/**
	 * 42 Card Acceptor Terminal Identification 收卡方终端标识 AN 15 sendM recN
	 */
	public static String BCTERMINALIDENTIFICATION = "BCTERMINALIDENTIFICATION";
	/**
	 * 43 Card Acceptor Name and Location 收卡方名称及位置 AN 40 sendM recN
	 */
	public static String BCNAMEANDLOCATION = "BCNAMEANDLOCATION";
	/**
	 * 48 Additional Data 额外数据 LLL + AN 220 sendM recN 参照下表字段格式
	 */
	public static String ADDITIONALDATA = "ADDITIONALDATA";
	/**
	 * 49 Transaction Currency Code 交易货币代码 N3 send M rec未用 M
	 */
	public static String TRANSACTIONCURRENCYCODE = "TRANSACTIONCURRENCYCODE";
	/**
	 * 90 Original Data Element 原始数据要素 N42 send M 参照下表 recN
	 */
	public static String ORIGINALDATAELEMENT = "ORIGINALDATAELEMENT";
	/**
	 * Catalogue Code 类别代码AN8
	 */
	public static String CATALOGUECODE = "CATALOGUECODE";
	/** Redemption Reference Number兑换参考号码 AN 12 上海银行交易返给伯桥 */
	public static String REFERENCENUMBER = "REFERENCENUMBER";
	/** Code identifying item to be redeemed. 物品的代码AN8 */
	public static String ITEMCODE = "ITEMCODE";
	/**
	 * Quantity of selected item required. 需要所选物品的数量N3
	 */
	public static String QUANTITY = "QUANTITY";
	/** FastTrack Indicator(N物品，B金额)快速兑换指示 */
	public static String FASTTRACKINDICATOR = "FASTTRACKINDICATOR";
	/**
	 * Points deducted from cardholder account to fulfil redemption.
	 * 从持卡人帐户扣除用来兑换的积分N8
	 */
	public static String POINTSREDEEMED = "POINTSREDEEMED";
	/**
	 * FastTrack Amount 快速兑换金额N9.2
	 */
	public static String FASTTRACKAMOUNT = "FASTTRACKAMOUNT";
	/**
	 * Original Message Type 原始消息类别 M N4--原始MTI
	 */
	public static String ORIGINALMESSAGETYPE = "ORIGINALMESSAGETYPE";
	/**
	 * Original System Trace Number 原始系统跟踪号--原始F11值
	 */
	public static String ORIGINALSYSTEMTRACENUMBE = "ORIGINALSYSTEMTRACENUMBE";
	/**
	 * Original Transaction Date – Time 原始交易日期-时间--原始F7值
	 */
	public static String ORIGINALTRANSACTIONDATE = "ORIGINALTRANSACTIONDATE";
	/**
	 * Original Acquire Institute ID Code 原始收单机构ID代码 M N11--原始F32值
	 */
	public static String ORIGINALACQUIREINSTITUTE = "ORIGINALACQUIREINSTITUTE";
	/**
	 * Original Forwarding Institute ID Code 原始转发机构ID代码 M N11 --原始F33值
	 */
	public static String ORIGINALFORWARDINGINSTITUTE = "ORIGINALFORWARDINGINSTITUTE";
	/** 商户订单序号 */
	public static String ORDERID = "ORDERID";
	/** 交易状态 */
	public static String RESPONSEMSG = "RESPONSEMSG";
	/** 发起撤销、退货、冲正对应原域 */
	public static String NEW7 = "NEW7";
	/** 发起撤销、退货、冲正对应原域 */
	public static String NEW11 = "NEW11";
	/** 发起撤销、退货、冲正对应原域 */
	public static String NEW12 = "NEW12";
	/** 发起撤销、退货、冲正对应原域 */
	public static String NEW13 = "NEW13";
	/** 发起撤销、退货、冲正对应原域 */
	public static String NEW15 = "NEW15";
	/** 发起撤销、退货、冲正对应原域 */
	public static String NEW37 = "NEW37";

	/** REAL类别代码 */
	public static String CATALOGUECODE1 = "CATALOGUECODE";
	/** REAL物品代码 */
	public static String ITEMCODE1 = "ITEMCODE";
	/** REAL伯乔产品ID */
	public static String BCPROID = "BCPROID";
	/** REAL产品信息 */
	public static String COMM = "COMM";
	/** REAL快速兑换指示 */
	public static String FASTTRACK = "FASTTRACK";
	/** REAL兑换数量 */
	public static String POINTSNUM = "POINTSNUM";
	/** REAL活动起始时间 */
	public static String STARTDATE = "STARTDATE";
	/** REAL活动结束时间 */
	public static String ENDDATE = "ENDDATE";
	/** REAL商户结算模式 */
	public static String FATTRADENUMC = "FATTRADENUMC";
	/** REAL银行结算模式 */
	public static String FATTRADENUMB = "FATTRADENUMB";
	/** 单笔伯乔收益 */
	public static String BQBENEFIT = "BQBENEFIT";
	/** 每周活动日期详情example    1#2 */
	public static String ACTIVITDAY = "ACTIVITDAY";
	/** 单个活动日名额限制 */
	public static String DAYPOINTSNUM = "DAYPOINTSNUM";
}
