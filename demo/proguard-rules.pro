######
#
# The DevFun libraries will work with proguard, but only cover their own sources.
#
# If you want your app to also function with proguard you will need effectively the same configuration that DevFun uses
# but using your own package.
#
######




###### The following entries are required for the app to function with DevFun when proguard is enabled. ######
#
# These keeps will only retain DevFun's generated sources.
#
# Both public and internal references will be directly referenced from generated sources and thus should be kept
# transitively. However references that are private or package-private (Java) are referenced with reflection and will
# not be kept automatically if there is nothing else (in your code) referencing them, and you will need to @Keep them
# or something.
#
######


#
# DevFun generates a map at compile-time to usages of @DeveloperFunction and @DeveloperCategory that is loaded using
# Java's ServiceLoader (which loads via reflection).
#
# NB: Remember to use the correct package if you override the generated package via APT options.
#
# Keep DevFun generated class
-keep class your.package.goes.here.** extends com.nextfaze.devfun.generated.DevFunGenerated


#
# Kotlin objects generate an "INSTANCE" field. To retrieve a KObject at runtime it must remain named as such.
#
# Don't rename KObject INSTANCE field
-keepclassmembernames class your.package.goes.here.** {
    public static final ** INSTANCE;
}


#
# If you use @Constructable then we need to ensure they (and their contructor) are kept.
#
# Keep @Constructable types and their constructor
-keep @com.nextfaze.devfun.inject.Constructable class your.package.goes.here.** {
    <init>(...);
}


#
# This is only needed if you implement your own DevFunModule (as it's loaded via ServiceLoader).
#
# Keep DevFunModule
-keep class your.package.goes.here.MyDevFunModule






###### These entries are unrelated to DevFun - needed for the purposes of the demo. ######

# Ignore warnings
-dontwarn **

# For Retrace
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# Logback
-keep class ch.qos.** { *; }

# Glide
-keep class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule

# Joda Time
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }
