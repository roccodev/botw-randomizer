package dev.rocco.botw.randomizer.profile.defaults;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;

public class Defaults {
    public static HashMap<String, DefaultList> lists = new HashMap<>();

    public static void loadDefaults() throws IOException {
        File defFolder = new File("RandomizerDefaults");
        for(File f : defFolder.listFiles()) {
            JSONObject obj = new JSONObject(String.join("\n", Files.readAllLines(f.toPath(), Charset.forName("UTF-8"))));
            lists.put(f.getName().replace(".json", ""), DefaultList.fromJson(obj));
        }
    }
}
