package dev.rocco.botw.randomizer.profile.defaults;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DefaultList {

    private HashMap<String, DefaultSubList> subLists = new HashMap<>();

    public String[] getEntries(String[] subLists) {
        List<String> total = new ArrayList<>();
        for(String sub : subLists) {
            total.addAll(this.subLists.get(sub).getEntries());
        }
        return total.toArray(new String[0]);
    }

    public static DefaultList fromJson(JSONObject object) {
        DefaultList result = new DefaultList();
        for (Iterator<String> it = object.keys(); it.hasNext(); ) {
            String key = it.next();
            JSONObject value = object.getJSONObject(key);

            result.subLists.put(key, DefaultSubList.fromJson(value));
        }
        return result;
    }

    public HashMap<String, DefaultSubList> getSubLists() {
        return subLists;
    }
}
