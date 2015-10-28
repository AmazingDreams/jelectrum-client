package nl.amazingsystems.electrum.clients;

import java.util.concurrent.Future;

import nl.amazingsystems.electrum.requests.AbstractElectrumRequest;
import nl.amazingsystems.electrum.responses.AbstractElectrumResponse;

public interface ElectrumClient {

    public Future<? extends AbstractElectrumResponse> sendRequest(final AbstractElectrumRequest request);

}
