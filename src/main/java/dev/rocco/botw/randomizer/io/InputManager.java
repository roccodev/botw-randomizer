package dev.rocco.botw.randomizer.io;

import dev.rocco.botw.randomizer.Config;

import java.io.File;

public class InputManager {

    private static File contentsFolder;

    public static void init() {
        contentsFolder = new File(Config.inputFile.getAbsolutePath() + "/contents");
    }

    public static File getContentsFolder() {
        return contentsFolder;
    }
}
