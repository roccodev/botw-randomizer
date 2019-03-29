package dev.rocco.botw.randomizer.profile;

import dev.rocco.botw.randomizer.profile.patch.MapPatch;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RandomizerFile {

    private String fileName;

    private HashMap<String, List<RandomizerPatch>> patches = new HashMap<>();

    private RandomizerFile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public HashMap<String, List<RandomizerPatch>> getPatches() {
        return patches;
    }

    public static RandomizerFile fromJson(String key, JSONObject json) {
        RandomizerFile result = new RandomizerFile(key);
        if(key.startsWith("Map/")) {
            json.keys().forEachRemaining(k -> {
                List<RandomizerPatch> patches = result.patches.getOrDefault(k, new ArrayList<>());
                patches.add(MapPatch.fromJson(json.getJSONObject(k)));

                result.patches.put(k, patches);
            });
        }

        return result;
    }

}
