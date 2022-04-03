package com.bob;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
<<<<<<< HEAD
		config.setWindowSizeLimits(480, 320, 480, 320);
=======
		//config.setWindowSizeLimits(234, 233, 666, 666);
>>>>>>> 518773d (init bob)
		new Lwjgl3Application(new Start(), config);
	}
}
