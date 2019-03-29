package dev.rocco.botw.randomizer;

import dev.rocco.botw.randomizer.gui.GuiMainMenu;

import java.io.IOException;

public class RandomizerMain {

    public static final String VERSION = "1.0";

    public static void main(String[] args) {
        try {
            FileInit.initFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Config.seed = System.currentTimeMillis();

        GuiMainMenu.show("Breath of the Wild Randomizer v" + VERSION);
    }
}
