package me.refluxo.updateservice.util;

import java.io.File;
import java.util.HashMap;

public class ResourceManager {

    private static final HashMap<String, File> resources = new HashMap<>();

    public void addResource(String key, File file) {
        resources.put(key, file);
    }

    public void removeResource(String key) {
        resources.remove(key);
    }

    public File getResource(String key) {
        return resources.get(key);
    }

}
