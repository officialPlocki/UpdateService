package me.refluxo.updateservice.util;

import com.sun.net.httpserver.HttpServer;
import me.refluxo.updateservice.handler.WebHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebService {

    private final HttpServer server;

    public WebService(int port, String context) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext(context, new WebHandler());
        server.setExecutor(null);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(1);
    }

}
