package dev.rocco.botw.randomizer.profile.patch;

import com.arbiter34.byml.nodes.ArrayNode;
import com.arbiter34.byml.nodes.DictionaryNode;
import com.arbiter34.byml.nodes.Node;
import dev.rocco.botw.randomizer.profile.RandomizerPatch;
import dev.rocco.botw.randomizer.profile.RandomizerProfile;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class MapPatch implements RandomizerPatch {

    private HashMap<String, String> values = new HashMap<>();
    private String hashId;

    private MapPatch() {}

    public HashMap<String, String> getValues() {
        return values;
    }

    public static MapPatch fromJson(String key, JSONObject object) {
        MapPatch result = new MapPatch();
        result.hashId = key;
        for (Iterator<String> it = object.keys(); it.hasNext(); ) {
            String k = it.next();

            result.values.put(k, object.getString(k));
        }

        return result;
    }

    @Override
    public void patch(RandomizerProfile profile, Object in) {
        ArrayNode objs = (ArrayNode) in;

        values.forEach((k, v) -> getByHash(hashId, objs).get(k).setValue(profile.pickValue(v)));
    }

    private static DictionaryNode getByHash(String hash, ArrayNode parent) {
        for(Node n : parent) {
            DictionaryNode dn = (DictionaryNode) n;
            String h = dn.get("HashId").getValue().toString();
            if(h.equals(hash)) return dn;
        }
        return null;
    }

    @Override
    public int getType() {
        return 0;
    }
}
