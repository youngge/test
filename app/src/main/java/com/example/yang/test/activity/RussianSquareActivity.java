package com.example.yang.test.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.yang.test.R;
import com.example.yang.test.util.LogUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashSet;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.x;
import static android.R.attr.y;

public class RussianSquareActivity extends AppCompatActivity {

    @ViewInject(R.id.iv_a1)
    private ImageView iv_a1;
    @ViewInject(R.id.iv_a2)
    private ImageView iv_a2;
    @ViewInject(R.id.iv_a3)
    private ImageView iv_a3;
    @ViewInject(R.id.iv_a4)
    private ImageView iv_a4;
    @ViewInject(R.id.iv_a5)
    private ImageView iv_a5;
    @ViewInject(R.id.iv_a6)
    private ImageView iv_a6;
    @ViewInject(R.id.iv_a7)
    private ImageView iv_a7;
    @ViewInject(R.id.iv_a8)
    private ImageView iv_a8;
    @ViewInject(R.id.iv_a9)
    private ImageView iv_a9;
    @ViewInject(R.id.iv_a10)
    private ImageView iv_a10;

    @ViewInject(R.id.iv_b1)
    private ImageView iv_b1;
    @ViewInject(R.id.iv_b2)
    private ImageView iv_b2;
    @ViewInject(R.id.iv_b3)
    private ImageView iv_b3;
    @ViewInject(R.id.iv_b4)
    private ImageView iv_b4;
    @ViewInject(R.id.iv_b5)
    private ImageView iv_b5;
    @ViewInject(R.id.iv_b6)
    private ImageView iv_b6;
    @ViewInject(R.id.iv_b7)
    private ImageView iv_b7;
    @ViewInject(R.id.iv_b8)
    private ImageView iv_b8;
    @ViewInject(R.id.iv_b9)
    private ImageView iv_b9;
    @ViewInject(R.id.iv_b10)
    private ImageView iv_b10;

    @ViewInject(R.id.iv_c1)
    private ImageView iv_c1;
    @ViewInject(R.id.iv_c2)
    private ImageView iv_c2;
    @ViewInject(R.id.iv_c3)
    private ImageView iv_c3;
    @ViewInject(R.id.iv_c4)
    private ImageView iv_c4;
    @ViewInject(R.id.iv_c5)
    private ImageView iv_c5;
    @ViewInject(R.id.iv_c6)
    private ImageView iv_c6;
    @ViewInject(R.id.iv_c7)
    private ImageView iv_c7;
    @ViewInject(R.id.iv_c8)
    private ImageView iv_c8;
    @ViewInject(R.id.iv_c9)
    private ImageView iv_c9;
    @ViewInject(R.id.iv_c10)
    private ImageView iv_c10;

    @ViewInject(R.id.iv_d1)
    private ImageView iv_d1;
    @ViewInject(R.id.iv_d2)
    private ImageView iv_d2;
    @ViewInject(R.id.iv_d3)
    private ImageView iv_d3;
    @ViewInject(R.id.iv_d4)
    private ImageView iv_d4;
    @ViewInject(R.id.iv_d5)
    private ImageView iv_d5;
    @ViewInject(R.id.iv_d6)
    private ImageView iv_d6;
    @ViewInject(R.id.iv_d7)
    private ImageView iv_d7;
    @ViewInject(R.id.iv_d8)
    private ImageView iv_d8;
    @ViewInject(R.id.iv_d9)
    private ImageView iv_d9;
    @ViewInject(R.id.iv_d10)
    private ImageView iv_d10;

    @ViewInject(R.id.iv_e1)
    private ImageView iv_e1;
    @ViewInject(R.id.iv_e2)
    private ImageView iv_e2;
    @ViewInject(R.id.iv_e3)
    private ImageView iv_e3;
    @ViewInject(R.id.iv_e4)
    private ImageView iv_e4;
    @ViewInject(R.id.iv_e5)
    private ImageView iv_e5;
    @ViewInject(R.id.iv_e6)
    private ImageView iv_e6;
    @ViewInject(R.id.iv_e7)
    private ImageView iv_e7;
    @ViewInject(R.id.iv_e8)
    private ImageView iv_e8;
    @ViewInject(R.id.iv_e9)
    private ImageView iv_e9;
    @ViewInject(R.id.iv_e10)
    private ImageView iv_e10;

    @ViewInject(R.id.iv_f1)
    private ImageView iv_f1;
    @ViewInject(R.id.iv_f2)
    private ImageView iv_f2;
    @ViewInject(R.id.iv_f3)
    private ImageView iv_f3;
    @ViewInject(R.id.iv_f4)
    private ImageView iv_f4;
    @ViewInject(R.id.iv_f5)
    private ImageView iv_f5;
    @ViewInject(R.id.iv_f6)
    private ImageView iv_f6;
    @ViewInject(R.id.iv_f7)
    private ImageView iv_f7;
    @ViewInject(R.id.iv_f8)
    private ImageView iv_f8;
    @ViewInject(R.id.iv_f9)
    private ImageView iv_f9;
    @ViewInject(R.id.iv_f10)
    private ImageView iv_f10;

    @ViewInject(R.id.iv_g1)
    private ImageView iv_g1;
    @ViewInject(R.id.iv_g2)
    private ImageView iv_g2;
    @ViewInject(R.id.iv_g3)
    private ImageView iv_g3;
    @ViewInject(R.id.iv_g4)
    private ImageView iv_g4;
    @ViewInject(R.id.iv_g5)
    private ImageView iv_g5;
    @ViewInject(R.id.iv_g6)
    private ImageView iv_g6;
    @ViewInject(R.id.iv_g7)
    private ImageView iv_g7;
    @ViewInject(R.id.iv_g8)
    private ImageView iv_g8;
    @ViewInject(R.id.iv_g9)
    private ImageView iv_g9;
    @ViewInject(R.id.iv_g10)
    private ImageView iv_g10;

    @ViewInject(R.id.iv_h1)
    private ImageView iv_h1;
    @ViewInject(R.id.iv_h2)
    private ImageView iv_h2;
    @ViewInject(R.id.iv_h3)
    private ImageView iv_h3;
    @ViewInject(R.id.iv_h4)
    private ImageView iv_h4;
    @ViewInject(R.id.iv_h5)
    private ImageView iv_h5;
    @ViewInject(R.id.iv_h6)
    private ImageView iv_h6;
    @ViewInject(R.id.iv_h7)
    private ImageView iv_h7;
    @ViewInject(R.id.iv_h8)
    private ImageView iv_h8;
    @ViewInject(R.id.iv_h9)
    private ImageView iv_h9;
    @ViewInject(R.id.iv_h10)
    private ImageView iv_h10;

    @ViewInject(R.id.iv_i1)
    private ImageView iv_i1;
    @ViewInject(R.id.iv_i2)
    private ImageView iv_i2;
    @ViewInject(R.id.iv_i3)
    private ImageView iv_i3;
    @ViewInject(R.id.iv_i4)
    private ImageView iv_i4;
    @ViewInject(R.id.iv_i5)
    private ImageView iv_i5;
    @ViewInject(R.id.iv_i6)
    private ImageView iv_i6;
    @ViewInject(R.id.iv_i7)
    private ImageView iv_i7;
    @ViewInject(R.id.iv_i8)
    private ImageView iv_i8;
    @ViewInject(R.id.iv_i9)
    private ImageView iv_i9;
    @ViewInject(R.id.iv_i10)
    private ImageView iv_i10;

    @ViewInject(R.id.iv_j1)
    private ImageView iv_j1;
    @ViewInject(R.id.iv_j2)
    private ImageView iv_j2;
    @ViewInject(R.id.iv_j3)
    private ImageView iv_j3;
    @ViewInject(R.id.iv_j4)
    private ImageView iv_j4;
    @ViewInject(R.id.iv_j5)
    private ImageView iv_j5;
    @ViewInject(R.id.iv_j6)
    private ImageView iv_j6;
    @ViewInject(R.id.iv_j7)
    private ImageView iv_j7;
    @ViewInject(R.id.iv_j8)
    private ImageView iv_j8;
    @ViewInject(R.id.iv_j9)
    private ImageView iv_j9;
    @ViewInject(R.id.iv_j10)
    private ImageView iv_j10;

    @ViewInject(R.id.iv_k1)
    private ImageView iv_k1;
    @ViewInject(R.id.iv_k2)
    private ImageView iv_k2;
    @ViewInject(R.id.iv_k3)
    private ImageView iv_k3;
    @ViewInject(R.id.iv_k4)
    private ImageView iv_k4;
    @ViewInject(R.id.iv_k5)
    private ImageView iv_k5;
    @ViewInject(R.id.iv_k6)
    private ImageView iv_k6;
    @ViewInject(R.id.iv_k7)
    private ImageView iv_k7;
    @ViewInject(R.id.iv_k8)
    private ImageView iv_k8;
    @ViewInject(R.id.iv_k9)
    private ImageView iv_k9;
    @ViewInject(R.id.iv_k10)
    private ImageView iv_k10;

    @ViewInject(R.id.iv_l1)
    private ImageView iv_l1;
    @ViewInject(R.id.iv_l2)
    private ImageView iv_l2;
    @ViewInject(R.id.iv_l3)
    private ImageView iv_l3;
    @ViewInject(R.id.iv_l4)
    private ImageView iv_l4;
    @ViewInject(R.id.iv_l5)
    private ImageView iv_l5;
    @ViewInject(R.id.iv_l6)
    private ImageView iv_l6;
    @ViewInject(R.id.iv_l7)
    private ImageView iv_l7;
    @ViewInject(R.id.iv_l8)
    private ImageView iv_l8;
    @ViewInject(R.id.iv_l9)
    private ImageView iv_l9;
    @ViewInject(R.id.iv_l10)
    private ImageView iv_l10;

    private ImageView[] ivA = null;
    private ImageView[] ivB = null;
    private ImageView[] ivC = null;
    private ImageView[] ivD = null;
    private ImageView[] ivE = null;
    private ImageView[] ivF = null;
    private ImageView[] ivG = null;
    private ImageView[] ivH = null;
    private ImageView[] ivI = null;
    private ImageView[] ivJ = null;
    private ImageView[] ivK = null;
    private ImageView[] ivL = null;

    private ImageView[][] imageViews = null;
    private int[][] nums = new int[10][12];
    private int[][] square = new int[4][4];

    private int x=1;
    private int y=3;

    private HashSet[][] hashSets = new HashSet[4][4];
    private Random random = new Random();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            updateUI(y);
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_russian_square);
        ViewUtils.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ivA = new ImageView[]{iv_a1,iv_a2,iv_a3,iv_a4,iv_a5,iv_a6,iv_a7,iv_a8,iv_a9,iv_a10};
        ivB = new ImageView[]{iv_b1,iv_b2,iv_b3,iv_b4,iv_b5,iv_b6,iv_b7,iv_b8,iv_b9,iv_b10};
        ivC = new ImageView[]{iv_c1,iv_c2,iv_c3,iv_c4,iv_c5,iv_c6,iv_c7,iv_c8,iv_c9,iv_c10};
        ivD = new ImageView[]{iv_d1,iv_d2,iv_d3,iv_d4,iv_d5,iv_d6,iv_d7,iv_d8,iv_d9,iv_d10};
        ivE = new ImageView[]{iv_e1,iv_e2,iv_e3,iv_e4,iv_e5,iv_e6,iv_e7,iv_e8,iv_e9,iv_e10};
        ivF = new ImageView[]{iv_f1,iv_f2,iv_f3,iv_f4,iv_f5,iv_f6,iv_f7,iv_f8,iv_f9,iv_f10};
        ivG = new ImageView[]{iv_g1,iv_g2,iv_g3,iv_g4,iv_g5,iv_g6,iv_g7,iv_g8,iv_g9,iv_g10};
        ivH = new ImageView[]{iv_h1,iv_h2,iv_h3,iv_h4,iv_h5,iv_h6,iv_h7,iv_h8,iv_h9,iv_h10};
        ivI = new ImageView[]{iv_i1,iv_i2,iv_i3,iv_i4,iv_i5,iv_i6,iv_i7,iv_i8,iv_i9,iv_i10};
        ivJ = new ImageView[]{iv_j1,iv_j2,iv_j3,iv_j4,iv_j5,iv_j6,iv_j7,iv_j8,iv_j9,iv_j10};
        ivK = new ImageView[]{iv_k1,iv_k2,iv_k3,iv_k4,iv_k5,iv_k6,iv_k7,iv_k8,iv_k9,iv_k10};
        ivL = new ImageView[]{iv_l1,iv_l2,iv_l3,iv_l4,iv_l5,iv_l6,iv_l7,iv_l8,iv_l9,iv_l10};

        imageViews= new ImageView[][]{ivA,ivB,ivC,ivD,ivE,ivF,ivG,ivH,ivI,ivJ,ivK,ivL};

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isDestroyed()&&y<11){
                    try {
                        Thread.sleep(1000);
                        y++;
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        createSquare(0,0,0,false,false);
//        updateUI(3);

    }

    private void updateUI(int rawY){
//        for (int y = 11 ; y>= 0 ;y--){
//            if (nums[xNum1][y]!=0||nums[xNum2][y]!=0){
//                nums[xNum1][y]=1;
//                nums[xNum2][y]=1;
//                break;
//            }
//        }
        for (int y = 3;y>=0;y--){
            for (int x = 0;x<4;x++) {
                if (square[x][y]==1){
                    imageViews[y][x].setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        }
    }

    private void createSquare(int index,int startX,int startY,boolean isAddY,boolean isAddX){
        if (index==4){
            return;
        }
        if (square[startX][startY] == 1){
            if (isAddY){
                startY--;
            }else if (isAddX){
                startX--;
            }
            if (random.nextInt(5)>2) {
                createSquare(index,random.nextInt(startX+1),startY,false,false);
            }else{
                createSquare(index,startX,random.nextInt(startY+1),false,false);
            }
        }else {
            index++;
            square[startX][startY] = 1;
            if (random.nextInt(5)>2) {
                startY++;
                createSquare(index,random.nextInt(startX + 1), startY,true,false);
            } else {
                startX++;
                createSquare(index,startX,  random.nextInt(startY + 1),false,true);
            }
        }
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