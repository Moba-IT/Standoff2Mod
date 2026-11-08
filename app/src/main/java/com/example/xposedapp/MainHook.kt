package com.example.xposedapp

import android.util.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage {
    companion object {
        private const val TAG = "Standoff2Mod"
        private const val TARGET_PACKAGE = "com.axlebolt.standoff2"
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != TARGET_PACKAGE) return

        Log.i(TAG, "Target package loaded: ${lpparam.packageName}")

        try {
            // Используем стандартный поиск класса через класс-лоадер приложения
            val outlineClass = XposedHelpers.findClassIfExists("Knife.PostProcessing.OutlineRenderer", lpparam.classLoader)
            
            if (outlineClass != null) {
                Log.i(TAG, "Found OutlineRenderer class successfully!")
                
                XposedHelpers.findAndHookMethod(outlineClass, "Render", object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        Log.d(TAG, "Hooked OutlineRenderer.Render method")
                    }
                })
            } else {
                Log.i(TAG, "OutlineRenderer class not found yet.")
            }
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to apply hooks", e)
        }
    }
}
