package nl.amazingsystems.electrum.requests;

import org.junit.Assert;
import org.junit.Test;

public class ServerVersionRequestTest {

	@Test
	public void testServerVersionRequest() {
		ServerVersionRequest request = new ServerVersionRequest();

		Assert.assertEquals("server.version", request.getMethod());
		Assert.assertEquals(
				"{\"id\":0,\"method\":\"server.version\",\"params\":[]}",
				request.toString());
	}

}
