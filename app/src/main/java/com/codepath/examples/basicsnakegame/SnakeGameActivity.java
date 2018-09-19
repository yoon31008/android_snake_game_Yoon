package com.codepath.examples.basicsnakegame;

import android.content.Context;
import android.os.Bundle;

public class SnakeGameActivity extends com.codepath.simplegame.GameActivity {

	public static Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		switchFullscreen();
		mContext = this;
		setContentView(new SnakeGamePanel(this));
	}
}
