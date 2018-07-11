package com.demo.test.analy

import android.util.Log
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

class SAXPraserHelper(var nodeName:String): DefaultHandler() {

    companion object {
        const val DEBUG = true
        const val TAG = "SAXPraserHelper"
    }

    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        super.startElement(uri, localName, qName, attributes)
        if (DEBUG) {
            Log.i(TAG, "startDocument :$qName")
        }
        if (qName.equals(nodeName)) {

        }

    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        super.endElement(uri, localName, qName)
        if (DEBUG) {
            Log.i(TAG, "endElement:$qName")
        }
    }

    override fun startDocument() {
        super.startDocument()
        if (DEBUG) {
            Log.i(TAG, "startDocument:")
        }
    }

    override fun endDocument() {
        super.endDocument()
        if (DEBUG) {
            Log.i(TAG, "endDocument:")
        }
    }
}