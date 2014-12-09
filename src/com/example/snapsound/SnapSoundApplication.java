package com.example.snapsound;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class SnapSoundApplication extends Application {
	
	public void onCreate() {
		
		  Parse.initialize(this, "9s7onecmVBfemhPNfnmRHyPLsJt2OrIzUs3oRYT2", "PVUvjVRUTXxcAfSu288QURSn4CxScr2bjLhELsDD");
		
	
	}

}
