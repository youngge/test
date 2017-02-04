package com.example.yang.test.activity;

import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.yang.test.R;
import com.example.yang.test.baseactivity.BaseActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketCameraActivity extends BaseActivity implements SurfaceHolder.Callback,
        Camera.PreviewCallback {
    private SurfaceView mSurfaceview = null; // SurfaceView对象：(视图组件)视频显示
    private SurfaceHolder mSurfaceHolder = null; // SurfaceHolder对象：(抽象接口)SurfaceView支持类
    private Camera mCamera = null; // Camera对象，相机预览
    Handler handler;
    /**
     * 服务器地址
     */
    private String pUsername = "yang";
    /**
     * 服务器地址
     */
    private String serverUrl = "192.168.1.110";
    /**
     * 服务器端口
     */
    private int serverPort = 3698;
    /**
     * 视频刷新间隔
     */
    private int VideoPreRate = 1;
    /**
     * 当前视频序号
     */
    private int tempPreRate = 0;
    /**
     * 视频质量
     */
    private int VideoQuality = 85;

    /**
     * 发送视频宽度比例
     */
    private float VideoWidthRatio = 1;
    /**
     * 发送视频高度比例
     */
    private float VideoHeightRatio = 1;

    /**
     * 发送视频宽度
     */
    private int VideoWidth = 320;
    /**
     * 发送视频高度
     */
    private int VideoHeight = 240;
    /**
     * 视频格式索引
     */
    private int VideoFormatIndex = 0;
    /**
     * 是否发送视频
     */
    private boolean startSendVideo = false;
    /**
     * 是否连接主机
     */
    private boolean connectedServer = false;

    private Button myBtn01, myBtn02;
    private TextView receive;
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private OutputStream os;
    private String content = "";
    private byte byteBuffer[] = new byte[1024];

    /**
     * Called when the activity is first created.
     */

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            receive.append(content);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_camera);

        //禁止屏幕休眠
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mSurfaceview = (SurfaceView) findViewById(R.id.camera_preview);
        myBtn01 = (Button) findViewById(R.id.button1);
        myBtn02 = (Button) findViewById(R.id.button2);
        receive = (TextView) findViewById(R.id.tv_receive);
        //开始连接主机按钮
        myBtn01.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Thread th = new MySocketThread();
                th.start();
            }
        });


        myBtn02.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (startSendVideo)//停止传输视频
                {
                    startSendVideo = false;
                    myBtn02.setText("开始传输");
                } else { // 开始传输视频
                    startSendVideo = true;
                    myBtn02.setText("停止传输");
                }
            }
        });

    }

    class MySocketThread extends Thread {

        public void run() {
            //实例化Socket
            try {
                socket = new Socket(serverUrl, serverPort);
                in = new BufferedReader(new InputStreamReader(socket
                        .getInputStream(), "utf-8"));
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream(), "utf-8")), true);
                os = socket.getOutputStream();

                out.println("start===========================!");

                while (true) {
                    System.out.println("!+============2");
                    if (!socket.isClosed()) {
                        System.out.println("!+============3");
                        if (socket.isConnected()) {
                            System.out.println("!+============4");
                            if (!socket.isInputShutdown()) {
                                System.out.println("!+============5");
                                if ((content = in.readLine()) != null) {
                                    content += "\n";
                                    System.out.println("!+============" + content);
                                    mHandler.sendMessage(mHandler.obtainMessage());
                                } else {

                                }
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
//                ShowDialog("login exception" + ex.getMessage());
            }
        }
    }


    @Override
    public void onStart()//重新启动的时候
    {
        mSurfaceHolder = mSurfaceview.getHolder(); // 绑定SurfaceView，取得SurfaceHolder对象
        mSurfaceHolder.addCallback(this); // SurfaceHolder加入回调接口
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 设置显示器类型，setType必须设置
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        InitCamera();
    }

    /**
     * 初始化摄像头
     */
    private void InitCamera() {
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mCamera != null) {
                mCamera.setPreviewCallback(null); // ！！这个必须在前，不然退出出错
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        if (mCamera == null) {
            return;
        }
        mCamera.stopPreview();
        mCamera.setPreviewCallback(this);
        mCamera.setDisplayOrientation(90); //设置横行录制
        //获取摄像头参数
        Camera.Parameters parameters = mCamera.getParameters();
        Size size = parameters.getPreviewSize();
        VideoWidth = size.width;
        VideoHeight = size.height;
        VideoFormatIndex = parameters.getPreviewFormat();

        mCamera.startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        if (null != mCamera) {
            mCamera.setPreviewCallback(null); // ！！这个必须在前，不然退出出错
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        // TODO Auto-generated method stub
        //如果没有指令传输视频，就先不传
        if (!startSendVideo)
            return;
        if (tempPreRate < VideoPreRate) {
            tempPreRate++;
            return;
        }
        tempPreRate = 0;
        Size size = camera.getParameters().getPreviewSize();
        try {
            if (data != null) {
                YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                if (image != null) {
                    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                    //在此设置图片的尺寸和质量
                    image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, outstream);
                    outstream.flush();

                    //启用线程将图像数据发送出去
                    Thread th = new MySendFileThread(outstream, pUsername, serverUrl, serverPort);
                    th.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送文件线程
     */
    class MySendFileThread extends Thread {
        private String username;
        private String ipname;
        private int port;
        private byte byteBuffer[] = new byte[1024];
        private OutputStream outsocket;
        private ByteArrayOutputStream myoutputstream;
        private PrintWriter Print = null;

        public MySendFileThread(ByteArrayOutputStream myoutputstream, String username, String ipname, int port) {
            this.myoutputstream = myoutputstream;
            this.username = username;
            this.ipname = ipname;
            this.port = port;
            try {
                myoutputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                //将图像数据通过Socket发送出去
                Socket tempSocket = new Socket(ipname, port);
//            	tempSocket = new Socket(ipname, port);
                outsocket = tempSocket.getOutputStream();
                
                /*out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        tempSocket.getOutputStream(),"GBK")), true);
                out.println("start...");
                out.close();*/

                ByteArrayInputStream inputstream = new ByteArrayInputStream(myoutputstream.toByteArray());
                int amount;
                while ((amount = inputstream.read(byteBuffer)) != -1) {
                    outsocket.write(byteBuffer, 0, amount);
                }
                myoutputstream.flush();
                myoutputstream.close();
                tempSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}