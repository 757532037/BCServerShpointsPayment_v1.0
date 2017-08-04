package com.shpoints.client;


/**
 * 
 * size+message type +bitmap +报文请求数据（域字段）
 * <p>发送客户端.</p>
 *
 */
public class SimpleClient extends AbstractClient{
	
	public SimpleClient(String ip,int port,int timeout) {
		super(ip, port, timeout);
	}

	
	// 接口长度的定义方式，可根据需求更改
	// 第一字节值=len/256;
	// 第二字节值=len%256;
	@Override
	protected int computeLength(byte[] lenBts){
		if(lenBts.length!=2){
			throw new IllegalArgumentException("字节长度不正确，预期值为2，实际值为："+lenBts.length);
		}
		return (lenBts[0] & 0xff) * 256
				+ (lenBts[1] & 0xff);
	}

}
