package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ConnectionPool {

	private static ConnectionPool instance = null;
	public static final int MAX_CON = 10;

	private Set<Connection> connections = new HashSet<>();
	private Set<Connection> connectionsB = new HashSet<>();
	private String url = "jdbc:derby://localhost:1527/db2";

	private ConnectionPool() {
		for (int i = 0; i < MAX_CON; i++) {
			try {
				Connection con = DriverManager.getConnection(url);
				connections.add(con);
				connectionsB.add(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized static ConnectionPool getInstance() {
		if (instance == null) {
			instance = new ConnectionPool();
		}
		return instance;
	}

	public synchronized Connection getConnection() {
		while (connections.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
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

	public void closeAllConnections() {
		for (Connection connection : connectionsB) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
