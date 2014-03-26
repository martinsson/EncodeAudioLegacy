package flux;

public class FluxTmlg implements IFluxTmlg {

	private IBodyTmlg body;

	public FluxTmlg(String message) throws ObixTmlgExeption {

		try {
			final IAudioAnnounceTmlg audioAnnounce = new AudioAnnounceTmlg(message);
			final ITravelInfoTmlg travelInfo = new ITravelInfoTmlg() {

				public IAudioAnnounceTmlg getAudioAnnounce() {
					return audioAnnounce;
				}
			};
			body = new IBodyTmlg() {

				public ITravelInfoTmlg getTravelInfo() {
					return travelInfo;
				}

				@Override
				public String toString() {
					return audioAnnounce.toString();
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