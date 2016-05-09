/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.DB;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;


// optimize database access
public class ConnectionPooling {
	private String driverName;
	private String password;
	private String url;
	private String user;
	private Driver driver;
	private Vector freeConnections;
	private int maxConn;
	private int count;

	public ConnectionPooling(String drivername, String conUrl,
			String conuser, String conpassword) throws SQLException {
		freeConnections = new Vector();
		driverName = drivername;
		url = conUrl;
		user = conuser;
		password = conpassword;
		try {
			driver = (Driver) Class.forName(driverName).newInstance();
			DriverManager.registerDriver(driver);
		} catch (Exception _ex) {
			new SQLException();
		}
		count = 0;
		maxConn = 5;
	}

	public void destroy() {
		closeAll();
		try {
			DriverManager.deregisterDriver(driver);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public synchronized void freeConnection(Connection connection) {
		freeConnections.addElement(connection);
		count--;
		notifyAll();
	}

	public synchronized Connection getConnection() {
		Connection connection = null;
		if (freeConnections.size() > 0) {
			connection = (Connection) freeConnections.elementAt(0);
			freeConnections.removeElementAt(0);
			try {
				if (connection.isClosed()) {
					connection = getConnection();
				}
			} catch (Exception e) {
				print(e.getMessage());
				connection = getConnection();
			}
			return connection;
		}
		if (count < maxConn) {
			connection = newConnection();
			print("NEW CONNECTION CREATED");
		}
		if (connection != null) {
			count++;
		}
		return connection;
	}

	private synchronized void closeAll() {
		for (Enumeration enumeration = freeConnections.elements(); enumeration
				.hasMoreElements();) {
			Connection connection = (Connection) enumeration.nextElement();
			try {
				connection.close();
			} catch (Exception e) {
				print(e.getMessage());
			}
		}
		freeConnections.removeAllElements();
	}

	private Connection newConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			print(e.getMessage());
			return null;
		}
		return connection;
	}
	private void print(String print) {
		System.out.println(print);
	}
}