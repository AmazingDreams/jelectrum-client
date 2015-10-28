package nl.amazingsystems.electrum.common.utils;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;

public class ByteBufUtils {

    public static String byteBufToString(ByteBuf byteBuf) {
	byte[] bytes;
	if (byteBuf.hasArray()) {
	    bytes = byteBuf.array();
	} else {
	    bytes = new byte[byteBuf.readableBytes()];
	    byteBuf.getBytes(0, bytes);
	}

	return new String(bytes, Charset.forName("UTF-8"));
    }

}
