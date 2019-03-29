package dev.rocco.botw.randomizer.io;

import java.io.File;

public class OutputManager {

    public static File outputFile = new File("RandomizerOutput/");

    public static File addToOutput(String fileNameAndPath) {
        File f = new File(outputFile.getAbsolutePath() + "/" + fileNameAndPath);
        f.getParentFile().mkdirs();
        return f;
    }

    public static String makeOutputPath(String initial) {
        return outputFile.getAbsolutePath() + initial;
    }

    public static String getAbsolutePath() {
        return outputFile.getAbsolutePath();
    }

    public static void clean() {
        outputFile.delete();
    }
}
