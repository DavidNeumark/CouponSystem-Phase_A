package core.exceptions;

public class MoreThanOneCouponException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MoreThanOneCouponException() {
	}

	public MoreThanOneCouponException(String message) {
		super(message);
	}

	public MoreThanOneCouponException(Throwable cause) {
		super(cause);
	}

	public MoreThanOneCouponException(String message, Throwable cause) {
		super(message, cause);
	}

	public MoreThanOneCouponException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
