package com.shpoints.exception;

/**
 * <p>框架运行时异常.</p>
 */
public class Simple8583Exception extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Simple8583Exception() {
        super();
    }

    public Simple8583Exception(String s) {
        super(s);
    }

    public Simple8583Exception(String s, Throwable throwable) {
        super(s, throwable);
    }

    public Simple8583Exception(Throwable throwable) {
        super(throwable);
    }
}
