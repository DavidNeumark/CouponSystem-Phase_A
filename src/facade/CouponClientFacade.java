package facade;

import java.sql.SQLException;

import core.exceptions.CompanyNotFound;
import core.exceptions.CouponSystemException;
import core.exceptions.CustomerNotFound;
import enumPackage.ClientType;

/**
 * @author D.Neumark
 *
 */
public interface CouponClientFacade {

	/**
	 * Login interface
	 * 
	 * @param name
	 * @param password
	 * @param clientType
	 * @throws SQLException
	 * @throws CouponSystemException
	 * @throws CompanyNotFound
	 * @throws CustomerNotFound
	 */
	public CouponClientFacade login(String name, String password, ClientType clientType)
			throws SQLException, CouponSystemException, CompanyNotFound, CustomerNotFound;

}