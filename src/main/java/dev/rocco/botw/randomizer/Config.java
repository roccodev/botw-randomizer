package dev.rocco.botw.randomizer;

import dev.rocco.botw.randomizer.profile.RandomizerProfile;

import java.io.File;
import java.util.HashMap;

public class Config {

    public static final String VENDOR_ID = "00050000";
    public static final String TITLE_ID = "101C9400";

    public static Long seed;
    public static HashMap<String, RandomizerProfile> profiles = new HashMap<>();
    public static File inputFile = new File("");
    public static String profileIndex;

    public static int outputMode; // 0: Emulator, 1: SDCafiine
    public static boolean aoc;

}
