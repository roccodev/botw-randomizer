package dev.rocco.botw.randomizer.profile.patch;

import dev.rocco.botw.randomizer.profile.RandomizerPatch;
import dev.rocco.botw.randomizer.profile.RandomizerProfile;

public class ShrinePatch implements RandomizerPatch {

    private String hashId;
    private String dungeonId;

    private String archivePath;
    private String fileInArchivePath;

    @Override
    public void patch(RandomizerProfile profile, Object in) {

    }

    public ShrinePatch(String hashId, String dungeonId) {
        this.hashId = hashId;
        this.dungeonId = dungeonId;

        archivePath = "Pack/Dungeon" + dungeonId + ".pack";
        fileInArchivePath = "Map/Dungeon" + dungeonId + ".smubin";
    }


    @Override
    public int getType() {
        return 1;
    }
}
