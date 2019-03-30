package dev.rocco.botw.randomizer.profile;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class RandomizerLocation {
    private String root;
    private boolean requiresAoc;

    private String[] items;

    public String getRoot() {
        return root;
    }

    public boolean requiresAoc() {
        return requiresAoc;
    }

    public String[] getItems() {
        return items;
    }

    private static RandomizerLocation fromJson(JSONObject obj) {
        RandomizerLocation result = new RandomizerLocation();
        result.root = obj.getString("root");
        result.requiresAoc = obj.getBoolean("requiresAoc");

        result.items = obj.getJSONArray("items").toList().toArray(new String[0]);

        return result;
    }

    public static RandomizerLocation fromFile(String name) {
        File f = new File("RandomizerLocations/" + name + ".json");

        try {
            return fromJson(new JSONObject(String.join("\n", Files.readAllLines(f.toPath(), Charset.forName("UTF-8")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
