package dev.rocco.botw.randomizer.profile.patch;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationCache {

    public static JSONObject cache;

    public static void init() throws IOException {
        File file = new File("FileCache/locations.json");
        if(!file.exists()) {
            file.createNewFile();
            cache = new JSONObject();
            saveCache();
        }
        else cache = new JSONObject(String.join("\n", Files.readAllLines(file.toPath(), Charset.forName("UTF-8"))));
    }

    public static List<String> getHashes(String file) {
        List<String> result = new ArrayList<>();
        if(!cache.has(file)) return result;
        return Arrays.asList(cache.getJSONArray(file).toList().toArray(new String[0]));
    }

    public static void setHashes(String file, List<String> hashes) {
        cache.put(file, hashes);
    }

    public static void saveCache() throws IOException {
        File file = new File("FileCache/locations.json");
        if(!file.exists()) file.createNewFile();
        Files.write(file.toPath(), cache.toString().getBytes(), StandardOpenOption.WRITE);
    }

}
