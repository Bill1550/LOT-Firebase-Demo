package com.loneoaktech.test.firebasedemo.utility

import android.os.Bundle
import android.os.Parcelable
import timber.log.Timber

/**
 * Created by BillH on 1/28/2019
 */

fun Bundle.toMap(): Map<String,Any?> = object : Map<String,Any?> {
    private val bundle = this@toMap
    
    override val entries: Set<Map.Entry<String, Any?>>
        get() = bundle.keySet().map { k ->
            object: Map.Entry<String,Any?> {
                override val key: String
                    get() = k
                override val value: Any?
                    get() = bundle.get(k)
            }
        }.toSet()

    override val keys: Set<String>
        get() = bundle.keySet()

    override val size: Int
        get() = bundle.size()

    override val values: Collection<Any?>
        get() = bundle.keySet().map { bundle.get(it) }

    override fun containsKey(key: String): Boolean = bundle.containsKey(key)

    override fun containsValue(value: Any?): Boolean {
        return values.firstOrNull { it == value } != null
    }

    override fun get(key: String): Any? = bundle.get(key)

    override fun isEmpty(): Boolean =  bundle.isEmpty
}

/**
 * Converts a map to a bundle as long as the map contains bundle-able types.
 *
 */
fun Map<String,Any?>.toBundle(): Bundle = Bundle().apply {
    this@toBundle.forEach { (k, value) ->
        value?.let { v ->
            when (v){
                is String -> putString(k, v)
                is Boolean -> putBoolean(k, v)
                is BooleanArray -> putBooleanArray(k, v)
                is Short -> putShort(k, v)
                is ShortArray -> putShortArray(k, v)
                is Int -> putInt(k, v)
                is IntArray -> putIntArray(k, v)
                is Long -> putLong(k, v)
                is LongArray -> putLongArray(k, v)
                is Float -> putFloat(k, v)
                is FloatArray -> putFloatArray(k, v)
                is Double -> putDouble(k, v)
                is DoubleArray -> putDoubleArray(k, v)
                is Bundle -> putBundle(k, v)
                is Parcelable -> putParcelable(k, v)
                else -> throw IllegalArgumentException("Type not support for bundle conversion: ${v.javaClass.name}")
            }
        } ?: putString(k, null)

    }
}

/**
 * Default bundle equals doesn't seem to work
 */
fun Bundle?.contentsEquals( other: Bundle?): Boolean {
    if ( this == null)
        return other == null

    if (other == null || other.isEmpty )
        return this.isEmpty

    // check keys
    if (this.keySet() != other.keySet())
        return false

    keySet().forEach { key ->
        if ( get(key) != other.get(key) ) {
            Timber.e("value not equal, key=$key, this='${get(key)}', other='${other.get(key)}'" )
            return false
        }
    }

    return true
}