package nl.amazingsystems.electrum.requests;

import org.junit.Assert;
import org.junit.Test;

public class ServerDonationAddressRequestTest {

	@Test
	public void testServerDonationAddressRequest() {
		ServerDonationAddressRequest request = new ServerDonationAddressRequest();

		Assert.assertEquals("server.donation_address", request.getMethod());
		Assert.assertEquals(
				"{\"id\":0,\"method\":\"server.donation_address\",\"params\":[]}",
				request.toString());
	}

}
