package me.refluxo.updateservice.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.refluxo.updateservice.UpdateService;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class WebHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) {
        Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
        AtomicBoolean get = new AtomicBoolean(false);
        AtomicReference<String> key = new AtomicReference<>("");
        AtomicReference<String> resource = new AtomicReference<>("");
        params.forEach((field, value) -> {
            System.out.println(field + " : " + value);
            if(field.equalsIgnoreCase("get")) {
                if(Boolean.parseBoolean(value)) {
                    get.set(true);
                }
            } else if(field.equalsIgnoreCase("key")) {
                key.set(value);
            } else if(field.equalsIgnoreCase("resource")) {
                resource.set(value);
            }
        });
        if(get.get()) {
            if(key.get().equalsIgnoreCase("!!!ES_GIBT_KEINEN_KEY_DU_VOLLIDIOT!_!!!")) {
                try {
                    File file = UpdateService.getResourceManager().getResource(resource.get());
                    byte[] response = Files.readAllBytes(file.toPath());
                    exchange.getResponseHeaders().add("Content-Disposition", "attachment; filename=" + file.getName());
                    OutputStream stream = exchange.getResponseBody();
                    exchange.sendResponseHeaders(200, response.length);
                    stream.write(response);
                    stream.flush();
                    stream.close();
                    exchange.getResponseBody().close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private Map<String, String> queryToMap(String query) {
        if(query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

}
