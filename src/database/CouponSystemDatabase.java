package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author David Neumark
 */
public class CouponSystemDatabase {

	public static void main(String[] args) {

		String driverName = "org.apache.derby.jdbc.ClientDriver";
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// URL to the database
		String url = "jdbc:derby://localhost:1527/db2; create=true";
		String sql = null;

		// connect
		try (Connection con = DriverManager.getConnection(url);) {

			System.out.println("connected to " + url);

			// create table COMPANIES
			Statement stmt = con.createStatement();
			sql = "create table companies(id bigint not null primary key generated always as identity(start with 1, increment by 1),";
			sql += " comp_name varchar(20), password varchar(20), email varchar(40))";
			stmt.executeUpdate(sql); // send the request to the DBMS

			// create table CUSTOMERS
			sql = "create table customers(id bigint not null primary key generated always as identity(start with 1, increment by 1),";
			sql += " cust_name varchar(20), email varchar(40), password varchar(20))";
			stmt.executeUpdate(sql);

			// create table COUPONS
			sql = "create table coupons(id bigint not null primary key generated always as identity(start with 1, increment by 1),";
			sql += " title varchar(120), start_date date, end_date date, amount integer, type varchar(40), ";
			sql += "message varchar(200), price float, image varchar(80))";
			stmt.executeUpdate(sql);

			// create JOIN table COUPONS_ID - COMP_ID
			sql = "create table companyCoupon(comp_id bigint, coupons_id bigint, ";
			sql += "primary key (comp_id, coupons_id))";
			stmt.executeUpdate(sql);

			// create JOIN table COUPONS_ID - CUST_ID
			sql = "create table customerCoupon(cust_id bigint, coupons_id bigint, ";
			sql += "primary key (cust_id, coupons_id))";
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			System.out.println(sql);
			e.printStackTrace();
		}

		System.out.println("disconnected from " + url);

	}

}
