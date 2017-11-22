package dbdao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import beans.Company;
import beans.Coupon;
import beans.Customer;
import connection.ConnectionPool;
import core.exceptions.CouponSystemException;
import dao.CouponDAO;
import enumPackage.CouponType;

/**
 * @author D.Neumark
 *
 */
/**
 * @author D.Neumark
 *
 */
public class CouponDBDAO implements CouponDAO {

	private ConnectionPool pool;

	/**
	 * Class CTOR
	 * 
	 * @throws CouponSystemException
	 */
	public CouponDBDAO() throws CouponSystemException {
		pool = ConnectionPool.getInstance();
	}

	/**
	 * Sets a new coupon data into "coupons" database table. The id will be
	 * generated automatically in the database.
	 * 
	 * @param coupon
	 * @return
	 * @throws CouponSystemException
	 */
	@Override
	public Coupon createCoupon(Coupon coupon) throws CouponSystemException {
		Connection con = this.pool.getConnection();
		String sql = "insert into coupons(title, start_date, end_date, amount, type, message, price, image) values(?,?,?,?,?,?,?,?)";

		try (PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			pstmt.setString(1, coupon.getTitle());
			pstmt.setDate(2, (Date) coupon.getStartDate());
			pstmt.setDate(3, (Date) coupon.getEndDate());
			pstmt.setInt(4, coupon.getAmount());
			pstmt.setString(5, CouponType.convertToString(coupon.getType()));
			pstmt.setString(6, coupon.getMessage());
			pstmt.setDouble(7, coupon.getPrice());
			pstmt.setString(8, coupon.getImage());

			int rows = pstmt.executeUpdate();

			// If the coupon was not inserted in the database, the "executeUpdate()" should
			// return 0
			if (rows != 0) {
				// getting the new coupon id from the database
				ResultSet gk = pstmt.getGeneratedKeys();
				if (gk.next()) {
					// setting the id into the coupon object.
					coupon.setID(gk.getLong(1));
					return coupon;
				} else {
					throw new CouponSystemException("Issue with the coupon id creation");
				}
			} else {
				throw new CouponSystemException("The coupon was not created in the database");
			}

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException(
					"The coupon: " + coupon.getTitle() + " was not created.", e);
			throw ex;
		} finally {
			this.pool.returnConnection(con);
		}
	}

	/**
	 * Deletes the coupon data in three database tables: companyCoupon,
	 * customerCoupon, coupons.
	 * 
	 * @param coupon
	 * @throws CouponSystemException.
	 */
	@Override
	public void deleteCoupon(Coupon coupon) throws CouponSystemException {
		Connection con = this.pool.getConnection();

		String sql1 = "delete from companyCoupon where coupons_id = ?";
		String sql2 = "delete from customerCoupon where coupons_id = ?";
		String sql3 = "delete from coupons where id = ?";

		try (

				PreparedStatement pstmt1 = con.prepareStatement(sql1);

				PreparedStatement pstmt2 = con.prepareStatement(sql2);

				PreparedStatement pstmt3 = con.prepareStatement(sql3);

		) {

			long couponID = coupon.getID();
			pstmt1.setLong(1, couponID);
			pstmt1.executeUpdate();

			pstmt2.setLong(1, couponID);
			pstmt2.executeUpdate();

			pstmt3.setLong(1, couponID);
			pstmt3.executeUpdate();

			System.out.println(coupon.getTitle() + " was removed from the system");

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException("Can't delete the coupon from the database.", e);
			throw ex;
		} finally {
			this.pool.returnConnection(con);
		}

	}

	/**
	 * Updates the coupon data(startDate, endDate and price) in the database.
	 * 
	 * @param coupon.
	 * @throws CouponSystemException.
	 */
	@Override
	public void updateCoupon(Coupon coupon) throws CouponSystemException {

		Connection con = this.pool.getConnection();
		String sql = "update coupons set start_date = ?, end_date = ?, price = ? where id = ?";

		try (PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setDate(1, (Date) coupon.getStartDate());
			pstmt.setDate(2, (Date) coupon.getEndDate());
			pstmt.setDouble(3, coupon.getPrice());
			pstmt.setLong(4, coupon.getID());
			int rows = pstmt.executeUpdate();
			if (rows == 0) {
				throw new CouponSystemException("Update failed");
			}

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException("Can't update the coupon data", e);
			throw ex;
		} finally {
			this.pool.returnConnection(con);
		}

	}

	/**
	 * Gets the coupon data from the database by id.
	 * 
	 * @param ID
	 * @return
	 * @throws CouponSystemException
	 */
	@Override
	public Coupon getCoupon(long ID) throws CouponSystemException {

		Connection con = this.pool.getConnection();
		Coupon coupon = null;

		String sql = "select * from coupons where id = ?";

		try (

				PreparedStatement pstmt = con.prepareStatement(sql);

				Statement stmt = con.createStatement();

		) {

			pstmt.setLong(1, ID);
			ResultSet rs = pstmt.executeQuery();

			rs.next();

			long id1 = rs.getLong(1);
			String title = rs.getString(2);
			Date startDate = rs.getDate(3);
			Date endDate = rs.getDate(4);
			int amount = rs.getInt(5);
			CouponType type = CouponType.valueOf(rs.getString(6));
			String message = rs.getString(7);
			double price = rs.getDouble(8);
			String image = rs.getString(9);

			coupon = new Coupon(id1, title, startDate, endDate, amount, type, message, price, image);

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException("Can't get the data of the coupon", e);
			throw ex;
		} finally {
			this.pool.returnConnection(con);
		}
		return coupon;
	}

	/**
	 * Gets all coupons of the system from the database.
	 * 
	 * @return list of coupons
	 * @throws CouponSystemException
	 */
	@Override
	public Set<Coupon> getAllCoupons() throws CouponSystemException {
		Coupon coupon;
		Set<Coupon> coupons = new HashSet<>();
		String sql = "select * from coupons";

		Connection currentConnection = this.pool.getConnection();

		try (Statement statement = currentConnection.createStatement()) {

			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {

				long id1 = rs.getLong(1);
				String title = rs.getString(2);
				Date startDate = rs.getDate(3);
				Date endDate = rs.getDate(4);
				int amount = rs.getInt(5);
				CouponType type = CouponType.valueOf(rs.getString(6));
				String message = rs.getString(7);
				double price = rs.getDouble(8);
				String image = rs.getString(9);

				coupon = new Coupon(id1, title, startDate, endDate, amount, type, message, price, image);
				coupons.add(coupon);

			}

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException(
					"Couldn't connect the driver. The list of all coupons was not created.", e);
			throw ex;
		} finally {
			this.pool.returnConnection(currentConnection);
		}
		if (coupons.isEmpty()) {
			System.out.println("The coupon set is empty");
		}
		return coupons;

	}

	/**
	 * Gets the data for all coupons from a particular company.
	 * 
	 * @param company
	 * @return
	 * @throws CouponSystemException
	 */
	public Set<Coupon> getAllCoupons(Company company) throws CouponSystemException {
		Coupon coupon;
		Set<Coupon> coupons = new HashSet<>();
		String sql = "select * from coupons where id in(select coupons_id from companyCoupon where comp_id = ?)";

		Connection con = this.pool.getConnection();

		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setLong(1, company.getID());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				long id = rs.getLong(1);
				String title = rs.getString(2);
				Date startDate = rs.getDate(3);
				Date endDate = rs.getDate(4);
				int amount = rs.getInt(5);
				CouponType type = CouponType.valueOf(rs.getString(6));
				String message = rs.getString(7);
				double price = rs.getDouble(8);
				String image = rs.getString(9);

				coupon = new Coupon(id, title, startDate, endDate, amount, type, message, price, image);
				coupons.add(coupon);

			}
			return coupons;

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException(
					"Can't get the list of all coupons of the company: " + company.getName() + " .", e);
			throw ex;

		} finally {
			this.pool.returnConnection(con);
		}

	}

	/**
	 * Gets the data for all coupons from a particular customer.
	 * 
	 * @param customer
	 * @return list of coupons
	 * @throws CouponSystemException
	 */
	public Set<Coupon> getAllCoupons(Customer customer) throws CouponSystemException {
		Coupon coupon;
		Set<Coupon> coupons = new HashSet<>();
		String sql1 = "select * from coupons where id in (select coupons_id from companyCoupon where comp_id in (select id from companies where comp_name = ?))";

		Connection con = this.pool.getConnection();

		try (PreparedStatement pstmt1 = con.prepareStatement(sql1);) {

			pstmt1.setString(1, customer.getCustName());
			ResultSet rs = pstmt1.executeQuery(sql1);

			while (rs.next()) {

				long id = rs.getLong(1);
				String title = rs.getString(2);
				Date startDate = rs.getDate(3);
				Date endDate = rs.getDate(4);
				int amount = rs.getInt(5);
				CouponType type = CouponType.valueOf(rs.getString(6));
				String message = rs.getString(7);
				double price = rs.getDouble(8);
				String image = rs.getString(9);

				coupon = new Coupon(id, title, startDate, endDate, amount, type, message, price, image);
				coupons.add(coupon);

			}
			return coupons;

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException(
					"Couldn't connect the driver. Can't get all coupons list of this customer.", e);
			throw ex;

		} finally {
			this.pool.returnConnection(con);
		}

	}

	/**
	 * Gets the data for all coupons of a given type.
	 * 
	 * @param couponType
	 * @return
	 * @throws CouponSystemException
	 * @throws SQLException
	 */
	@Override
	public Set<Coupon> getCouponByType(CouponType couponType) throws CouponSystemException, SQLException {

		Connection con = this.pool.getConnection();
		Set<Coupon> coupons = new HashSet<>();
		Coupon coupon = null;

		String sql = "select * from coupons where type = ? ";

		try (

				PreparedStatement pstmt = con.prepareStatement(sql);

				Statement stmt = con.createStatement();

		) {

			pstmt.setString(1, CouponType.convertToString(couponType));
			pstmt.executeUpdate();

			Connection currentConnection = this.pool.getConnection();

			try (Statement stmnt = currentConnection.createStatement()) {

				ResultSet rs = stmnt.executeQuery(sql);

				while (rs.next()) {
					long id1 = rs.getLong(1);
					String title = rs.getString(2);
					Date startDate = rs.getDate(3);
					Date endDate = rs.getDate(4);
					int amount = rs.getInt(5);
					CouponType type1 = CouponType.valueOf(rs.getString(6));
					String message = rs.getString(7);
					double price = rs.getDouble(8);
					String image = rs.getString(9);

					coupon = new Coupon(id1, title, startDate, endDate, amount, type1, message, price, image);
					coupons.add(coupon);
				}
				System.out.println("Coupon was readed successfully by type:" + couponType);
			} catch (SQLException e) {
				CouponSystemException ex = new CouponSystemException(
						"Couldn't connect the driver. Can't get all coupons list of this type.", e);
				throw ex;
			} finally {
				this.pool.returnConnection(currentConnection);
			}
			return coupons;
		}
	}

	/**
	 * Gets the coupon id using the coupon title.
	 * 
	 * @param coupon
	 * @throws CouponSystemException
	 */
	public void getCouponIdByTitle(Coupon coupon) throws CouponSystemException {
		ConnectionPool pool = ConnectionPool.getInstance();
		String sql = "select id from coupons where title = ?";

		Connection con = this.pool.getConnection();

		try (PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, coupon.getTitle());

			ResultSet rs = pstmt.executeQuery();
			rs.next();

			long id = rs.getLong(1);
			coupon.setID(id);

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException("Can't set the id of this coupon", e);
			throw ex;
		} finally {
			pool.returnConnection(con);
		}
	}

	/**
	 * Gets the coupon id using the coupon title.
	 * 
	 * @param _title
	 * @return
	 * @throws CouponSystemException
	 */
	public Coupon getCouponByTitle(String _title) throws CouponSystemException {

		ConnectionPool pool = ConnectionPool.getInstance();
		String sql = "select * from coupons where title = ?";

		Connection con = this.pool.getConnection();

		try (PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, _title);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				long id = rs.getLong(1);
				String title = rs.getString(2);
				Date startDate = rs.getDate(3);
				Date endDate = rs.getDate(4);
				int amount = rs.getInt(5);
				CouponType type = CouponType.valueOf(rs.getString(6));
				String message = rs.getString(7);
				double price = rs.getDouble(8);
				String image = rs.getString(9);

				return new Coupon(id, title, startDate, endDate, amount, type, message, price, image);

			} else {
				return null;
			}

		} catch (SQLException e) {
			CouponSystemException ex = new CouponSystemException("Can't get the coupon data by title.", e);
			throw ex;
		} finally {
			pool.returnConnection(con);
		}
	}

}