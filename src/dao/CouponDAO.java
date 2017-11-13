package dao;

import java.sql.SQLException;
import java.text.ParseException;
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
	 * @throws SQLException
	 * @throws CouponSystemException
	 * @throws ParseException
	 */
	public Coupon createCoupon(Coupon coupon) throws SQLException, CouponSystemException, ParseException;

	/**
	 * Deletes the coupon data from the database.
	 * 
	 * @param coupon
	 * @throws SQLException
	 * @throws CouponSystemException
	 */
	public void deleteCoupon(Coupon coupon) throws SQLException, CouponSystemException;

	/**
	 * Updates the coupon data in the database.
	 * 
	 * @param coupon
	 * @throws SQLException
	 * @throws CouponSystemException
	 */
	public void updateCoupon(Coupon coupon) throws SQLException, CouponSystemException;

	/**
	 * Gets the coupon data from the database by id.
	 * 
	 * @param ID
	 * @return a coupon object
	 * @throws SQLException
	 * @throws CouponSystemException
	 */
	public Coupon getCoupon(long ID) throws SQLException, CouponSystemException;

	/**
	 * Gets all coupons data.
	 * 
	 * @return collection of all coupon types
	 * @throws SQLException
	 * @throws CouponSystemException
	 */
	public Set<Coupon> getAllCoupons() throws SQLException, CouponSystemException;

	/**
	 * Gets the data for all coupons of a given type
	 * 
	 * @param couponType
	 * @return
	 * @throws SQLException
	 */
	public Set<Coupon> getCouponByType(CouponType couponType) throws SQLException;

}
