-- Create table
create table BCCORE.SHPOINTSPAYMENT
(
  MESSAGETYPEIDENTIFIER       VARCHAR2(4),
  ACCOUNTNUMBER               VARCHAR2(22),
  PROCESSINGCODE              VARCHAR2(6),
  TRANSACTIONAMOUNT           VARCHAR2(12),
  TRANSACTIONDATEANDTIME      VARCHAR2(10),
  SYSTEMTRACEAUDITNUMBER      VARCHAR2(6),
  LOCALTRANSACTIONTIME        VARCHAR2(6),
  LOCALTRANSACTIONDATE        VARCHAR2(4),
  CAPTUREDATE                 VARCHAR2(4),
  ENTRYMODE                   VARCHAR2(3),
  POINTOFSERVICE              VARCHAR2(2),
  ACQUIRINGINSTITUTE          VARCHAR2(22),
  FORWARDINGINSTITUTE         VARCHAR2(22),
  RETRIEVALREFERENCENUMBER    VARCHAR2(12),
  APPROVALCODE                VARCHAR2(6),
  RESPONSECODE                VARCHAR2(2),
  TERMINALIDENTIFICATION      VARCHAR2(8),
  BCTERMINALIDENTIFICATION    VARCHAR2(15),
  BCNAMEANDLOCATION           VARCHAR2(40),
  ADDITIONALDATA              VARCHAR2(225),
  TRANSACTIONCURRENCYCODE     VARCHAR2(3),
  ORIGINALDATAELEMENT         VARCHAR2(42),
  CATALOGUECODE               VARCHAR2(8),
  REFERENCENUMBER             VARCHAR2(20),
  ITEMCODE                    VARCHAR2(8),
  QUANTITY                    VARCHAR2(3),
  FASTTRACKINDICATOR          VARCHAR2(1),
  POINTSREDEEMED              VARCHAR2(8),
  FASTTRACKAMOUNT             VARCHAR2(9),
  ORIGINALMESSAGETYPE         VARCHAR2(4),
  ORIGINALSYSTEMTRACENUMBE    VARCHAR2(6),
  ORIGINALTRANSACTIONDATE     VARCHAR2(10),
  ORIGINALACQUIREINSTITUTE    VARCHAR2(11),
  ORIGINALFORWARDINGINSTITUTE VARCHAR2(11),
  ORDERID                     VARCHAR2(30),
  RESPONSEMSG                 VARCHAR2(30),
  NEW7                        VARCHAR2(10),
  NEW11                       VARCHAR2(6),
  NEW12                       VARCHAR2(6),
  NWE13                       VARCHAR2(4),
  NEW15                       VARCHAR2(4),
  NEW37                       VARCHAR2(12)
)
tablespace TBS_CORE_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns 
comment on column BCCORE.SHPOINTSPAYMENT.MESSAGETYPEIDENTIFIER
  is 'Message Type Identifier
消息类型标识  N 4       send  M     recM';
comment on column BCCORE.SHPOINTSPAYMENT.ACCOUNTNUMBER
  is '2Account Number
帐户号  LL + N 19       send  M      recM';
comment on column BCCORE.SHPOINTSPAYMENT.PROCESSINGCODE
  is '3Processing Code
处理代码  N 6        send  M       recM';
comment on column BCCORE.SHPOINTSPAYMENT.TRANSACTIONAMOUNT
  is '4Transaction Amount
交易金额  N 12  M
        sendM(默认值为0  )    recM';
comment on column BCCORE.SHPOINTSPAYMENT.TRANSACTIONDATEANDTIME
  is '7Transaction Date and Time
交易日期和时间  N 10        sendM    rec  M';
comment on column BCCORE.SHPOINTSPAYMENT.SYSTEMTRACEAUDITNUMBER
  is '11System Trace Audit Number
系统跟踪审计号(当天唯一)  N 6      sendM       recM';
comment on column BCCORE.SHPOINTSPAYMENT.LOCALTRANSACTIONTIME
  is '12  Approval Code
核准代码  AN 6          sendM   recM';
comment on column BCCORE.SHPOINTSPAYMENT.LOCALTRANSACTIONDATE
  is '13Local Transaction Date
当地交易日期  N 4     sendM    recM';
comment on column BCCORE.SHPOINTSPAYMENT.CAPTUREDATE
  is '15Capture Date
清算日期  N 4     sendM  rec  M';
comment on column BCCORE.SHPOINTSPAYMENT.ENTRYMODE
  is '22Entry Mode
服务点输入模式  N 3     sendM(参考银联规范２２域  )   recM';
comment on column BCCORE.SHPOINTSPAYMENT.POINTOFSERVICE
  is '25Point of Service Condition Code
POS代码  N 2    sendM
(ATM – “02”
IVR – “08”
CDM – “12”
Internet – “42”
Branch – “04”
Multimedia Kiosk – “05”
Others – “06”    defaulte-50)   recM';
comment on column BCCORE.SHPOINTSPAYMENT.ACQUIRINGINSTITUTE
  is '32Acquiring Institute Identification Code
收单机构识别代码  LL + N 11     sendM  rec  M';
comment on column BCCORE.SHPOINTSPAYMENT.FORWARDINGINSTITUTE
  is '33Forwarding Institute Identification Code
转发机构识别代码(同32)  LL + N11    sendM rec  M';
comment on column BCCORE.SHPOINTSPAYMENT.RETRIEVALREFERENCENUMBER
  is '37  Retrieval Reference Number       调单参考号码  AN 12    sendM  rec  M';
comment on column BCCORE.SHPOINTSPAYMENT.APPROVALCODE
  is '38  Approval Code
核准代码  AN 6         sendN    recM';
comment on column BCCORE.SHPOINTSPAYMENT.RESPONSECODE
  is '39  Response Code
应答代码  AN2       sendN    recM
';
comment on column BCCORE.SHPOINTSPAYMENT.TERMINALIDENTIFICATION
  is '41  Terminal Identification
终端识别号码  AN 8  send  M   rec  M';
comment on column BCCORE.SHPOINTSPAYMENT.BCTERMINALIDENTIFICATION
  is '42  Card Acceptor Terminal Identification
收卡方终端标识  AN 15     sendM     recN';
comment on column BCCORE.SHPOINTSPAYMENT.BCNAMEANDLOCATION
  is '43  Card Acceptor Name and Location
收卡方名称及位置  AN 40        sendM  recN  ';
comment on column BCCORE.SHPOINTSPAYMENT.ADDITIONALDATA
  is '48  Additional Data
额外数据  LLL + AN 220   sendM    recN
参照下表字段格式  ';
comment on column BCCORE.SHPOINTSPAYMENT.TRANSACTIONCURRENCYCODE
  is '49  Transaction Currency Code
交易货币代码  N3      send  M
      rec未用  M';
comment on column BCCORE.SHPOINTSPAYMENT.ORIGINALDATAELEMENT
  is '90  Original Data Element
原始数据要素  N42    send  M
参照下表    recN';
comment on column BCCORE.SHPOINTSPAYMENT.CATALOGUECODE
  is 'Catalogue Code
类别代码AN8';
comment on column BCCORE.SHPOINTSPAYMENT.REFERENCENUMBER
  is 'Redemption Reference Number兑换参考号码 AN 12   上海银行交易返给伯桥';
comment on column BCCORE.SHPOINTSPAYMENT.ITEMCODE
  is 'Code identifying item to be redeemed. 物品的代码AN8';
comment on column BCCORE.SHPOINTSPAYMENT.QUANTITY
  is 'Quantity of selected item required.
需要所选物品的数量N3';
comment on column BCCORE.SHPOINTSPAYMENT.FASTTRACKINDICATOR
  is 'FastTrack Indicator(N物品，B金额)';
comment on column BCCORE.SHPOINTSPAYMENT.POINTSREDEEMED
  is 'Points deducted from cardholder account to fulfil redemption.
从持卡人帐户扣除用来兑换的积分N8';
comment on column BCCORE.SHPOINTSPAYMENT.FASTTRACKAMOUNT
  is 'FastTrack Amount
快速兑换金额N9.2';
comment on column BCCORE.SHPOINTSPAYMENT.ORIGINALMESSAGETYPE
  is 'Original Message Type
原始消息类别  M  N4--原始MTI';
comment on column BCCORE.SHPOINTSPAYMENT.ORIGINALSYSTEMTRACENUMBE
  is 'Original System Trace Number
原始系统跟踪号--原始F11值';
comment on column BCCORE.SHPOINTSPAYMENT.ORIGINALTRANSACTIONDATE
  is 'Original Transaction Date – Time
原始交易日期-时间--原始F7值';
comment on column BCCORE.SHPOINTSPAYMENT.ORIGINALACQUIREINSTITUTE
  is 'Original Acquire Institute ID Code
原始收单机构ID代码  M  N11--原始F32值';
comment on column BCCORE.SHPOINTSPAYMENT.ORIGINALFORWARDINGINSTITUTE
  is 'Original Forwarding Institute ID Code
原始转发机构ID代码  M  N11  --原始F33值';
comment on column BCCORE.SHPOINTSPAYMENT.ORDERID
  is '商户订单序号';
comment on column BCCORE.SHPOINTSPAYMENT.RESPONSEMSG
  is '交易状态';
comment on column BCCORE.SHPOINTSPAYMENT.NEW7
  is '发起撤销、退货、冲正对应原域';
comment on column BCCORE.SHPOINTSPAYMENT.NEW11
  is '发起撤销、退货、冲正对应原域';
comment on column BCCORE.SHPOINTSPAYMENT.NEW12
  is '发起撤销、退货、冲正对应原域';
comment on column BCCORE.SHPOINTSPAYMENT.NWE13
  is '发起撤销、退货、冲正对应原域';
comment on column BCCORE.SHPOINTSPAYMENT.NEW15
  is '发起撤销、退货、冲正对应原域';
comment on column BCCORE.SHPOINTSPAYMENT.NEW37
  is '发起撤销、退货、冲正对应原域';

  
  -------------------------------上海活动关联表----------------------------------------------------------
 -- Create table
create table BCCORE.SHPOINTS_BQSH_PROREAL
(
  CATALOGUECODE   VARCHAR2(8),
  ITEMCODE        VARCHAR2(8),
  BCPROID         VARCHAR2(22),
  COMM            VARCHAR2(200),
  FASTTRACK       VARCHAR2(1),
  POINTSNUM       VARCHAR2(8),
  START_DATE      VARCHAR2(8),
  END_DATE        VARCHAR2(8),
  FAT_TRADE_NUM_C VARCHAR2(5),
  FAT_TRADE_NUM_B VARCHAR2(5),
  BQ_BENEFIT      VARCHAR2(10),
  ACTIVITDAY      VARCHAR2(20),
  DAYPOINTSNUM    VARCHAR2(8)
)
tablespace TBS_CORE_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns 
comment on column BCCORE.SHPOINTS_BQSH_PROREAL.CATALOGUECODE
  is '银行类别代码';
comment on column BCCORE.SHPOINTS_BQSH_PROREAL.ITEMCODE
  is '银行物品代码';
comment on column BCCORE.SHPOINTS_BQSH_PROREAL.BCPROID
  is '伯乔商户号CUSTID';
comment on column BCCORE.SHPOINTS_BQSH_PROREAL.COMM
  is '产品信息';
comment on column BCCORE.SHPOINTS_BQSH_PROREAL.FASTTRACK
  is '快速兑换指示';
comment on column BCCORE.SHPOINTS_BQSH_PROREAL.POINTSNUM
  is '购买产品数';
comment on column BCCORE.SHPOINTS_BQSH_PROREAL.START_DATE
  is '活动起始日期';
comment on column BCCORE.SHPOINTS_BQSH_PROREAL.END_DATE
  is '活动截止日期';
comment on column BCCORE.SHPOINTS_BQSH_PROREAL.FAT_TRADE_NUM_C
  is '商户结算价（单价1元）';
comment on column BCCORE.SHPOINTS_BQSH_PROREAL.FAT_TRADE_NUM_B
  is '银行结算价（单价1元）';
comment on column BCCORE.SHPOINTS_BQSH_PROREAL.BQ_BENEFIT
  is '单笔伯桥盈利（单笔）';
comment on column BCCORE.SHPOINTS_BQSH_PROREAL.ACTIVITDAY
  is '活动日期用|分割如星期一和星期二活动日即1#2';
comment on column BCCORE.SHPOINTS_BQSH_PROREAL.DAYPOINTSNUM
  is '单个活动日名额';


-----------------------------卡号信息表-------------------------------------------------------
-- Create table
create table BCCORE.SHBK_CUST_INFO
(
  CARD_BIN  VARCHAR2(20),
  CARD_NO   VARCHAR2(30),
  USER_ID   VARCHAR2(50),
  TRADEDATE VARCHAR2(8) default to_char(sysdate,'yyyymmdd')
)
tablespace BCPAY_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns 
comment on column BCCORE.SHBK_CUST_INFO.CARD_BIN
  is '卡BIN号';
comment on column BCCORE.SHBK_CUST_INFO.CARD_NO
  is '卡号';
comment on column BCCORE.SHBK_CUST_INFO.USER_ID
  is '用户ID';
comment on column BCCORE.SHBK_CUST_INFO.TRADEDATE
  is '插入时间';
