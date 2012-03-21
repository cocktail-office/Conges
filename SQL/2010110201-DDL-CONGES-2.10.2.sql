--
-- Patch DDL de CONGES du 02/11/2010 à éxecuter depuis le user CONGES
--
-- Rem : fichier encodé UTF-8


SET DEFINE OFF;

--
--
-- Fichier : 1
-- Type : DML
-- Schéma : CONGES
-- Numéro de version : 2.10.2
-- Date de publication : 02/11/2010
-- Auteur(s) : Cyril TARADE
-- Licence : CeCILL version 2
--
--


/*
Ajout de l'année universitaire dans la liste des alertes (demandes en attente de validation)
*/

WHENEVER SQLERROR EXIT SQL.SQLCODE;



CREATE OR REPLACE FORCE VIEW conges.v_alerte (oid,
                                              nom_usuel,
                                              prenom,
                                              no_individu,
                                              lc_structure,
                                              libelle,
                                              commentaire,
                                              c_structure,
                                              c_structure_composante,
                                              d_creation,
                                              oid_occ,
                                              flg_drh,
                                              occ_status,
                                              occ_dte_debut,
                                              occ_dte_fin,
                                              t_libelle_court,
                                              oid_aff_ann,
                                              plg_status,
                                              annee
                                             )
AS
   SELECT   a.oid, nom_usuel, prenom, no_individu, s.lc_structure, a.libelle,
            commentaire, s.c_structure, vs.c_structure_composante,
            a.d_creation, occ.oid oid_occ, tocc.flg_drh,
            occ.status occ_status, occ.dte_debut occ_dte_debut,
            occ.dte_fin occ_dte_fin, tocc.libelle_court t_libelle_court,
            a.oid_aff_ann, aff_ann.sts_planning, aff_ann.annee
       FROM grhum.individu_ulr i,
            grhum.structure_ulr s,
            grhum.affectation aff,
            conges.plng_per_aff_ann paa,
            conges.cng_alerte a,
            conges.plng_occ occ,
            conges.plng_typ_occ tocc,
            grhum.v_service vs,
            conges.plng_aff_ann aff_ann
      WHERE aff.no_dossier_pers = i.no_individu
        AND s.c_structure = aff.c_structure
        AND aff.no_seq_affectation = paa.oid_affectation
        AND paa.oid_affectation = aff.no_seq_affectation
        AND paa.oid_aff_ann = a.oid_aff_ann
        AND vs.c_structure = s.c_structure
        AND a.oid_occupation = occ.oid(+)
        AND occ.type = tocc.oid(+)
        AND a.oid_aff_ann = aff_ann.oid
   ORDER BY d_creation DESC;


INSERT INTO CONGES.DB_VERSION ( DB_VERSION_ID, DB_VERSION_LIBELLE, DB_VERSION_DATE, DB_INSTALL_DATE,DB_COMMENT ) VALUES 
	( 2102, '2.10.2',  TO_DATE( '02/11/2010', 'DD/MM/YYYY'),  SYSDATE, 'Base de donnees CONGES v2.10.2'); 



COMMIT;
