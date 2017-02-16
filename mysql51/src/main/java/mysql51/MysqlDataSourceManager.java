package mysql51;


import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcCall;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import lombok.extern.log4j.Log4j2;

/**
 * Cette classe permet la gestion de pool de connexions.
 * 
 * © @author hongjie.zhang 02 DEC 2016
 */
@Log4j2
public class MysqlDataSourceManager {

	private final BasicDataSource  source = new BasicDataSource ();

	private final NamedParameterJdbcTemplate namedTemplate;
	private final JdbcTemplate jdbcTemplate;
	private final NamedParameterJdbcCall namedJdbcCall;
	private final DataSourceTransactionManager txManager;
	private static final Logger logger = LogManager.getLogger(MysqlDataSourceManager.class);

	private MysqlDataSourceManager() {
		init();
		namedTemplate = new NamedParameterJdbcTemplate(source);
		jdbcTemplate = new JdbcTemplate(source);
		namedJdbcCall = new NamedParameterJdbcCall(source);
		txManager = new DataSourceTransactionManager(source);
	}

	public NamedParameterJdbcCall getNamedJdbcCall() {
		return namedJdbcCall;
	}

	private static class MysqlDataSourceManagerHolder {
		private static final MysqlDataSourceManager instance = new MysqlDataSourceManager();
	}

	/**
	 * Initialisation de pool de connexion.
	 */
	private void init() {
		try {
			source.setDriverClassName(PropertyHandler.getInstance().getProperty("bulletinage.db.driver.class.name"));
			source.setUrl(PropertyHandler.getInstance().getProperty("bulletinage.db.url")+"/"+PropertyHandler.getInstance().getProperty("bulletinage.db.name")+"?noAccessToProcedureBodies=true");
			source.setUsername(PropertyHandler.getInstance().getProperty("bulletinage.db.username"));
			source.setPassword(PropertyHandler.getInstance().getProperty("bulletinage.db.password"));
		} catch (NumberFormatException e) {
			logger.error("Une erreur s'est produite lors de l'initialisation du pool de connexion : " + e.getMessage());
		}
	}

	public NamedParameterJdbcTemplate getNamedTemplate() {
		return namedTemplate;
	}
	
	/**
	 * récupérer une connecxion depuis le pool de connexion.
	 * 
	 * @return Une connexion avec la base de données.
	 */
	public Connection getConnection() {
		try {
			return source.getConnection();

		} catch (SQLException e) {
			logger.error("Une erreur s'est produite lors de la récupération de connection : " + e.getMessage());
			return null;
		}

	}

	public static MysqlDataSourceManager getInstance() {
		return MysqlDataSourceManagerHolder.instance;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	

	public DataSourceTransactionManager getTxManager() {
		return txManager;
	}
}
