package encode.audio.entrypoint;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import flux.AnnonceAudioTmlg;
import flux.IAnnonceAudioTmlg;

public class XpathTest {

	@Test
	public void test() throws XPathExpressionException, Exception {
		InputStream stream = getClass().getClassLoader().getResourceAsStream("ogive2pscsAudio_with_transformed_audio.xml");
		String fileContent = new Scanner(stream).useDelimiter("\\A").next();
		assertNotNull(stream);
		
		IAnnonceAudioTmlg annonceAudio = readXml(fileContent);
		assertThat(annonceAudio.getFileName()).startsWith("10.151.156.180Mon_Nov_04_140724_CET_2013343.");
		assertThat(annonceAudio.getUrl()).isEqualTo("http://localhost:12306/10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3");
		assertThat(annonceAudio.getFormat()).isEqualTo("mp3");
	}

	private static IAnnonceAudioTmlg readXml(String fileContent)
			throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException {
		return new AnnonceAudioTmlg(fileContent);
	}

}
