package core.exceptions;

public class DuplicateCouponException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateCouponException(String string) {
		// TODO Auto-generated constructor stub
		super(string);
	}

	public DuplicateCouponException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DuplicateCouponException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public DuplicateCouponException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DuplicateCouponException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
