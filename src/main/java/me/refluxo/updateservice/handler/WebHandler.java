package me.refluxo.updateservice.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.refluxo.updateservice.UpdateService;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class WebHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());

        //get values
        AtomicBoolean get = new AtomicBoolean(false);
        AtomicReference<String> key = new AtomicReference<>("");
        AtomicReference<String> resource_get = new AtomicReference<>("");

        //access values
        AtomicBoolean access = new AtomicBoolean(false);
        AtomicReference<String> resource_access = new AtomicReference<>();

        //activation values
        AtomicBoolean activation = new AtomicBoolean(false);
        AtomicReference<String> activation_key = new AtomicReference<>("");
        AtomicReference<String> activation_resource = new AtomicReference<>("");

        params.forEach((field, value) -> {
            if(field.equalsIgnoreCase("get")) {
                if(Boolean.parseBoolean(value)) {
                    get.set(true);
                }
            } else if(field.equalsIgnoreCase("access")) {
                if(Boolean.parseBoolean(value)) {
                    access.set(true);
                }
            } else if(field.equalsIgnoreCase("activation")) {
                if(Boolean.parseBoolean(value)) {
                    access.set(true);
                }
            } else {
                if(get.get()) {
                    if(field.equalsIgnoreCase("key")) {
                        key.set(value);
                    } else if(field.equalsIgnoreCase("resource")) {
                        resource_get.set(value);
                    }
                } else if(access.get()) {
                    if(field.equalsIgnoreCase("resource")) {
                        resource_access.set(value);
                    }
                } else if(activation.get()) {
                    if(field.equalsIgnoreCase("key")) {
                        activation_key.set(value);
                    } else if(field.equalsIgnoreCase("resource")) {
                        activation_resource.set(value);
                    }
                }
            }
        });
        if(get.get()) {
            if(key.get().equalsIgnoreCase("!!!ES_GIBT_KEINEN_KEY_DU_VOLLIDIOT!_!!!")) {
                try {
                    File file = UpdateService.getResourceManager().getResource(resource_get.get());
                    byte[] response = Files.readAllBytes(file.toPath());
                    exchange.getResponseHeaders().add("Content-Disposition", "attachment; filename=" + file.getName());
                    OutputStream stream = exchange.getResponseBody();
                    exchange.sendResponseHeaders(200, response.length);
                    stream.write(response);
                    stream.flush();
                    stream.close();
                    exchange.getResponseBody().close();
                    exchange.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if(access.get()) {
            if(UpdateService.getResourceManager().getResource(resource_access.get()).exists()) {
                String response = UpdateService.getRAS().generate(resource_access.get());
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().flush();
                exchange.getResponseBody().close();
                exchange.close();
            }
        } else if(activation.get()) {
            if(UpdateService.getResourceManager().getResource(activation_resource.get()).exists()) {
                if(UpdateService.getRAS().get(activation_key.get()) != null) {
                    String response = "true";
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().flush();
                    exchange.getResponseBody().close();
                    exchange.close();
                } else {
                    String response = "false";
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().flush();
                    exchange.getResponseBody().close();
                    exchange.close();
                }
            } else {
                String response = "false";
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().flush();
                exchange.getResponseBody().close();
                exchange.close();
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
