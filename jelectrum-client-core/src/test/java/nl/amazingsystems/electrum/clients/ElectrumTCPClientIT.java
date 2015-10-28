package nl.amazingsystems.electrum.clients;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.amazingsystems.electrum.requests.GetServerVersionRequest;
import nl.amazingsystems.electrum.responses.GetServerVersionResponse;

public class ElectrumTCPClientIT {

    private ElectrumTCPClient client;

    @Before
    public void setUp() throws Exception {
	this.client = new ElectrumTCPClient("electrum2.egulden.org", 5037);
    }

    @Test
    public void testGetServerVersion() throws Exception {
	GetServerVersionRequest request = new GetServerVersionRequest();
	GetServerVersionResponse response = (GetServerVersionResponse) this.client.sendRequest(request).get();

	Assert.assertEquals("1.0", response.getResult());
    }

    @After
    public void tearDown() throws Exception {
	this.client.close();
    }
}
