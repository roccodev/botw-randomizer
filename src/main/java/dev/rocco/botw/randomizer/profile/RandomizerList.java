package dev.rocco.botw.randomizer.profile;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RandomizerList {

    private Map<String, Double> values = new HashMap<>();

    public Map<String, Double> getValues() {
        return values;
    }

    public static RandomizerList fromJson(JSONObject obj) {
        RandomizerList result = new RandomizerList();
        obj.keys().forEachRemaining(k -> {
            result.values.put(k, obj.getDouble(k));
        });

        return result;
    }
}
