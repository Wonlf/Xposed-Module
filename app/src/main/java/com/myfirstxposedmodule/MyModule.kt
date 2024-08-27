package com.myfirstxposedmodule

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MyModule : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        // Filtering unnecessary applications
        if (lpparam.packageName != "me.kyuubiran.xposedapp") return
        // Execute Hook
        hook(lpparam)
    }

    private fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        val main = XposedHelpers.findClass("me.kyuubiran.xposedapp.MainActivity", lpparam.classLoader)
        val guess = XposedHelpers.findClass("me.kyuubiran.xposedapp.Guess", lpparam.classLoader)

        XposedHelpers.findAndHookMethod(main, "onCreate", Bundle::class.java, object : XC_MethodHook() { //메인 액티비티 onCreate 후킹
            override fun afterHookedMethod(param: MethodHookParam) {
                Toast.makeText(param.thisObject as Activity, "Module loaded successfully!" , Toast.LENGTH_SHORT).show()
            }
        })


        XposedHelpers.findAndHookMethod(
            "me.kyuubiran.xposedapp.Guess",
            lpparam.classLoader,
            "isDraw",
            Int::class.java, // If the parameter is the host class, you can use findClass to load that class or fill in the full name of that class!
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    // Set the return value to false to indicate that we are not tied
                    param.result = false
                }
            })

        XposedHelpers.findAndHookMethod(
            "me.kyuubiran.xposedapp.Guess",
            lpparam.classLoader,
            "isWin",
            Int::class.java, // If the parameter is the host class, you can use findClass to load that class or fill in the full name of that class!
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    // Set the return value to false to indicate that we are not tied
                    param.result = true
                }
            })




    }
}