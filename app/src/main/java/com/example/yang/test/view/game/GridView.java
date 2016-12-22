package com.example.yang.test.view.game;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import com.example.yang.test.activity.GameActivity;
import com.example.yang.test.activity.GameActivity;
import com.example.yang.test.view.game.Card;

import java.util.ArrayList;
import java.util.List;

public class GridView extends GridLayout {

	private Card[][] cardmap = new Card[4][4];
	private List<Point> emptyPoints = new ArrayList<Point>();// 存放没有数字的卡片的位置

	public GridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public GridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public GridView(Context context) {
		super(context);
		initView();
	}

	/**
	 * 设置GridView的列数并添加屏幕监听
	 */
	public void initView() {
		// 设置列数为4
		setColumnCount(4);
		// 设置背景颜色
//		setBackgroundColor(Color.parseColor("#50000000"));
		setOnTouchListener(new View.OnTouchListener() {

			private float startX, startY, endX, endY;

			@Override
			public boolean onTouch(View view, MotionEvent event) {

				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						// 获取点击屏幕时的X坐标
						startX = event.getX();
						// 获取点击屏幕时的Y坐标
						startY = event.getY();
						break;

					case MotionEvent.ACTION_UP:
						endX = event.getX() - startX;
						endY = event.getY() - startY;

						if (Math.abs(endX) > Math.abs(endY)) {
							if (endX > 5) {
								// 手指向右滑的动作
								slideRight();
							} else if (endX < -5) {
								// 手指向左滑的动作
								slideLeft();
							}
						} else {
							if (endY > 5) {
								// 手指向下滑的动作
								slideDown();
							} else if (endY < -5) {
								// 手指向上滑的动作
								slideUp();
							}
						}

						break;
				}

				return true;
			}
		});
	}

	/**
	 * 手指滑向左边的逻辑
	 */
	public void slideLeft() {
		boolean add = false;
		for (int raw = 0; raw < 4; raw++) {
			for (int column = 0; column < 4; column++) {
				TAG: for (int i = column + 1; i < 4; i++) {
					if (cardmap[column][raw].getNum() > 0) {
						if (cardmap[i][raw].getNum() > 0) {
							if (cardmap[column][raw].equals(cardmap[i][raw])) {
								cardmap[column][raw]
										.setNum(cardmap[column][raw].getNum() * 2);
								cardmap[i][raw].setNum(0);
								add = true;
								GameActivity.getMainActivity().addScore(
										cardmap[column][raw].getNum());
							}
							break TAG;
						}
					} else {
						if (cardmap[i][raw].getNum() > 0) {
							cardmap[column][raw].setNum(cardmap[i][raw].getNum());
							cardmap[i][raw].setNum(0);
							add = true;
						}
					}

				}
			}
		}
		//如果移动卡片或两个卡片叠加成功，则添加卡片数字并判断游戏是否结束
		if (add) {
			addRandomNum();
			if (isGameOver()) {
				GameActivity.getMainActivity().dialog();
			}
		}
	}

	/**
	 * 手指滑向右边的逻辑
	 */
	public void slideRight() {
		boolean add = false;
		for (int raw = 0; raw < 4; raw++) {
			for (int column = 3; column >= 0; column--) {
				TAG: for (int i = column - 1; i >= 0; i--) {
					if (cardmap[column][raw].getNum() > 0) {
						if (cardmap[i][raw].getNum() > 0) {
							if (cardmap[column][raw].equals(cardmap[i][raw])) {
								cardmap[column][raw]
										.setNum(cardmap[column][raw].getNum() * 2);
								cardmap[i][raw].setNum(0);
								add = true;
								GameActivity.getMainActivity().addScore(
										cardmap[column][raw].getNum());
							}
							break TAG;
						}
					} else {
						if (cardmap[i][raw].getNum() > 0) {
							cardmap[column][raw].setNum(cardmap[i][raw].getNum());
							cardmap[i][raw].setNum(0);
							add = true;
						}
					}

				}
			}
		}
		//如果移动卡片或两个卡片叠加成功，则添加卡片数字并判断游戏是否结束
		if (add) {
			addRandomNum();
			if (isGameOver()) {
				GameActivity.getMainActivity().dialog();
			}
		}
	}

	/**
	 * 手指滑向上边的逻辑
	 */
	public void slideUp() {
		boolean add = false;
		for (int column = 0; column < 4; column++) {
			for (int raw = 0; raw < 4; raw++) {
				TAG: for (int i = raw + 1; i < 4; i++) {
					if (cardmap[column][raw].getNum() > 0) {
						if (cardmap[column][i].getNum() > 0) {
							if (cardmap[column][raw].equals(cardmap[column][i])) {
								cardmap[column][raw]
										.setNum(cardmap[column][raw].getNum() * 2);
								cardmap[column][i].setNum(0);
								add = true;
								GameActivity.getMainActivity().addScore(
										cardmap[column][raw].getNum());
							}
							break TAG;
						}
					} else {
						if (cardmap[column][i].getNum() > 0) {
							cardmap[column][raw].setNum(cardmap[column][i].getNum());
							cardmap[column][i].setNum(0);
							add = true;
						}
					}

				}
			}
		}
		//如果移动卡片或两个卡片叠加成功，则添加卡片数字并判断游戏是否结束
		if (add) {
			addRandomNum();
			if (isGameOver()) {
				GameActivity.getMainActivity().dialog();
			}
		}
	}

	/**
	 * 手指滑向下边的逻辑
	 */
	public void slideDown() {
		boolean add = false;
		for (int column = 0; column < 4; column++) {
			for (int raw = 3; raw >= 0; raw--) {
				TAG: for (int i = raw - 1; i >= 0; i--) {
					if (cardmap[column][raw].getNum() > 0) {
						if (cardmap[column][i].getNum() > 0) {
							if (cardmap[column][raw].equals(cardmap[column][i])) {
								cardmap[column][raw]
										.setNum(cardmap[column][raw].getNum() * 2);
								cardmap[column][i].setNum(0);
								add = true;
								GameActivity.getMainActivity().addScore(
										cardmap[column][raw].getNum());
							}
							break TAG;
						}
					} else {
						if (cardmap[column][i].getNum() > 0) {
							cardmap[column][raw].setNum(cardmap[column][i].getNum());
							cardmap[column][i].setNum(0);
							add = true;
						}
					}

				}
			}
		}
		//如果移动卡片或两个卡片叠加成功，则添加卡片数字并判断游戏是否结束
		if (add) {
			addRandomNum();
			if (isGameOver()) {
				GameActivity.getMainActivity().dialog();
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// 每个卡片的宽度
		int cardSize = (Math.min(w, h) - 20) / 4;
		addCards(cardSize, cardSize);
		startGame();
	}

	/**
	 * 添加卡片
	 *
	 * @param cardSize 卡片宽度
	 * @param cardSize2 卡片高度
	 */
	private void addCards(int cardSize, int cardSize2) {
		Card card;
		for (int raw = 0; raw < 4; raw++) {
			for (int column = 0; column < 4; column++) {
				card = new Card(getContext());
				addView(card, cardSize, cardSize2);
				cardmap[column][raw] = card;
			}
		}
	}

	/**
	 * 开始游戏时清除棋盘内的数字并随机添加两个数字
	 */
	public void startGame() {
		for (int raw = 0; raw < 4; raw++) {
			for (int column = 0; column < 4; column++) {
				cardmap[column][raw].setNum(0);
			}
		}
		GameActivity.getMainActivity().cleanScore();

		// 一开始添加两个随机数
		addRandomNum();
		addRandomNum();
	}

	/**
	 * 获取没有数字的位置并添加到emptyPoints集合里，在emptyPoints集合里随机抽取一个位置添加随机数
	 */
	private void addRandomNum() {
		emptyPoints.clear();
		for (int raw = 0; raw < 4; raw++) {
			for (int column = 0; column < 4; column++) {
				if (cardmap[column][raw].getNum() <= 0) {
					emptyPoints.add(new Point(column, raw));
				}
			}
		}

		final Point point = emptyPoints.get((int) (Math.random() * emptyPoints.size()));
		cardmap[point.x][point.y].setNum(Math.random() > 0.1 ? 2 : 4);// 概率比为9:1
	}

	/**
	 *
	 * @return游戏是否结束
	 */
	public boolean isGameOver() {
		//首先判断所有cardmap是否存在0，如果存在则返回游戏还没结束
		for (int raw = 0; raw < 4; raw++) {
			for (int column = 0; column < 4; column++) {
				if (cardmap[raw][column].getNum() == 0) {
					return false;
				}
			}
		}

		//判断1~3行和1~3列的数字跟其旁边的数字是否有相同，如果有相同则返回游戏还没结束
		for (int raw = 0; raw < 3; raw++) {
			for (int column = 0; column < 3; column++) {
				if (cardmap[raw][column].getNum() == cardmap[raw + 1][column].getNum()
						|| cardmap[raw][column].getNum() == cardmap[raw][column + 1].getNum()) {
					return false;
				}
			}
		}
		//判断第4列的前三行数字是否跟其下边的数字相同，如果有相同则返回游戏还没结束
		for (int raw = 0; raw < 3; raw++) {
			if (cardmap[3][raw].getNum() == cardmap[3][raw + 1].getNum()) {
				return false;
			}
		}
		//判断第4行的前三列数字是否跟其右边的数字相同，如果有相同则返回游戏还没结束
		for (int column = 0; column < 3; column++) {
			if (cardmap[column][3].getNum() == cardmap[column + 1][3].getNum()) {
				return false;
			}
		}
		//返回游戏结束
		return true;
	}


}
