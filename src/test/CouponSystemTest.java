package test;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import beans.Company;
import beans.Coupon;
import beans.Customer;
import core.exceptions.CouponSystemException;
import couponSystemSingleton.CouponSystem;
import enumPackage.ClientType;
import enumPackage.CouponType;
import facade.AdminFacade;
import facade.CompanyFacade;
import facade.CustomerFacade;

public class CouponSystemTest {

	public static void main(String[] args) throws SQLException, CouponSystemException {

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

		Coupon coupon15 = new Coupon("coupon15", sqlStartDate1, sqlEndDate1, 45, CouponType.HEALTH, "message", 199.9,
				"image");
		Coupon coupon16 = new Coupon(2, "coupon16", sqlStartDate1, sqlEndDate1, 3, CouponType.HEALTH, "message", 3,
				"image");

		cal.set(2015, 4, 0);
		utilStartDate2 = cal.getTime();
		java.sql.Date sDate2 = convertUtilToSql(utilStartDate2);

		cal.set(2018, 9, 29);
		utilEndDate2 = cal.getTime();
		// java.sql.Date eDate2 = convertUtilToSql(utilEndDate2);

		// Coupon coupon14 = new Coupon("coupon14", sDate2, eDate2, 19,
		// CouponType.TRAVELLING, "TRAVELLING.message", 122,
		// "sports.image");

		Set<Coupon> coupons1 = new HashSet<>();

		Customer customer16 = new Customer("Customer16", "customer16@gmail.com", "customer16123", coupons1);
		Customer customer17 = new Customer("Customer17", "customer17@gmail.com", "customer17123", coupons1);

		// Company company23 = new Company("Company23", "1234", "company23@mail.com");
		Company company24 = new Company(1, "Company24", "company24123", "company24@mail.com");
		Company company25 = new Company(2, "Company25", "company25123", "company25@mail.com");
		try {
			AdminFacade adminFacade = (AdminFacade) CouponSystem.getInstance().login("admin", "1234", ClientType.ADMIN);

			adminFacade.createCompany(company24);
			adminFacade.createCompany(company25);
			try {
				adminFacade.createCompany(company25);
			} catch (CouponSystemException e) {
				System.out.println(e.getMessage());
			}
			company24 = adminFacade.getCompanyByName("Company24");
			company24.setEmail("company24@NEW_mail.com");
			adminFacade.updateCompany(company24);
			System.out.println(adminFacade.getCompany(company24.getID()));
			System.out.println(adminFacade.getAllCompanies());

			adminFacade.createCustomer(customer17);
			adminFacade.createCustomer(customer16);
			try {
				adminFacade.createCustomer(customer16);
			} catch (CouponSystemException e) {
				System.out.println(e.getMessage());
			}
			customer17 = adminFacade.getCustomerByName("Customer17");
			customer17.setCustEmail("customer17@NEW_gmail.com");
			adminFacade.updateCustomer(customer17);
			System.out.println(adminFacade.getCustomer(customer16.getID()));
			System.out.println(adminFacade.getAllCustomers());

			System.out.println("=============Delete=======================");
			adminFacade.removeCustomer(customer16);
			adminFacade.deleteCompany(company25);
			System.out.println("OK");
			CompanyFacade companyFacade = (CompanyFacade) CouponSystem.getInstance().login(company24.getName(),
					company24.getPassword(), ClientType.COMPANY);

			System.out.println("=========companyFacade LOGIN test===============");

			companyFacade.createCoupon(coupon16);
			companyFacade.createCoupon(coupon15);
			try {
				companyFacade.createCoupon(coupon15);
			} catch (CouponSystemException e) {
				System.out.println(e.getMessage());
			}
			coupon16 = companyFacade.getCouponByTitle(coupon16.getTitle());
			coupon16.setTitle("NEW_coupon16");
			companyFacade.updateCoupon(coupon16);
			System.out.println(companyFacade.getCoupon(coupon16.getID()));
			System.out.println(companyFacade.getAllCoupons());
			System.out.println(companyFacade.getCouponByType(CouponType.HEALTH));
			System.out.println(companyFacade.getCouponByPrice(131));
			System.out.println(companyFacade.getCouponByDate(sDate2));
			System.out.println(companyFacade.getCouponByTitle(coupon16.getTitle()));
			companyFacade.removeCoupon(coupon16);

			CustomerFacade customerFacade = (CustomerFacade) CouponSystem.getInstance().login(customer16.getCustName(),
					customer16.getPassword(), ClientType.CUSTOMER);

			coupon16 = customerFacade.getCouponByTitle("coupon16");
			customerFacade.purchaseCoupon(coupon16);
			System.out.println(customerFacade.getAllPurchasedCoupons());
			System.out.println(customerFacade.getAllPurchasedCouponByType(CouponType.TRAVELLING));
			System.out.println(customerFacade.getAllPurchasedCouponPrice(coupon16.getPrice()));

			CouponSystem.getInstance().shutdown();

		} catch (CouponSystemException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	private static java.sql.Date convertUtilToSql(java.util.Date uDate) {
		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		return sDate;
	}

}
