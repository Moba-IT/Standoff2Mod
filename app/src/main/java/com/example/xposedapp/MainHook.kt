package com.example.xposedapp

import android.util.Log
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage {
    companion object {
        private const val TAG = "Standoff2Mod"
        private const val TARGET_PACKAGE = "com.axlebolt.standoff2"
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != TARGET_PACKAGE) return

        EzXHelperInit.initAllHooks(lpparam)
        Log.i(TAG, "Target package loaded: ${lpparam.packageName}")

        try {
            loadClassOrNull("Knife.PostProcessing.OutlineRenderer")?.let { outlineClass ->
                Log.i(TAG, "Found OutlineRenderer class successfully!")
                
                outlineClass.hookBefore("Render", "UnityEngine.Rendering.PostProcessing.PostProcessRenderContext") { param ->
                    Log.d(TAG, "Hooked OutlineRenderer.Render method")
                }
            }
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to apply hooks", e)
        }
    }
}
