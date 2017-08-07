package com.vexsoftware.votifier.bungee.forwarding.proxy;

import com.vexsoftware.votifier.bungee.forwarding.ForwardingVoteSource;
import com.vexsoftware.votifier.bungee.forwarding.cache.VoteCache;
import com.vexsoftware.votifier.bungee.forwarding.proxy.client.VotifierProtocol2Encoder;
import com.vexsoftware.votifier.bungee.forwarding.proxy.client.VotifierProtocol2HandshakeHandler;
import com.vexsoftware.votifier.bungee.forwarding.proxy.client.VotifierResponseHandler;
import com.vexsoftware.votifier.model.Vote;
import com.orbitmines.bungee.OrbitMinesBungee;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class ProxyForwardingVoteSource implements ForwardingVoteSource {

    private static final int MAX_RETRIES = 5;
    private final OrbitMinesBungee plugin;
    private final NioEventLoopGroup eventLoopGroup;
    private final List<BackendServer> backendServers;
    private final VoteCache voteCache;
    private static final StringDecoder STRING_DECODER = new StringDecoder(StandardCharsets.US_ASCII);

    public ProxyForwardingVoteSource(OrbitMinesBungee plugin, NioEventLoopGroup eventLoopGroup, List<BackendServer> backendServers, VoteCache voteCache) {
        this.plugin = plugin;
        this.eventLoopGroup = eventLoopGroup;
        this.backendServers = backendServers;
        this.voteCache = voteCache;
    }

    public void forward(Vote v) {
        for (BackendServer server : backendServers) {
            forwardVote(server, v, 0);
        }
    }

    private int fib(int t) {
        if (t <= 1)
            return 1;
        return fib(t - 2) + fib(t - 1);
    }

    private void forwardVote(final BackendServer server, final Vote v, final int tries) {
        ((new Bootstrap().channel(NioSocketChannel.class)).group(eventLoopGroup)).handler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new DelimiterBasedFrameDecoder(256, true, Delimiters.lineDelimiter()));
                //TODO channel.pipeline().addLast(new ReadTimeoutHandler(8L, TimeUnit.SECONDS));
                channel.pipeline().addLast(ProxyForwardingVoteSource.STRING_DECODER);
                channel.pipeline().addLast(new VotifierProtocol2Encoder(server.key));
                channel.pipeline().addLast(new VotifierProtocol2HandshakeHandler(v, new VotifierResponseHandler() {

                    public void onSuccess() {
                        if (plugin.isDebug()) {
                            plugin.getLogger().info("Successfully forwarded vote " + v + " to " + server.address + ".");
                        }
                    }

                    public void onFailure(Throwable error) {
                        ProxyForwardingVoteSource.this.handleFailure(server, v, error, tries);
                    }
                }, plugin));
            }
        }).connect(server.address).addListener((ChannelFuture future) -> {
            if (!future.isSuccess()) {
                ProxyForwardingVoteSource.this.handleFailure(server, v, future.cause(), tries);
            }
        });
    }

    private void handleFailure(final BackendServer server, final Vote v, Throwable cause, final int tries) {
        int nextDelay = fib(tries + 1);
        boolean willRetry = tries < 5;

        String msg = "Unable to send vote to " + server.address + ".";
        if (willRetry)
            msg = msg + " Will retry sending in " + nextDelay + " second(s).";
        else if (voteCache == null)
            msg = msg + " This vote will be lost!";
        else
            voteCache.addToCache(v, server.name);


        if (plugin.isDebug())
            plugin.getLogger().log(Level.SEVERE, msg, cause);
        else
            plugin.getLogger().log(Level.SEVERE, msg);

        if (willRetry)
            plugin.getProxy().getScheduler().schedule(plugin, () -> ProxyForwardingVoteSource.this.forwardVote(server, v, tries + 1), nextDelay, TimeUnit.SECONDS);
    }

    public void halt() {}

    public static class BackendServer {
        private final String name;

        private final InetSocketAddress address;
        private final Key key;

        public BackendServer(String name, InetSocketAddress address, Key key) {
            this.name = name;
            this.address = address;
            this.key = key;
        }
    }
}
