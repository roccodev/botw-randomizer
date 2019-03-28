# Breath of the Wild Randomizer

# Build using Gradle
gradle clean build

# Include default files into the build
cp -a include/* build/libs
mkdir build/libs/botw-randomizer
mv build/libs/* build/libs/botw-randomizer
(cd build/libs/; zip -r ../../botw-randomizer.zip .)