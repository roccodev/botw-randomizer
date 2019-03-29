package dev.rocco.botw.randomizer;

import java.io.IOException;

public class RandomizerMain {

    public static void main(String[] args) {
        try {
            FileInit.initFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Config.seed = System.currentTimeMillis();
    }
}
