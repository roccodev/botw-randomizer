package dev.rocco.botw.randomizer.io;

import dev.rocco.botw.randomizer.Config;

import java.io.File;

public class InputManager {

    private static File contentsFolder, contentsFolderUniversal;

    public static void init() {
        String suffix = Config.aoc ? "/aoc/content/0010/" : "/content/";
        contentsFolder = new File(Config.inputFile.getAbsolutePath() + suffix);
        contentsFolderUniversal = new File(Config.inputFile.getAbsolutePath() + "/content/");
    }

    public static File getContentsFolder() {
        return contentsFolder;
    }

    public static File getContentsFolderUniversal() {
        return contentsFolderUniversal;
    }
}
