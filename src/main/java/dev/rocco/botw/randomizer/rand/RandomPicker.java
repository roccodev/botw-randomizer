package dev.rocco.botw.randomizer.rand;

import java.util.Map;
import java.util.Random;

public class RandomPicker {

    private static Random rand;

    public static String getKey(Map<String, Double> percents, Long seed) {
        if(rand == null)
            rand = seed != null ? new Random(seed) : new Random();

        double randNum = rand.nextDouble() * 100D;

        for(Map.Entry<String, Double> entry : percents.entrySet()) {
            randNum -= entry.getValue();

            if(randNum <= 0D) return entry.getKey();
        }
        System.err.println("Couldn't pick randomly. Random: " + randNum);
        return "Npc_SouthHateru007";
    }

}
