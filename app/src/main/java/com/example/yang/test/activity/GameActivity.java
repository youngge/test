package com.example.yang.test.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.yang.test.R;
import com.example.yang.test.baseactivity.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class GameActivity extends BaseActivity {

    @ViewInject(R.id.top_score)
    private TextView top_scoreTextView;
    @ViewInject(R.id.score)
    private TextView scoreTextView;

    private static GameActivity mainActivity = null;
    private int top_score, score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ViewUtils.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar!=null){
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

        SharedPreferences sp = getSharedPreferences("topScore.xml", MODE_PRIVATE);
        top_score=sp.getInt("score", 0);
        top_scoreTextView.setText(top_score+"");
    }

    public void dialog() {
        SharedPreferences sp = getSharedPreferences("topScore.xml", MODE_PRIVATE);
        if (sp.getInt("score", 0)<score) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("score", score);
            editor.commit();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("恭喜!");
        builder.setMessage("刷新得分" + score);
        builder.setNegativeButton("replay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(GameActivity.this,GameActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setPositiveButton("exit",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });
        builder.setCancelable(false);
        builder.show();
    }

    public GameActivity() {
        mainActivity = this;
    }

    public static GameActivity getMainActivity() {
        return mainActivity;
    }

    public void showScore() {
        scoreTextView.setText(score + "");
    }

    public void cleanScore() {
        score = 0;
        showScore();
    }

    public void addScore(int s) {
        score = score + s;
        showScore();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
