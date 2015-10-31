package nl.amazingsystems.electrum.clients;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
import nl.amazingsystems.electrum.common.utils.ByteBufUtils;
import nl.amazingsystems.electrum.requests.AbstractElectrumRequest;
import nl.amazingsystems.electrum.responses.AbstractElectrumResponse;

public class ElectrumTCPClient implements ElectrumClient {

	private final Channel channel;

	private final EventLoopGroup workerGroup;

	private final ExecutorService executor;

	private final AtomicLong idCounter;

	private final Map<Long, AbstractElectrumRequest> outGoingRequests;

	private final Map<Long, AbstractElectrumResponse> incomingResponses;

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
		this.outGoingRequests = new HashMap<>();
		this.incomingResponses = new HashMap<>();
		this.executor = Executors.newFixedThreadPool(2);

	}

	public void close() throws InterruptedException {
		// Close the client and wait for it to shut down
		this.workerGroup.shutdownGracefully().sync();
	}

	public Future<? extends AbstractElectrumResponse> sendRequest(
			final AbstractElectrumRequest request) {
		request.setId(this.idCounter.incrementAndGet());
		this.outGoingRequests.put(request.getId(), request);

		String messageStr = request.toString() + "\n";
		ByteBuf message = Unpooled.copiedBuffer(messageStr.getBytes());

		this.channel.writeAndFlush(message);

		Callable<AbstractElectrumResponse> responseCallable = new Callable<AbstractElectrumResponse>() {

			@Override
			public AbstractElectrumResponse call() throws Exception {
				Long requestId = new Long(request.getId());
				while (!incomingResponses.containsKey(requestId)) {
					Thread.sleep(1);
				}

				return incomingResponses.get(request.getId());
			}
		};

		return executor.submit(responseCallable);
	}

	private class ElectrumChannelInitializer
			extends
				ChannelInitializer<Channel> {

		@Override
		protected void initChannel(Channel ch) throws Exception {
			ch.pipeline().addLast(new ChannelHandlerAdapter());
		}

	}

	private class ChannelHandlerAdapter extends ChannelInboundHandlerAdapter {

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object message) {
			ByteBuf byteBuf = (ByteBuf) message;

			String msgStr = ByteBufUtils.byteBufToString(byteBuf);
			Class<? extends AbstractElectrumResponse> responseClass = this
					.determineActualResponseClass(msgStr);

			Gson gson = new Gson();
			AbstractElectrumResponse actualResponse = gson.fromJson(msgStr,
					responseClass);

			incomingResponses.put(actualResponse.getId(), actualResponse);
		}

		private Class<? extends AbstractElectrumResponse> determineActualResponseClass(
				String message) {
			Gson gson = new Gson();

			BasicElectrumResponse baseResponse = gson.fromJson(message,
					BasicElectrumResponse.class);
			long requestId = baseResponse.getId();
			AbstractElectrumRequest originalRequest = outGoingRequests
					.get(requestId);

			return originalRequest.getResponseClass();
		}
	}

	private class BasicElectrumResponse extends AbstractElectrumResponse {
	}
}