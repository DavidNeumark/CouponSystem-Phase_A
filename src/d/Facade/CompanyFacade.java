package d.Facade;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import b.DBDAO.CompanyDBDAO;
import b.DBDAO.CouponDBDAO;
import b.Enum.ClientType;
import b.Enum.CouponType;
import b.beans.Company;
import b.beans.Coupon;
import core.exceptions.CompanyNotFound;
import core.exceptions.CouponNotFound;
import core.exceptions.CouponSystemException;

/**
 * @author D.Neumark
 *
 */
public class CompanyFacade implements CouponClientFacade {

	private CouponDBDAO couponDBDAO;
	private CompanyDBDAO companyDBDAO;
	private Company company;

	/**
	 * CompanyFacade CTOR
	 */
	public CompanyFacade() {
		couponDBDAO = new CouponDBDAO();
		companyDBDAO = new CompanyDBDAO();
	}

	/**
	 * inserts the data of the new coupon into two tables in the database, "coupons"
	 * and "companyCoupon".R
	 * 
	 * @param coupon
	 * @return a coupon object
	 * @throws CouponSystemException
	 * @throws ParseException
	 * @throws CouponNotFound
	 */
	public Coupon createCoupon(Coupon coupon) throws CouponSystemException, ParseException, CouponNotFound {

		Coupon couponChecked = this.couponDBDAO.getCouponByTitle(coupon.getTitle());

		if (couponChecked == null) {

			couponDBDAO.createCoupon(coupon);

			companyDBDAO.createCompanyCoupon(company.getID(), coupon.getID());

		} else {
			throw new CouponSystemException("The coupon already exists in the database.");
		}
		return coupon;

	}

	/**
	 * Deletes the coupon data from the system.
	 * 
	 * @param coupon
	 * @throws CouponSystemException
	 */
	public void removeCoupon(Coupon coupon) throws CouponSystemException {

		couponDBDAO.getCouponIdByTitle(coupon);

		couponDBDAO.deleteCoupon(coupon);

	}

	/**
	 * Updates the coupon data(startDate, endDate and price) in the database.
	 * 
	 * @param coupon
	 * @throws CouponSystemException
	 */
	public void updateCoupon(Coupon coupon) throws CouponSystemException {

		couponDBDAO.updateCoupon(coupon);

	}

	/**
	 * Gets the data of the coupon from the database.
	 * 
	 * @param id
	 * @return a coupon object
	 * @throws CouponSystemException
	 */
	public Coupon getCoupon(long id) throws CouponSystemException {

		return couponDBDAO.getCoupon(id);
	}

	/**
	 * This method gets from the database a list with the data of all coupons in the
	 * system.
	 * 
	 * @return a collection of coupons
	 * @throws CouponSystemException
	 */
	public Set<Coupon> getAllCoupons() throws CouponSystemException {

		return couponDBDAO.getAllCoupons(this.company);
	}

	/**
	 * This method gets from the database a list of all coupons in the system of a
	 * specific type.
	 * 
	 * @param couponType
	 * @return list of coupons
	 * @throws CouponSystemException
	 */
	public Set<Coupon> getCouponByType(CouponType couponType) throws CouponSystemException {

		Set<Coupon> couponsByType = new HashSet<>();

		for (Coupon coupon : couponDBDAO.getAllCoupons()) {
			if (coupon.getType() == couponType) {
				couponsByType.add(coupon);
			}
		}

		return couponsByType;
	}

	/**
	 * This method gets from the database a list of all coupons in the system up to
	 * the specified price.
	 * 
	 * @param couponPrice
	 * @return list of coupons
	 * @throws CouponSystemException
	 */
	public Set<Coupon> getCouponByPrice(double couponPrice) throws CouponSystemException {

		Set<Coupon> couponsByPrice = new HashSet<>();

		for (Coupon coupon : couponDBDAO.getAllCoupons()) {
			if (coupon.getPrice() <= couponPrice) {
				couponsByPrice.add(coupon);
			}

		}

		return couponsByPrice;
	}

	/**
	 * This method gets from the database a list of all coupons in the system up to
	 * the specified date.
	 * 
	 * @param endDate
	 * @return list of coupons
	 * @throws CouponSystemException
	 */
	public Set<Coupon> getCouponByDate(Date date) throws CouponSystemException {

		Set<Coupon> getCouponByExpirationDate = new HashSet<>();

		for (Coupon coupon : couponDBDAO.getAllCoupons()) {

			if (coupon.getEndDate().before(date)) {
				getCouponByExpirationDate.add(coupon);
			}

		}

		return getCouponByExpirationDate;
	}

	public Coupon getCouponByTitle(String _title) throws CouponNotFound {

		return couponDBDAO.getCouponByTitle(_title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see d.Facade.CouponClientFacade#login(java.lang.String, java.lang.String,
	 * b.Enum.ClientType)
	 */
	@Override
	public CouponClientFacade login(String name, String password, ClientType clientType)
			throws SQLException, CouponSystemException, CompanyNotFound {

		if (!name.equals("") && !password.equals("")) {
			if (this.companyDBDAO.login(name, password)) {
				this.company = this.companyDBDAO.getCompanyByName(name);
				return this;
			} else {
				this.company = null;
				return null;
			}
		}

		return null;
	}

}
