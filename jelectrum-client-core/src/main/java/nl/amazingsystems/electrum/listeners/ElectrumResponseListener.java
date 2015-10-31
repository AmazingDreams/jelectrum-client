package nl.amazingsystems.electrum.listeners;

import nl.amazingsystems.electrum.responses.AbstractElectrumResponse;

public interface ElectrumResponseListener {

	public boolean onMessageReceived(AbstractElectrumResponse message);

}
