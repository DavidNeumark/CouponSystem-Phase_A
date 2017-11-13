package dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import beans.Coupon;
import beans.Customer;
import connection.ConnectionPool;
import core.exceptions.CouponSystemException;
import core.exceptions.CustomerNotFound;
import dao.CustomerDAO;
import enumPackage.CouponType;

public class CustomerDBDAO implements CustomerDAO {

	private ConnectionPool pool;

	/**
	 * Class CTOR
	 */
	public CustomerDBDAO() {
		pool = ConnectionPool.getInstance();
	}

	/**
	 * Login into the customer account.
	 * 
	 * @param customerName
	 * @param password
	 * @return
	 * @throws CouponSystemException
	 */
	@Override
	public Boolean login(String custName, String custPassword) {

		String sql = "select password from customers where cust_name = ?";

		try (

				Connection con = this.pool.getConnection();

				PreparedStatement pstmt = con.prepareStatement(sql);

		) {

			pstmt.setString(1, custName);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				if (custPassword.equals(rs.getString(1))) {
					return true;
				}
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Inserts the data of a new customer in the database.
	 * 
	 * @param customer
	 * @return a customer object
	 * @throws CouponSystemException
	 */
	@Override
	public Customer createCustomer(Customer customer) throws CouponSystemException {
		Connection con = this.pool.getConnection();
		String sql = "insert into customers(cust_name, email, password) values(?, ?, ?)";

		try (PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

			pstmt.setString(1, customer.getCustName());
			pstmt.setString(2, customer.getCustEmail());
			pstmt.setString(3, customer.getPassword());
			int row = pstmt.executeUpdate();
			if (row != 0) {
				ResultSet gk = pstmt.getGeneratedKeys();
				if (gk.next()) {
					customer.setID(gk.getLong(1));
					return customer;
				} else {
					throw new CouponSystemException("Key wasn't Gen in Customer table.");
				}

			} else {
				throw new CouponSystemException("Customer wasn't created.");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw new CouponSystemException("can't create the customer", e);
		} finally {
			this.pool.returnConnection(con);
		}
	}

	/**
	 * Sets a new coupon into customerCoupon table.
	 * 
	 * @param Coupon
	 * @exception SQLException.
	 * @throws CouponSystemException.
	 */
	public boolean createCustomerCoupon(long cust_id, long coupon_id) throws SQLException, CouponSystemException {

		String sql = "insert into customerCoupon(cust_id, coupons_id) values(?, ?)";
		Connection con = this.pool.getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setLong(1, cust_id);
			pstmt.setLong(2, coupon_id);
			int row = pstmt.executeUpdate();

			if (row != 0) {
				return true;
			} else {
				throw new CouponSystemException("The coupon wasn't created in the CustomerCoupon table.");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new CouponSystemException("Coupon ID: " + coupon_id + " was not inserted.", e);

		} finally {
			this.pool.returnConnection(con);
		}
	}

	public synchronized void purchaseCustomerCoupon(long cust_id, long coupon_id) throws CouponSystemException {

		String sql = "insert into customerCoupon values(?, ?)";
		String sql2 = "update coupons set  amount = amount -1 where id = ?";

		Connection con = this.pool.getConnection();

		try (

				PreparedStatement pstmt = con.prepareStatement(sql);
				PreparedStatement pstmt2 = con.prepareStatement(sql2);

		) {
			pstmt.setLong(1, cust_id);
			pstmt.setLong(2, coupon_id);
			pstmt.executeUpdate();

			pstmt2.setLong(1, coupon_id);
			pstmt2.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.pool.returnConnection(con);
		}
	}

	public Boolean outOfStockCoupon(Coupon coupon) {

		Connection con = this.pool.getConnection();
		boolean checker = false;

		String sql = "select amount from coupons where id = ?";

		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setLong(1, coupon.getID());
			ResultSet rs = pstmt.executeQuery();
			rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return checker;

	}

	/**
	 * Delete the customer data from the database.
	 * 
	 * @param customer
	 * @throws CouponSystemException
	 */
	@Override
	public void deleteCustomer(Customer customer) throws CouponSystemException {

		Connection con = this.pool.getConnection();
		long id = customer.getID();

		String sql1 = "delete from customerCoupon where cust_id = ?";
		String sql2 = "delete from customers where id = ?";

		try (PreparedStatement pstmt1 = con.prepareStatement(sql1);

				PreparedStatement pstmt2 = con.prepareStatement(sql2);

		) {

			pstmt1.setLong(1, id);
			pstmt1.executeUpdate();

			pstmt2.setLong(1, id);
			pstmt2.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new CouponSystemException("can't delete Customer data from the database", e);
		} finally {
			this.pool.returnConnection(con);
		}

	}

	/**
	 * Update the new customer data in the database.
	 * 
	 * @param customer
	 * @throws CouponSystemException
	 */
	@Override
	public void updateCustomer(Customer customer) throws CouponSystemException {
		Connection con = this.pool.getConnection();
		String sql = "update customers set password = ?, email = ? where id = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setString(1, customer.getPassword());
			pstmt.setString(2, customer.getCustEmail());
			pstmt.setLong(3, customer.getID());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new CouponSystemException("updateCustomer failed", e);
		} finally {
			if (con != null)
				this.pool.returnConnection(con);
		}

	}

	/**
	 * Gets the customer data using the ID.
	 * 
	 * @param ID
	 * @return a customer object.
	 * @throws CouponSystemException
	 */
	@Override
	public Customer getCustomer(long ID) throws CouponSystemException {
		Connection con = this.pool.getConnection();
		Customer customer = new Customer();
		String sql = "select * from customers where id = ?";

		try (PreparedStatement pstmt = con.prepareStatement(sql); Statement stmt = con.createStatement();) {

			pstmt.setLong(1, ID);

			ResultSet rs = pstmt.executeQuery();
			rs.next();

			long id1 = rs.getLong(1);
			String custName = rs.getString(2);
			String custEmail = rs.getString(3);
			String password = rs.getString(4);
			customer = new Customer(id1, custName, custEmail, password);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new CouponSystemException("can't get the customer data - getCustomer failed", e);
		} finally {
			this.pool.returnConnection(con);
		}
		return customer;
	}

	/**
	 * @param cust_name
	 * @return a customer object containing even its ID.
	 * @throws CustomerNotFound
	 */
	public Customer getCustomerByName(String cust_name) throws CustomerNotFound {

		String sql = "select * from customers where cust_name = ?";

		Connection con = this.pool.getConnection();

		try (PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, cust_name);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				long id = rs.getLong(1);
				String name = rs.getString(2);
				String email = rs.getString(3);
				String password = rs.getString(4);

				return new Customer(id, name, password, email);

			} else {
				throw new CustomerNotFound("Customer " + cust_name + " not found in the database.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Can't get the customer data from the database.");
		} finally {
			this.pool.returnConnection(con);
		}
		return null;
	}

	/**
	 * Gets from the database the id of the customer according to the client's name.
	 * 
	 * @param customer
	 */
	public void getCustomerIdByName(Customer customer) {
		ConnectionPool pool = ConnectionPool.getInstance();
		String sql = "select id from customers where cust_name = ?";

		Connection con = this.pool.getConnection();

		try (PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, customer.getCustName());

			ResultSet rs = pstmt.executeQuery();
			rs.next();
			long id = rs.getLong(1);
			customer.setID(id);

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Can't get the id of this customer");
		} finally {
			pool.returnConnection(con);
		}
	}

	/**
	 * Gets the data of all customers in the system.
	 * 
	 * @return collection of customers.
	 * @throws CouponSystemException
	 */
	@Override
	public Set<Customer> getAllCustomers() throws CouponSystemException {
		Customer customer;
		Set<Customer> customers = new HashSet<>();
		String sql = "select * from customers";

		Connection con = this.pool.getConnection();

		try (Statement statement = con.createStatement()) {

			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				long id = rs.getLong(1);
				String name = rs.getString(2);
				String email = rs.getString(3);
				String password = rs.getString(4);

				customer = new Customer(id, name, email, password);
				customers.add(customer);

			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("Can't load the list of coupons.");
		} finally {
			this.pool.returnConnection(con);
		}
		if (customers.isEmpty()) {
			System.out.println("Company table in the database is empty.");
		}
		return customers;

	}

	/**
	 * Gets a collection of coupons from a particular customer
	 * 
	 * @param customer
	 * @return collection of coupons
	 * @throws SQLException
	 * @throws CouponSystemException
	 */
	@Override
	public Set<Coupon> getCoupons(Customer customer) throws SQLException, CouponSystemException {

		Set<Coupon> coupons = new HashSet<>();

		String sql1 = "select * from coupons where id in(select coupons_id from customerCoupon where cust_id = ?)";

		Connection con = this.pool.getConnection();

		try (

				PreparedStatement pstmt1 = con.prepareStatement(sql1);

		) {

			pstmt1.setLong(1, customer.getID());
			ResultSet rs = pstmt1.executeQuery();

			while (rs.next()) {
				Coupon c = new Coupon();
				c.setID(rs.getLong(1));
				c.setTitle(rs.getString(2));
				c.setStartDate(rs.getDate(3));
				c.setEndDate(rs.getDate(4));
				c.setAmount(rs.getInt(5));
				c.setType(CouponType.convertToCouponType(rs.getString(6)));
				c.setMessage(rs.getString(7));
				c.setPrice(rs.getDouble(8));
				c.setImage(rs.getString(9));

				coupons.add(c);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new CouponSystemException("can't get the coupons of this company", e);
		} finally {
			this.pool.returnConnection(con);
		}
		return coupons;
	}

}
