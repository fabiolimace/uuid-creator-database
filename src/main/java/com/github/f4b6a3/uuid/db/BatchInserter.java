package com.github.f4b6a3.uuid.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.github.f4b6a3.uuid.UuidCreator;
import com.github.f4b6a3.uuid.creator.NoArgumentsUuidCreator;
import com.github.f4b6a3.uuid.exception.UuidCreatorException;
import com.github.f4b6a3.uuid.util.UuidConverter;
import com.github.f4b6a3.uuid.util.UuidUtil;

public class BatchInserter {

	private int database;
	private String user;
	private String password;
	private int port;

	private Connection connection = null;

	private static final int DATABASE_SQLITE = 0;
	private static final int DATABASE_POSTGRESQL = 1;
	private static final int DATABASE_MYSQL = 2;

	private static final String URL_SQLITE = "jdbc:sqlite:db/sqlite/uuidcreator.sqlite.db";
	private static final String URL_POSTGRESQL = "jdbc:postgresql://localhost:port/uuidcreator";
	private static final String URL_MYSQL = "jdbc:mysql://localhost:port/uuidcreator";

	private static final int PORT_POSTGRESQL = 5432;
	private static final int PORT_MYSQL = 3306;

	private static final int TYPE_TIME_BASED = 1;
	private static final int TYPE_TIME_ORDERED = 6;
	private static final int TYPE_RANDOM_BASED = 4;
	private static final int TYPE_COMB = 10;

	public BatchInserter(int database, String user, String password, int port) {
		this.database = database;
		this.user = user;
		this.password = password;
		this.port = port;
	}

	public BatchInserter(int database) {
		this.database = database;
		this.user = "uuidcreator";
		this.password = "123456";
		if (database == DATABASE_POSTGRESQL) {
			this.port = PORT_POSTGRESQL;
		} else if (database == DATABASE_MYSQL) {
			this.port = PORT_MYSQL;
		}
	}

	public void openConnection() {

		String url = null;

		if (database == DATABASE_POSTGRESQL) {
			url = URL_POSTGRESQL.replace("port", String.valueOf(port));
		} else if (database == DATABASE_MYSQL) {
			url = URL_MYSQL.replace("port", String.valueOf(port));
		} else if (database == DATABASE_SQLITE) {
			url = URL_SQLITE;
		}

		this.connection = getConnection(url, user, password);
	}

	public Connection getConnection(String url, String user, String password) {
		try {
			if (connection != null && !connection.isClosed()) {
				return connection;
			}

			Class.forName("org.postgresql.Driver");
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, user, password);
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void execute(String sql) {

		Statement statement = null;

		try {
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void clearTable() {
		String uuidTableDelete = "DELETE FROM tb_uuid;";
		execute(uuidTableDelete);
	}

	public void createTable() {

		String binaryType = ":BINARYTYPE";
		String datetimeType = ":DATETIMETYPE";

		String uuidTableDdl = "CREATE TABLE IF NOT EXISTS tb_uuid (uuid_binary :BINARYTYPE NOT NULL, uuid_version INTEGER NOT NULL, uuid_datetime :DATETIMETYPE, uuid_timestamp BIGINT, uuid_clockseq INTEGER, uuid_nodeid BIGINT, uuid_counter INTEGER, uuid_threadid INTEGER);";

		if (database == DATABASE_POSTGRESQL) {
			uuidTableDdl = uuidTableDdl.replace(binaryType, "UUID").replaceAll(datetimeType, "TIMESTAMP");
		} else if (database == DATABASE_MYSQL) {
			uuidTableDdl = uuidTableDdl.replace(binaryType, "BINARY(16)").replaceAll(datetimeType, "TIMESTAMP");
		} else if (database == DATABASE_SQLITE) {
			uuidTableDdl = uuidTableDdl.replace(binaryType, "BLOB").replaceAll(datetimeType, "DATETIME");
		}

		execute(uuidTableDdl);
	}

	public void closeConnection() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void dropTable() {
		String uuidTableDrop = "DROP TABLE IF EXISTS tb_uuid;";
		execute(uuidTableDrop);
	}

	public void insert(UUID uuid, int threadId, int type) {

		byte[] bytes = UuidConverter.toBytes(uuid);
		int version = uuid.version();
		Timestamp datetime = null;
		long timestamp = 0;
		long clockseq = 0;
		long nodeid = 0;

		if (type == TYPE_TIME_BASED || type == TYPE_TIME_ORDERED) {
			datetime = Timestamp.from(UuidUtil.extractInstant(uuid));
			timestamp = UuidUtil.extractTimestamp(uuid);
			clockseq = UuidUtil.extractClockSequence(uuid);
			nodeid = UuidUtil.extractNodeIdentifier(uuid);
		}

		String uuidTableInsert = "INSERT INTO tb_uuid(uuid_binary, uuid_version, uuid_datetime, uuid_timestamp, uuid_clockseq, uuid_nodeid, uuid_counter, uuid_threadid) VALUES(?, ?, ?, ?, ?, ?, ?, ?);";

		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement(uuidTableInsert);
			if (database == DATABASE_POSTGRESQL) {
				statement.setObject(1, uuid);
			} else {
				statement.setBytes(1, bytes);
			}
			statement.setInt(2, version);
			statement.setTimestamp(3, datetime != null ? datetime : Timestamp.from(Instant.now()));
			statement.setLong(4, timestamp);
			statement.setInt(5, (int) clockseq);
			statement.setLong(6, nodeid);
			statement.setInt(7, (int) (timestamp % 10_000));
			statement.setInt(8, threadId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void startTransaction() {
		try {
			this.connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void endTransaction() {
		try {
			this.connection.commit();
		} catch (SQLException e) {
			try {
				this.connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private static class InsertThread extends Thread {

		private BatchInserter batch;
		private int type;
		private NoArgumentsUuidCreator creator;
		private int loopMax;
		private int threadNumber;
		private int threadId;
		private List<UUID> list = new ArrayList<>();

		public InsertThread(BatchInserter batch, int type, NoArgumentsUuidCreator creator, int loopMax, int threadNumber) {
			this.batch = batch;
			this.type = type;
			this.creator = creator;
			this.loopMax = loopMax;
			this.threadNumber = threadNumber;
		}

		@Override
		public void run() {

			threadId = this.hashCode();

			System.out.println(String.format(Instant.now() + " Thread %02d: started", threadNumber));
			for (int i = 0; i < loopMax; i++) {
				double progress = (i * 1.0 / loopMax) * 100;
				if (progress % 1 == 0) {
					System.out.println(String.format(Instant.now() + " Thread %02d progress: %02d%%", threadNumber,
							(int) progress));
				}
				try {
					UUID uuid = creator.create();
					list.add(uuid);
				} catch (UuidCreatorException e) {
					// Try again if an exception occurs
					UUID uuid = creator.create();
					list.add(uuid);
				}
			}

			flush();
			System.out.println(String.format(Instant.now() + " Thread %02d: finished", threadNumber));
		}

		public void flush() {
			for (int i = 0; i < list.size(); i++) {
				double progress = (i * 1.0 / list.size()) * 100;
				if (progress % 1 == 0) {
					System.out.println(
							String.format(Instant.now() + " Thread %02d insert: %02d%%", threadNumber, (int) progress));
				}
				batch.insert(list.get(i), threadId, type);
			}
			list.clear();
		}
	}

	/**
	 * Returns a factory according to the version required.
	 * 
	 * Three versions are accepted: sequential, time-based and secure
	 * random-based.
	 * 
	 * If an not accepted or null version is informed, it returns a fast
	 * random-based factory.
	 * 
	 * @param version
	 * @return
	 */
	protected NoArgumentsUuidCreator getNoArgumentsUuidCreator(int type) {
		NoArgumentsUuidCreator creator;

		if (type == TYPE_TIME_ORDERED) {
			creator = UuidCreator.getTimeOrderedCreator();
		} else if (type == TYPE_TIME_BASED) {
			creator = UuidCreator.getTimeBasedCreator();
		} else if (type == TYPE_RANDOM_BASED) {
			creator = UuidCreator.getRandomBasedCreator();
		} else if (type == TYPE_COMB) {
			creator = UuidCreator.getCombCreator();
		} else {
			creator = UuidCreator.getRandomBasedCreator().withFastRandomGenerator();
		}
		return creator;
	}

	public void batchInsert(BatchInserter batch, int type, int threadCount, int loopMax) {

		batch.startTransaction();
		batch.dropTable();
		batch.createTable();

		InsertThread[] threads = new InsertThread[threadCount];

		for (int i = 0; i < threadCount; i++) {
			NoArgumentsUuidCreator creator = getNoArgumentsUuidCreator(type);
			threads[i] = new InsertThread(batch, type, creator, loopMax, i);
			threads[i].start();
		}

		// Wait all the threads to finish
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		batch.endTransaction();
	}

	public static void main(String[] args) {

		int threadCount = 16;
		int loopMax = 1_000_000;
		BatchInserter batch = null;
		int type = TYPE_TIME_BASED;
		String user = "uuidcreator";
		String pass = "123456";

		batch = new BatchInserter(DATABASE_SQLITE);
		batch.openConnection();
		batch.batchInsert(batch, type, threadCount, loopMax);
		batch.closeConnection();

//		batch = new BatchInserter(DATABASE_POSTGRESQL, user, pass, 5432);
//		batch.openConnection();
//		batch.batchInsert(batch, type, threadCount, loopMax);
//		batch.closeConnection();

//		batch = new BatchInserter(DATABASE_MYSQL, user, pass, 3306);
//		batch.openConnection();
//		batch.batchInsert(batch, type, threadCount, loopMax);
//		batch.closeConnection();
	}
}
