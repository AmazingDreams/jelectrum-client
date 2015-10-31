package nl.amazingsystems.electrum.requests;

import nl.amazingsystems.electrum.responses.AbstractElectrumResponse;
import nl.amazingsystems.electrum.responses.GetServerVersionResponse;

public class ServerVersionRequest extends AbstractElectrumRequest {

    public ServerVersionRequest() {
	super("server.version");
    }

    @Override
    public Class<? extends AbstractElectrumResponse> getResponseClass() {
	return GetServerVersionResponse.class;
    }

}
