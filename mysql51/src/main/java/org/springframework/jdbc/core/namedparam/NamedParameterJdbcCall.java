package org.springframework.jdbc.core.namedparam;

import static com.diffplug.common.base.Errors.rethrow;
import static java.util.stream.Collectors.toMap;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.CallableStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;

public class NamedParameterJdbcCall extends NamedParameterJdbcTemplate {

	public NamedParameterJdbcCall(DataSource dataSource) {
		super(dataSource);
	}

	public Map<String, ?> execute(String sql, MapSqlParameterSource paramSource) throws DataAccessException,SQLException {
		ParsedSql parsedSql = getParsedSql(sql);
		List<String> paramNames = parsedSql.getParameterNames();
		Map<String, Integer> paramIndexes = paramNames.stream().filter(pName->pName.startsWith("@")).collect(toMap(pName->pName.substring(1), pName->1+paramNames.indexOf(pName)));
		
		CallableStatementCallback<Map<String, ?>> action = new CallableStatementCallback<Map<String, ?>>() {
            public Map<String, ?> doInCallableStatement(CallableStatement cs) throws SQLException {
                cs.execute();
                return paramIndexes.entrySet().stream().collect(toMap(entry->entry.getKey(), entry->rethrow().get(()->cs.getObject(entry.getValue()))));
            }
        };

		return getJdbcOperations().execute(getCallableStatementCreator(parsedSql, paramSource), action);
	}
	
	protected CallableStatementCreator getCallableStatementCreator(ParsedSql parsedSql, MapSqlParameterSource paramSource) {
		String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
		List<SqlParameter> declaredParameters = NamedParameterUtils.buildSqlParameterList(parsedSql, paramSource);
		declaredParameters = declaredParameters.stream().filter(param->paramSource.getValues().keySet().contains(param.getName())).collect(Collectors.toList());
		CallableStatementCreatorFactory cscf = new CallableStatementCreatorFactory(sqlToUse, declaredParameters);
		return cscf.newCallableStatementCreator(paramSource.getValues());
	}
}
