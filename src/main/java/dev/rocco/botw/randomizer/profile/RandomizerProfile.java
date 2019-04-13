package dev.rocco.botw.randomizer.profile;

import dev.rocco.botw.randomizer.Config;
import dev.rocco.botw.randomizer.gui.ProgressDialog;
import dev.rocco.botw.randomizer.io.OutputManager;
import dev.rocco.botw.randomizer.profile.defaults.DefaultList;
import dev.rocco.botw.randomizer.profile.defaults.Defaults;
import dev.rocco.botw.randomizer.profile.patch.MapLocation;
import dev.rocco.botw.randomizer.profile.patch.MapPatch;
import dev.rocco.botw.randomizer.profile.patch.ShrineLocation;
import dev.rocco.botw.randomizer.rand.RandomPicker;
import dev.rocco.botw.randomizer.utils.rstb.ResourceTable;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RandomizerProfile {

    private String name, description, author;
    private String[] items;

    private JSONObject json;

    private HashMap<String, RandomizerFile> filePatches = new HashMap<>();
    private HashMap<String, RandomizerList> lists = new HashMap<>();

    private ResourceTable rstb;

    public static RandomizerProfile fromJson(JSONObject object) {
        RandomizerProfile result = new RandomizerProfile();
        result.name = object.getString("name");
        result.description = object.getString("description");
        result.author = object.getString("author");

        result.items = object.getJSONArray("items").toList().toArray(new String[0]);

        result.json = object;

        JSONObject lists = object.getJSONObject("lists");
        lists.keys().forEachRemaining(k -> result.lists.put(k, RandomizerList.fromJson(lists.getJSONObject(k))));

        return result;
    }

    public void loadPatches() {
        JSONObject patches = json.getJSONObject("patches");

        patches.keys().forEachRemaining(k -> {
            if(k.charAt(0) == '#') {
                RandomizerLocation loc = RandomizerLocation.fromFile(k.substring(1));
                if(!loc.requiresAoc() || Config.aoc) {

                    JSONObject values = patches.getJSONObject(k);
                    System.out.println("Location type: " + loc.getType());
                    switch(loc.getType()) {
                        case 0 /* Map */:
                            MapPatch patch = MapPatch.fromJson(k, values);

                            try {
                                MapLocation.findHashes(loc.getItems(), patch);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 1 /* Shrine */:
                            MapPatch patchForShrine = MapPatch.fromJson(k, values);
                            ShrineLocation.find(loc, patchForShrine);
                            break;
                    }

                }

            }
            else filePatches.put(k, RandomizerFile.fromJson(k, patches.getJSONObject(k)));
        });
        System.out.println("Total matches: " + MapLocation.getResults().size());
        if(MapLocation.getResults().size() != 0)
            filePatches.putAll(MapLocation.getResults());
        if(ShrineLocation.getResults().size() != 0)
            filePatches.putAll(ShrineLocation.getResults());
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

    public HashMap<String, RandomizerFile> getFilePatches() {
        return filePatches;
    }

    public ResourceTable getRstb() {
        return rstb;
    }

    public String pickValue(String input) {
        if(input.charAt(0) == '@') {
            String listName = input.substring(1);
            RandomizerList list = getList(listName);
            if(list == null) return listName;

            return RandomPicker.getKey(list.getValues(), Config.seed);
        }
        else if(input.charAt(0) == '§') {
            String listName = input.substring(1);
            DefaultList list = getDefaultList(listName);
            if(list == null) return listName;

            return RandomPicker.getKey(list.getEntries(list.getSubLists().keySet().toArray(new String[0])), Config.seed);
        }
        else return input;
    }

    private RandomizerList getList(String name) {
        if(!lists.containsKey(name)) return null;
        return lists.get(name);
    }

    private DefaultList getDefaultList(String name) {
        if(!Defaults.lists.containsKey(name)) return null;
        return Defaults.lists.get(name);
    }

    public void patchAll() throws IOException {


        OutputManager.clean();
        OutputManager.copyReadme();
        OutputManager.patchVersion();

        rstb = new ResourceTable();
        rstb.parse();

        int total = filePatches.size();
        int count = 0;

        for(Map.Entry<String, RandomizerFile> entry : filePatches.entrySet()) {
            entry.getValue().setFiles();
            entry.getValue().patchAll(this);
            if(++count == total) {
                ProgressDialog.inst.dispose();
                int input = JOptionPane.showOptionDialog(
                        null, "Patch successful.",
                        "BOTW Randomizer",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, new String[] {"Go to output"}, null);

                if(input == JOptionPane.OK_OPTION) {
                    Desktop.getDesktop().open(OutputManager.outputFile);
                }

            }
            else ProgressDialog.inst.progressBar1.setValue(count / total * 100);
        }

        rstb.write();
    }
}
