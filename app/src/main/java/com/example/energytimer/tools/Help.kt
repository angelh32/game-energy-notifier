package com.example.energytimer.tools

import android.util.Log

class Help {
	companion object {
		fun printLog(origin: String, message: String){
			Log.i("APX-$origin", message)
		}
	}
}