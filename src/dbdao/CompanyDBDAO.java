package dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import beans.Company;
import beans.Coupon;
import connection.ConnectionPool;
import core.exceptions.CompanyNotFound;
import core.exceptions.CouponSystemException;
import dao.CompanyDao;
import enumPackage.CouponType;

/**
 * @author D.Neumark
 */
public class CompanyDBDAO implements CompanyDao {

	private ConnectionPool pool;

	/**
	 * Class CTOR.
	 * 
	 * @throws CouponSystemException
	 */
	public CompanyDBDAO() throws CouponSystemException {
		pool = ConnectionPool.getInstance();
	}

	/**
	 * Login into a Company account.
	 * 
	 * @param compName
	 * @param compPassword
	 * @return a boolean value
	 * @throws CouponSystemException
	 */
	@Override
	public boolean login(String compName, String compPassword) throws CouponSystemException {

		String sql = "select * from companies where comp_name = ?";

		try (Connection con = this.pool.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setString(1, compName);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				if (compPassword.equals(rs.getString(3))) {
					return true;
				}
			} else {
				return false;
			}

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException("Login failed.", e);
			throw ex;

		}
		return false;
	}

	/**
	 * Sets a company data in the companies database table. The id will be
	 * automatically created by the database and this method will set it in the
	 * company object.
	 * 
	 * @param company
	 * @return a company object
	 * @throws CouponSystemException
	 */
	@Override
	public Company createCompany(Company company) throws CouponSystemException {
		Connection con = this.pool.getConnection();

		/**
		 * The "sql" object represents a SQL statement. The "executeUpdate()" executes
		 * the "sql" statement in the PreparedStatement object.
		 */
		String sql = "insert into companies(comp_name, password, email) values(?,?,?)";
		try (PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			pstmt.setString(1, company.getName());
			pstmt.setString(2, company.getPassword());
			pstmt.setString(3, company.getEmail());
			int row = pstmt.executeUpdate();

			/**
			 * If the company was successfully inserted in the database the
			 * "pstmt.executeUpdate ()" should receive a nonzero value. But if the company
			 * was not entered, the value to be returned will be zero.
			 */
			if (row != 0) {

				/**
				 * In order to retrieve from the database the auto-generated key of the newly
				 * created company, first we have to check if the company was created in the
				 * database and than we set the id of it.
				 */
				ResultSet gk = pstmt.getGeneratedKeys();
				if (gk.next()) {

					/**
					 * Since the company id was automatically generated in the database, it is
					 * necessary to retrieve the new id and insert it into the company object.
					 */
					company.setID(gk.getLong(1));
					return company;
				} else {
					throw new CouponSystemException("Can't set the id in the database.");
				}
			} else {
				throw new CouponSystemException("The company data was not inserted in the database.");
			}

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException("The company was not created in the database.", e);
			throw ex;
		} finally {
			this.pool.returnConnection(con);
		}

	}

	/**
	 * Deletes the company data from four database tables: customerCoupon, coupons,
	 * customerCoupon and companies.
	 * 
	 * @param company
	 * @throws CouponSystemException.
	 */
	@Override
	public void deleteCompany(Company company) throws CouponSystemException {

		Connection con = this.pool.getConnection();
		long compId = company.getID();

		String sql1 = "delete from customerCoupon where coupons_id in(select coupons_id from companyCoupon where comp_id = ?)";
		String sql2 = "delete from coupons where id in (select coupons_id from companyCoupon where id = ?)";
		String sql3 = "delete from companyCoupon where coupons_id in(select coupons_id from companyCoupon where comp_id = ?)";
		String sql4 = "delete from companies where id = ?";

		try (PreparedStatement pstmt1 = con.prepareStatement(sql1);

				PreparedStatement pstmt2 = con.prepareStatement(sql2);

				PreparedStatement pstmt3 = con.prepareStatement(sql3);

				PreparedStatement pstmt4 = con.prepareStatement(sql4);

		) {

			pstmt1.setLong(1, compId);
			pstmt1.executeUpdate();

			pstmt2.setLong(1, compId);
			pstmt2.executeUpdate();

			pstmt3.setLong(1, compId);
			pstmt3.executeUpdate();

			pstmt4.setLong(1, compId);
			pstmt4.executeUpdate();

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException(
					"Can't delete company: " + company.getName() + " from the database.", e);
			throw ex;
		} finally {
			this.pool.returnConnection(con);
		}

	}

	/**
	 * Update the company data (password, email) in the database.
	 * 
	 * @param company
	 * @throws CouponSystemException.
	 */
	@Override
	public void updateCompany(Company company) throws CouponSystemException {
		Connection con = this.pool.getConnection();
		String sql = "update companies set password = ?, email = ? where id = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setString(1, company.getPassword());
			pstmt.setString(2, company.getEmail());
			pstmt.setLong(3, company.getID());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException(company.getName() + " was not updated.", e);
			throw ex;
		} finally {
			this.pool.returnConnection(con);
		}

	}

	/**
	 * Gets the data of the company from the database using the id.
	 * 
	 * @param ID.
	 * @throws CouponSystemException.
	 */
	@Override
	public Company getCompany(long ID) throws CouponSystemException {
		Connection con = this.pool.getConnection();
		Company company = null;

		String sql = "select * from companies where id = ?";

		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setLong(1, ID);

			ResultSet rs = pstmt.executeQuery();

			rs.next();

			long id1 = rs.getLong(1);
			String name = rs.getString(2);
			String password = rs.getString(3);
			String email = rs.getString(4);

			company = new Company(id1, name, password, email);
			Set<Coupon> coupons = getCoupons(company);
			company.setCoupons(coupons);

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException("Can't get the company data.", e);
			throw ex;
		} finally {
			this.pool.returnConnection(con);
		}

		return company;
	}

	/**
	 * Gets the data of all the companies.
	 * 
	 * @return get company data
	 * @throws CouponSystemException
	 */
	@Override
	public Set<Company> getAllCompanies() throws CouponSystemException {
		Company company;
		Set<Company> companies = new HashSet<>();
		String sql = "select * from companies";

		Connection con = this.pool.getConnection();

		try (Statement statement = con.createStatement()) {

			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				long id = rs.getLong(1);
				String name = rs.getString(2);
				String password = rs.getString(3);
				String email = rs.getString(4);

				company = new Company(id, name, password, email);

				companies.add(company);

			}

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException("Can't get the list of all companies data.", e);
			throw ex;

		} finally {
			this.pool.returnConnection(con);
		}
		if (companies.isEmpty()) {
			CouponSystemException ex = new CouponSystemException(
					"There are no companies in the database table 'companies'");
			throw ex;
		}
		return companies;

	}

	/**
	 * Gets the data of all coupons from the database.
	 * 
	 * @param company
	 * @return list of all companies in the system
	 * @throws CouponSystemException
	 */
	@Override
	public Set<Coupon> getCoupons(Company company) throws CouponSystemException {

		Set<Coupon> coupons = new HashSet<>();

		String sql = "select * from coupons where id in(select coupons_id from companyCoupon where comp_id = ?)";

		Connection con = this.pool.getConnection();

		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setLong(1, company.getID());
			ResultSet rs = pstmt.executeQuery();

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
			CouponSystemException ex = new CouponSystemException("Can't get the list of coupons.", e);
			throw ex;

		} finally {
			this.pool.returnConnection(con);
		}
		return coupons;
	}

	/**
	 * This method gets the company data from the database without using the id.
	 * 
	 * @param comp_name
	 * @return a company object
	 * @throws CouponSystemException
	 */
	public Company getCompanyByName(String comp_name) throws CouponSystemException {
		ConnectionPool pool = ConnectionPool.getInstance();
		String sql = "select * from companies where comp_name = ?";

		Connection con = this.pool.getConnection();

		try (PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, comp_name);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				long id = rs.getLong(1);
				String name = rs.getString(2);
				String password = rs.getString(3);
				String email = rs.getString(4);

				return new Company(id, name, password, email);

			} else {
				throw new CompanyNotFound("Company " + comp_name + " not found.");
			}

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException("Can't get the company data by this name.", e);
			throw ex;
		} finally {
			pool.returnConnection(con);
		}
	}

	/**
	 * This method gets the id of the company using the name of the company.
	 * 
	 * @param company
	 * @throws CouponSystemException
	 */
	public void getCompanyIdByName(Company company) throws CouponSystemException {
		ConnectionPool pool = ConnectionPool.getInstance();
		String sql = "select id from companies where comp_name = ?";

		Connection con = this.pool.getConnection();

		try (PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, company.getName());

			ResultSet rs = pstmt.executeQuery();
			rs.next();
			long id = rs.getLong(1);
			company.setID(id);

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException("Can't get the id of the company.", e);
			throw ex;
		} finally {
			pool.returnConnection(con);
		}
	}

	/**
	 * Inserts a new coupon data into companyCoupon database table.
	 * 
	 * @param comp_id,
	 *            coupon_id
	 * @throws CouponSystemException.
	 */
	public boolean createCompanyCoupon(long comp_id, long coupon_id) throws CouponSystemException {

		Connection con = this.pool.getConnection();
		String sql = "insert into companyCoupon(comp_id, coupons_id) values(?, ?)";

		try (PreparedStatement pstmt1 = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

			pstmt1.setLong(1, comp_id);
			pstmt1.setLong(2, coupon_id);
			int rows = pstmt1.executeUpdate();
			if (rows != 0) {
				return true;
			} else {
				throw new CouponSystemException("The coupon was not be inserted in the 'companyCoupon' database table");
			}

		} catch (Exception e) {
			CouponSystemException ex = new CouponSystemException(
					"The coupon with ID: \" + coupon_id + \"was not inserted into the companyCoupon table.", e);
			throw ex;
		} finally {
			this.pool.returnConnection(con);
		}
	}

}
