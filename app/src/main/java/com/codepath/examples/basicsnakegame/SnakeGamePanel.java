package com.codepath.examples.basicsnakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.codepath.simplegame.AbstractGamePanel;

public class SnakeGamePanel extends AbstractGamePanel {

	public SnakeGamePanel(Context context) {
		super(context);
	}

	private SnakeActor snake;
	private AppleActor apple;
	private ScoreBoard score;
	private boolean isPaused = false;

	private float gameRestartRect_width = 350;
	private float gameRestartRect_height = 100;
	private float gameRestartRect_left;
	private float gameRestartRect_top;
	private float gameRestartRect_right;
	private float gameRestartRect_bottom;
	private float gameRestartText_left;
	private float gameRestartText_bottom;

	private float gameOverText_bottom;

	private float drawControllButton_width = 400;
	private float drawControllButton_height = 400;
	private float drawControllButton_left;
	private float drawControllButton_right;
	private float drawControllButton_top;
	private float drawControllButton_bottom;

	Paint myPaint = new Paint();
	Paint myPaint2 = new Paint();
	Paint myPaint3 = new Paint();

	@Override
	public void onStart() {
		this.snake = new SnakeActor(100, 100);
		this.apple = new AppleActor(200, 50);
		this.score = new ScoreBoard(this);

		gameRestartRect_left = getWidth() / 2 - gameRestartRect_width / 2;
		gameRestartRect_top = getHeight() * 4/5 - gameRestartRect_height / 2;
		gameRestartRect_right = gameRestartRect_left + gameRestartRect_width;
		gameRestartRect_bottom = gameRestartRect_top + gameRestartRect_height;
		gameRestartText_left = gameRestartRect_left + 8;
		gameRestartText_bottom = gameRestartRect_bottom - 15;

		gameOverText_bottom = getHeight() * 1/3;

		drawControllButton_left = getWidth() / 2 - drawControllButton_width / 2;
		drawControllButton_top = getHeight() / 2 - drawControllButton_height / 2;
		drawControllButton_right = drawControllButton_left + drawControllButton_width;
		drawControllButton_bottom = drawControllButton_top + drawControllButton_height;
	}

	@Override
	public void onTimer() {
		if (!isPaused) {
			if (this.snake.checkBoundsCollision(this)) {
				this.snake.setEnabled(false);
			}
			this.snake.move();
			if (this.apple.intersect(this.snake)) {
				this.snake.grow();
				this.score.earnPoints(50);
				this.apple.reposition(this);
			}
		}
	}



	@Override
	public void redrawCanvas(Canvas canvas) {
		if (this.snake.isEnabled()) {

			drawControllButton(canvas);
			this.snake.draw(canvas);
			this.apple.draw(canvas);
			this.score.draw(canvas);

		} else {
			drawRestart(canvas);
			drawGameOver(canvas);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		this.snake.handleKeyInput(keyCode);
		if (keyCode == KeyEvent.KEYCODE_G) {
			this.onStart();
		}
		if (keyCode == KeyEvent.KEYCODE_P) {
			isPaused = !isPaused;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			this.snake.handleTouchInput(event);
			if(!this.snake.isEnabled() && restartTouchInput(event)){
				this.onStart();
			}
			return true;
		}
		return false;
	}

	public boolean restartTouchInput(MotionEvent event){
		if(event.getX() >= gameRestartRect_left && event.getX() <= gameRestartRect_right
				&&
				event.getY() >= gameRestartRect_top && event.getY() <= gameRestartRect_bottom)
			return true;
		else
			return false;
	}

	public void drawRestart(Canvas canvas){
		myPaint.setColor(Color.GREEN);
		myPaint.setStyle(Paint.Style.STROKE);
		myPaint.setStrokeWidth(10);
		canvas.drawRect(gameRestartRect_left, gameRestartRect_top, gameRestartRect_right, gameRestartRect_bottom, myPaint);

		myPaint2.setTextSize(95);
		myPaint2.setStrokeWidth(3);
		myPaint2.setColor(Color.GREEN);
		canvas.drawText("Restart!", gameRestartText_left, gameRestartText_bottom, myPaint2);
	}

	public void drawGameOver(Canvas canvas){
		myPaint2.setTextSize(95);
		myPaint2.setColor(Color.RED);
		canvas.drawText("Game over!", 300, gameOverText_bottom, myPaint2);
	}

	public void drawControllButton(Canvas canvas){
		myPaint3.setColor(Color.GREEN);
		myPaint3.setStyle(Paint.Style.STROKE);
		myPaint3.setStrokeWidth(10);
		canvas.drawRect(drawControllButton_left, drawControllButton_top, drawControllButton_right, drawControllButton_bottom, myPaint3);
	}
}
