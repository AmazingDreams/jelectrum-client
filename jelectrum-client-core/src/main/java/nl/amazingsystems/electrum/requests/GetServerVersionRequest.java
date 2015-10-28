package nl.amazingsystems.electrum.requests;

import nl.amazingsystems.electrum.responses.AbstractElectrumResponse;
import nl.amazingsystems.electrum.responses.GetServerVersionResponse;

public class GetServerVersionRequest extends AbstractElectrumRequest {

    public GetServerVersionRequest() {
	super("server.version");
    }

    @Override
    public Class<? extends AbstractElectrumResponse> getResponseClass() {
	return GetServerVersionResponse.class;
    }

}
