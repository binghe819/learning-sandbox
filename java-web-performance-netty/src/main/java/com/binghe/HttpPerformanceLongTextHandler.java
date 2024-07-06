package com.binghe;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class HttpPerformanceLongTextHandler extends SimpleChannelInboundHandler<HttpObject> {



    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;

            String contentString = "What is Lorem Ipsum?\n" +
                    "Lorem Ipsum is a type of placeholder text used in the printing and typesetting industry. It has been the standard dummy text since the 1500s, originating when an unknown printer scrambled a set of type to create a type specimen book. Lorem Ipsum has survived through the centuries, even adapting to electronic typesetting without significant changes. Its popularity surged in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and it continues to be widely used in modern desktop publishing software such as Aldus PageMaker.\n" +
                    "Why do we use it?\n" +
                    "The use of Lorem Ipsum is based on the idea that a reader will be distracted by the actual content of a page when looking at its layout. By using Lorem Ipsum, which mimics the distribution of letters in English without forming readable text, designers can focus on the visual aspects of a page. It is now the default placeholder text in many desktop publishing packages and web page editors. Searching for 'lorem ipsum' online will reveal numerous websites in their early stages of development. Over time, various versions of Lorem Ipsum have emerged, sometimes altered intentionally or accidentally with added humor.\n" +
                    "Where does it come from?\n" +
                    "Contrary to popular belief, Lorem Ipsum is not just random text. Its origins trace back to classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, discovered its source by researching the Latin word \"consectetur\" found in a Lorem Ipsum passage. He identified the text as coming from sections 1.10.32 and 1.10.33 of Cicero's \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil), a treatise on ethics popular during the Renaissance. The famous first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet,\" originates from section 1.10.32 of this work.\n" +
                    "The Standard Chunk\n" +
                    "The standard chunk of Lorem Ipsum used since the 1500s is provided below for those interested. Sections 1.10.32 and 1.10.33 from Cicero's \"de Finibus Bonorum et Malorum\" are reproduced in their original Latin form, along with English translations from the 1914 version by H. Rackham.\n" +
                    "Where can I get some?\n" +
                    "There are many versions of Lorem Ipsum passages available, though many have been altered with injected humor or random words that don't appear believable. If you choose to use a Lorem Ipsum passage, ensure it doesn't contain any hidden, embarrassing text. The Internet hosts numerous Lorem Ipsum generators that often repeat predefined chunks, making this generator one of the first true sources. It uses a dictionary of over 200 Latin words and several model sentence structures to create Lorem Ipsum that looks reasonable, ensuring it is free from repetition, humor, or non-characteristic words.\n";
            byte[] content = contentString.getBytes(StandardCharsets.UTF_8);

            ByteBufAllocator byteBufAllocator = ctx.channel().alloc();
            ByteBuf byteBuf = byteBufAllocator.directBuffer();

            boolean keepAlive = HttpUtil.isKeepAlive(req);
            FullHttpResponse response = new DefaultFullHttpResponse(req.protocolVersion(), OK,
                    Unpooled.wrappedBuffer(content));

            response.headers()
                    .set(CONTENT_TYPE, TEXT_PLAIN)
                    .setInt(CONTENT_LENGTH, response.content().readableBytes());

            if (keepAlive) {
                if (!req.protocolVersion().isKeepAliveDefault()) {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                }
            } else {
                // Tell the client we're going to close the connection.
                response.headers().set(CONNECTION, CLOSE);
            }

            ChannelFuture f = ctx.write(response);

            if (!keepAlive) {
                f.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
