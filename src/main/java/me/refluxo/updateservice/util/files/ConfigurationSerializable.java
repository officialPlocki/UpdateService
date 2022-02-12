package me.refluxo.updateservice.util.files;

import java.util.Map;

public interface ConfigurationSerializable {

    Map<String, Object> serialize();
}