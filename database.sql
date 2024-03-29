-- DROP DATABASE IF EXISTS COPPER_EXAMPLE;
-- DROP TABLE COP_WORKFLOW_INSTANCE_ERROR ;
-- DROP TABLE COP_WORKFLOW_INSTANCE;
-- DROP TABLE COP_WAIT;
-- DROP TABLE COP_RESPONSE;
-- DROP TABLE COP_QUEUE;
-- DROP TABLE COP_AUDIT_TRAIL_EVENT;
-- DROP TABLE COP_ADAPTERCALL;
-- DROP TABLE COP_LOCK;




CREATE DATABASE IF NOT EXISTS COPPER_EXAMPLE;
USE COPPER_EXAMPLE;


--
-- BUSINESSPROCESS
--
CREATE TABLE COP_WORKFLOW_INSTANCE (
  ID VARCHAR (128) NOT NULL,
  STATE TINYINT NOT NULL,
  PRIORITY TINYINT NOT NULL,
  LAST_MOD_TS TIMESTAMP NOT NULL,
  PPOOL_ID VARCHAR (32) NOT NULL,
  DATA MEDIUMTEXT NULL,
  OBJECT_STATE MEDIUMTEXT NULL,
  CS_WAITMODE TINYINT,
  MIN_NUMB_OF_RESP SMALLINT,
  NUMB_OF_WAITS SMALLINT,
  TIMEOUT TIMESTAMP,
  CREATION_TS TIMESTAMP NOT NULL,
  CLASSNAME VARCHAR (512) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE = INNODB DEFAULT CHARSET = UTF8 ;


--
--
--
CREATE TABLE COP_WORKFLOW_INSTANCE_ERROR (
  WORKFLOW_INSTANCE_ID VARCHAR (128) NOT NULL,
  EXCEPTION TEXT NOT NULL,
  ERROR_TS TIMESTAMP NOT NULL
) ENGINE = INNODB DEFAULT CHARSET = UTF8 ;

CREATE INDEX IDX_COP_WFID_WFID 
ON COP_WORKFLOW_INSTANCE_ERROR (WORKFLOW_INSTANCE_ID) ;


--
-- RESPONSE
--
CREATE TABLE COP_RESPONSE (
  RESPONSE_ID VARCHAR (128) NOT NULL,
  CORRELATION_ID VARCHAR (128) NOT NULL,
  RESPONSE_TS TIMESTAMP NOT NULL,
  RESPONSE MEDIUMTEXT,
  RESPONSE_TIMEOUT TIMESTAMP,
  RESPONSE_META_DATA VARCHAR (4000),
  PRIMARY KEY (RESPONSE_ID)
) ENGINE = INNODB DEFAULT CHARSET = UTF8 ;

CREATE INDEX IDX_COP_RESP_CID 
ON COP_RESPONSE (CORRELATION_ID) ;


--
-- WAIT
--
CREATE TABLE COP_WAIT (
  CORRELATION_ID VARCHAR (128) NOT NULL,
  WORKFLOW_INSTANCE_ID VARCHAR (128) NOT NULL,
  MIN_NUMB_OF_RESP SMALLINT NOT NULL,
  TIMEOUT_TS TIMESTAMP NULL,
  STATE TINYINT NOT NULL,
  PRIORITY TINYINT NOT NULL,
  PPOOL_ID VARCHAR (32) NOT NULL,
  PRIMARY KEY (CORRELATION_ID)
) ENGINE = INNODB DEFAULT CHARSET = UTF8 ;

CREATE INDEX IDX_COP_WAIT_WFI_ID 
ON COP_WAIT (WORKFLOW_INSTANCE_ID) ;


--
-- QUEUE
--
CREATE TABLE COP_QUEUE (
  PPOOL_ID VARCHAR (32) NOT NULL,
  PRIORITY TINYINT NOT NULL,
  LAST_MOD_TS TIMESTAMP NOT NULL,
  WORKFLOW_INSTANCE_ID VARCHAR (128) NOT NULL,
  ENGINE_ID VARCHAR (16) NULL,
  PRIMARY KEY (WORKFLOW_INSTANCE_ID)
) ENGINE = INNODB DEFAULT CHARSET = UTF8 ;


--
--
--
CREATE TABLE COP_AUDIT_TRAIL_EVENT (
  SEQ_ID BIGINT NOT NULL AUTO_INCREMENT,
  OCCURRENCE TIMESTAMP NOT NULL,
  CONVERSATION_ID VARCHAR (64) NOT NULL,
  LOGLEVEL TINYINT NOT NULL,
  CONTEXT VARCHAR (128) NOT NULL,
  INSTANCE_ID VARCHAR (128) NULL,
  CORRELATION_ID VARCHAR (128) NULL,
  TRANSACTION_ID VARCHAR (128) NULL,
  LONG_MESSAGE LONGTEXT NULL,
  MESSAGE_TYPE VARCHAR (256) NULL,
  PRIMARY KEY (SEQ_ID)
) ENGINE = INNODB DEFAULT CHARSET = UTF8 ;


--
--
--
CREATE TABLE COP_ADAPTERCALL (
  WORKFLOWID VARCHAR (128) NOT NULL,
  ENTITYID VARCHAR (128) NOT NULL,
  ADAPTERID VARCHAR (128) NOT NULL,
  PRIORITY BIGINT NOT NULL,
  DEFUNCT CHAR(1) DEFAULT '0' NOT NULL,
  DEQUEUE_TS TIMESTAMP,
  METHODDECLARINGCLASS VARCHAR (1024) NOT NULL,
  METHODNAME VARCHAR (1024) NOT NULL,
  METHODSIGNATURE VARCHAR (2048) NOT NULL,
  ARGS LONGTEXT,
  PRIMARY KEY (ADAPTERID, WORKFLOWID, ENTITYID)
) ENGINE = INNODB DEFAULT CHARSET = UTF8 ;

CREATE INDEX COP_IDX_ADAPTERCALL 
ON COP_ADAPTERCALL (ADAPTERID, PRIORITY) ;


--
-- COP_LOCK
--
CREATE TABLE COP_LOCK (
  LOCK_ID VARCHAR (128) NOT NULL,
  CORRELATION_ID VARCHAR (128) NOT NULL,
  WORKFLOW_INSTANCE_ID VARCHAR (128) NOT NULL,
  INSERT_TS TIMESTAMP NOT NULL,
  REPLY_SENT CHAR(1) NOT NULL,
  PRIMARY KEY (LOCK_ID, WORKFLOW_INSTANCE_ID)
) ENGINE = INNODB DEFAULT CHARSET = UTF8 ;
