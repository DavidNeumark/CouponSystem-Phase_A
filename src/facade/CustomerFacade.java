package facade;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import beans.Coupon;
import beans.Customer;
import core.exceptions.CouponSystemException;
import dbdao.CouponDBDAO;
import dbdao.CustomerDBDAO;
import enumPackage.ClientType;
import enumPackage.CouponType;

/**
 * @author D.Neumark
 */
public class CustomerFacade implements CouponClientFacade {

	private CouponDBDAO couponDBDAO;
	private CustomerDBDAO customerDBDAO;
	private Customer customer;

	/**
	 * Class CTOR
	 * 
	 * @throws CouponSystemException
	 */
	public CustomerFacade() throws CouponSystemException {
		couponDBDAO = new CouponDBDAO();
		customerDBDAO = new CustomerDBDAO();
	}

	/**
	 * Sets a new coupon in the customer account.
	 * 
	 * @param coupon
	 * @throws CouponSystemException
	 */
	public void purchaseCoupon(Coupon coupon) throws CouponSystemException {
		couponDBDAO.getCouponIdByTitle(coupon);
		boolean check = false;

		if (coupon.getAmount() > 0) {
			check = true;

			Set<Coupon> coupons = customerDBDAO.getCoupons(customer);
			for (Coupon coupon1 : coupons) {
				if (coupon.getID() == coupon1.getID()) {
					check = false;
					break;
				}
			}
			Date date = new Date();
			date.setTime(System.currentTimeMillis());
			if (coupon.getEndDate().compareTo(date) <= 0) {
				check = false;
			}

		}
		if (check) {
			customerDBDAO.purchaseCustomerCoupon(customer.getID(), coupon.getID());
		} else {
			throw new CouponSystemException(
					"Purchasing this coupon failed. Maybe the customer already bought a coupon like this, maybe the expiration date expired or there is no coupon like this in the stock");
		}

	}

	/**
	 * Gets all purchased coupons by the customer.
	 * 
	 * @return
	 * @throws CouponSystemException
	 */
	public Set<Coupon> getAllPurchasedCoupons() throws CouponSystemException {

		Set<Coupon> coupons = this.customerDBDAO.getCoupons(customer);

		return coupons;
	}

	/**
	 * Gets all purchased coupons by type
	 * 
	 * @param couponType
	 * @return a collection with all purchased coupons there are in the account of
	 *         the customer, selected according to the type of coupon
	 * @throws CouponSystemException
	 */
	public Set<Coupon> getAllPurchasedCouponByType(CouponType couponType) throws CouponSystemException {

		Set<Coupon> coupons = this.customerDBDAO.getCoupons(customer);
		Set<Coupon> couponsByType = new HashSet<>();

		for (Coupon coupon : coupons) {
			if (coupon.getType() == couponType) {
				couponsByType.add(coupon);
			}
		}

		return couponsByType;
	}

	/**
	 * Get all purchased coupon up to the given price
	 * 
	 * @param price
	 * @return a collection with all purchased coupons there are in the account of
	 *         the customer, selected according to the price
	 * @throws CouponSystemException
	 */
	public Set<Coupon> getAllPurchasedCouponPrice(double price) throws CouponSystemException {

		Set<Coupon> coupons = this.customerDBDAO.getCoupons(customer);
		Set<Coupon> couponsByPrice = new HashSet<>();

		for (Coupon coupon : coupons) {
			if (coupon.getPrice() <= price) {
				couponsByPrice.add(coupon);
			}
		}

		return couponsByPrice;
	}

	/**
	 * Sets the customer id in the customer object using the customer name that is
	 * stored in the object
	 * 
	 * @param customer
	 * @throws CouponSystemException
	 */
	public void setCustomerIdByName(Customer customer) throws CouponSystemException {
		customerDBDAO.getCustomerIdByName(customer);
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
			if (this.customerDBDAO.login(name, password)) {
				this.customer = this.customerDBDAO.getCustomerByName(name);
				return this;
			} else {
				this.customer = null;
				return null;
			}
		}

		return null;
	}
}
