package com.gilded.thegame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "TheGame";
		cfg.useGL20 = true;
		cfg.fullscreen = false;
		cfg.resizable = false;
		cfg.width = 800;
		cfg.height = 451;
		cfg.vSyncEnabled = true;
		
		new LwjglApplication(new TheGame(), cfg);
 	}
}
