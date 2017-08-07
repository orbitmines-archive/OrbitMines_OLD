package com.vexsoftware.votifier.net.protocol;

import com.vexsoftware.votifier.VotifierPlugin;
import com.vexsoftware.votifier.net.VotifierSession;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class VotifierGreetingHandler extends ChannelInboundHandlerAdapter {

    public VotifierGreetingHandler() {}

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        VotifierSession session = ctx.channel().attr(VotifierSession.KEY).get();
        VotifierPlugin plugin = ctx.channel().attr(VotifierPlugin.KEY).get();

        String version = "VOTIFIER " + plugin.getVersion() + " " + session.getChallenge() + "\n";
        ByteBuf versionBuf = Unpooled.copiedBuffer(version, StandardCharsets.UTF_8);
        ctx.writeAndFlush(versionBuf);
    }
}
