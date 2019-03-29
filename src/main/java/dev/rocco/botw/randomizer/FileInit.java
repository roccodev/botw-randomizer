package dev.rocco.botw.randomizer;

import dev.rocco.botw.randomizer.io.InputManager;
import dev.rocco.botw.randomizer.profile.RandomizerProfile;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileInit {

    public static void initFiles() throws IOException {
        checkAndCreate("FileCache", true);

        InputManager.init();
        File profiles = new File("RandomizerProfiles");
        for(File f : profiles.listFiles()) {
            Config.profiles.put(f.getName().replace(".json", ""),
                    RandomizerProfile.fromJson(new JSONObject(String.join("\n",
                            Files.readAllLines(Paths.get(f.toURI()), Charset.forName("UTF-8"))))));
        }

        Config.profileIndex = Config.profiles.keySet().stream().findFirst().get();
    }

    private static void checkAndCreate(String path, boolean dir) {
        File f = new File(path);
        if(!f.exists()) {
            if(dir) f.mkdirs();
            else {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
