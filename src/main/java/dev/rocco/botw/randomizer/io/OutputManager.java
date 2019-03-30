package dev.rocco.botw.randomizer.io;

import dev.rocco.botw.randomizer.Config;
import dev.rocco.botw.randomizer.RandomizerMain;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

public class OutputManager {

    public static File outputFile = new File("RandomizerOutput/");
    public static File backupFile = new File("RandomizerBackup/");

    public static File addToOutput(String fileNameAndPath) {
        String filePath = Config.aoc ? "aoc/" + fileNameAndPath.replace("content/", "content/0010/")
                : fileNameAndPath;
        String path = Config.outputMode == 0 ?
                /* Emulator mode */ outputFile.getAbsolutePath() + "/mlc01/usr/title/" + Config.VENDOR_ID + "/" + Config.TITLE_ID +
                "/" + filePath :
                /* SDCafiine mode */ outputFile.getAbsolutePath() + "/sdcafiine/" + Config.VENDOR_ID + Config.TITLE_ID +
                "/BOTWRandomizer/" + filePath;

        File f = new File(path);
        f.getParentFile().mkdirs();
        return f;
    }

    public static void backup(File file, String nameAndPath) {
        String filePath = Config.aoc ? "aoc/" + nameAndPath.replace("content/", "content/0010/") : nameAndPath;
        String backupPath = "RandomizerBackup/mlc01/usr/title/" + Config.VENDOR_ID + "/" + Config.TITLE_ID +
                "/" + filePath;

        File newFile = new File(backupPath);
        newFile.getParentFile().mkdirs();

        try {
            newFile.createNewFile();
            Files.copy(Paths.get(file.toURI()), Paths.get(newFile.toURI()), StandardCopyOption.REPLACE_EXISTING);
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

    private static void clean(File f) {
        try {
            Files.walk(Paths.get(f.toURI()))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException ignored) {}
    }

    public static void copyReadme() {
        String fileName = Config.outputMode == 0 ? "readme-emu" : "readme-sdcafiine";
        File target = new File("RandomizerOutput/Instructions (READ ME).txt");

        target.getParentFile().mkdirs();
        try {
            target.createNewFile();
            Files.copy(RandomizerMain.class.getResourceAsStream("/" + fileName), target.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void patchVersion() {
        String path = "content/System/Version.txt";
        File out = addToOutput(path);

        try {
            Files.write(out.toPath(), "1.5.0-Randomized".getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clean() {
        clean(outputFile);
        clean(backupFile);
    }
}
