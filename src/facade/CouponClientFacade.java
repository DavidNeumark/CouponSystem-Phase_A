package facade;

import core.exceptions.CouponSystemException;
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
	 * @return a boolean value
	 * @throws CouponSystemException
	 */
	public CouponClientFacade login(String name, String password, ClientType clientType) throws CouponSystemException;

}
