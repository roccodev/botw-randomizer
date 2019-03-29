package dev.rocco.botw.randomizer.profile.patch;

import com.arbiter34.byml.nodes.DictionaryNode;
import dev.rocco.botw.randomizer.profile.RandomizerPatch;
import dev.rocco.botw.randomizer.profile.RandomizerProfile;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class MapPatch implements RandomizerPatch {

    private HashMap<String, String> values = new HashMap<>();

    private MapPatch() {}

    public HashMap<String, String> getValues() {
        return values;
    }

    public static MapPatch fromJson(JSONObject object) {
        MapPatch result = new MapPatch();
        for (Iterator<String> it = object.keys(); it.hasNext(); ) {
            String k = it.next();

            result.values.put(k, object.getString(k));
        }

        return result;
    }

    @Override
    public void patch(RandomizerProfile profile, Object in) {
        DictionaryNode node = (DictionaryNode) in;
        values.forEach((k, v) -> node.get(k).setValue(profile.pickValue(v)));
    }
}
