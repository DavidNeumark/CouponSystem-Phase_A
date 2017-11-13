package dao;

import java.sql.SQLException;
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
	 * @return
	 * @throws SQLException
	 * @throws CouponSystemException
	 */
	public Company createCompany(Company company) throws SQLException, CouponSystemException;

	/**
	 * Deletes the company data.
	 * 
	 * @param company
	 * @throws SQLException
	 * @throws CouponSystemException
	 */
	public void deleteCompany(Company company) throws SQLException, CouponSystemException;

	/**
	 * Update the company data (password, email) in the database.
	 * 
	 * @param company
	 * @throws SQLException
	 */
	public void updateCompany(Company company) throws SQLException;

	/**
	 * Gets the data of the company from the database using the id.
	 * 
	 * @param ID
	 * @return
	 * @throws SQLException
	 * @throws CouponSystemException
	 */
	public Company getCompany(long ID) throws SQLException, CouponSystemException;

	/**
	 * Gets the data of all the companies in system.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Set<Company> getAllCompanies() throws SQLException;

	/**
	 * Gets the data of all coupons from the database.
	 * 
	 * @param company
	 * @return
	 * @throws SQLException
	 */
	public Collection<Coupon> getCoupons(Company company) throws SQLException;

	/**
	 * Login into a Company account.
	 * 
	 * @param compName
	 * @param password
	 * @return
	 * @throws SQLException
	 * @throws CouponSystemException
	 */
	public boolean login(String compName, String password) throws SQLException, CouponSystemException;

}
