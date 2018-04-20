# Releasing
In general it is desirable to release all aspects of DevFun even for untouched modules for point-releases etc.  
This is to done to simplify the documentation and installation process for users, as well as for our own sanity, rather than having different
versions for every other module.


<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->


- [Release Process](#release-process)
  - [PlantUML](#plantuml)
  - [Doctoc](#doctoc)
  - [GitHub Pages](#github-pages)
  - [Artifacts](#artifacts)
    - [Modules](#modules)
    - [Gradle Plugin](#gradle-plugin)
- [TLDR;](#tldr)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Release Process

Due to the different module types/aspects (a Gradle plugin, java and android libs, Dokka/Javadoc generation, GitHub pages, etc.) there are a
number of steps that need to be run in a specific order:

1. Finalize and tag release commit (latest git tag is in referenced in documentation)
2. Update [UML](#plantuml) (if needed)
3. Update [Doctoc](#doctoc) (README, RELEASING, and Wiki etc.) _before GH Pages as it may affect Wiki object line numbers_
4. Update [GitHub Pages](#github-pages) _must be done before artifact release for documentation linking_
5. Build/upload [Artifacts](#artifacts)
6. Merge/push changes/tags etc. _should be done after artifacts are deployed to ensure README is valid at time of push_


## PlantUML
[PlantUML](http://plantuml.com/) is used to generate UML images (gh-pages/assets/uml/).

To (re)generate UML images/image maps:
```bash
./gradlew renderPlantUml
```


To preview PlantUML in the IDE there is an IntelliJ/Android Studio plugin.  
For linux (and probably Mac, not sure about Windows) you will also need graphviz:
```bash
sudo apt install graphviz
``` 


## Doctoc
[Doctoc](https://github.com/thlorenz/doctoc/) is used to generate the README and various Wiki entries table of contents.

Install using npn:
```bash
npm install -g doctoc
```

It is used for the README and RELEASING documents and should be run whenever they are altered:
```bash
doctoc *.md --github --notitle
```

It is also used in the Wiki (currently only `Components.kt`), however the Dokka task will handle that automatically (see
 `dokka/build.gradle.kts#L123`), but if need be can be run manually (due to issues with Dokka generation we need to adjust the output with
  `sed` as well):
```bash
doctoc dokka/src/main/java/wiki/Components.kt --github --notitle && sed '/<!-- START doctoc generated TOC/,/<!-- END doctoc generated TOC/s/^( *)/ * \1\1/' -ri dokka/src/main/java/wiki/Components.kt
```


## GitHub Pages
Both the external documentation and "wiki" use GitHub Pages.

Artifact Javadoc generation (via Dokka) links the external documentation during generation of artifacts,
thus it is necessary to do this *prior* to that (specifically, Dokka looks for the generated `package-list` file).
 
1. Checkout `gh-pages` branch in project directory named `gh-pages` (folder is in main project `.gitignore`)  
   Alternatively checkout elsewhere and create symlink:
   ```bash
   git clone -b gh-pages --single-branch git@github.com:NextFaze/dev-fun.git gh-pages
   ln -s path/to/gh-pages gh-pages # if cloned elsewhere
   ```

2. Update GH-Pages (Dokka)  
    Run `dokka` task (this will also clear previously generated Dokka files):
   ```bash
   ./gradlew dokka
    ```

3. Commit/push changes: (substitute version)
   ```bash
   cd gh-pages
   git add .
   git commit -m "Update for 1.1.0"
   git tag 1.1.0-pages
   git push && git push --tags
   ```


## Artifacts
All libraries/modules/plugins should be tagged/published as the latest version even if there were no changes to simplify the documentation
and installation process for users.

When adding a new module you also need to tell BinTray to add the artifact to JCenter (via bintray on the artifact's page).  
The final push/merge to master should be delayed until you receive the notification from JCenter that the artifact has been added (~12 hours).

### Modules
- **Note: Due to Gradle and/or BinTray plugin issues, parallel task execution may result in skipped/empty uploads!**    
    _Typically this is enabled with `org.gradle.parallel=true` in your user/project `gradle.properties`._  
    _You can tell if you have it enabled if the first line of your Gradle output has something like "Parallel execution is an incubating feature"._
    - **If this is enabled, you must disable it (and `./gradlew --stop`), or manually call each module's `:module:bintrayUpload` task to ensure successful deployment.**  
     
This should be done *after* GitHub Pages (Dokka) due to external documentation linking.
- Snapshots are defined by `gradle.properties` value `VERSION_SNAPSHOT` (true/false).  
- Remember to check BinTray task options `dryRun`, `publish`, and `override`, and also `mavenCentralSync.sync`. 

```bash
./gradlew clean bintrayUpload
```

_This can take 10-15 minutes!_

### Gradle Plugin
This should be done after the artifacts are updated.

```bash
./gradlew publishPlugins
```


# TLDR;
- On local copy merge in release to master, and if needed, run doctoc, uml generation, etc.
- Add git tag for release
- Turn off snapshot flag `VERSION_SNAPSHOT=false` in `gradle.properties`
- Turn off dryRun flag `dryRun = false` in `publishing.gradle`
- Ensure gradle parallel is turned off (`org.gradle.parallel=false`)
- Run commands:
    ```bash
    git tag 1.1.0
    ./gradlew clean
    ./gradlew assemble
    ./gradlew dokka
    ./gradlew bintrayUpload
    ./gradlew publishPlugins
   cd gh-pages
   git add .
   git commit -m "Update for 1.1.0"
   git tag 1.1.0-pages
   git push && git push --tags
   cd ..
   git push && git push --tags
    ```
