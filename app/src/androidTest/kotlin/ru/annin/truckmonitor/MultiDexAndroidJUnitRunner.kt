package ru.annin.truckmonitor

import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.test.runner.AndroidJUnitRunner

/**
 * @author Pavel Annin.
 */
class MultiDexAndroidJUnitRunner : AndroidJUnitRunner() {
    override fun onCreate(arguments: Bundle) {
        //To make it work on MultiDex environment.
        //https://plus.google.com/+OleksandrKucherenko/posts/i7qZdVEy3Ue
        MultiDex.install(targetContext)
        super.onCreate(arguments)
    }
}