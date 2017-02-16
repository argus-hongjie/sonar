package mysql51;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;


public class DBClientAttentePointageTest extends MyIntegrationTest{

	private DBClientAttentePointage dbClient = new DBClientAttentePointage();
	private static final MysqlDataSourceManager mysql = MysqlDataSourceManager.getInstance();

	
	@Test
	public void testMajArgusDemandePointage() throws SQLException {
		dbClient.majArgusDemandePointage("632966", 632966, "titreTest", "84798465352143", "20161114/0131940202408.pdf", "20161114", "20161114 11:11:11");
		
		Map<String, Object> map = mysql.getNamedTemplate().queryForMap("SELECT * FROM `argus_demande_pointage` WHERE TITRE = :titre",  ImmutableMap.of("titre","titreTest"));
		assertThat(map.get("TITRE")).isEqualTo("titreTest");
	}

	@Test
	public void testCallWsEsb() throws SQLException {
		Map<String, ?> retour = dbClient.callWsEsb(null, "20161114", "20161114 11:11:11", "84798465352143", "20161114/0131940202408.pdf", false);
		assertThat(retour.get("code_retour")).isEqualTo("-1");
	}

}
