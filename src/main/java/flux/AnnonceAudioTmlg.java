package flux;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public final class AnnonceAudioTmlg implements IAnnonceAudioTmlg {
	private String url;
	private String format;
	private String fileName;

	public AnnonceAudioTmlg(String message) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(new ByteArrayInputStream(message.getBytes()) );
		XPathFactory newInstance = XPathFactory.newInstance();
		XPath xPath = newInstance.newXPath();
		Node annonceNode = (Node) xPath.evaluate("//obj[@name='annonceAudio']", document, XPathConstants.NODE);
		fileName = getValOffAttribute(xPath, annonceNode, "fileName");
		url = getValOffAttribute(xPath, annonceNode, "url");
		format = getValOffAttribute(xPath, annonceNode, "format");
	}

	private static String getValOffAttribute(XPath xPath, Node annonceNode,
			String attribute) throws XPathExpressionException {
		return xPath.evaluate("*[@name='ATTR']/@val".replace("ATTR", attribute), annonceNode);
	}


	public void setUrl(String string) {
		url = string;

	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setFileName(String name) {
		fileName = name;
	}

	public String getUrl() {
		return url;
	}

	public String getFileName() {
		return fileName;
	}

	@Override
	public String toString() {
		return "AnnonceAudioTmlg [url=" + url + ", format=" + format
				+ ", fileName=" + fileName + "]\n";
	}

	public String getFormat() {
		return format;
	}


}