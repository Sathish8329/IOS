# Remove unused code
-dontshrink
-dontoptimize

# Rename all variables and methods
-renamesourcefileattribute SourceFile
-renameclassattribute Class

# Obfuscate the code
-obfuscate
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class name{*;}
-keepattributes InnerClass$*{*;}

# Keep important classes and methods
-keep class com.canaraswayam.MainActivity {
  public void onCreate(android.os.Bundle);
}

-keep class com.canaraswayam.SomeClass {
  public static void someMethod();
}

-keep class com.facebook.hermes.unicode.** { *; }
-keep class com.facebook.jni.** { *; }