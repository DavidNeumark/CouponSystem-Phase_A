package core.exceptions;

import java.sql.SQLException;

public class UpdateCompanyException extends SQLException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UpdateCompanyException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UpdateCompanyException(String reason, String sqlState, int vendorCode, Throwable cause) {
		super(reason, sqlState, vendorCode, cause);
		// TODO Auto-generated constructor stub
	}

	public UpdateCompanyException(String reason, String SQLState, int vendorCode) {
		super(reason, SQLState, vendorCode);
		// TODO Auto-generated constructor stub
	}

	public UpdateCompanyException(String reason, String sqlState, Throwable cause) {
		super(reason, sqlState, cause);
		// TODO Auto-generated constructor stub
	}

	public UpdateCompanyException(String reason, String SQLState) {
		super(reason, SQLState);
		// TODO Auto-generated constructor stub
	}

	public UpdateCompanyException(String reason, Throwable cause) {
		super(reason, cause);
		// TODO Auto-generated constructor stub
	}

	public UpdateCompanyException(String reason) {
		super(reason);
		// TODO Auto-generated constructor stub
	}

	public UpdateCompanyException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
