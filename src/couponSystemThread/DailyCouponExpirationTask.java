package couponSystemThread;

import java.util.Date;
import java.util.Set;

import beans.Coupon;
import core.exceptions.CouponSystemException;
import dbdao.CouponDBDAO;

/**
 * @author David Neumark
 */
public class DailyCouponExpirationTask implements Runnable {

	private Boolean booleanQuit = false;
	private static CouponDBDAO couponDBDAO;

	public DailyCouponExpirationTask() {
		this.couponDBDAO = new CouponDBDAO();
	}

	public void stopTask() {
		booleanQuit = true;
	}

	@Override
	public void run() {
		while (!booleanQuit) {
			Set<Coupon> coupons;
			try {
				coupons = couponDBDAO.getAllCoupons();
				Date today = new Date();
				today.setTime(System.currentTimeMillis());

				for (Coupon coupon : coupons) {
					if (today.compareTo(coupon.getEndDate()) >= 0) {
						couponDBDAO.deleteCoupon(coupon);
					}
				}
				try {
					Thread.sleep(86_400_000);
				} catch (InterruptedException e1) {
					booleanQuit = false;
				}
			} catch (CouponSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}