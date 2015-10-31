package nl.amazingsystems.electrum.clients;

import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.amazingsystems.electrum.clients.tcp.ElectrumTCPClient;
import nl.amazingsystems.electrum.listeners.ElectrumResponseListener;
import nl.amazingsystems.electrum.requests.ServerVersionRequest;
import nl.amazingsystems.electrum.responses.ElectrumResponse;
import nl.amazingsystems.electrum.responses.ServerVersionResponse;

public class ElectrumTCPClientIT {

	private ElectrumTCPClient client;

	@Before
	public void setUp() throws Exception {
		this.client = new ElectrumTCPClient("electrum2.egulden.org", 5037);
	}

	@Test
	public void testGetServerVersion() throws Exception {
		int repeat = 1;

		final CountDownLatch latch = new CountDownLatch(repeat);

		for (int i = 0; i < repeat; i++) {
			ServerVersionRequest request = new ServerVersionRequest();
			this.client.sendRequest(request, new ElectrumResponseListener() {
				@Override
				public boolean onMessageReceived(ElectrumResponse message) {
					if (!(message instanceof ServerVersionResponse)) {
						Assert.fail("Somehow received another message");
					}

					Assert.assertEquals("1.0", message.getResult());
					latch.countDown();

					return true;
				}
			});
		}

		latch.await();
	}

	@After
	public void tearDown() throws Exception {
		this.client.close();
	}
}
