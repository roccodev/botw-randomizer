package dev.rocco.botw.randomizer.profile;

import com.arbiter34.byml.BymlFile;
import com.arbiter34.byml.nodes.ArrayNode;
import com.arbiter34.byml.nodes.DictionaryNode;
import com.arbiter34.file.io.BinaryAccessFile;
import com.arbiter34.yaz0.Yaz0Decoder;
import com.arbiter34.yaz0.Yaz0Encoder;
import dev.rocco.botw.randomizer.gui.ProgressDialog;
import dev.rocco.botw.randomizer.io.InputManager;
import dev.rocco.botw.randomizer.io.OutputManager;
import dev.rocco.botw.randomizer.profile.patch.MapPatch;
import dev.rocco.filelib.sarc.SarcPacker;
import dev.rocco.filelib.sarc.SarcUnpacker;
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
    private boolean universalPath = false;

    public void setUniversalPath(boolean universalPath) {
        this.universalPath = universalPath;
    }

    private HashMap<String, List<RandomizerPatch>> patches = new HashMap<>();

    public RandomizerFile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFiles() {
        this.file = new File((universalPath ? InputManager.getContentsFolderUniversal()
                : InputManager.getContentsFolder()).getAbsolutePath() + "/" + fileName);
        this.outputFile = universalPath ?
                OutputManager.addToOutputUniversal("/content/" + fileName)
                : OutputManager.addToOutput("/content/" + fileName);
        OutputManager.backup(file, "/content/" + fileName);
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

        int patchType = patches.values().stream().findAny().get().get(0).getType();
        if (patchType == 0) {
            BinaryAccessFile inputDecomp = Yaz0Decoder.decode(new BinaryAccessFile(this.file, "r"));
            BymlFile byml = BymlFile.parse(inputDecomp);
            DictionaryNode root = (DictionaryNode) byml.getRoot();
            ArrayNode objs = (ArrayNode) root.get("Objs");
            ProgressDialog.prog("Patching " + file.getName());
            for (List<RandomizerPatch> patches : patches.values()) {
                for (RandomizerPatch patch : patches) {
                    if (patch.getType() == 0) {

                        patch.patch(profile, objs);
                    }
                }
            }
            String uuid = UUID.randomUUID().toString();
            byml.write("FileCache/Decomp-" + uuid);
            BinaryAccessFile out = new BinaryAccessFile("FileCache/Decomp-" + uuid, "r");

            int alignedSize = (int) ((out.length() + 31) & -32);
            profile.getRstb().setSize(fileName, alignedSize + 0xe4 + 0x20);

            Yaz0Encoder.encode(out, outputFile.getAbsolutePath());

            inputDecomp.clean();
            out.clean();
        } else if (patchType == 1) {
            String dirUuid = UUID.randomUUID().toString();
            File outputDir = new File("FileCache/" + dirUuid);
            outputDir.mkdirs();
            SarcUnpacker.unpack(getFile(), outputDir);

            String dgnName = getFile().getName().replace(".pack", "");


            String filePath = outputDir.getAbsolutePath() + "/Map/CDungeon/" + dgnName + "/" +
                    dgnName + "_Static.smubin";

            BinaryAccessFile mapFile = Yaz0Decoder.decode(new BinaryAccessFile(filePath, "r"));

            BymlFile byml = BymlFile.parse(mapFile);
            DictionaryNode root = (DictionaryNode) byml.getRoot();
            ArrayNode objs = (ArrayNode) root.get("Objs");

            for (List<RandomizerPatch> patches : patches.values()) {
                for (RandomizerPatch patch : patches) {
                    if (patch.getType() == 1) {
                        patch.patch(profile, objs);
                    }
                }
            }
            String tempPath = outputDir.getAbsolutePath() + "/Map/CDungeon/" + dgnName + ".tmp";
            byml.write(tempPath);
            BinaryAccessFile out = new BinaryAccessFile(tempPath, "r");
            Yaz0Encoder.encode(out, filePath);

            SarcPacker.packIntoSarc(outputDir, outputFile);

            int alignedSize = (int) ((outputFile.length() + 31) & -32);
            profile.getRstb().setSize(fileName, alignedSize + 0xe4 + 0x20);

            mapFile.clean();
            out.clean();
        }
    }

}
