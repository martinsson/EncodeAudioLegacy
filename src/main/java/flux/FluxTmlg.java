package flux;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

public class FluxTmlg implements IFluxTmlg {

	private IBodyTmlg body;

	public FluxTmlg(String message) throws ObixTmlgExeption {

		try {
			final IAnnonceAudioTmlg annonceAudio = new AnnonceAudioTmlg(message);
			final IInfoVoyageurTmlg infoVoyageur = new IInfoVoyageurTmlg() {

				public IAnnonceAudioTmlg getAnnonceAudio() {
					return annonceAudio;
				}
			};
			body = new IBodyTmlg() {

				public IInfoVoyageurTmlg getInfoVoyageur() {
					return infoVoyageur;
				}
				@Override
				public String toString() {
					return annonceAudio.toString();
				}
			};
		} catch (Exception e) {
			ObixTmlgExeption oe = new ObixTmlgExeption();
			oe.initCause(e);
			throw oe;
		}
	}

	public IBodyTmlg getBody() {
		return body;
	}

	@Override
	public String toString() {
		return "FluxTmlg [body=" + body + "]";
	}


}
