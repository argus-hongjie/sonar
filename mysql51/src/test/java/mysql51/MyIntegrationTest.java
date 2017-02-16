package mysql51;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


public class MyIntegrationTest{
	
	protected static MysqlDataSourceManager mysql = MysqlDataSourceManager.getInstance();
	private static PlatformTransactionManager mysqlTxMgr;
	private TransactionStatus mysqlStatus;
	
	@BeforeClass
	public static void beforeClass(){
		
	}
	
    @Before
    public void before() throws Exception {
        mysqlTxMgr = mysql.getTxManager();
        mysqlStatus = mysqlTxMgr.getTransaction(new DefaultTransactionDefinition());
        mysql.getJdbcTemplate().update("set foreign_key_checks=0");
    }
    
	@After
	public void after() {
		mysqlTxMgr.rollback(mysqlStatus);
	}

}
