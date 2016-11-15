package com.example.ly.launcher.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ly.launcher.R;
import com.example.ly.launcher.adapter.MyFragmentAdapter;
import com.example.ly.launcher.beans.FragmentInfo;
import com.example.ly.launcher.beans.ServerVersionInfo;
import com.example.ly.launcher.fragment.FirstFragment;
import com.example.ly.launcher.fragment.SecondFragment;
import com.example.ly.launcher.fragment.ThirdFragment;
import com.example.ly.launcher.ui.UpdateDialog;
import com.example.ly.launcher.utils.AppUtils;
import com.example.ly.launcher.utils.Fields;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewpager;
    private LinearLayout mMark;
    private List<FragmentInfo> mShowItems = new ArrayList<>();
    private int previousPosition = 0;//用来记录上一个点的位置

    public static final int VERSIONJSON = 100;
    public static final int DOWNFINISH = 101;
    private static final int INSTALLREQUESTCODE = 102;

    //定义一个handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VERSIONJSON://下载
                    //校验
                    if (msg.obj == null) {
                        return;
                    }
                    //解析获得数据
                    ServerVersionInfo info = parseJson(msg.obj);

                    //校验
                    if (info == null) {
                        return;
                    }

                    //获取本地版本号
                    int loacalVersionNum = AppUtils.getLocationVersionCode(MainActivity.this);
                    int versioncode = info.versioncode;
                    String downurl = info.downurl;
                    //对比服务器版本是否与装机版本一致
                    if (versioncode != loacalVersionNum) {  //去升级

                        String[] items = {"1.增加消息通知功能,被回复的评论可以通知到用户", "2.添加设计师分类,100位设计师查找更加方便"
                                , "3.画报分享及评论功能优化", "4.图片轮播效果优化",
                                "5.性能优化以及bug修复", "6.增加一行为了显示效果"};

                        //弹出更新的dialog
                        showVersionDialog(items, downurl);
                    } else {
                        // Toast.makeText(MainActivity.this, "已经是最新版本了", Toast.LENGTH_LONG).show();
                        return;
                    }
                    break;


                case DOWNFINISH://下载完成后安装
                    installAPK((File) msg.obj);
                    break;
            }
        }

        private ServerVersionInfo parseJson(Object object) {
            ServerVersionInfo info = null;
            //把object装成string
            String jsonContent = (String) object;
            try {
                //定义json对象
                JSONObject jsonObject = new JSONObject(jsonContent);
                int versioncode = jsonObject.getInt("versioncode");
                String downurl = jsonObject.getString("downurl");

                //赋值
                info = new ServerVersionInfo();
                info.downurl = downurl;
                info.versioncode = versioncode;

                //返回
                return info;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    };


    private void installAPK(File file) {

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri fileScheme = Uri.fromFile(file);
        intent.setDataAndType(fileScheme, "application/vnd.android.package-archive");

        //开启
        startActivityForResult(intent, INSTALLREQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (INSTALLREQUESTCODE == requestCode) {
            //如果取消安装   即resultCode=0
            if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(MainActivity.this, "进入主界面", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隐藏本activity的标题栏
        getSupportActionBar().hide();

        //加载布局
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        //初始化界面
        initView();

        //初始化数据
        initData();
        //获取服务器版本
        checkServerVersion();
    }

    private void initData() {
        String[] mark = {"1", "2", "3"};
        mShowItems.add(new FragmentInfo(mark[0], new FirstFragment()));
        mShowItems.add(new FragmentInfo(mark[1], new SecondFragment()));
        mShowItems.add(new FragmentInfo(mark[2], new ThirdFragment()));

        mViewpager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), mShowItems));
        // for (int i = 0; i < mShowItems.size(); i++) {
        //动态添加小圆点
        for (int i = 0; i < mShowItems.size(); i++) {
            //创建小圆点
            ImageView point = new ImageView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(12,12);
            params.leftMargin = 20;
//            point.setImageResource(R.drawable.point_selected);
            point.setBackgroundResource(R.drawable.point_bg_selector);

            if (i != 0) {
//                point.setImageResource(R.drawable.point_normal);
                point.setLayoutParams(params);
                point.setEnabled(false);//除了第一个点,其他点设置为不可用
            }
//            point.setBackgroundResource(R.drawable.point_normal);
            mMark.addView(point);
        }

    }

    private void initView() {
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        mMark = (LinearLayout) findViewById(R.id.mark);

        //
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //先把前一个 点设置为未选中状态
                mMark.getChildAt(previousPosition).setEnabled(false);
                previousPosition = position;

                //对应的小圆点被选中
                mMark.getChildAt(position).setEnabled(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 检查服务器版本信息
     */
    private void checkServerVersion() {
        //网络请求json信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Response response = AppUtils.responseGet(Fields.versionUrl);

                    //获得版本信息
                    String versionContent = response.body().string();

                    Message msg = Message.obtain();
                    msg.obj = versionContent;
                    msg.what = VERSIONJSON;

                    //返回版本信息数据
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    //TODO:进入主界面

                }
            }
        }).start();
    }

    //打开更新对话框
    private void showVersionDialog(String[] datas, final String downurl) {
        UpdateDialog.Builder dialog = new UpdateDialog.Builder(this, datas);

        dialog.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //下载apk
                downApk(downurl);
            }
        });

        dialog.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "下次记得升级哦", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.create().show();

    }

    private void downApk(final String downurl) {
        new Thread(new Runnable() {


            @Override
            public void run() {
                try {
                    Response response = AppUtils.responseGet(downurl);
                    //拿到输入流
                    InputStream inputStream = response.body().byteStream();
                    //判断当前sd卡的状态
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        //当前状态可写
                        File file = new File(Environment.getExternalStorageDirectory(), "ccc.apk");

                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] buffer = new byte[1024 * 10];
                        int len = -1;
                        while ((len = inputStream.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                        inputStream.close();

                        //下载完成  handler返回下载完成信息
                        Message msg = Message.obtain();
                        msg.what = DOWNFINISH;
                        msg.obj = file;//下载的文件
                        mHandler.sendMessage(msg);

                    } else {
                        //不可写
                        Toast.makeText(MainActivity.this, "请检查sd卡的状态", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
