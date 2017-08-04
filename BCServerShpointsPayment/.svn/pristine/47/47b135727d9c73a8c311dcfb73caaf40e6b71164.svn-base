package bc.core.checkpoints;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="message")
public class BosGetUserInfoBean {
	//信息类型
	@XmlElement
	private String infoType;
	//卡号
	@XmlElement
	private String pan;
	//刷卡金额
	@XmlElement
	private BigDecimal money;
	//伯乔流水号
	@XmlElement
	private String bcseq;
	//该卡的客户号
	@XmlElement
	private String customerId;
	//返回码
	@XmlElement
	private String retCode;
	//附加响应
	@XmlElement
	private String commentRes;
	//保留域
	@XmlElement
	private String reserved;
	/**
	 * @Description 信息类型
	 * @return the infoType
	 */
	public String getInfoType() {
		return infoType;
	}
	/**
	 * @Description 信息类型
	 * @params infoType 
	 */
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	/**
	 * @Description 卡号
	 * @return the pan
	 */
	public String getPan() {
		return pan;
	}
	/**
	 * @Description 卡号
	 * @params pan 
	 */
	public void setPan(String pan) {
		this.pan = pan;
	}
	/**
	 * @Description 刷卡金额
	 * @return the money
	 */
	public BigDecimal getMoney() {
		return money;
	}
	/**
	 * @Description 刷卡金额
	 * @params money 
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	/**
	 * @Description 伯乔流水号
	 * @return the bcseq
	 */
	public String getBcseq() {
		return bcseq;
	}
	/**
	 * @Description 伯乔流水号
	 * @params bcseq 
	 */
	public void setBcseq(String bcseq) {
		this.bcseq = bcseq;
	}
	/**
	 * @Description 该卡的客户号
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}
	/**
	 * @Description 该卡的客户号
	 * @params customerId 
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	/**
	 * @Description 返回码
	 * @return the retCode
	 */
	public String getRetCode() {
		return retCode;
	}
	/**
	 * @Description 返回码
	 * @params retCode 
	 */
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	/**
	 * @Description 附加响应
	 * @return the commentRes
	 */
	public String getCommentRes() {
		return commentRes;
	}
	/**
	 * @Description 附加响应
	 * @params commentRes 
	 */
	public void setCommentRes(String commentRes) {
		this.commentRes = commentRes;
	}
	/**
	 * @Description 保留域
	 * @return the reserved
	 */
	public String getReserved() {
		return reserved;
	}
	/**
	 * @Description 保留域
	 * @params reserved 
	 */
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	
	public boolean isSuccess(){
		return this.retCode.equals("000000");
	}
}
