# Xander's Fabric Template for Kotlin
The best Fabric template using kotlin.

## Usage
**Mod Development**:
* Please read the [Fabric Wiki](https://fabricmc.net/wiki) for general mod development help.

**Using the template**
* Click the big green `Use this template` button to clone the template to your GitHub.
* Replace relevant information in the files
    * `build.gradle.kts`
        * `group`
        * `version`
    * `src/main/resources/fabric.mod.json`
        * basically everything in there
    * `LICENSE`
        * I recommend you use the [LGPL 2.1](https://www.gnu.org/licenses/lgpl-2.1.html) license
    * `README.md`
        * Can't forget this! It's what your reading now!
* Once set up in your IDE of choice, you will probably want to run your mod. Here are a few options
    * You can use `gradlew run` to run from the command line.
    * You can create a run configuration in your IDE with the gradle commands `run`.
    * Use the automatically populated run configuration that fabric-loom generated for you. (sometimes this doesn't work)
    * Run in a production environment by building a jar with `gradlew build`.

## License
* This template is licensed under [The Unlicense](https://unlicense.org/) in an attempt to gift this project to the public domain.
  (you can use this however you want)
