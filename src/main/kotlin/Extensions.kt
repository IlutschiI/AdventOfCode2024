package org.example

fun String.readAsString(): String {
    return object{}.javaClass.classLoader.getResource(this)!!.readText()
}