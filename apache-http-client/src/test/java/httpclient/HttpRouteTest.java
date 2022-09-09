package httpclient;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class HttpRouteTest {

    @Test
    void HttpRoute_는_() throws IOException, ExecutionException, InterruptedException {
        // given
        HttpClientContext context = HttpClientContext.create();
        BasicHttpClientConnectionManager connMrg = new BasicHttpClientConnectionManager();
        HttpRoute route = new HttpRoute(new HttpHost("www.baeldung.com", 80));
// Request new connection. This can be a long process
        ConnectionRequest connRequest = connMrg.requestConnection(route, null);
// Wait for connection up to 10 sec
        HttpClientConnection conn = connRequest.get(10, TimeUnit.SECONDS);
        do {
            long current = System.currentTimeMillis();
            System.out.println(connRequest.cancel());
            connMrg.close();
        } while (!connRequest.cancel());
        System.out.println("end");

        System.out.println(route.getHopCount());
        System.out.println(route.getProxyHost().toHostString());
    }
}
