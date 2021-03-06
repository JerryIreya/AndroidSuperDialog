package com.mylhyl.superdialog.sample;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mylhyl.superdialog.SuperDialog;
import com.mylhyl.superdialog.res.values.ColorRes;
import com.mylhyl.superdialog.sample.okFile.ReqProgressCallBack;
import com.mylhyl.superdialog.sample.okFile.okFileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView
        .OnItemLongClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1
                , new String[]{"提示框", "确定框", "换头像", "消息框", "动态改变内容", "动态改变items", "输入框", "进度框"}));
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        switch (position) {
            case 0:
                new SuperDialog.Builder(this).setRadius(10)
//                        .setAlpha(0.5f)
//                        .setTitle("标题", 80, 205)
                        .setMessage("可以看到？")
//                        .setDimEnabled(false)
//                        .setBackgroundColor(Color.WHITE)
//                        .setMessage("内容", Color.RED,250)
                        .setPositiveButton("确定", new SuperDialog.OnClickPositiveListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), "点了确定", Toast.LENGTH_LONG).show();
                            }
                        }).build();
                break;
            case 1:
                new SuperDialog.Builder(this).setTitle("标题").setMessage("内容从这里开始了")
                        .setBackgroundColor(Color.WHITE)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new SuperDialog.OnClickPositiveListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), "点了确定", Toast.LENGTH_LONG).show();
                            }
                        }).build();
                break;
            case 2:
                final String[] strings = {"拍照", "从相册选择", "小视频"};
//                List<People> list = new ArrayList<>();
//                list.add(new People(1, "拍照"));
//                list.add(new People(2, "从相册选择"));
//                list.add(new People(3, "小视频"));
                new SuperDialog.Builder(this)
//                        .setBackgroundColor(Color.WHITE)
                        //.setAlpha(0.5f)
                        .setTitle("上传头像", ColorRes.negativeButton)
                        .setCanceledOnTouchOutside(false)
                        //setItems默认是底部位置
                        .setItems(strings, new SuperDialog.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Toast.makeText(MainActivity.this, strings[position], Toast
                                        .LENGTH_LONG).show();
                            }
                        })
                        //注意
                        //.setGravity(Gravity.CENTER)请在setItems之后设置setGravity
                        .setNegativeButton("取消", null)
                        .setConfigDialog(new SuperDialog.ConfigDialog() {
                            @Override
                            public void onConfig(Dialog dialog, Window window, WindowManager
                                    .LayoutParams wlp, DisplayMetrics dm) {
                                //window.setWindowAnimations(R.style.dialogWindowAnim);
                                wlp.y = 100;//底部距离
                            }
                        })
                        .setItemsBottomMargin(20)
                        .setWindowAnimations(R.style.dialogWindowAnim)//动画
                        .build();
                break;
            case 3:
                new SuperDialog.Builder(this).setRadius(50)
                        //.setWidth(0.75f)
//                        .setConfigDialog(new SuperDialog.ConfigDialog() {
//                            @Override
//                            public void onConfig(Dialog dialog, Window window, WindowManager
//                                    .LayoutParams wlp, DisplayMetrics dm) {
//                                wlp.gravity = Gravity.TOP;
//                                wlp.y = 200;
//                                wlp.x = 100;
//                                wlp.width = 300;
//                                wlp.height = 300;
//                            }
//                        })
//                        .setCanceledOnTouchOutside(false)
//                        .setCancelable(false)
                        .setGravity(Gravity.TOP)
                        .setPadding(50, 0, 50, 0)
//                        .setTitle("提示", 80, 205)
                        .setMessage("正在授权解锁...").build();
                break;
            case 4:
                final SuperDialog.Builder builderBodySingle = new SuperDialog.Builder(this).setRadius(10)
                        .setTitle("换内容")
                        .setMessage("请等几秒！");
                builderBodySingle.build();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        builderBodySingle.setMessage("错误请重试！");
                        TranslateAnimation animation = new TranslateAnimation(15, -15, 0, 0);
                        animation.setInterpolator(new OvershootInterpolator());
                        animation.setDuration(100);
                        animation.setRepeatCount(3);
                        animation.setRepeatMode(Animation.RESTART);
                        builderBodySingle.refreshContent(animation);
                    }
                }, 3000);
                break;
            case 5:
                final List<People> list = new ArrayList<>();
                list.add(new People(1, "拍照"));
                list.add(new People(2, "从相册选择"));
                list.add(new People(3, "小视频"));
                final SuperDialog.Builder builderBodyMultiple = new SuperDialog.Builder(this)
                        .setTitle("上传头像", ColorRes.negativeButton)
                        .setCanceledOnTouchOutside(false)
                        //setItems默认是底部位置
                        .setItems(list, new SuperDialog.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Toast.makeText(MainActivity.this, list.get(position).name, Toast
                                        .LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("取消", null);
                builderBodyMultiple.build();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.add(new People(4, "大视频"));
                        builderBodyMultiple.refreshContent();
                    }
                }, 3000);
                break;
            case 6:
                new SuperDialog.Builder(this).setRadius(10)
                        .setTitle("确定要回退")
                        .setInput("请填写回退理由")
                        .setPositiveInputButton("确定", new SuperDialog.OnClickPositiveInputListener() {
                            @Override
                            public void onClick(String text, View v) {
                                Toast.makeText(v.getContext(), text, Toast.LENGTH_LONG).show();
                            }
                        }).build();
                break;
            case 7:
                new SuperDialog.Builder(this).setTitle("发现新版本").setMessage("是否更新？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("更新", new SuperDialog.OnClickPositiveListener() {
                            @Override
                            public void onClick(View v) {
                                downFile();
                            }
                        }).build();
                break;
        }
    }

    private void downFile() {
        final SuperDialog.Builder progressDialog = new SuperDialog.Builder(this);
        progressDialog.setCanceledOnTouchOutside(false).setCancelable(false)
                .setProgress(0)
                .setTitle("正在下载").setNegativeButton("取消", new SuperDialog.OnClickNegativeListener() {
            @Override
            public void onClick(View v) {
            }
        });
        final DialogFragment dialogFragment = progressDialog.build();
        okFileHelper.getInstance(MainActivity.this)
                .downLoadFile("http://hot.m.shouji.360tpcdn.com/170228/f7531b825dae5ebe2be09c5ccd3b8b4e/com" +
                        ".qihoo.appstore_300070026.apk", Environment.getExternalStorageDirectory()
                        .getAbsolutePath(), new ReqProgressCallBack<File>() {
                    @Override
                    public void onProgress(long total, long current) {
                        progressDialog.refreshProgress((int) total, (int) current);
                    }

                    @Override
                    public void onReqSuccess(File result) {
                        if (dialogFragment != null)
                            dialogFragment.dismiss();
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onClickBtn1() {
        new SuperDialog.Builder(this).setTitle("边距").setMessage("setWidth将无效")
                .setPadding(10, 0, 10, 0)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", null).build();
    }

    public void onClickBtn2(View v) {
        final String[] strings = {"增加", "删除"};
        new SuperDialog.Builder(this).setDimEnabled(false).setWidth(0.25f)
                .setBackgroundColor(Color.BLACK).setAlpha(0.5f).setRadius(0)
                .setShowAsDropDown(v, 0, 0)
                .setItems(strings, new SuperDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(MainActivity.this, strings[position], Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        onClickBtn2(view);
        return true;
    }
}
