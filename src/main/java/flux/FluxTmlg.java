package flux;

public class FluxTmlg implements IFluxTmlg {

	private IBodyTmlg body;

	public FluxTmlg(AudioAnnounceTmlg audioAnnounceTmlg) {

			final IAudioAnnounceTmlg audioAnnounce = new AudioAnnounceTmlg(audioAnnounceTmlg);
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
	}

	public IBodyTmlg getBody() {
		return body;
	}

	@Override
	public String toString() {
		return "FluxTmlg [body=" + body + "]";
	}
}