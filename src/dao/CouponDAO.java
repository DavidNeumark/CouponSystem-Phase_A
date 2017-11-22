package dao;

import java.sql.SQLException;
import java.util.Set;

import beans.Coupon;
import core.exceptions.CouponSystemException;
import enumPackage.CouponType;

public interface CouponDAO {

	/**
	 * Sets a new coupon data into "coupons" database table.
	 * 
	 * @param coupon
	 * @return a coupon object
	 * @throws CouponSystemException
	 */
	public Coupon createCoupon(Coupon coupon) throws CouponSystemException;

	/**
	 * Deletes the coupon data from the database.
	 * 
	 * @param coupon
	 * @throws CouponSystemException
	 */
	public void deleteCoupon(Coupon coupon) throws CouponSystemException;

	/**
	 * Updates the coupon data in the database.
	 * 
	 * @param coupon
	 * @throws CouponSystemException
	 */
	public void updateCoupon(Coupon coupon) throws CouponSystemException;

	/**
	 * Gets the coupon data from the database by id.
	 * 
	 * @param ID
	 * @return a coupon object
	 * @throws CouponSystemException
	 */
	public Coupon getCoupon(long ID) throws CouponSystemException;

	/**
	 * Gets all coupons data.
	 * 
	 * @return collection of all coupon types
	 * @throws CouponSystemException
	 */
	public Set<Coupon> getAllCoupons() throws CouponSystemException;

	/**
	 * Gets the data for all coupons of a given type
	 * 
	 * @param couponType
	 * @return
	 * @throws CouponSystemException
	 * @throws SQLException
	 */
	public Set<Coupon> getCouponByType(CouponType couponType) throws CouponSystemException, SQLException;

}
