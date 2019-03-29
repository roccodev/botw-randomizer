package dev.rocco.botw.randomizer.profile;

import dev.rocco.botw.randomizer.Config;
import dev.rocco.botw.randomizer.rand.RandomPicker;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RandomizerProfile {

    private String name, description, author;
    private String[] items;

    private HashMap<String, List<RandomizerFile>> filePatches = new HashMap<>();
    private HashMap<String, RandomizerList> lists = new HashMap<>();

    public static RandomizerProfile fromJson(JSONObject object) {
        RandomizerProfile result = new RandomizerProfile();
        result.name = object.getString("name");
        result.description = object.getString("description");
        result.author = object.getString("author");

        result.items = object.getJSONArray("items").toList().toArray(new String[0]);

        JSONObject patches = object.getJSONObject("patches");

        patches.keys().forEachRemaining(k -> {
            List<RandomizerFile> files = result.filePatches.getOrDefault(k, new ArrayList<>());
            files.add(RandomizerFile.fromJson(k, patches.getJSONObject(k)));

            result.filePatches.put(k, files);
        });

        JSONObject lists = object.getJSONObject("lists");
        lists.keys().forEachRemaining(k -> result.lists.put(k, RandomizerList.fromJson(lists.getJSONObject(k))));

        return result;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getItems() {
        return items;
    }

    public HashMap<String, List<RandomizerFile>> getFilePatches() {
        return filePatches;
    }

    public String pickValue(String input) {
        if(input.charAt(0) == '@') {
            String listName = input.substring(1);
            RandomizerList list = getList(listName);
            if(list == null) return listName;

            return RandomPicker.getKey(list.getValues(), Config.seed);
        }
        else return input;
    }

    private RandomizerList getList(String name) {
        if(!lists.containsKey(name) && !RandomizerStorage.lists.containsKey(name)) return null;
        return lists.containsKey(name) ? lists.get(name) : RandomizerStorage.lists.get(name);
    }
}
