package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import core.exceptions.CouponSystemException;

/**
 * @author D.Neumark
 *
 */
public class ConnectionPool {

	private static ConnectionPool instance = null;
	public static final int MAX_CON = 10;

	private Set<Connection> connections = new HashSet<>();
	private Set<Connection> connectionsB = new HashSet<>();
	// private String url = "jdbc:mysql://localhost:3306/db1";
	private String url = "jdbc:derby://localhost:1527/db2";

	private ConnectionPool() throws CouponSystemException {
		String driverName = "org.apache.derby.jdbc.ClientDriver";
		// String driverName = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e1) {
			CouponSystemException ex = new CouponSystemException("connection failed", e1);
			throw ex;
		}

		for (int i = 0; i < MAX_CON; i++) {
			try {
				Connection con = DriverManager.getConnection(url);
				connections.add(con);
				connectionsB.add(con);
			} catch (SQLException e) {
				CouponSystemException ex = new CouponSystemException("Can't connect to the driver.", e);
				throw ex;
			}
		}
	}

	public synchronized static ConnectionPool getInstance() throws CouponSystemException {
		if (instance == null) {
			instance = new ConnectionPool();
		}
		return instance;
	}

	public synchronized Connection getConnection() throws CouponSystemException {
		while (connections.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				CouponSystemException ex = new CouponSystemException("The connection is busy.", e);
				throw ex;
			}
		}

		Iterator<Connection> it = connections.iterator();
		Connection con = it.next();
		it.remove();
		return con;
	}

	public synchronized void returnConnection(Connection con) {
		connections.add(con);
		notify();

	}

	public void closeAllConnections() throws CouponSystemException {
		for (Connection connection : connectionsB) {
			try {
				connection.close();
			} catch (SQLException e) {
				CouponSystemException ex = new CouponSystemException("Can't close the connection.", e);
				throw ex;
			}
		}
	}

}
