package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlTest {

	public static void main(String[] args) {

		String driverName = "com.mysql.jdbc.Driver";
		System.out.println("Loading driver...");
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Driver loaded!");

		String dbName = "sql11207844";
		String userName = "sql11207844";
		String password = "xKQsauXEBI";
		String hostname = "sql11.freemysqlhosting.net";
		String port = "3306";
		String url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password="
				+ password;

		try {
			System.out.println("trying to connect...");
			Connection con = DriverManager.getConnection(url);
			System.out.println("connected to the database!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
