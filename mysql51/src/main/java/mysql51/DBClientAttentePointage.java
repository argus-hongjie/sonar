package mysql51;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DBClientAttentePointage {
	private static final Logger log = LogManager.getLogger(DBClientAttentePointage.class);
	/**
	 * @param uniteSource
	 * @param idSource
	 * @param titre
	 * @param codeBarre
	 * @param cheminFeuillePointage
	 * @param dateParution
	 * @param dateFaciale
	 * @return
	 * @throws SQLException
	 */
	public void majArgusDemandePointage(String uniteSource, Integer idSource, String titre, 
			String codeBarre, String cheminFeuillePointage, String dateParution, String dateFaciale) throws SQLException {
		try(Connection connection =  MysqlDataSourceManager.getInstance().getConnection()) {
			String sqlMajArgusDemandePointage = "INSERT INTO `argus_demande_pointage` (UNIQUE_KEY,`DISPLAY`,`CREATE_USER`,`CREATE_DATE`,`MODIFY_USER`,`MODIFY_DATE`,`STATUT`,`UNITE_SOURCE`,`ID_SOURCE`,`TITRE`,`UTILISATEUR`,`CODE_BARRE`,`CHEMIN_FEUILLE_POINTAGE`,`DATE_PARUTION`,`DATE_FACIALE`,`NOMBRE_PAGE`,`MESSAGE_POINTAGE`,`DATE_POINTAGE`,`PREDICTED_ISSUE`,`STATUT_LABEL`,`DATE_PARUTION_LABEL`,`DATE_POINTAGE_LABEL`,`NUMERO_PUBLICATION`,`NUMERO_LOT`,`MODE_APPROVISIONNEMENT`,`MODE_APPROVISIONNEMENT_LABEL`,`TYPE_DE_TRAITEMENT`,`INFO_SUPP`,`IS_REJEU`,`ID_INSTANCE_APPRO`,`TRAITEMENT_ID`,`TRAITEMENT_LABEL`) SELECT COALESCE(max(unique_key), 0)+1 ,NULL, 'ESB', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, '1', :unite_source, :id_source, :titre, NULL, :code_barre, :chemin_feuille_pointage, :date_parution, :date_faciale, NULL, NULL, NULL, NULL, 'A compléter', '', NULL, '', '0', '6', 'Flux numérique', '1', NULL, NULL, '0', '2', 'TraitementPDFArticle' FROM argus_demande_pointage";
			
			int affectedRows = MysqlDataSourceManager.getInstance().getNamedTemplate().update(sqlMajArgusDemandePointage, new MapSqlParameterSource()
            	.addValue("unite_source", uniteSource)
	            .addValue("id_source", idSource)
	            .addValue("titre", titre)
	            .addValue("code_barre", codeBarre)
	            .addValue("chemin_feuille_pointage", cheminFeuillePointage)
	            .addValue("date_parution", dateParution)
	            .addValue("date_faciale", dateFaciale)
            );
			if (affectedRows != 1) {
                throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(sqlMajArgusDemandePointage, 1, affectedRows);
			}
        } catch (SQLException e) {
			log.error("Erreur lors de l'insertion dans la table `argus_demande_pointage` : " + e.getMessage());
			throw (e);
		}
	}
	
	/**
	 * @param identifiantUniteSource
	 * @param dateParution
	 * @param dateFaciale
	 * @param codeBarres
	 * @param cheminFeuillePointage
	 * @param dateFiable
	 * @return
	 * @throws SQLException
	 */
	public Map<String, ?> callWsEsb(Integer identifiantUniteSource, String dateParution, String dateFaciale, String codeBarres, String cheminFeuillePointage, Boolean dateFiable) throws SQLException {
		try(Connection connection =  MysqlDataSourceManager.getInstance().getConnection()) {
            String procedureCall = "CALL `ps_maj_reception`(:identifiant_unite_source, :date_parution, :date_faciale, :code_barres, :chemin_feuille_pointage, 0, '', 0, :date_fiable, 0, 'ESB', :@code_retour, :@message_retour)";
            return  MysqlDataSourceManager.getInstance().getNamedJdbcCall().execute(procedureCall,  new MapSqlParameterSource()
    	        	.addValue("identifiant_unite_source", identifiantUniteSource)
    	            .addValue("date_parution", dateParution)
    	            .addValue("date_faciale", dateFaciale)
    	            .addValue("code_barres", codeBarres)
    	            .addValue("chemin_feuille_pointage", cheminFeuillePointage)
    	            .addValue("date_fiable", dateFiable));
        } catch (SQLException e) {
			log.error("Une erreur s'est produite lors de la appeler webservice ESB : " + e.getMessage());	
			throw (e);
		}
	}


}
