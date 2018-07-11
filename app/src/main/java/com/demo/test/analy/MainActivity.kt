package com.demo.test.analy

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.util.Xml
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException


class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "AnalyAPK_MainActivity"
    }

    lateinit var button:Button
    lateinit var textView: TextView
    lateinit var imageView: ImageView

    lateinit var mResource:Resources

    lateinit var wallpaperPkgName:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button1)
        textView = findViewById(R.id.textview)
        imageView = findViewById(R.id.imageview)

        val pkm = packageManager
        val action = "com.demo.action.livepaper"

        val intent1 = Intent()
        intent1.action = action
        val resolveInfos = pkm.queryBroadcastReceivers(intent1, 0)

        Log.i(TAG, "onCreate resolveInfos:$resolveInfos")
        for (i in resolveInfos.indices) {
            wallpaperPkgName = resolveInfos[i].activityInfo.packageName
            Log.i(TAG, "onCreate pkgname:" + resolveInfos[i].activityInfo.packageName)
        }

        var wallpaperContext = createPackageContext(wallpaperPkgName,0)

        mResource = wallpaperContext.resources



        button.setOnClickListener {
            val analyApkTask = AnalyApkTask(mResource,wallpaperPkgName,this@MainActivity)
            analyApkTask.execute()
        }

    }

    inner class AnalyApkTask(resources: Resources,pkgName:String,context: Context): AsyncTask<Void, Void, Drawable?>() {

        val res = resources
        val packageName = pkgName
        val context = context

        override fun doInBackground(vararg p0: Void?): Drawable? {
            Log.i(TAG, "doInBackground:")
            var string:String? = null
            var drawable:Drawable? = null
            var resId = res.getIdentifier("wallpaper","xml",packageName)
            if (resId != null && resId != 0) {

                val tv = TypedValue()
                res.getValue(resId, tv, false)
                Log.d(TAG, "[getXml] cookie =" + tv.assetCookie)
                Log.d(TAG, "[getXml] resId =$resId")

//                drawable = res.getDrawable(WallpapaerR.wallpaper_preview)
                string = res.getString(WallpapaerR.wallpaper_name)

                Log.i(TAG, "doInBackground string:$string")

                var xmlParse = res.getXml(resId)

                drawable = parseAndroidXml(xmlParse,res,packageName,context)

            }

            return drawable
        }


        override fun onPostExecute(result: Drawable?) {
            Log.i(TAG, "onPostExecute:$result" )
            imageView.setImageDrawable(result)

            super.onPostExecute(result)
        }

        @SuppressLint("ResourceType")
        @Throws(XmlPullParserException::class, IOException::class)
        private fun parseAndroidXml(parser: XmlResourceParser,resources: Resources,pkgName: String,context: Context):Drawable? {

            var tagType = parser.eventType
            var drawable:Drawable? = null
            var wallpaperName = ""
            while (tagType != XmlPullParser.END_DOCUMENT){
                when (tagType) {
                    XmlPullParser.START_DOCUMENT -> {
                        Log.i(TAG, "parseAndroidXml START_DOCUMENT:$tagType")
                    }
                    XmlPullParser.START_TAG -> {
                        val tagName = parser.name
                        if ("pic" == tagName) {
                            val attr = Xml.asAttributeSet(parser)

                            var typedArray = resources.obtainAttributes(attr,WallpapaerR.wallpaper_preview_test_src)

                            drawable = typedArray.getDrawable(0)

                            Log.d(TAG, "getTagName finished tagName = $tagName")
                            Log.d(TAG, "getTagName finished wallpaperName = $wallpaperName")
                            typedArray.recycle()

                        }


                    }
                    XmlPullParser.END_DOCUMENT -> {
                        Log.i(TAG, "parseAndroidXml END_DOCUMENT:")
                    }

                }
                tagType = parser.next()
                Log.i(TAG, "parseAndroidXml next:$tagType")
            }

            return drawable

        }

    }
}
