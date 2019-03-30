package dev.rocco.botw.randomizer.io;

import dev.rocco.botw.randomizer.Config;

import java.io.File;

public class InputManager {

    private static File contentsFolder;

    public static void init() {
        String suffix = Config.aoc ? "/aoc/content/" : "/content/";
        contentsFolder = new File(Config.inputFile.getAbsolutePath() + suffix);
    }

    public static File getContentsFolder() {
        return contentsFolder;
    }
}
