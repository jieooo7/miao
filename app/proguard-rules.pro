# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class mallprice to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-dontoptimize
-ignorewarnings
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes Signature


-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-keep class com.ut.** {*;}
-dontwarn com.ut.**
-keep class com.ta.** {*;}
-dontwarn com.ta.**

-dontwarn android.support.**

-keep class android.support.v4.app.** { *; }

-keep interface android.support.v4.app.** { *; }
-keep class * extends java.lang.annotation.Annotation { *; }

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.FragmentActivity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService


-keepclassmembers class * extends android.support.v4.app.Fragment {
  public *;
}


#忽略警告
-ignorewarning

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#-keepclassmembers enum * {
#  public static **[] values();
#  public static ** valueOf(java.lang.String);
#}


# webview + js
-keepattributes *JavascriptInterface*
# keep 使用 webview 的类
-keepclassmembers class  com.veidy.activity.WebViewActivity {
   public *;
}
# keep 使用 webview 的类的所有的内部类
-keepclassmembers  class  com.veidy.activity.WebViewActivity$*{
    *;
}

# 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}



-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}


-dontwarn com.igexin.**
-keep class com.igexin.**{*;}

-dontwarn com.baidu.**
-keep class com.baidu.**{*;}

-keep class vi.com.gdi.bgl.android.**{*;}

-dontwarn com.google.**
-keep class com.google.**{*;}

-dontwarn com.android.volley.**
-keep class com.android.volley.**{*;}

-dontwarn com.json.simple.**
-keep class com.json.simple.**{*;}

-dontwarn com.lidroid.**
-keep class com.lidroid.**{*;}

-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn cn.sharesdk.**
-dontwarn **.R$*

-keep class m.framework.**{*;}
-keep class com.liuzhuni.lzn.GetInfo{*;}

-keep class * extends java.lang.annotation.Annotation { *; }
-keep class android.support.** {*;}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class * implements java.io.Serializable {
    *;
}

#-keep class com.liuzhuni.lzn.volley.** {*;}
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* { *; }
-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }
-keep class com.android.volley.*{*; }
-keep class com.android.volley.toolbox.*{*; }


-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
   }

-keepclassmembers class * {
      public <init>(org.json.JSONObject);
   }
-keep public class com.liuzhuni.lzn.R$*{
public static final int *;
}

-keep class com.umeng.**{*;}

-keep interface com.android.volley.Response$Listener {
*;
}
-keep interface com.android.volley.Response$ErrorListener {
*;
}

-keepattributes Signature
-keep class com.google.gson.** { *; }

#gson
#-libraryjars libs/gson-2.2.2.jar
# Gson specific classes
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }