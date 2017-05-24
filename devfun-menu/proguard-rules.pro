#
# Using proguard removes META-INF/dev-module_release.kotin_module for final JAR (needed for kotlin-reflection)
# So for now proguard is disabled.
#
#
-injars /home/awaters/Developer/dev-fun/menu/build/intermediates/classes/release/META-INF(**.*)

-printconfiguration proguard-config.txt
-printmapping proguard-mapping.txt
-printseeds proguard-seeds.txt

-dontwarn **

# -dontshrink
-keepattributes *
-keepnames class *
-dontoptimize
-dontobfuscate

-keep class com.nextfaze.devfun.** { *; }
-keeppackagenames com.nextfaze.devfun.**

-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

-keep class kotlin.** { *; }

-keepclassmembers,allowoptimization enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    **[] $VALUES;
    public *;
}

-keepattributes InnerClasses
