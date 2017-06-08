#
# To log memory info we need to access Glide.memoryCache and BitmapPool.currentSize via reflection.
#

-keepclassmembernames class com.bumptech.glide.Glide {
    ** memoryCache;
}

-keepclassmembernames class com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool {
    *** currentSize;
}
