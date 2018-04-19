# Releasing

1. Finalize and tag release commit (latest git tag is in referenced in documentation)
2. Update [UML](#plantuml) (if needed)
3. Update [Doctoc](#doctoc) (README and Wiki etc.) _before GH Pages as it may affect Wiki object line numbers_
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

It is used for the README and should be run whenever it is altered:
```bash
doctoc README.md --github --notitle
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
   git commit -m "Update for 1.0.2"
   git tag 1.0.2-pages
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
