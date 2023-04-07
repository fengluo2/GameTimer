package com.maple.gametimer.utils

import android.content.Context
import android.util.Log
import java.lang.reflect.Field;

object ResourceUtil {
    private val TAG: String = "ResourceUtil"

    fun getAnimId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "anim", context.packageName)
    }

    fun getAnimatorId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "animator", context.packageName)
    }

    fun getAttrId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "attr", context.packageName)
    }

    fun getBoolId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "bool", context.packageName)
    }

    fun getColorId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "color", context.packageName)
    }

    fun getDimenId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "dimen", context.packageName)
    }

    fun getRawId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "raw", context.packageName)
    }

    fun getDrawableId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "drawable", context.packageName)
    }

    fun getId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "id", context.packageName)
    }

    fun getIntegerId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "integer", context.packageName)
    }

    fun getInterpolatorId(context: Context, defType: String?): Int {
        return context.resources
            .getIdentifier(defType, "interpolator", context.packageName)
    }

    fun getLayoutId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "layout", context.packageName)
    }

    fun getPluralsId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "plurals", context.packageName)
    }

    fun getStringId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "string", context.packageName)
    }

    fun getStyleId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "style", context.packageName)
    }

    fun getStyleableId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "styleable", context.packageName)
    }

    fun getXmlId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "xml", context.packageName)
    }

    fun getMipmapId(context: Context, defType: String?): Int {
        return context.resources.getIdentifier(defType, "mipmap", context.packageName)
    }

    /**
     * 通过反射来读取int[]类型资源Id
     * @param context
     * @param name
     * @return
     */
    fun getResourceDeclareStyleableIntArray(context: Context, name: String?): Any? {
        try {
            val fields2: Array<Field> =
                Class.forName(context.packageName + ".R\$styleable").fields
            for (f in fields2) {
                if (f.name.equals(name)) {
                    return f.get(null)
                }
            }
        } catch (t: Throwable) {
            Log.e(TAG, "getResourceDeclareStyleableIntArray: " + t.message)
        }
        return null
    }
}
