package nl.amazingsystems.electrum.requests;

import nl.amazingsystems.electrum.responses.AbstractElectrumResponse;
import nl.amazingsystems.electrum.responses.ServerDonationAddressResponse;

public class ServerDonationAddressRequest extends AbstractElectrumRequest {

	public ServerDonationAddressRequest() {
		super("server.donation_address");
	}

	@Override
	public Class<? extends AbstractElectrumResponse> getResponseClass() {
		return ServerDonationAddressResponse.class;
	}

}
