package me.refluxo.updateservice.util;

import java.util.HashMap;
import java.util.UUID;

public class ResourceActivationService {

    private final HashMap<String, String> activationKeys;

    public ResourceActivationService() {
        activationKeys = new HashMap<>();
    }

    public String get(String key) {
        return activationKeys.getOrDefault(key, null);
    }

    public String generate(String resource) {
        String key = UUID.randomUUID().toString();
        activationKeys.put(key, resource);
        return key;
    }

}
