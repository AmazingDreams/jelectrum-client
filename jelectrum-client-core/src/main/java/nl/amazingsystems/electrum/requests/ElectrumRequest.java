package nl.amazingsystems.electrum.requests;

import nl.amazingsystems.electrum.responses.AbstractElectrumResponse;

public interface ElectrumRequest {

	public long getId();

	public Class<? extends AbstractElectrumResponse> getResponseClass();

	public void setId(long id);

}