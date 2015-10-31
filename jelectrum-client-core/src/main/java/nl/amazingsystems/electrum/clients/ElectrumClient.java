package nl.amazingsystems.electrum.clients;

import nl.amazingsystems.electrum.listeners.ElectrumResponseListener;
import nl.amazingsystems.electrum.requests.ElectrumRequest;

public interface ElectrumClient {

	public void addResponseListener(final ElectrumResponseListener listener);

	public void sendRequest(final ElectrumRequest request,
			final ElectrumResponseListener listener);

}
