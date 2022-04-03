package com.bob;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
<<<<<<< HEAD
		config.useCompass = false;
		//config.useWakelock = true;
=======
>>>>>>> a0e9a47 (init bob)
		initialize(new Start(), config);
	}
}
