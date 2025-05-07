package dev.byjtech.erp

import android.content.Context

object AndroidEnv {
    private var props: Map<String, String>? = null

    fun init(context: Context) {
        if (props == null) {
            val map = mutableMapOf<String, String>()
            try{
                context.assets.open("env.properties").bufferedReader().useLines { lines ->
                    lines.filter { it.isNotBlank() && !it.startsWith("#") }
                        .forEach {
                            val (key, value) = it.split("=", limit = 2).map { it.trim() }
                            map[key] = value
                        }
                }
                props = map
            } catch (e: Exception){
                throw IllegalStateException("Error opening env.properties", e)
            }

        }
    }

    fun get(key: String): String {
        val value = props?.get(key)
        if (value.isNullOrBlank()) {
            throw IllegalStateException("Missing or invalid environment property: $key")
        }
        return value
    }
}