package core.exceptions;

public class CloseConnectionsException extends CouponSystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CloseConnectionsException() {
		super();
	}

	public CloseConnectionsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CloseConnectionsException(String message) {
		super(message);
	}

	public CloseConnectionsException(Throwable cause) {
		super(cause);
	}

}
