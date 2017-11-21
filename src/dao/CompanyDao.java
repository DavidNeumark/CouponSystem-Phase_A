package dao;

import java.util.Collection;
import java.util.Set;

import beans.Company;
import beans.Coupon;
import core.exceptions.CouponSystemException;

/**
 * @author D.Neumark
 *
 */
public interface CompanyDao {

	/**
	 * Sets a new company data in the companies database table.
	 * 
	 * @param company
	 * @return Company
	 * @throws CouponSystemException
	 */
	public Company createCompany(Company company) throws CouponSystemException;

	/**
	 * Deletes the company data.
	 * 
	 * @param company
	 * @throws CouponSystemException
	 */
	public void deleteCompany(Company company) throws CouponSystemException;

	/**
	 * Update the company data (password, email) in the database.
	 * 
	 * @param company
	 * @throws CouponSystemException
	 */
	public void updateCompany(Company company) throws CouponSystemException;

	/**
	 * Gets the data of the company from the database using the id.
	 * 
	 * @param ID
	 * @return a company object
	 * @throws CouponSystemException
	 */
	public Company getCompany(long ID) throws CouponSystemException;

	/**
	 * Gets the data of all the companies in system.
	 * 
	 * @return get company data
	 * @throws CouponSystemException
	 */
	public Set<Company> getAllCompanies() throws CouponSystemException;

	/**
	 * Gets the data of all coupons from the database.
	 * 
	 * @param company
	 * @return list of all companies in the system
	 * @throws CouponSystemException
	 */
	public Collection<Coupon> getCoupons(Company company) throws CouponSystemException;

	/**
	 * Login into a Company account.
	 * 
	 * @param compName
	 * @param password
	 * @return a boolean value
	 * @throws CouponSystemException
	 */
	public boolean login(String compName, String password) throws CouponSystemException;

}
