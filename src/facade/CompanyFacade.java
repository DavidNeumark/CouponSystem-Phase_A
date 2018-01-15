package facade;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import beans.Company;
import beans.Coupon;
import core.exceptions.CouponSystemException;
import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import enumPackage.ClientType;
import enumPackage.CouponType;

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
	 * 
	 * @throws CouponSystemException
	 */
	public CompanyFacade() throws CouponSystemException {
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
	 */
	public Coupon createCoupon(Coupon coupon) throws CouponSystemException {

		Coupon couponChecked = this.couponDBDAO.getCouponByTitle(coupon.getTitle());

		if (couponChecked == null) {

			couponDBDAO.createCoupon(coupon);

			companyDBDAO.createCompanyCoupon(company.getID(), coupon.getID());

		} else {
			throw new CouponSystemException("The coupon: " + coupon.getTitle() + ", already exists in the database.");
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

		couponDBDAO.getCouponByTitle(coupon.getTitle());

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
	 * system of a particular Company.
	 * 
	 * @return a collection of coupons
	 * @throws CouponSystemException
	 */
	public Set<Coupon> getAllCoupons() throws CouponSystemException {

		return couponDBDAO.getAllCoupons(company);
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

		for (Coupon coupon : couponDBDAO.getAllCoupons(company)) {
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

		for (Coupon coupon : couponDBDAO.getAllCoupons(company)) {
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
	 * @param date
	 * @return list of coupons
	 * @throws CouponSystemException
	 */
	public Set<Coupon> getCouponByDate(Date date) throws CouponSystemException {

		Set<Coupon> getCouponByExpirationDate = new HashSet<>();

		for (Coupon coupon : couponDBDAO.getAllCoupons(company)) {

			if (coupon.getEndDate().before(date)) {
				getCouponByExpirationDate.add(coupon);
			}

		}

		return getCouponByExpirationDate;
	}

	/**
	 * Gets the coupon object by the title
	 * 
	 * @param _title
	 * @return
	 * @throws CouponSystemException
	 */
	public Coupon getCouponByTitle(String _title) throws CouponSystemException {

		return couponDBDAO.getCouponByTitle(_title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see d.Facade.CouponClientFacade#login(java.lang.String, java.lang.String,
	 * b.Enum.ClientType)
	 */
	@Override
	public CouponClientFacade login(String name, String password, ClientType clientType) throws CouponSystemException {

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
