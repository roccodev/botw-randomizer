package dev.rocco.botw.randomizer.profile;

import dev.rocco.botw.randomizer.Config;
import dev.rocco.botw.randomizer.gui.ProgressDialog;
import dev.rocco.botw.randomizer.io.OutputManager;
import dev.rocco.botw.randomizer.rand.RandomPicker;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RandomizerProfile {

    private String name, description, author;
    private String[] items;

    private HashMap<String, RandomizerFile> filePatches = new HashMap<>();
    private HashMap<String, RandomizerList> lists = new HashMap<>();

    public static RandomizerProfile fromJson(JSONObject object) {
        RandomizerProfile result = new RandomizerProfile();
        result.name = object.getString("name");
        result.description = object.getString("description");
        result.author = object.getString("author");

        result.items = object.getJSONArray("items").toList().toArray(new String[0]);

        JSONObject patches = object.getJSONObject("patches");

        patches.keys().forEachRemaining(k -> result.filePatches.put(k, RandomizerFile.fromJson(k, patches.getJSONObject(k))));

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

    public HashMap<String, RandomizerFile> getFilePatches() {
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

    public void patchAll() throws IOException {

        new Thread(ProgressDialog::showDialog).start();
        OutputManager.clean();
        OutputManager.copyReadme();
        OutputManager.patchVersion();

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
    }
}
