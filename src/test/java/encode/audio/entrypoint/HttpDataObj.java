package encode.audio.entrypoint;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.management.RuntimeErrorException;

import encode.audio.entrypoint.DataObject;

public class HttpDataObj implements DataObject {
	private final String tmpdir;
	private String urlEmbarqueGet;

	HttpDataObj(String tmpdir, String urlEmbarqueGet) {
		this.tmpdir = tmpdir;
		this.urlEmbarqueGet = urlEmbarqueGet;
	}

	public String getString(String key) {
		// TODO Auto-generated method stub
		if (key.equals("url_embarque_server_get"))
			return urlEmbarqueGet;
		else if (key.equals("audio_temp_path"))
			return tmpdir;
		else
//			return tmpdir;
			throw new RuntimeException("didnt configure " + key);
	}

	public boolean getBoolean(String key) {
		// TODO Auto-generated method stub
		return false;
	}

}