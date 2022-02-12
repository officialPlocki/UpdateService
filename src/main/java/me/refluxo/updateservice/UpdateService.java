package me.refluxo.updateservice;

import me.refluxo.updateservice.util.ResourceActivationService;
import me.refluxo.updateservice.util.ResourceManager;
import me.refluxo.updateservice.util.WebService;
import me.refluxo.updateservice.util.files.FileBuilder;
import me.refluxo.updateservice.util.files.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UpdateService {

    private static ResourceManager rm;
    private static ResourceActivationService ras;

    public static void main(String[] args) throws IOException {
        rm = new ResourceManager();
        ras = new ResourceActivationService();
        FileBuilder builder = new FileBuilder("resources.yml");
        YamlConfiguration yml = builder.getYaml();
        if(!builder.getFile().exists()) {
            builder.getFile().createNewFile();
            yml.set("resources", List.of("default"));
            yml.set("r.default.name", "default");
            yml.set("r.default.filePath", "files/default.jar");
            builder.save();
        }
        for(String resource : yml.getStringList("resources")) {
            String name = yml.getString("r." + resource + ".name");
            File file = new File(yml.getString("r." + resource + ".filePath"));
            if(!file.exists()) {
                file.createNewFile();
            }
            rm.addResource(name, file);
        }
        WebService service = new WebService(26, "/download");
        service.start();
        Runtime.getRuntime().addShutdownHook(new Thread(service::stop));
    }

    public static ResourceActivationService getRAS() {
        return ras;
    }

    public static ResourceManager getResourceManager() {
        return rm;
    }

}
