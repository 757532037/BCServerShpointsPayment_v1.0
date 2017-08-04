package com.shpoints.enm;

public enum Mti {

	/**积分兑奖*/
	TRADE("0240","0250","350000","积分兑奖"),
	/**积分兑奖撤消*/
	CANCEL("0260","0270","360000","积分兑奖撤消"),
	/**积分兑奖冲正*/
	REVERSE("0280","0290","370000","积分兑奖冲正"),
	/**积分兑奖撤消冲正*/
	CANCELREVERSE("0300","0310","380000","积分兑奖撤消冲正"),
	/**积分余额查询*/
	QUERY("0320","0330","390000","积分余额查询"),
	/**积分兑奖退货*/
	REFUND("0340","0350","400000","积分兑奖退货");
	
	/**获取Mit*/
	public static Mti getMit(String msg_type_snd){
		return getBymsg_type_snd(msg_type_snd);
	}
	/***
	 * 根据msg_type_snd找MIT
	 * @param msg_type_snd
	 * @return
	 */
	public static Mti getBymsg_type_snd(String msg_type_snd){
		if(TRADE.getMsg_type_snd().equals(msg_type_snd)){
			return TRADE;
		}else if(CANCEL.getMsg_type_snd().equals(msg_type_snd)){
			return CANCEL;
		}else if(REVERSE.getMsg_type_snd().equals(msg_type_snd)){
			return REVERSE;
		}else if(CANCELREVERSE.getMsg_type_snd().equals(msg_type_snd)){
			return CANCELREVERSE;
		}else if(QUERY.getMsg_type_snd().equals(msg_type_snd)){
			return QUERY;
		}else if(REFUND.getMsg_type_snd().equals(msg_type_snd)){
			return REFUND;
		}
		return  null;
	}
	
	String msg_type_snd;
	String msg_type_rcv;
	String code_snd;
	String msg;
	Mti(String msg_type_snd,String msg_type_rcv, String code_snd,String msg){
		this.msg_type_snd = msg_type_snd;
		this.msg_type_rcv = msg_type_rcv;
		this.code_snd = code_snd;
		this.msg = msg;
	}
	public String getMsg_type_snd() {
		return msg_type_snd;
	}
	public void setMsg_type_snd(String msg_type_snd) {
		this.msg_type_snd = msg_type_snd;
	}
	public String getMsg_type_rcv() {
		return msg_type_rcv;
	}
	public void setMsg_type_rcv(String msg_type_rcv) {
		this.msg_type_rcv = msg_type_rcv;
	}
	public String getCode_snd() {
		return code_snd;
	}
	public void setCode_snd(String code_snd) {
		this.code_snd = code_snd;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
