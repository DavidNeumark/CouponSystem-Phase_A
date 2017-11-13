package dao;

import java.sql.SQLException;
import java.util.Set;

import beans.Coupon;
import beans.Customer;
import core.exceptions.CouponSystemException;

public interface CustomerDAO {

	/**
	 * Inserts the data of a new customer in the database.
	 * 
	 * @param customer
	 * @return a customer object
	 * @throws CouponSystemException
	 */
	public Customer createCustomer(Customer customer) throws CouponSystemException;

	/**
	 * Delete the customer data from the database.
	 * 
	 * @param customer
	 * @throws CouponSystemException
	 */
	public void deleteCustomer(Customer customer) throws CouponSystemException;

	/**
	 * Update the new customer data in the database.
	 * 
	 * @param customer
	 * @throws CouponSystemException
	 */
	public void updateCustomer(Customer customer) throws CouponSystemException;

	/**
	 * Gets the customer data using the ID.
	 * 
	 * @param ID
	 * @return a customer object.
	 * @throws CouponSystemException
	 */
	public Customer getCustomer(long ID) throws CouponSystemException;

	/**
	 * Gets the data of all customers in the system.
	 * 
	 * @return collection of customers
	 * @throws CouponSystemException
	 */
	public Set<Customer> getAllCustomers() throws CouponSystemException;

	/**
	 * Gets a collection of coupons from a particular customer
	 * 
	 * @param customer
	 * @return collection of coupons
	 * @throws SQLException
	 * @throws CouponSystemException
	 */
	public Set<Coupon> getCoupons(Customer customer) throws SQLException, CouponSystemException;

	/**
	 * Login into the customer account.
	 * 
	 * @param customerName
	 * @param password
	 * @return boolean
	 * @throws CouponSystemException
	 */
	public Boolean login(String customerName, String password) throws CouponSystemException;

}
