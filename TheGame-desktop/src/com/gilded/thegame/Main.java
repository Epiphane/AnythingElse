package com.gilded.thegame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "TheGame";
		cfg.useGL20 = false;
		cfg.fullscreen = false;
		cfg.resizable = false;
		cfg.width = 1600;
		cfg.height = 900;
		cfg.vSyncEnabled = true;
		
		new LwjglApplication(new TheGame(), cfg);
	}
}
