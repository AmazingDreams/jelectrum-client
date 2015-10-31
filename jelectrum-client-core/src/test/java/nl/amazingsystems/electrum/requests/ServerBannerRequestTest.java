package nl.amazingsystems.electrum.requests;

import org.junit.Assert;
import org.junit.Test;

public class ServerBannerRequestTest {

	@Test
	public void testServerBannerRequest() {
		ServerBannerRequest request = new ServerBannerRequest();

		Assert.assertEquals("server.banner", request.getMethod());
		Assert.assertEquals(
				"{\"id\":0,\"method\":\"server.banner\",\"params\":[]}",
				request.toString());
	}

}
