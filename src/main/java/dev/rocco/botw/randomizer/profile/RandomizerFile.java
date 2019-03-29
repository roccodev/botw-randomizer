package dev.rocco.botw.randomizer.profile;

import com.arbiter34.byml.BymlFile;
import com.arbiter34.byml.nodes.ArrayNode;
import com.arbiter34.byml.nodes.DictionaryNode;
import com.arbiter34.file.io.BinaryAccessFile;
import com.arbiter34.yaz0.Yaz0Decoder;
import com.arbiter34.yaz0.Yaz0Encoder;
import dev.rocco.botw.randomizer.io.InputManager;
import dev.rocco.botw.randomizer.io.OutputManager;
import dev.rocco.botw.randomizer.profile.patch.MapPatch;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RandomizerFile {

    private String fileName;
    private File file, outputFile;

    private HashMap<String, List<RandomizerPatch>> patches = new HashMap<>();

    private RandomizerFile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFiles() {
        this.file = new File(InputManager.getContentsFolder().getAbsolutePath() + "/" + fileName);
        this.outputFile = OutputManager.addToOutput(fileName);
    }

    public HashMap<String, List<RandomizerPatch>> getPatches() {
        return patches;
    }

    public static RandomizerFile fromJson(String key, JSONObject json) {
        RandomizerFile result = new RandomizerFile(key);
        if(key.startsWith("Map/")) {
            json.keys().forEachRemaining(k -> {
                List<RandomizerPatch> patches = result.patches.getOrDefault(k, new ArrayList<>());
                patches.add(MapPatch.fromJson(k, json.getJSONObject(k)));

                result.patches.put(k, patches);
            });
        }

        return result;
    }

    private File getFile() {
        return file;
    }

    public void patchAll(RandomizerProfile profile) throws IOException {
        BinaryAccessFile inputDecomp = Yaz0Decoder.decode(new BinaryAccessFile(this.file, "r"));
        BymlFile byml = BymlFile.parse(inputDecomp);
        DictionaryNode root = (DictionaryNode) byml.getRoot();
        ArrayNode objs = (ArrayNode) root.get("Objs");

        for(List<RandomizerPatch> patches : patches.values()) {
            for(RandomizerPatch patch : patches) {
                if(patch.getType() == 0) {
                    patch.patch(profile, objs);
                }
            }
        }
        String uuid = UUID.randomUUID().toString();
        byml.write("FileCache/Decomp-" + uuid);
        Yaz0Encoder.encode(new BinaryAccessFile("FileCache/Decomp-" + uuid, "r"), outputFile.getAbsolutePath());
    }

}
