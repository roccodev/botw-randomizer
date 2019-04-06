package dev.rocco.botw.randomizer.profile.defaults;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DefaultSubList {

    private String key;
    private String[] items;

    public String getKey() {
        return key;
    }

    public String[] getItems() {
        return items;
    }

    public List<String> getEntries() {
        List<String> entries = new ArrayList<>();
        for(String item : items) {
            entries.add(key.replace("%s", item));
        }
        return entries;
    }

    public static DefaultSubList fromJson(JSONObject object) {
        DefaultSubList result = new DefaultSubList();
        result.key = object.getString("key");
        result.items = object.getJSONArray("items").toList().toArray(new String[0]);
        return result;
    }
}
