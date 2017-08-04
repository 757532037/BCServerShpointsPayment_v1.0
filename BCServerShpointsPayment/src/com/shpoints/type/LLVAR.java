package com.shpoints.type;

/**
 * <p>.</p>
 *
 * @author Magic Joey
 * @version LLVAR.java 1.0 Created@2015-06-15 23:04 $
 */
public class LLVAR implements IsoType {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3347253144560646566L;

	@Override
    public boolean isLVar() {
        return false;
    }

    @Override
    public String setByteValue(byte[] bts) {
        return null;
    }

    @Override
    public byte[] setValue(String bts) {
        return new byte[0];
    }

    @Override
    public int varLength(byte[] bytes) {
        return 0;
    }

}
