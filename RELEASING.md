# Releasing

1. Finalize and tag release commit (latest git tag is in referenced in documentation)
2. Update [UML](#plantuml) (if needed)
3. Update [Doctoc](#doctoc) (README and Wiki etc.) _before GH Pages as it may affect Wiki object line numbers_
4. Update [GitHub Pages](#github-pages) _must be done before artifact release for documentation linking_
5. Build/upload [Artifacts](#artifacts)


## PlantUML
[PlantUML](http://plantuml.com/) is used to generate UML images (gh-pages/assets/uml/).

This is a little cumbersome at the moment (need to look into plugin or something).  
To use it, you'll need to grab the jar from the website and set the path ([renderPlantUml()](plant_uml.gradle)).
```bash
./gradlew renderPlantUml -PplantUmlJar=<path to jar> 
```


## Doctoc
[Doctoc](https://github.com/thlorenz/doctoc/) is used to generate the README and various Wiki entries table of contents.

Install using npn `npm install -g doctoc`.  
Should be run whenever README/Wiki is altered.

Currently needed:
```bash
doctoc README.md --github --notitle
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
   git commit -m "Update for 0.1.3"
   git tag 0.1.3
   git push --all
   ```


## Artifacts
- **Note: Due to Gradle and/or BinTray plugin issues, parallel task execution may result in skipped/empty uploads!**    
    _Typically this is enabled with `org.gradle.parallel=true` in your user/project `gradle.properties`._  
    _You can tell if you have it enabled if the first line of your Gradle output has something like "Parallel execution is an incubating feature"._
    - **If this is enabled, you must disable it (and `./gradlew --stop`), or manually call each module's `:module:bintrayUpload` task to ensure successful deployment.**  
     
This should be done *after* GitHub Pages (Dokka) due to external documentation linking.

- Snapshots are defined by `gradle.properties` value `VERSION_SNAPSHOT` (true/false).  
- Remember to check BinTray task options `dryRun`, `publish`, and `override`, and also `mavenCentralSync.sync`. 

```bash
./gradlew bintrayUpload
```
