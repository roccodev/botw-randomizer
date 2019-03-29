package dev.rocco.botw.randomizer.profile;

public interface RandomizerPatch {

    void patch(RandomizerProfile profile, Object in);

    int getType();

}
