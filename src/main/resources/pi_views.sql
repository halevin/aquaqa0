--- Support for PI View
-- ----------------------------------------------
-- ObsProject: some info plus the status
-- ----------------------------------------------
create or replace view
    piv_obs_project as
select
    prjct.code,
    prjct.pi,
    pop.contact_account_id,
    acc.firstname,
    acc.lastname,
    contacc.firstname as contact_firstname,
    contacc.lastname as contact_lastname,
    prjct.title,
    prjct.prj_version,
    prjct.prj_letter_grade,
    obsproposal.dc_letter_grade as prj_grade,
    prjct.prj_scientific_rank   as rank,
    pstat.obs_project_id,
    pstat.domain_entity_state
from
    bmmv_obsproject prjct
inner join
    obs_project_status pstat
on
    prjct.prj_archive_uid = pstat.obs_project_id
inner join
    bmmv_obsproposal obsproposalxml
on
    obsproposalxml.projectuid = pstat.obs_project_id
left outer join
    proposal obsproposal
on
    obsproposal.archive_uid = obsproposalxml.archive_uid
left outer join
    account acc
on
    acc.account_id = prjct.pi
left outer join
    prj_operations pop
on
    pop.obs_project_archive_uid = prjct.prj_archive_uid
left outer join
    account contacc
on
    contacc.account_id = pop.contact_account_id;
    
    
-- EXAMPLE QUERY
select * from piv_obs_project
where pi = 'lricci'
order by code desc;


-- ----------------------------------------------
-- ObsUnitSet: some info plus the status
-- ----------------------------------------------

create or replace view piv_obs_unit_set
as
select ouss.obs_project_id,
       ouss.status_entity_id,
       ouss.parent_obs_unit_set_status_id,
       ouss.domain_entity_state,
       ous.partid,
       ous.name,
       ous.requestedarray,
       aqua.qa2status
from obs_unit_set_status ouss
left outer join bmmv_obsunitset ous 
on ous.ARCHIVE_UID = ouss.OBS_PROJECT_ID
and ous.PARTID = ouss.DOMAIN_ENTITY_ID
left outer join aqua_ous aqua 
on aqua.OBSUNITSETUID = ouss.OBS_PROJECT_ID
and aqua.OBSUNITSETPARTID = ouss.DOMAIN_ENTITY_ID;

-- EXAMPLE QUERY

select * from piv_obs_unit_set
where obs_project_id = 'uid://A001/Xa0/Xeb9';


-- ----------------------------------------------
-- SchedBlock: some info plus the status
-- ----------------------------------------------

create or replace view piv_sched_block
as
select sbs.obs_project_id,
       sbs.status_entity_id,
       sbs.parent_obs_unit_set_status_id,
       sbs.domain_entity_state,
       sbs.flags,
       sbs.domain_entity_id as sched_block_uid,
       sb.sb_name,
       sb.totalestimatedtime,
       sb.totalestimatedtimeunits,
       sb.ref_ra,
       sb.ref_dec,
       sb.execution_count,
       aous.qa2status,
       sb.nominalconfiguration
from sched_block_status sbs
left outer join bmmv_schedblock sb 
on sb.archive_uid = sbs.domain_entity_id
left outer join obs_unit_set_status ousst
on ousst.status_entity_id = sbs.parent_obs_unit_set_status_id
left outer join aqua_ous aous
on aous.obsunitsetuid = ousst.obs_project_id
and aous.obsunitsetpartid = ousst.domain_entity_id;

-- EXAMPLE QUERY

select * from piv_sched_block
where sched_block_uid = 'uid://A001/X6f/X3c';

-- ----------------------------------------------
-- Proposal Authors: full name
-- ----------------------------------------------

create or replace view piv_proposal_authors
    (
        ACCOUNT_ID,
        FIRSTNAME,
        LASTNAME,
        INITIALS,
        SEQUENCE,
        AUTHOR_TYPE,
        PROJECT_UID
    ) AS
SELECT
    ACCOUNT.ACCOUNT_ID,
    ACCOUNT.FIRSTNAME,
    ACCOUNT.LASTNAME,
    account.initials,
    BMMV_OBSPROPOSAL_AUTHORS.ID,
    BMMV_OBSPROPOSAL_AUTHORS.AUTHTYPE,
    BMMV_OBSPROPOSAL.PROJECTUID AS project_uid
FROM
    ACCOUNT,
    BMMV_OBSPROPOSAL_AUTHORS,
    BMMV_OBSPROPOSAL
WHERE
    BMMV_OBSPROPOSAL_AUTHORS.ARCHIVE_UID = BMMV_OBSPROPOSAL.ARCHIVE_UID
AND BMMV_OBSPROPOSAL_AUTHORS.USERID = ACCOUNT.ACCOUNT_ID
ORDER BY
    BMMV_OBSPROPOSAL_AUTHORS.AUTHTYPE, ACCOUNT.LASTNAME
    ;    
-- EXAMPLE QUERY

select * from piv_proposal_authors
where project_uid = 'uid://A001/Xa0/Xeb9';

-- ----------------------------------------------
-- ObsProposal
-- ----------------------------------------------

CREATE OR REPLACE VIEW 
    PIV_OBS_PROPOSALS
    (
        CODE,
        TITLE,
        CREATION_DATE,
        PRIORITY_FLAG,
        ASSOCIATEDEXEC,
        SCIENTIFIC_CATEGORY,
        SCIENTIFIC_CATEGORY_STRING,
        ABSTRACT_TEXT,
        CYCLE,
        PROPOSAL_TYPE,
        PROJECTUID,
        ARCHIVEUID,
        CONSENSUS_REPORT,
        CONSENSUS_REPORT1,
        PROJECT_STATE
    ) AS
SELECT
    proj.code,
    proj.title,
    proj.prj_time_of_creation AS creation_date,
    proj.prj_letter_grade     AS priority_flag,
    prop.associatedexec,
    prop.scientific_category,
    extractvalue(xml_prop.xml, '/*:ObsProposal/*:scientificCategoryString') AS
    scientific_category_string,
    prop.abstract_text,
    prop.cycle,
    prop.proposal_type,
    prop.projectuid,
    prop.archive_uid                                              AS ARCHIVEUID,
    ph1m_prop.aprc_comment                                        AS consensus_report,
    extractvalue(xml_proj.xml, '/*:ObsProject/*:consensusReport') AS consensus_report1,
    pstat.domain_entity_state                                     AS project_state
FROM
    bmmv_obsproject proj
INNER JOIN
    bmmv_obsproposal prop
ON
    proj.prj_archive_uid = prop.projectuid
LEFT OUTER JOIN
    proposal ph1m_prop
ON
    prop.archive_uid = ph1m_prop.archive_uid
INNER JOIN
    obs_project_status pstat
ON
    proj.prj_archive_uid = pstat.obs_project_id
INNER JOIN
    XML_OBSPROPOSAL_ENTITIES xml_prop
ON
    xml_prop.archive_uid = prop.archive_uid
INNER JOIN
    XML_OBSPROJECT_ENTITIES xml_proj
ON
    xml_proj.archive_uid = prop.projectuid
    ;
	
-- EXAMPLE QUERY
select * from piv_obs_proposals
where PROJECTUID = 'uid://A001/Xa0/Xeb9';

-- --------------------------------------------------------
-- User Accounts (only active ones)
-- --------------------------------------------------------

CREATE OR REPLACE VIEW piv_account AS
SELECT acc.account_id,
       acc.request_handler_id,
       acc.firstname, 
       acc.lastname,
       acc.initials,
       acc.preferredarc,
       acc.email,
       inst.executive,
       acc.inst_no as INSTNO
FROM account acc, 
     institution inst
WHERE acc.inst_no = inst.inst_no
AND   acc.active = 'T';

-- EXAMPLE QUERY
select * from piv_account
where account_id = 'lricci';

-- -----------------------------------------------------
-- Sub-view of piv_science_goal, should not be used 
-- by itself.
-- Identifies Science Goal OUSs from the SchedBlock's
-- entity UID, going through the Member OUS and the
-- Group OUS.
-- -----------------------------------------------------
CREATE OR REPLACE VIEW piv_science_goal_ous_subview AS
SELECT sg_ouss.status_entity_id AS ous_status_uid,
       sg_ouss.obs_project_id   AS project_uid,
       sg_ouss.domain_entity_id AS ous_part_id,
       sbs.domain_entity_id     AS sb_uid
FROM  obs_unit_set_status sg_ouss,
      sched_block_status sbs
WHERE    sg_ouss.status_entity_id = (
SELECT gr_ouss.parent_obs_unit_set_status_id 
        FROM   obs_unit_set_status gr_ouss
        WHERE  gr_ouss.status_entity_id = (
                SELECT mb_ouss.parent_obs_unit_set_status_id 
                FROM   obs_unit_set_status mb_ouss
                WHERE  mb_ouss.status_entity_id = (
                        SELECT parent_obs_unit_set_status_id
                        FROM   sched_block_status 
                        WHERE  sched_block_status.domain_entity_id = sbs.domain_entity_id
                        )
                )
        );


-- Example query, should return 
--   uid://A001/X10f/X1b2
--   uid://A001/X196/X91
--   X644753181
select project_uid, ous_status_uid, ous_part_id 
from piv_science_goal_ous_subview
where sb_uid = 'uid://A001/X196/X8c';

-- -----------------------------------------------------
-- Sub-view of piv_science_goal, should not be used 
-- by itself.
-- Searches inside an ObsProject's XML text.
-- -----------------------------------------------------
CREATE OR REPLACE VIEW piv_science_goal_subview AS
SELECT xml_proj.archive_uid                     AS project_uid,
       extractValue( xml_proj.xml, '//*:code' ) AS project_code,
       science_goal_fields.*
FROM XML_OBSPROJECT_ENTITIES xml_proj, 
     XMLTABLE ( 
         '//*:ScienceGoal' 
         PASSING xml
         COLUMNS science_goal_name varchar2(256) PATH '*:name',
                 ous_part_id varchar2(256) PATH '*:ObsUnitSetRef/@partId'
         ) science_goal_fields;

-- Example query, should return "para-H2CO, CO, and continuum map of W51"
--   THIS IS A SLOW QUERY, WILL SCAN THE ENTIRE TABLE
select science_goal_name from piv_science_goal_subview where project_code = '2013.1.00308.S';

-- Example query, should return:
-- PROJECT_UID             PROJECT_CODE    SCIENCE_GOAL_NAME  OUS_PART_ID 
-- uid://A002/X6f9b0f/X1e  2012.A.00033.S  HCN, CO, CS        X904740052   
-- uid://A002/X6f9b0f/X1e  2012.A.00033.S  HNC, H2CO, CH3OH   X2050641065  
select * from piv_science_goal_subview where project_uid = 'uid://A002/X6f9b0f/X1e';

-- -----------------------------------------------------
-- Extract Science Goal information from the ObsProject
-- and the Science Goal OUS
-- NOTE: this view ONLY works for its original use case,
--       that is, retrieving Science Goal information
--       by SchedBlock UID
-- -----------------------------------------------------
CREATE OR REPLACE VIEW piv_science_goal AS
SELECT sg.project_uid,
       sg.project_code,
       sg.science_goal_name,
       sg.ous_part_id,
       ous.ous_status_uid,
       ous.sb_uid
FROM            piv_science_goal_subview     sg
LEFT OUTER JOIN piv_science_goal_ous_subview ous 
    ON ( sg.ous_part_id = ous.ous_part_id AND sg.project_uid = ous.project_uid);

-- Example query, should return 
--    CH3CN 5-4 & isotopologue, H2CS 3-2, HCO+ 1-0, HCN 1-0, HNC 1-0 map
select science_goal_name from piv_science_goal where sb_uid = 'uid://A001/X147/X90';



-- -----------------------------------------------------
-- Return the latest state changes for SBs, OUSs
-- and ObsProjects
-- -----------------------------------------------------
CREATE OR REPLACE VIEW piv_state_changes AS
SELECT
    s.STATE_CHANGES_ID as STATE_CHANGES_ID,
    s.STATUS_ENTITY_ID as STATUS_ENTITY_ID,
    s.DOMAIN_ENTITY_ID as DOMAIN_ENTITY_ID,
    s.DOMAIN_ENTITY_STATE as DOMAIN_ENTITY_STATE,
    s.TIMESTAMP as TIMESTAMP,
    s.ENTITY_TYPE as ENTITY_TYPE,
    s.DOMAIN_PART_ID as DOMAIN_PART_ID,
    sbs.domain_entity_id as CHILD_SB_UID
FROM
    STATE_CHANGES s
LEFT OUTER JOIN sched_block_status sbs 
ON (s.STATUS_ENTITY_ID = sbs.parent_obs_unit_set_status_id)
WHERE
    (
        s.TIMESTAMP =
        (
            SELECT
                MAX(ss.TIMESTAMP)
            FROM
                STATE_CHANGES ss
            WHERE
                ss.DOMAIN_ENTITY_ID=s.DOMAIN_ENTITY_ID ));

-- Example query, should return the list of states  
select DOMAIN_ENTITY_STATE from piv_state_changes where DOMAIN_ENTITY_ID = 'uid://A001/X147/X90';


-- -----------------------------------------------------
-- ExecBlock: some info plus the status
-- -----------------------------------------------------
CREATE OR REPLACE VIEW PIV_EXECBLOCK AS
SELECT
    a.execBlockUID,
    a.qa0Status,
    a.starttime                 AS aquastartTime,
    a.endtime		            AS aquaendTime,
    en.se_start                 AS startTime,
    en.se_timestamp             AS endTime,
    en.se_sb_id                 AS schedBlockUid,
    en.se_obsProjectPI          AS obsProjectPi,
    en.se_reprFrequency         AS representativeFrequency,
    sb.prj_ref                  AS obsProjectUid
FROM
    aqua_execblock a
LEFT OUTER JOIN
    aqua_session se
ON
    se.sessionId = a.sessionId
LEFT OUTER JOIN
    shiftlog_entries en
ON
    en.se_eb_uid = a.execBlockUid
LEFT OUTER JOIN
    mv_schedblock sb
ON
    sb.sb_archive_uid = en.se_sb_id;

-- EXAMPLE QUERY

select * from PIV_EXECBLOCK
where execBlockUID = 'uid://A002/X899169/Xbec';
    