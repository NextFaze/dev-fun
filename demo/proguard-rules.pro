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
# DevFun generates a map at compile-time to usages of @DeveloperFunction and @DeveloperCategory that is loaded using
# Java's ServiceLoader (which loads via reflection).
#
# NB: Remember to use the correct package if you override the generated package via APT options.
#
# Keep DevFun generated class
-keep class your.package.goes.here.** extends com.nextfaze.devfun.generated.DevFunGenerated


#
# If you implement your own DevFunModule you may need to explicitly keep it (as it's loaded via ServiceLoader).
#
# Keep DevFunModule
-keep class your.package.goes.here.MyDevFunModule


#
# If you have @DeveloperFunction methods that are unreferenced (e.g. non-public) you will need to tell proguard to keep them.
#
# Keep @DeveloperFunction methods
-keep class your.package.goes.here.** {
    @com.nextfaze.devfun.annotations.DeveloperFunction *;
}


#
# Similarly for non-public @DeveloperCategory annotated classes.
#
# Keep @DeveloperCategory classes
-keep @com.nextfaze.devfun.annotations.DeveloperCategory class your.package.goes.here.**


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








###### These entries are unrelated to DevFun - needed for the purposes of the demo. ######

# Ignore warnings
-dontwarn **

# Keep Logback
-keep class ch.qos.** { *; }

# Keep GlideModule
-keep class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule
