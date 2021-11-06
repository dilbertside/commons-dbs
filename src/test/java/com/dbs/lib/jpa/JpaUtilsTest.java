/**
 * JpaUtilsTest
 */
package com.dbs.lib.jpa;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

/**
 * @author dbs at 1 Nov 2021 10:48:40
 * @since 1.1.2
 * @version 1.0
 */
@Tag("jpa")
public class JpaUtilsTest {

	@Test
	public void testDetermineDatabaseH2() throws SQLException {
		SingleConnectionDataSource ds = new SingleConnectionDataSource("jdbc:h2:mem:h2mem;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE", "john", "pwd", false);
		String dbName = "H2";
		String dbFound = JpaUtils.determineDatabase(ds);
		assertTrue(dbName.equals(dbFound));
	}
	
	/**
	 * use unit container
	 * @throws SQLException
	 */
	//@Test
	public void testDetermineDatabasePG() throws SQLException {
		SingleConnectionDataSource ds = new SingleConnectionDataSource("jdbc:postgresql://localhost:3389/mine", "john", "pwd", false);
		String dbName = "postgresql";
		String dbFound = JpaUtils.determineDatabase(ds);
		assertTrue(dbName.equals(dbFound));
	}
}
