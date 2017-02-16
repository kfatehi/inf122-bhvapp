package com.company;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by keyvan on 2/16/17.
 */
public class KeyValueStore {
    private String propsFile;
    private Properties props = new Properties();

    public KeyValueStore(String filePath){
        propsFile = filePath;
    }

    public void load() throws IOException {
        props.load(new FileReader(propsFile));
    }

    public void save() throws IOException {
        props.store(new FileOutputStream(propsFile), null);
    }

    public String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    public void set(String key, String value) {
        props.setProperty(key, value);
    }

    public void delete(String key) {
        props.remove(key);
    }
}
