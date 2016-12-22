package com.example.yang.test.view.game;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {

	private int num = 0;
	private TextView textView;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
		if (num <= 0) {
			textView.setText("");
		} else if (num > 0) {
			textView.setText(num + "");
		}
		int number = 0;
		if (!textView.getText().toString().equals("")) {
			number = Integer.valueOf(textView.getText().toString());
		}
		setCardColor(number);

	}

	public Card(Context context) {
		super(context);

		textView = new TextView(getContext());
		textView.setTextSize(32);
		textView.setGravity(Gravity.CENTER);
		textView.setBackgroundColor(0xffEECBAD);
		// LayoutParams(-1,-1)参数-1代表match_parent，-2代表wrap_content
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		params.setMargins(20, 20, 0, 0);
		addView(textView, params);
	}

	public boolean equals(Card o) {
		return getNum() == o.getNum();
	}

	/**
	 * 设置card的背景颜色
	 */
	public void setCardColor(int number) {
		switch (number) {
			case 0:
				textView.setBackgroundColor(Color.parseColor("#EECBAD"));
				break;

			case 2:
				textView.setBackgroundColor(Color.parseColor("#FFE4E1"));
				break;

			case 4:
				textView.setBackgroundColor(Color.parseColor("#FFC125"));
				break;

			case 8:
				textView.setBackgroundColor(Color.parseColor("#FF7F00"));
				break;

			case 16:
				textView.setBackgroundColor(Color.parseColor("#FFB6C1"));
				break;

			case 32:
				textView.setBackgroundColor(Color.parseColor("#FFBBFF"));
				break;

			case 64:
				textView.setBackgroundColor(Color.parseColor("#FFA500"));
				break;

			case 128:
				textView.setBackgroundColor(Color.parseColor("#FF3E96"));
				break;

			case 256:
				textView.setBackgroundColor(Color.parseColor("#FF3030"));
				break;

			case 512:
				textView.setBackgroundColor(Color.parseColor("#C6E2FF"));
				break;

			case 1024:
				textView.setBackgroundColor(Color.parseColor("#C0FF3E"));
				break;

			case 2048:
				textView.setBackgroundColor(Color.parseColor("#8E8E38"));
				break;

			case 4096:
				textView.setBackgroundColor(Color.parseColor("#00F5FF"));
				break;

			default:
				textView.setBackgroundColor(Color.parseColor("#006400"));
				break;
		}
	}

}
