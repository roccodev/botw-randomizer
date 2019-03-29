package dev.rocco.botw.randomizer;

import dev.rocco.botw.randomizer.profile.RandomizerProfile;

import java.io.File;
import java.util.HashMap;

public class Config {

    public static Long seed;
    public static HashMap<String, RandomizerProfile> profiles = new HashMap<>();
    public static File inputFile;
    public static String profileIndex;

}
