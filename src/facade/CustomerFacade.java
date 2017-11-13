package facade;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import beans.Coupon;
import beans.Customer;
import core.exceptions.CouponSystemException;
import core.exceptions.CustomerNotFound;
import core.exceptions.EndDateException;
import core.exceptions.MoreThanOneCouponException;
import core.exceptions.PurchasedException;
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
	 */
	public CustomerFacade() {
		couponDBDAO = new CouponDBDAO();
		customerDBDAO = new CustomerDBDAO();
	}

	/**
	 * Sets a new coupon in the customer account.
	 * 
	 * @param coupon
	 * @throws EndDateException
	 * @throws MoreThanOneCouponException
	 * @throws CouponSystemException
	 * @throws SQLException
	 * @throws PurchasedException
	 */
	public void purchaseCoupon(Coupon coupon) throws EndDateException, MoreThanOneCouponException,
			CouponSystemException, SQLException, PurchasedException {
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
			throw new PurchasedException(
					"Purchasing this coupon failed. Maybe the customer already bought a coupon like this, maybe the expiration date expired or there is no coupon like this in the stock");
		}

	}

	/**
	 * @param couponType
	 * @return a collection with all purchased coupons there are in the account of
	 *         the customer, selected according to the type of coupon
	 * @throws SQLException
	 * @throws CouponSystemException
	 */
	public Set<Coupon> getAllPurchasedCouponByType(CouponType couponType) throws SQLException, CouponSystemException {

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
	 * @param price
	 * @return a collection with all purchased coupons there are in the account of
	 *         the customer, selected according to the price
	 * @throws SQLException
	 * @throws CouponSystemException
	 */
	public Set<Coupon> getAllPurchasedCouponByPrice(double price) throws SQLException, CouponSystemException {

		Set<Coupon> coupons = this.customerDBDAO.getCoupons(customer);
		Set<Coupon> couponsByPrice = new HashSet<>();

		for (Coupon coupon : coupons) {
			if (coupon.getPrice() < price) {
				couponsByPrice.add(coupon);
			}
		}

		return couponsByPrice;
	}

	public void setCustomerIdByName(Customer customer) {
		customerDBDAO.getCustomerIdByName(customer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see d.Facade.CouponClientFacade#login(java.lang.String, java.lang.String,
	 * b.Enum.ClientType)
	 */
	@Override
	public CouponClientFacade login(String name, String password, ClientType clientType) throws CustomerNotFound {

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
