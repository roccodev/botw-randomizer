package dev.rocco.botw.randomizer.profile.patch;

import com.arbiter34.byml.BymlFile;
import com.arbiter34.byml.nodes.ArrayNode;
import com.arbiter34.byml.nodes.DictionaryNode;
import com.arbiter34.file.io.BinaryAccessFile;
import com.arbiter34.yaz0.Yaz0Decoder;
import dev.rocco.botw.randomizer.io.InputManager;
import dev.rocco.botw.randomizer.profile.RandomizerFile;
import dev.rocco.botw.randomizer.profile.RandomizerPatch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MapLocation {

    private static HashMap<String, RandomizerFile> matches = new HashMap<>();

    public static void findHashes(String[] hashes, MapPatch patch) throws IOException {
        String path = "/Map/MainField/";
        File folder = new File(InputManager.getContentsFolder() + "/" + path);

        List<String> hashesL = Arrays.asList(hashes);

        for(File mapFolder : folder.listFiles()) {
            if(mapFolder == null || !mapFolder.isDirectory()) continue;
            for(File f : mapFolder.listFiles()) {
                if(!f.getName().endsWith(".smubin")) continue;
                BinaryAccessFile inputDecomp = Yaz0Decoder.decode(new BinaryAccessFile(f, "r"));
                BymlFile byml = BymlFile.parse(inputDecomp);
                DictionaryNode root = (DictionaryNode) byml.getRoot();
                ArrayNode objs = (ArrayNode) root.get("Objs");
                List<String> cached = LocationCache.getHashes(f.getName());
                List<String> matchHashes = findHash(hashesL, objs, cached);
                if(matchHashes.size() != 0) {
                    String fileName = path + "/" + mapFolder.getName() + "/" + f.getName();
                    RandomizerFile file = matches.get(fileName);

                    RandomizerFile ff = file != null ? file : new RandomizerFile(fileName);

                    List<RandomizerPatch> patches = new ArrayList<>();
                    matchHashes.forEach(h -> patches.add(new MapPatch(patch, h)));

                    matchHashes.forEach(h -> ff.getPatches().put(h, patches));

                    matches.put(fileName, ff);

                    LocationCache.setHashes(f.getName(), cached);
                }

                inputDecomp.clean();
            }
        }

        LocationCache.saveCache();
    }

    private static List<String> findHash(List<String> hashes, ArrayNode parent, List<String> cache) {
        return parent.stream().filter(n -> {
            DictionaryNode dn = (DictionaryNode) n;
            String h = dn.get("HashId").getValue().toString();
            if(cache.contains(h)) return true;
            if(hashes.contains(h)) {
                cache.add(h);
                return true;
            }
            return false;
        }).map(node -> {
            DictionaryNode dn = (DictionaryNode) node;
            return dn.get("HashId").getValue().toString();
        }).collect(Collectors.toList());
    }

    public static HashMap<String, RandomizerFile> getResults() {
        return matches;
    }
}
