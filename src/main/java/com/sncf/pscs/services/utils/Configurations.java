package com.sncf.pscs.services.utils;

public interface Configurations {
	
	// Nom du fichier de configuration pour log4j.
	public final String LOG4J_FILENAME = "pscsEsb-log4j.xml";
	
	// Connection factory pour faire de la publication.
	public static final String MOM_PUB_CONNECTION_FACTORY_NAME = "jms/com/sncf/pscs/topicFactory";
	
	public static final String FABRIQUE_DE_CONNECTIONS = "jms/com/sncf/pscs/factoryPool";
	public static final String FILE_DESTINATION = "jms/com/sncf/pscs/failedQueue";
	
	public static final String messageFormatFaultName = "messageFormatFault"; 
	public static final String ERREUR_JMS_STACK = "probl�me avec la pile JMS. ";
	public static final String ERREUR_JMS_NAMING = "probl�me avec l'acces aux ressources JMS. ";
	public static final String ERREUR_JMS_FORMAT = "Format du message JMS  non reconnu : \n";
	public static final String ERREUR_COM_JMS = "Erreur de communication avec la file de sauvegarde ";
	
	// type enum pour distinguer le type de files d'attentes 
	public enum typeDestination {
		INFOVOYAGEUR,
		MAINTENANCE		
	}

	
	/** 
	 * Divers
	 */
	public static final String BLANK = "";
	public static final String SEPARATOR = ";";
	
	
	/**
	 * Nom des champs des tables de configuration de la BDD
	 */
	public static final String ENCODING_ACTIVATE = "encoding_activate";
	public static final String PATH_PROGRAM_EXE = "path_program_exe";
	public static final String FINAL_AUDIO_FILE_EXTENSION = "final_audio_file_extension";
	public static final String ENCODING_BITRATE = "encoding_bitrate";
	public static final String ENCODING_VBR_QUALITY = "encoding_vbr_quality";
	public static final String PATH_UPLOAD_CCUE_FILE = "path_upload_ccue_file";
	public static final String AUDIO_TEMP_PATH = "audio_temp_path";
	public static final String ANNONCE_AUDIO_TEST_FILE = "annonce_audio_test_file";
	public static final String MESSAGE_ANNONCE_AUDIO_TEST = "message_annonce_audio_test";
	public static final String URL_LOCAL_SERVER_POST = "url_local_server_post";
	public static final String URL_LOCAL_SERVER_GET = "url_local_server_get";
	public static final String URL_EMBARQUE_SERVER_GET = "url_embarque_server_get";
	public static final String URL_EMBARQUE_SERVER_POST = "url_embarque_server_post";
	public static final String SSL_TRUST_STORE_PATH = "ssl_trust_store_path";
	public static final String SSL_TRUST_STORE_PASSWD = "ssl_trust_store_passwd";
	
	
}
