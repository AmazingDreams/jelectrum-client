package nl.amazingsystems.electrum.clients;

import nl.amazingsystems.electrum.listeners.ElectrumResponseListener;
import nl.amazingsystems.electrum.requests.AbstractElectrumRequest;

public interface ElectrumClient {

	public void addResponseListener(final ElectrumResponseListener listener);

	public void sendRequest(final AbstractElectrumRequest request,
			final ElectrumResponseListener listener);

}
