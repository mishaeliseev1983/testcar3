package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

/*
 * For db's queries
 */
public class ConnectionDB {
	public static String dburl;
	public static String user;
	public static String pwd;
	private static Properties connectionProps;
	private static BasicDataSource dataSource;

	public static Connection getConnection() {

		if (dataSource == null) {

			String driver = connectionProps.getProperty("jdbc.driver");
			if (driver != null) {
				try {
					Class.forName(driver);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

			BasicDataSource ds = new BasicDataSource();
			ds.setUrl(connectionProps.getProperty("jdbc.url"));
			ds.setUsername(connectionProps.getProperty("jdbc.username"));
			ds.setPassword(connectionProps.getProperty("jdbc.password"));
			ds.setMinIdle(10);
			ds.setMaxIdle(20);
			ds.setMaxOpenPreparedStatements(100);
			dataSource = ds;
		}

		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static String getSearchSql() {
		String sql = "select o.id, o.name as oname, o.surname as osurname, o.patronymic as opatronymic,  "
				+ "ci.name as ciname, ca.model as camodel , ca.number as canumber  "
				+ "  from owner o, city ci, car ca  "
				+ "where upper(ci.name) like upper(?) and ci.id=o.id_city and "
				+ "o.id=ca.id_owner and upper(ca.model) like upper(?)"
				+ " and upper(o.name) like upper(?) "
				+ " and upper(o.surname) like upper(?) "
				+ " and upper(o.patronymic) like upper(?)";
		return sql;
	}

	public static String getLoginSql(String login, String password) {
		String sql = "select id from users where username = ? and password = ?";
		return sql;
	}

	public static String getQueryValue(String val) {

		val = val.trim();
		String all = "%%";
		if (val == null || val.isEmpty())
			return all;
		return "%" + val + "%";
	}

	public static void setConectionProps(Properties readProps) {
		connectionProps = readProps;
	}

}
