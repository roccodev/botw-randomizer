package dev.rocco.botw.randomizer.rand;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RandomPicker {

    public static String getKey(Map<String, Double> percents, Long seed) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        if(seed != null) rand.setSeed(seed);

        double randNum = rand.nextDouble(100D);

        for(Map.Entry<String, Double> entry : percents.entrySet()) {
            randNum -= entry.getValue();

            if(randNum <= 0D) return entry.getKey();
        }
        System.err.println("Couldn't pick randomly. Random: " + randNum);
        return "Npc_SouthHateru007";
    }

}
