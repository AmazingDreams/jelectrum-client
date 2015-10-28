package nl.amazingsystems.electrum.requests;

import org.junit.Assert;
import org.junit.Test;

public class GetServerVersionRequestTest {

    @Test
    public void testGetServerVersionRequest() {
	GetServerVersionRequest request = new GetServerVersionRequest();

	Assert.assertEquals("server.version", request.getMethod());

	Assert.assertEquals("{\"id\":0,\"method\":\"server.version\",\"params\":[]}", request.toString());

	System.out.println(request.toString());
    }

}
