package test;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import beans.Company;
import beans.Coupon;
import beans.Customer;
import core.exceptions.CompanyNotFound;
import core.exceptions.CouponNotFound;
import core.exceptions.CouponSystemException;
import core.exceptions.CreateCompanyExcetion;
import core.exceptions.CustomerNotFound;
import core.exceptions.EndDateException;
import core.exceptions.MoreThanOneCouponException;
import core.exceptions.PurchasedException;
import couponSystemSingleton.CouponSystem;
import dbdao.CouponDBDAO;
import enumPackage.ClientType;
import enumPackage.CouponType;
import facade.AdminFacade;
import facade.CompanyFacade;
import facade.CustomerFacade;

public class CouponSystemTest {

	public static void main(String[] args)
			throws CreateCompanyExcetion, CouponSystemException, SQLException, ParseException, CouponNotFound,
			CompanyNotFound, CustomerNotFound, EndDateException, MoreThanOneCouponException, PurchasedException {

		Date utilStartDate1;
		Date utilStartDate2;
		Date utilEndDate1;
		Date utilEndDate2;
		Calendar cal = Calendar.getInstance();

		cal.set(2012, 3, 0);
		utilStartDate1 = cal.getTime();
		java.sql.Date sqlStartDate1 = convertUtilToSql(utilStartDate1);

		cal.set(2018, 7, 15);
		utilEndDate1 = cal.getTime();
		java.sql.Date sqlEndDate1 = convertUtilToSql(utilEndDate1);

		Coupon coupon13 = new Coupon("coupon13Title", sqlStartDate1, sqlEndDate1, 45, CouponType.HEALTH, "message",
				199.9, "image");

		cal.set(2015, 4, 0);
		utilStartDate2 = cal.getTime();
		java.sql.Date sDate2 = convertUtilToSql(utilStartDate2);

		cal.set(2018, 9, 29);
		utilEndDate2 = cal.getTime();
		java.sql.Date eDate2 = convertUtilToSql(utilEndDate2);

		Coupon coupon14 = new Coupon("coupon14Title", sDate2, eDate2, 19, CouponType.TRAVELLING, "TRAVELLING.message",
				122, "sports.image");

		Set<Coupon> coupons1 = new HashSet<>();

		Customer customer15 = new Customer(45, "Customer15", "customer15@gmail.com", "customer15123", coupons1);
		Customer customer16 = new Customer("Customer16", "customer16@gmail.com", "customer16123", coupons1);

		Company company22 = new Company("Company22", "company22123", "company22@mail.com");
		Company company23 = new Company("Company23", "1234", "company23@mail.com");
		Company company24 = new Company("Company24", "company24123", "company24@mail.com");

		AdminFacade adminFacade = (AdminFacade) CouponSystem.getInstance().login("admin", "1234", ClientType.ADMIN);

		adminFacade.createCompany(company23);
		company24 = adminFacade.getCompanyByName("Company21");
		company24.setEmail("AAAAAA");
		adminFacade.updateCompany(company24);
		System.out.println(adminFacade.getCompany(21));
		adminFacade.createCustomer(customer15);
		customer16 = adminFacade.getCustomerByName("CustomerA");
		customer16.setCustEmail("newUpdated");
		adminFacade.updateCustomer(customer16);
		System.out.println(adminFacade.getCustomer(35));
		System.out.println(adminFacade.getAllCustomers());
		adminFacade.removeCustomer(customer15);
		adminFacade.deleteCompany(company22);

		CompanyFacade companyFacade = (CompanyFacade) CouponSystem.getInstance().login(company23.getName(),
				company23.getPassword(), ClientType.COMPANY);

		CouponDBDAO couponDBDAO = new CouponDBDAO();
		Coupon coupon2 = couponDBDAO.getCouponByTitle("coupon2");
		coupon2.setTitle("coupon2NewTitle");
		companyFacade.createCoupon(coupon13);
		companyFacade.createCoupon(coupon14);
		companyFacade.updateCoupon(coupon14);
		System.out.println(companyFacade.getCoupon(2));
		System.out.println(companyFacade.getAllCoupons());
		System.out.println(companyFacade.getCouponByType(CouponType.CAMPING));
		System.out.println(companyFacade.getCouponByPrice(11));
		System.out.println(companyFacade.getCouponByDate(sDate2));
		companyFacade.getCouponByTitle(coupon14.getTitle());
		companyFacade.removeCoupon(coupon14);

		CustomerFacade customerFacade = (CustomerFacade) CouponSystem.getInstance().login("Customer15", "customer15123",
				ClientType.CUSTOMER);
		customerFacade.purchaseCoupon(coupon13);
		System.out.println(customerFacade.getAllPurchasedCouponByType(CouponType.HEALTH));
		System.out.println(customerFacade.getAllPurchasedCouponByPrice(coupon13.getPrice()));

		CouponSystem couponSystem = null;
		couponSystem.shutdown();
	}

	private static java.sql.Date convertUtilToSql(java.util.Date uDate) {
		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		return sDate;
	}

}
