package com.company;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Stream;

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

    public String join(Stream<String> iter) {
        return String.join(",", iter.toArray(String[]::new));
    }

    public ArrayList<String> getList(String key) {
        return new ArrayList<>(Arrays.asList(get(key, "").split(",")));
    }
}
