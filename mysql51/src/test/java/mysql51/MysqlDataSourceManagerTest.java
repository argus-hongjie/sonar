package mysql51;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class MysqlDataSourceManagerTest extends MyIntegrationTest{
	private static final MysqlDataSourceManager mysql = MysqlDataSourceManager.getInstance();
	
	@Test
	public void testGetConnection() {
		assertNotNull(mysql.getConnection());
	}

	@Test
	public void testGetJdbcTemplate() {
		assertNotNull(mysql.getNamedTemplate());
	}

	@Test
	public void testGetInstance() {
		assertNotNull(mysql);
	}

	
	@Test
	public void testSelect() {
		assertThat(
				mysql.getNamedTemplate().queryForObject("select :param", ImmutableMap.of("param",1), Integer.class )
        ).isEqualTo(1);
	}
}
