# Breath of the Wild Randomizer
[![CodeFactor](https://www.codefactor.io/repository/github/roccodev/botw-randomizer/badge)](https://www.codefactor.io/repository/github/roccodev/botw-randomizer)
[![Maintainability](https://api.codeclimate.com/v1/badges/93dcbe571540aa9c5e45/maintainability)](https://codeclimate.com/github/RoccoDev/botw-randomizer/maintainability)
[![Build Status](https://travis-ci.org/RoccoDev/botw-randomizer.svg?branch=master)](https://travis-ci.org/RoccoDev/botw-randomizer)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a037a977bc444ff7ac8f87c0df323377)](https://www.codacy.com/app/RoccoDev/aamp-java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=RoccoDev/aamp-java&amp;utm_campaign=Badge_Grade)
![Downloads](https://img.shields.io/github/downloads/RoccoDev/botw-randomizer/total.svg)

![Preview](https://rocco.dev/img/botw-rando.png)
*Here the built-in `Normal` profile changed the Bokoblin to a Lizalfos.*

## How does this work?
This randomizer generates data files to put into the `contents` folder of the Breath of the Wild ROM.

## Does this work on a real console?
The generated files work on both an emulator and a real Wii U.  
To load the files on a real console, use [SDCafiine](https://github.com/Maschell/SDCafiine).

## How do I add new profiles?
You can add them in the `RandomizerProfiles` folder.  
If you wish to make them public, fork [this repository](https://github.com/RoccoDev/botw-randomizer-profiles) and submit a pull request.

## Contributing
First, clone the repository.
```
git clone --recursive https://github.com/RoccoDev/botw-randomizer
```
Then, run
```
sh build.sh
```
It will build the project and generate a zip file with everything needed.
