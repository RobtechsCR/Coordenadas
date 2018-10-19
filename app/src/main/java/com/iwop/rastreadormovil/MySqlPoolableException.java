package com.iwop.rastreadormovil;

public class MySqlPoolableException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8643803910884700751L;

	public MySqlPoolableException(final String msg, Exception e) {
        super(msg, e);
    }
}