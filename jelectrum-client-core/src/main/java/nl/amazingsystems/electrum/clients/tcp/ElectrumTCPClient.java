package nl.amazingsystems.electrum.clients.tcp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.google.gson.Gson;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import nl.amazingsystems.electrum.clients.ElectrumClient;
import nl.amazingsystems.electrum.common.utils.ByteBufUtils;
import nl.amazingsystems.electrum.listeners.ElectrumResponseListener;
import nl.amazingsystems.electrum.requests.ElectrumRequest;
import nl.amazingsystems.electrum.requests.SubscribeRequest;
import nl.amazingsystems.electrum.responses.AbstractElectrumResponse;
import nl.amazingsystems.electrum.responses.ElectrumResponse;

public class ElectrumTCPClient implements ElectrumClient {

	private class BasicElectrumResponse extends AbstractElectrumResponse {
		@Override
		public Object getResult() {
			return null;
		}
	}

	private class ChannelHandlerAdapter extends ChannelInboundHandlerAdapter {

		private Gson gson = new Gson();

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object message) {
			// Convert the raw message to a string
			ByteBuf byteBuf = (ByteBuf) message;
			String msgStr = ByteBufUtils.byteBufToString(byteBuf);

			// Get the original request
			BasicElectrumResponse baseResponse = this.gson.fromJson(msgStr,
					BasicElectrumResponse.class);
			ElectrumRequest originalRequest = originalRequests
					.get(baseResponse.getId());

			// Clean up the map if we don't need the original request anymore
			if (!(originalRequest instanceof SubscribeRequest)) {
				originalRequests.remove(baseResponse.getId());
			}

			// Determine the actual response class
			Class<? extends AbstractElectrumResponse> responseClass = originalRequest
					.getResponseClass();

			// Let Gson convert the message to the actual response class
			ElectrumResponse actualResponse = this.gson.fromJson(msgStr,
					responseClass);

			handleMessageReceived(actualResponse);
		}
	}

	private class ElectrumChannelInitializer
			extends
				ChannelInitializer<Channel> {

		@Override
		protected void initChannel(Channel ch) throws Exception {
			ch.pipeline().addLast(new ChannelHandlerAdapter());
		}
	}

	private final Channel channel;

	private final AtomicLong idCounter;

	private final Map<Long, ElectrumRequest> originalRequests;

	private final List<ElectrumResponseListener> responseListeners;

	private final EventLoopGroup workerGroup;

	public ElectrumTCPClient(String host, int port)
			throws InterruptedException {
		workerGroup = new NioEventLoopGroup();

		Bootstrap bootstrap = new Bootstrap().group(workerGroup)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new ElectrumChannelInitializer());

		// Create connection and wait for it
		channel = bootstrap.connect(host, port).sync().channel();

		this.idCounter = new AtomicLong();
		this.responseListeners = new ArrayList<ElectrumResponseListener>();
		this.originalRequests = new HashMap<Long, ElectrumRequest>();
	}

	@Override
	public void addResponseListener(ElectrumResponseListener listener) {
		synchronized (this.responseListeners) {
			this.responseListeners.add(listener);
		}
	}

	public void close() throws InterruptedException {
		// Close the client and wait for it to shut down
		this.workerGroup.shutdownGracefully().sync();
	}

	private void handleMessageReceived(ElectrumResponse message) {
		synchronized (responseListeners) {
			Iterator<ElectrumResponseListener> iterator = this.responseListeners
					.iterator();

			while (iterator.hasNext()) {
				boolean remove = iterator.next().onMessageReceived(message);

				if (remove) {
					iterator.remove();
				}
			}
		}
	}

	public void sendRequest(final ElectrumRequest request,
			final ElectrumResponseListener listener) {
		request.setId(this.idCounter.incrementAndGet());

		String messageStr = request.toString() + "\n";
		ByteBuf message = Unpooled.copiedBuffer(messageStr.getBytes());

		this.responseListeners.add(listener);
		this.originalRequests.put(request.getId(), request);
		this.channel.writeAndFlush(message);
	}
}