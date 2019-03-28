package dev.rocco.botw.randomizer;

import java.io.File;
import java.io.IOException;

public class FileInit {

    public static void initFiles() {
        checkAndCreate("FileCache", true);
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
