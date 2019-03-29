package dev.rocco.botw.randomizer.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class OutputManager {

    public static File outputFile = new File("RandomizerOutput/");

    public static void addToOutput(String fileNameAndPath, byte[] data) {
        File f = new File(outputFile.getAbsolutePath() + fileNameAndPath);
        f.getParentFile().mkdirs();

        try {
            f.createNewFile();
            Files.write(Paths.get(f.toURI()), data, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
