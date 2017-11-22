package core.exceptions;

public class NullException extends CouponSystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NullException() {
	}

	public NullException(String arg0) {
		super(arg0);
	}

	public NullException(Throwable arg0) {
		super(arg0);
	}

	public NullException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NullException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
