package dev.rocco.botw.randomizer.profile.patch;

import dev.rocco.botw.randomizer.profile.RandomizerFile;
import dev.rocco.botw.randomizer.profile.RandomizerLocation;
import dev.rocco.botw.randomizer.profile.RandomizerPatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShrineLocation {

    private static HashMap<String, RandomizerFile> results = new HashMap<>();

    public static void find(RandomizerLocation location, MapPatch patch) {
        System.out.println("Finding shrines...");
        for(String item : location.getItems()) {
            System.out.println("Item: " + item);
            String[] data = item.split("@");
            String hash = data[0];
            String dungeonId = data[1];

            String fileName = "Pack/Dungeon" + dungeonId + ".pack";
            RandomizerFile forDgn = results.getOrDefault(fileName, new RandomizerFile(fileName));
            forDgn.setUniversalPath(true);
            List<RandomizerPatch> patches = forDgn.getPatches().getOrDefault(hash, new ArrayList<>());
            patch = new MapPatch(patch, hash);
            patch.setType(1);
            patches.add(patch);

            forDgn.getPatches().put(hash, patches);
            results.put(dungeonId, forDgn);
        }
    }

    public static HashMap<String, RandomizerFile> getResults() {
        return results;
    }
}
