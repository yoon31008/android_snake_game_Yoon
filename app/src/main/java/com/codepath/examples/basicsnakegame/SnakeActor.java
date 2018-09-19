package com.codepath.examples.basicsnakegame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.codepath.simplegame.AbstractGamePanel;
import com.codepath.simplegame.Velocity;
import com.codepath.simplegame.actors.SimpleMovingActor;

import java.util.ArrayList;



public class SnakeActor extends SimpleMovingActor {
	public static final int DRAW_SIZE = 25;
	public static final int STEP = 25;
	public ArrayList<Point> tailPos;

	public double x1, x2;
	public double y1, y2;

	public double touched_x, touched_y;

	public SnakeGamePanel InsSnakeGamePanel = new SnakeGamePanel(SnakeGameActivity.mContext);

	public SnakeActor(int x, int y) {
		super(x, y, DRAW_SIZE, DRAW_SIZE);
		getVelocity().stop().setXDirection(Velocity.DIRECTION_RIGHT).setXSpeed(STEP);
		tailPos = new ArrayList<Point>();
		tailPos.add(new Point(x - this.getWidth(), y));
		tailPos.add(new Point(x - this.getWidth() * 2, y));
	}

	@Override
	public void stylePaint(Paint p) {
		p.setColor(Color.GREEN);
		p.setStyle(Style.FILL);
	}

	@Override
	public void draw(Canvas canvas) {
		getPaint().setColor(Color.GREEN);
		canvas.drawRect(getRect(), getPaint());
		for (Point p : tailPos) {
			Rect r = new Rect(p.x, p.y, p.x + this.getWidth(), p.y + this.getHeight());
			canvas.drawRect(r, getPaint());
		}
	}

	public void move() {
		if (this.isEnabled()) {
			int headX = getPoint().x;
			int headY = getPoint().y;
			for (int x = tailPos.size() - 1; x > 0; x--) {
				tailPos.get(x).set(tailPos.get(x - 1).x, tailPos.get(x - 1).y);
			}
			tailPos.get(0).set(headX, headY);
			super.move();
		}
	}

	public void grow() {
		this.tailPos.add(new Point(getX(), getY()));
	}

	public boolean checkBoundsCollision(AbstractGamePanel panel) {
		if (this.getX() < 0) {
			return true;
		} else if (this.getX() >= (panel.getWidth() - this.getWidth())) {
			return true;
		} else if (this.getY() < 0) {
			return true;
		} else if (this.getY() >= (panel.getHeight() - this.getHeight())) {
			return true;
		}
		return false;
	}

	public void handleKeyInput(int keyCode) {
		if (keyCode == KeyEvent.KEYCODE_W) {
			getVelocity().stop().setYDirection(Velocity.DIRECTION_UP).setYSpeed(STEP);
		} else if (keyCode == KeyEvent.KEYCODE_S) {
			getVelocity().stop().setYDirection(Velocity.DIRECTION_DOWN).setYSpeed(STEP);
		} else if (keyCode == KeyEvent.KEYCODE_A) {
			getVelocity().stop().setXDirection(Velocity.DIRECTION_LEFT).setXSpeed(STEP);
		} else if (keyCode == KeyEvent.KEYCODE_D) {
			getVelocity().stop().setXDirection(Velocity.DIRECTION_RIGHT).setXSpeed(STEP);
		}
	}

	public void handleTouchInput(MotionEvent event) {

		Canvas canvas = new Canvas();
		Paint mPaint = new Paint();

		mPaint.setColor(Color.RED);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(10);

		touched_x = touchRotate_X(event);
		touched_y = touchRotate_Y(event);;

		Log.d("Yoon", "X  is" + event.getX());
		Log.d("Yoon", "Y  is" + event.getY());
		Log.d("Yoon", "X modified is" + touched_x);
		Log.d("Yoon", "Y modified is" + touched_y);

		canvas.drawRect(0,0,50,50, mPaint);


		if (getVelocity().getYSpeed() == 0) {
			if (upBtnTouched()) {
				getVelocity().stop().setYDirection(Velocity.DIRECTION_UP).setYSpeed(STEP);
			} else if (event.getY() > this.getY() && getVelocity().getYSpeed() == 0) {
				getVelocity().stop().setYDirection(Velocity.DIRECTION_DOWN).setYSpeed(STEP);
			}
		} else if (getVelocity().getXSpeed() == 0) {
			if (event.getX() < this.getX()) {
				getVelocity().stop().setXDirection(Velocity.DIRECTION_LEFT).setXSpeed(STEP);
			} else if (event.getX() > this.getX()) {
				getVelocity().stop().setXDirection(Velocity.DIRECTION_RIGHT).setXSpeed(STEP);
			}
		}
	}

	public double touchRotate_X(MotionEvent event){
		x1 = event.getX() - InsSnakeGamePanel.drawControllButtonCenter_X;
		x2 = x1 * Math.cos(-45) - y1 * Math.sin(-45);
		return x2 + InsSnakeGamePanel.drawControllButtonCenter_X;
	}

	public double touchRotate_Y(MotionEvent event){
		y1 = event.getY() - InsSnakeGamePanel.drawControllButtonCenter_Y;
		y2 = x1 * Math.sin(-45) + y1 * Math.cos(-45);
		return y2 + InsSnakeGamePanel.drawControllButtonCenter_Y;
	}

	public boolean upBtnTouched(){
		if(touched_x >= InsSnakeGamePanel.drawControllButton_left && touched_x <= InsSnakeGamePanel.drawControllButtonCenter_X
				&& touched_y >= InsSnakeGamePanel.drawControllButtonCenter_Y && touched_y <= InsSnakeGamePanel.drawControllButton_top) {
			return true;
		}
		return false;
	}

    /*public boolean downBtnTouched(MotionEvent event){
		if(event.getX() >= boxcenter && event.getX() <= boxright
				&& event.getY() >= boxbottom && event.getY() <= boxCenter)
			return true;
    }

    public boolean leftBtnTouched(MotionEvent event){
		if(event.getX() >= boxleft && event.getX() <= boxCenter
				&& event.getY() >= boxbottom && event.getY() <= boxCenter)
			return true;
    }

    public boolean rightBtnTouched(MotionEvent event){
		if(event.getX() >= boxcenter && event.getX() <= boxright
				&& event.getY() >= boxcenter && event.getY() <= boxTop)
			return true;
    }*/
}
