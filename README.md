[![Discord](https://img.shields.io/discord/1121806340873015327.svg?color=7289da&logo=discord&label=ME13-General&style=for-the-badge)](https://discord.gg/Zg7GT84uEy)
[![Stars](https://img.shields.io/github/stars/MindustryExtended13/MaterialEnergy?color=7289da&label=⭐️%20Star%20Material%20Energy%21&style=for-the-badge)](https://github.com/MindustryExtended13/MaterialEnergy)
[![Download](https://img.shields.io/github/v/release/MindustryExtended13/MaterialEnergy?color=6aa84f&include_prereleases&label=Latest%20version&logo=github&logoColor=white&style=for-the-badge)](https://github.com/MindustryExtended13/MaterialEnergy/releases)[![Total Downloads](https://img.shields.io/github/downloads/MindustryExtended13/MaterialEnergy/total?color=7289da&label&logo=docusign&logoColor=white&style=for-the-badge)](https://github.com/MindustryExtended13/MaterialEnergy/releases)

# ME13: Material Energy

## Building for Desktop Testing

1. Install JDK **17**.
2. Run `gradlew jar` [1].
3. Your mod jar will be in the `build/libs` directory. **Only use this version for testing on desktop. It will not work with Android.**
To build an Android-compatible version, you need the Android SDK. You can either let Github Actions handle this, or set it up yourself. See steps below.

## Building through Github Actions

This repository is set up with Github Actions CI to automatically build the mod for you every commit. This requires a Github repository, for obvious reasons.
To get a jar file that works for every platform, do the following:
1. Make a Github repository with your mod name, and upload the contents of this repo to it. Perform any modifications necessary, then commit and push. 
2. Check the "Actions" tab on your repository page. Select the most recent commit in the list. If it completed successfully, there should be a download link under the "Artifacts" section. 
3. Click the download link (should be the name of your repo). This will download a **zipped jar** - **not** the jar file itself [2]! Unzip this file and import the jar contained within in Mindustry. This version should work both on Android and Desktop.

## Building Locally

Building locally takes more time to set up, but shouldn't be a problem if you've done Android development before.
1. Download the Android SDK, unzip it and set the `ANDROID_HOME` environment variable to its location.
2. Make sure you have API level 30 installed, as well as any recent version of build tools (e.g. 30.0.1)
3. Add a build-tools folder to your PATH. For example, if you have `30.0.1` installed, that would be `$ANDROID_HOME/build-tools/30.0.1`.
4. Run `gradlew deploy`. If you did everything correctlly, this will create a jar file in the `build/libs` directory that can be run on both Android and desktop.
