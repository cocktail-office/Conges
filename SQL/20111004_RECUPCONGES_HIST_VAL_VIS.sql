--
-- Patch DDL de CONGES du 15/03/2012 à éxecuter depuis le user GRHUM
--
-- Rem : fichier encodé UTF-8


SET DEFINE OFF;

--
--
-- Fichier : 1
-- Type : DDL
-- Schéma : CONGES
-- Numéro de version : 2.10.6.9
-- Date de publication : 15/03/2012
-- Auteur(s) : Cyril TARADE
-- Licence : CeCILL version 2
--
--


CREATE OR REPLACE PROCEDURE CONGES.Recupconges
(
     a_no_seq_affectation IN NUMBER, -- l'affectation gepeto qu'il faudra associer au planning recupere
    a_new_oid_aff_ann   IN  NUMBER, -- le planning incorrect (oid de AFF_ANN de GEST a effacer), mettre NULL si rien a effacer
    a_ref_oid_aff_ann  IN  NUMBER    -- le planning correct (oid de AFF_ANN de GESTEST a recupere)
)
----------------------------------------------------------------------
--  Auteur : CRI
--  Creation : 23/05/2006
--  Modification : 15/03/2012
--  Objectif : Recuperer un planning d'une personne suite a une suppression par erreur
--  Remarques : 
--  * Desactiver le trigger GRHUM.TRG_CNG_AFFECTATION de GEST
--  * Arreter l'application conges pendant l'execution de la procedure.
--  * Si le DB_LINK n'esite pas, il faut creer le creer vers la base de production (GEST)
--      exemple :
--             CREATE DATABASE LINK GEST.WORLD
--             CONNECT TO conges
--             IDENTIFIED BY "<pass_conges>"
--             USING 'GEST'
--  * Executer a partir de la base de donnees 'propre' (GESTEST)
----------------------------------------------------------------------
IS
    new_oid_aff_ann    NUMBER;
BEGIN



    -- ETAPE 1 : EFFACER LES DONNEES ERONNEES

    DELETE FROM PLNG_CLC_AFF_ANN@gest.world WHERE oid_aff_ann = a_new_oid_aff_ann;

    DELETE FROM PLNG_ANN_PER_FER@gest.world WHERE oid_aff_ann = a_new_oid_aff_ann;

    DELETE FROM PLNG_PLG_HEB@gest.world WHERE oid_per_aff_ann IN
           (SELECT oid
           FROM PLNG_PER_AFF_ANN@gest.world
           WHERE oid_aff_ann = a_new_oid_aff_ann);


    DELETE FROM PLNG_HOR@gest.world WHERE oid_aff_ann = a_new_oid_aff_ann;

    DELETE FROM CNG_HIST_VAL_VIS@gest.world WHERE oid_occupation IN
              (SELECT oid
           FROM PLNG_OCC@gest.world
           WHERE oid_aff_ann = a_new_oid_aff_ann);

    DELETE FROM CNG_HIST_VAL_VIS@gest.world WHERE oid_aff_ann = a_new_oid_aff_ann;

    DELETE FROM PLNG_OCC@gest.world WHERE oid_aff_ann = a_new_oid_aff_ann;

    DELETE FROM PLNG_PER_AFF_ANN@gest.world WHERE oid_aff_ann = a_new_oid_aff_ann;

    DELETE FROM PLNG_AFF_ANN@gest.world WHERE oid = a_new_oid_aff_ann;



    -- ETAPE 2 : IMPORT DEPUIS LA BASE 'PROPRE'

    -- les affectations annuelles
    INSERT INTO PLNG_AFF_ANN@gest.world
           SELECT *
           FROM PLNG_AFF_ANN
           WHERE oid = a_ref_oid_aff_ann;

    -- les periodes (association affectation / aff. anuelle)
    INSERT INTO PLNG_PER_AFF_ANN@gest.world
           SELECT *
           FROM PLNG_PER_AFF_ANN
           WHERE oid_aff_ann = a_ref_oid_aff_ann;

    UPDATE PLNG_PER_AFF_ANN@gest.world
    SET oid_affectation = a_no_seq_affectation
    WHERE oid_aff_ann = a_ref_oid_aff_ann;

    -- les occupations
    INSERT INTO PLNG_OCC@gest.world
           SELECT *
           FROM PLNG_OCC
           WHERE oid_aff_ann = a_ref_oid_aff_ann;

    -- les historiques de validation
    INSERT INTO CNG_HIST_VAL_VIS@gest.world(oid, oid_individu, oid_occupation, oid_aff_ann, typ_val, d_creation, d_modification)
           SELECT CNG_HIST_VAL_VIS_SEQ.NEXTVAL@gest.world, oid_individu, oid_occupation, hsv.oid_aff_ann, typ_val, hsv.d_creation, hsv.d_modification
           FROM CNG_HIST_VAL_VIS hsv, PLNG_OCC o
           WHERE hsv.oid_occupation = o.oid
           AND o.oid_aff_ann = a_ref_oid_aff_ann;

    INSERT INTO CNG_HIST_VAL_VIS@gest.world(oid, oid_individu, oid_occupation, oid_aff_ann, typ_val, d_creation, d_modification)
           SELECT CNG_HIST_VAL_VIS_SEQ.NEXTVAL@gest.world, oid_individu, oid_occupation, hsv.oid_aff_ann, typ_val, hsv.d_creation, hsv.d_modification
           FROM CNG_HIST_VAL_VIS hsv
           WHERE hsv.oid_occupation = null
           AND hsv.oid_aff_ann = a_ref_oid_aff_ann;

    -- les horaires
    INSERT INTO PLNG_HOR@gest.world
           SELECT *
           FROM PLNG_HOR
           WHERE oid_aff_ann = a_ref_oid_aff_ann;

    -- les associations horaires / semaines
     INSERT INTO PLNG_PLG_HEB@gest.world
           SELECT *
           FROM PLNG_PLG_HEB
           WHERE oid_per_aff_ann IN
                 (SELECT oid
                 FROM PLNG_PER_AFF_ANN
                    WHERE oid_aff_ann = a_ref_oid_aff_ann);

    -- les annulations de periode de fermeture
    INSERT INTO PLNG_ANN_PER_FER@gest.world
           SELECT *
           FROM PLNG_ANN_PER_FER
           WHERE oid_aff_ann = a_ref_oid_aff_ann;

    -- les calculs
    INSERT INTO PLNG_CLC_AFF_ANN@gest.world
             SELECT *
            FROM PLNG_CLC_AFF_ANN
            WHERE oid_aff_ann = a_ref_oid_aff_ann;

    -- les alertes
    INSERT INTO CNG_ALERTE@gest.world
            SELECT *
            FROM CNG_ALERTE
            WHERE oid_aff_ann = a_ref_oid_aff_ann;


END;
/


COMMIT;