package com.shpoints.enm;

import com.shpoints.enm.ResultCode;

public enum ResultCode {
		
	SUCCESS("执行成功","00"),
	CARDTYPEERROR("公司卡或者非主卡不允许积分兑换!","U1"),
	CARDUSERDATEERROR("卡片不在有效期","U2"),
	OLDCARD("卡片为旧卡且新卡已激活","U3"),
	CARESTATUSERROR("信用卡状态异常","U4"),
	CHECKCODEERROR("监控代码检查","U5"),
	PARERROR("交易无积分或产品不可兑换","U6"),
	NOTENOUGHPOINTS("兑换积分不足","U7"),
	NOTENOUGHCDK("兑换物品库存不足","U8"),
	NOTINTITCARD("卡片在有效期但未激活","U9"),
	CARDIDNOTFUND("卡号或账户状态不正常","14"),
	NOTRADEFUND("未找到原交易","25"),
	SYSERROR("未知的卡片异常!","99");
	String err_msg;
	String err_code;
	/**获取ErrorCode*/
	public static ResultCode getErrorCode(String code){
		return getErrorCodeByCode(code);
	}

	/**根据CODE找ErrorCode*/
	public static ResultCode getErrorCodeByCode(String code){
		if(CARDTYPEERROR.getErr_code().equals(code)){
			return CARDTYPEERROR;
		}else if(CARDUSERDATEERROR.getErr_code().equals(code)){
			return CARDUSERDATEERROR;
		}else if(OLDCARD.getErr_code().equals(code)){
			return OLDCARD;
		}else if(CARESTATUSERROR.getErr_code().equals(code)){
			return CARESTATUSERROR;
		}else if(CHECKCODEERROR.getErr_code().equals(code)){
			return CHECKCODEERROR;
		}else if(PARERROR.getErr_code().equals(code)){
			return PARERROR;
		}else if(NOTENOUGHPOINTS.getErr_code().equals(code)){
			return NOTENOUGHPOINTS;
		}else if(NOTENOUGHCDK.getErr_code().equals(code)){
			return NOTENOUGHCDK;
		}else if(NOTINTITCARD.getErr_code().equals(code)){
			return NOTINTITCARD;
		}else if(CARDIDNOTFUND.getErr_code().equals(code)){
			return CARDIDNOTFUND;
		}else if(NOTRADEFUND.getErr_code().equals(code)){
			return NOTRADEFUND;
		}else if(SUCCESS.getErr_code().equals(code)){
			return SUCCESS;
		}else{
			return SYSERROR;
		}
	}
	
	ResultCode(String err_msg,String err_code){
		this.err_msg = err_msg;
		this.err_code = err_code;
	}
	public String getErr_msg() {
		return err_msg;
	}
	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}
	public String getErr_code() {
		return err_code;
	}
	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}
	
	
}
