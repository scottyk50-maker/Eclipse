package com.dstsystems.bps.convdb.database;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dstsystems.bps.convdb.exceptions.ConvDBException;

public class ConvDB {
	static Logger logger = Logger.getLogger(ConvDB.class.getName());

	public void Connect(final String jdbcDriver, final String url,
			final String userId, final String pwd) throws ConvDBException {

		String msg = "";

		if (jdbcDriver.length() < 1 || url.length() < 1 || userId.length() < 1 /*
																				 * ||
																				 * pwd
																				 * .
																				 * length
																				 * (
																				 * )
																				 * <
																				 * 1
																				 */) {
			msg = String.format(
					"Invalid or: DRIVER<%s> URL<%s> USERID<%s> or PASSWORD",
					jdbcDriver, url, userId);
			logger.log(Level.SEVERE, msg);
			throw new ConvDBException(msg);
		}

		try {
			Class.forName(jdbcDriver);
			_conn = DriverManager.getConnection(url, userId, pwd);

		} catch (final SQLException e) {
			msg = String.format(
					"Connection Error: DRIVER<%s> URL<%s> USERID<%s>",
					jdbcDriver, url, userId);
			logger.log(Level.SEVERE, msg + "\n" + e.getMessage());
			throw new ConvDBException(msg);
		} catch (final ClassNotFoundException e) {
			msg = String
					.format("ClassNotFoundException Error: DRIVER<%s> URL<%s> HOST<%s> USERID<%s>",
							jdbcDriver, url, userId);
			logger.log(Level.SEVERE, msg + "\n" + e.getMessage());
			throw new ConvDBException(msg);
		} catch (final Exception e) {
			msg = String
					.format("Unknown Exception occured: DRIVER<%s> URL<%s> HOST<%s> USERID<%s>",
							jdbcDriver, url, userId);
			logger.log(Level.SEVERE, msg + "\n" + e.getMessage());
			throw new ConvDBException(msg);
		}
	}

	public int GetDB2Port() {
		try {
			if (_conn != null) {
				final Properties props = _conn.getClientInfo();
				if (props != null) {
					for (final Map.Entry<Object, Object> e : props.entrySet()) {
						final String key = (String) e.getKey();
						final String value = (String) e.getValue();
						System.out.println(key + " = " + value);
					}
				} else {
					System.out
							.println("Connection return null Properties Object");
				}
			} else {
				System.out.println("Connection is null");
			}

		} catch (final SQLException e) {
			System.out.println(e.getMessage());
		} catch (final Exception e) {
			System.out.println(e.getMessage());
		}
		return 0;
	}

	public Clob GetClob() throws ConvDBException {
		Clob clob = null;
		try {
			clob = _conn.createClob();
		} catch (final SQLException e) {
			final String msg = "Failed to Create CLOB Object";
			logger.log(Level.SEVERE, msg + "\n" + e.getMessage());
			throw new ConvDBException(msg);
		} catch (final Exception e) {
			final String msg = "Unknown Exception occured when Creating Clob Object";
			logger.log(Level.SEVERE, msg + "\n" + e.getMessage());
			throw new ConvDBException(msg);
		}
		return clob;
	}

	public Blob GetBlob() throws ConvDBException {
		Blob blob = null;
		try {
			blob = _conn.createBlob();
		} catch (final SQLException e) {
			final String msg = "Failed to Create BLOB Object";
			logger.log(Level.SEVERE, msg + "\n" + e.getMessage());
			throw new ConvDBException(msg);
		} catch (final Exception e) {
			final String msg = "Unknown Exception occured when Creating Blob Object";
			logger.log(Level.SEVERE, msg + "\n" + e.getMessage());
			throw new ConvDBException(msg);
		}
		return blob;
	}
	public Recordset CreateCallableStatement(final String sql)
			throws ConvDBException {
		final Recordset rs = new Recordset();
		String msg;
		try {
			rs.cstmt = _conn.prepareCall(sql);
		} catch (final SQLException e) {
			msg = "SQLException occured when creating CallableStatement : "
					+ sql;
			logger.log(Level.SEVERE, msg + "\n" + e.getMessage());
			rs.close();
			throw new ConvDBException(msg);
		} catch (final Exception e) {
			msg = "Unknown Exception occured when creating CallableStatement : "
					+ sql;
			logger.log(Level.SEVERE, msg + "\n" + e.getMessage());
			rs.close();
			throw new ConvDBException(msg);
		}
		return rs;
	}

	public Blob CreateBlob() throws ConvDBException {
		Blob blob = null;
		String msg;
		try {
			blob = _conn.createBlob();
		} catch (final SQLException e) {
			msg = "Failed to Create Blob object";
			logger.log(Level.SEVERE, e.getMessage());
			throw new ConvDBException(msg);
		} catch (final Exception e) {
			msg = "Unknown Exception - Failed to Create Blob object";
			logger.log(Level.SEVERE, e.getMessage());
			throw new ConvDBException(msg);
		}
		return blob;

	}

	public Recordset CreateCallablePreparedStatement(final String sql)
			throws ConvDBException {
		final Recordset rs = new Recordset();
		String msg;

		try {
			rs.preparedstmt = _conn.prepareStatement(sql);
			msg = "Created PreparedStatement using : " + sql;
			logger.log(Level.INFO, msg);
		} catch (final Exception e) {
			msg = "Unknown Exception occured creating PreparedStatement using : "
					+ sql;
			logger.log(Level.SEVERE, msg + "\n" + e.getMessage());
			throw new ConvDBException(msg);
		}
		return rs;
	}

	/*
	 * public void CreateUpdateVRSDPreparedStatement(final String sql)throws
	 * ConvDBException { String msg;
	 * 
	 * try{ rsUpdateVRSD.preparedstmt = _conn.prepareStatement(sql); msg =
	 * "Created UpdateVRSDPreparedStatement using : " +
	 * rsUpdateVRSD.preparedstmt.toString(); logger.log( Level.INFO, msg ); }
	 * catch(Exception e){ msg =
	 * "Unknown Exception occured creating UpdateVRSDPreparedStatement using : "
	 * + sql; logger.log( Level.SEVERE, msg + "\n" + e.getMessage() ); throw new
	 * ConvDBException(msg); } }
	 */
	public void ExecuteQuery(final Recordset rs) throws ConvDBException {
		String msg;
		if (rs.cstmt == null) {
			msg = "CallableStatement parameter can not be null";
			logger.log(Level.SEVERE, msg);
			throw new ConvDBException(msg);
		}
		try {
			rs.rset = rs.cstmt.executeQuery();
		} catch (final SQLException e) {
			msg = "SQLException occured CallableStatement.executeQuery";
			logger.log(Level.SEVERE, msg + "\n" + e.getMessage());
			rs.close();
			throw new ConvDBException(msg);
		} catch (final Exception e) {
			msg = "Unknown Exception occured CallableStatement.executeQuery";
			logger.log(Level.SEVERE, msg + "\n" + e.getMessage());
			rs.close();
			throw new ConvDBException(msg);
		}

	}

	public Recordset ExecuteQuery(final String sqlQuery) throws ConvDBException {
		final Recordset rs = new Recordset();
		rs.rset = null;
		rs.preparedstmt = null;

		try {
			if (sqlQuery.length() < 11) {
				throw new ConvDBException("Invalid SQL length" + sqlQuery);
			}

			rs.preparedstmt = _conn.prepareStatement(sqlQuery);

			rs.rset = rs.preparedstmt.executeQuery();
		} catch (final SQLException e) {
			logger.log(Level.SEVERE, "ExecuteQuery failed: " + sqlQuery);
			logger.log(Level.SEVERE, e.getMessage());
			RecordsetCleanup(rs);
		} catch (final Exception e) {
			logger.log(Level.SEVERE, "Unknown Exception in ExecuteQuery: "
					+ sqlQuery);
			logger.log(Level.SEVERE, e.getMessage());
			RecordsetCleanup(rs);
		}

		return rs;
	}

	public int ExcecuteUpdate(final ArrayList<FieldTypeValue> fieldList,
			final String sql) throws ConvDBException {

		final Recordset rs = new Recordset();
		rs.preparedstmt = null;
		int rowsUpdated = 0;

		if ("".equals(sql)) {
			throw new ConvDBException("SQL Cannot Be Null or Empty" + sql);
		}

		try {
			rs.preparedstmt = _conn.prepareStatement(sql);
			for (final FieldTypeValue fieldTypeValue : fieldList) {

				logger.log(Level.INFO, fieldTypeValue.toString());
				BindParameter(fieldTypeValue, rs.preparedstmt);
			}

			logger.log(Level.FINE, rs.preparedstmt.toString());

			rowsUpdated = rs.preparedstmt.executeUpdate();

			if (rs.preparedstmt != null) {
				logger.log(Level.INFO, "Closing prepared statement");
				rs.preparedstmt.close();
				rs.preparedstmt = null;
			}

			RecordsetCleanup(rs);
			return rowsUpdated;
		} catch (final SQLException e) {
			final String msg = "SQLExcecute failed: " + sql;
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.getMessage());
			RecordsetCleanup(rs);
			throw new ConvDBException(msg);
		} catch (final Exception e) {
			final String msg = "Unknown Exception in SQLExcecute: " + sql;
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.getMessage());
			RecordsetCleanup(rs);
			throw new ConvDBException(msg);
		}
	}

	public Recordset ExecuteQuery(final ArrayList<FieldTypeValue> fieldList,
			final PreparedStatement preparedstmt) throws ConvDBException {

		final Recordset rs = new Recordset();
		rs.rset = null;
		rs.preparedstmt = null;
		String msg;
		if (preparedstmt == null) {
			msg = "PreparatedStatement Object cannot be null";
			logger.log(Level.SEVERE, msg);
			RecordsetCleanup(rs);
			throw new ConvDBException(msg);
		}

		try {

			for (final FieldTypeValue fieldTypeValue : fieldList) {

				logger.log(Level.INFO, fieldTypeValue.toString());
				BindParameter(fieldTypeValue, preparedstmt);
			}

			logger.log(Level.INFO, rs.preparedstmt.toString());

			rs.rset = rs.preparedstmt.executeQuery();
			msg = "Executed PreparedStatement using : "
					+ rs.preparedstmt.toString();
			logger.log(Level.INFO, msg);

			return rs;
		} catch (final SQLException e) {
			msg = "SQLExcecute failed: " + rs.preparedstmt.toString();
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.getMessage());
			RecordsetCleanup(rs);
			throw new ConvDBException(msg);
		} catch (final Exception e) {
			msg = "Unknown Exception in SQLExcecute: "
					+ rs.preparedstmt.toString();;
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.getMessage());
			RecordsetCleanup(rs);
			throw new ConvDBException(msg);
		}
	}

	public int ExecutePSUpdate(final ArrayList<FieldTypeValue> fieldList,
			final Recordset rs) throws ConvDBException {
		String msg;
		int rowsUpdated = 0;

		try {
			for (final FieldTypeValue fieldTypeValue : fieldList) {

				logger.log(Level.INFO, fieldTypeValue.toString());
				BindParameter(fieldTypeValue, rs.preparedstmt);
			}

			logger.log(Level.INFO, rs.preparedstmt.toString());

			rowsUpdated = rs.preparedstmt.executeUpdate();
			msg = "Executed PreparedStatement using : "
					+ rs.preparedstmt.toString();
			logger.log(Level.INFO, msg);

			return rowsUpdated;
		} catch (final SQLException e) {
			msg = "SQLExecute failed: " + rs.preparedstmt.toString();
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.getMessage());
			throw new ConvDBException(msg);
		} catch (final Exception e) {
			msg = "Unknown Exception in SQLExecute: "
					+ rs.preparedstmt.toString();
			logger.log(Level.SEVERE, msg);
			logger.log(Level.SEVERE, e.getMessage());
			throw new ConvDBException(msg);
		}
	}

	private void BindParameter(final FieldTypeValue fieldTypeValue,
			final PreparedStatement stmt) throws SQLException {

		final int type = fieldTypeValue.get_fieldType();
		final int pos = fieldTypeValue.get_fieldPos();
		final Object val = fieldTypeValue.get_dataValue();

		switch (type) {
			case TYPE_INT : {
				stmt.setInt(pos, (int) val);
				break;
			}
			case TYPE_STRING : {
				stmt.setString(pos, (String) val);
				break;
			}
			case TYPE_TIMESTAMP : {
				stmt.setTimestamp(pos, (Timestamp) val);
				break;
			}
			case TYPE_LONG : {
				stmt.setLong(pos, (long) val);
				break;
			}
			case TYPE_CLOB : {
				stmt.setClob(pos, (Clob) val);
				break;
			}
			case TYPE_BYTE_ARRAY : {
				// final byte[] bytePwd = (byte[])val;
				stmt.setBytes(pos, (byte[]) val);
				break;
			}
			case TYPE_BLOB : {
				stmt.setBlob(pos, (Blob) val);
				break;
			}

		}
	}

	public void SQLExecute(final String statement) throws ConvDBException {
		Statement stmt = null;
		try {
			stmt = _conn.createStatement();
			if (stmt != null) {
				logger.log(Level.FINE, "SQLExecute:" + statement);
				stmt.execute(statement);
			}
		} catch (final SQLException e) {
			logger.log(Level.SEVERE, e.getMessage());
			StatementCleanup(stmt);
			throw new ConvDBException("Failed to Execute: " + statement);
		} catch (final Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			StatementCleanup(stmt);
			throw new ConvDBException("Unkown Exception - Failed to Execute: "
					+ statement);
		} finally {
			StatementCleanup(stmt);
		}
	}

	private void StatementCleanup(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (final SQLException e) {
			logger.log(Level.SEVERE, e.getMessage());
			stmt = null;
		}

	}

	private void RecordsetCleanup(Recordset rs) {
		if (rs != null) {
			rs.close();
			rs = null;
		}
	}

	public void Disconnect() {
		try {
			if (_conn != null) {
				_conn.close();
				_conn = null;
			}
		} catch (final SQLException e) {
			logger.log(Level.SEVERE,
					"Failed to close Connection" + e.getMessage());
		} finally {
			_conn = null;
		}
	}

	Connection _conn;
	public static final int TYPE_INT = 1;
	public static final int TYPE_STRING = 2;
	public static final int TYPE_TIMESTAMP = 3;
	public static final int TYPE_LONG = 4;
	public static final int TYPE_CLOB = 5;
	public static final int TYPE_BYTE_ARRAY = 6;
	public static final int TYPE_BLOB = 7;

}
