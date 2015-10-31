package nl.amazingsystems.electrum.listeners;

import nl.amazingsystems.electrum.responses.ElectrumResponse;

public interface ElectrumResponseListener {

	public boolean onMessageReceived(ElectrumResponse message);

}
