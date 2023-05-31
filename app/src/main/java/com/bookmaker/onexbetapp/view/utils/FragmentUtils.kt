package com.bookmaker.onexbetapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bookmaker.onexbetapp.view.MainActivity

fun Fragment.closeApp(){
    (requireActivity() as MainActivity).destroyApp()
}

fun Fragment.navigateTo(id: Int){
    (requireActivity() as MainActivity).navigateTo(id)
}
fun Fragment.navigateTo(id: Int, data: Bundle){
    (requireActivity() as MainActivity).navigateTo(id, data)
}

val Fragment.regionIsRu: Boolean
    get() = (requireActivity() as MainActivity).region()