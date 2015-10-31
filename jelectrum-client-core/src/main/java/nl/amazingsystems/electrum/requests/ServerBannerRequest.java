package nl.amazingsystems.electrum.requests;

import nl.amazingsystems.electrum.responses.AbstractElectrumResponse;
import nl.amazingsystems.electrum.responses.ServerBannerResponse;

public class ServerBannerRequest extends AbstractElectrumRequest {

	public ServerBannerRequest() {
		super("server.banner");
	}

	@Override
	public Class<? extends AbstractElectrumResponse> getResponseClass() {
		return ServerBannerResponse.class;
	}

}
