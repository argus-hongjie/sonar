DELIMITER $$
CREATE DEFINER=`bulletinage`@`%` PROCEDURE `ps_maj_reception`(IN `identifiant_unite_source` INT(11), IN `date_parution` CHAR(8), IN `date_faciale` VARCHAR(64), IN `code_barres` VARCHAR(64), IN `chemin_feuille_pointage` VARCHAR(255), IN `nombre_pages` INT(11), IN `numero_parution` VARCHAR(200), IN `id_inst_appro` INT(11), IN `date_fiable` BIT, IN `force_pointage_auto` BIT, IN `nom_utilisateur` VARCHAR(64), OUT `code_retour` INT, OUT `message_retour` VARCHAR(255))
BEGIN
    SET code_retour = -1;
    SET message_retour = 'Paramètre manquant : identifiant_unite_source';
END$$
DELIMITER ;
