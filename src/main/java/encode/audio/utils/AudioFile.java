package encode.audio.utils;

/**
 * Classe regroupant les informations n�cessaires pour le traitement d'un fichier audio
 * 
 * @author FJU07292
 */
public class AudioFile {

	
	private String name;
	private String format;
	private byte[] binary;
	
	//========================================
	// Constructeurs
	
	public AudioFile(String fileName, String format){
		this.name = fileName;
		this.format = format;
		this.binary = null; 
	}

	//========================================
	// Getters/Setters
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public byte[] getBinary() {
		return binary;
	}
	public void setBinary(byte[] binary) {
		this.binary = binary;
	}
	
	
}
