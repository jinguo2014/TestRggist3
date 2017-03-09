package com.example.cyt.testrggist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.UUID;

import android.view.View.OnClickListener;
import android.widget.Toast;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.net.sip.SipErrorCode.TIME_OUT;


@SuppressWarnings("deprecation")
public class HeadImgActivity extends Activity implements OnClickListener
{
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 10000000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";

    private ImageButton ib = null;
    private ImageView iv = null;
    private Button btn = null;
    private String tp = null;

   //String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
//      String PREFIX = "--", LINE_END = "\r\n";
//      String CONTENT_TYPE = "multipart/form-data"; // 内容类型
      String RequestURL = "http://app.chinaiys.com/saveheaderimg.ac?userid=12&nickname=123";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle arg0)
    {
        super.onCreate(arg0);
        setContentView(R.layout.activity_head_img);
      //初始化
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().build());StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
            // 这里可以替换为detectAll() 就包括了磁盘读写和网络I/O.penaltyLog()
                // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
            // 探测SQLite数据库操作 //打印logcat
        init();
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        System.out.print("ttttttttttttttttt"+ts);
    }

    /**
     * 初始化方法实现
     */
    private void init() {
        ib = (ImageButton) findViewById(R.id.imageButton1);
        iv = (ImageView) findViewById(R.id.imageView1);
        btn = (Button) findViewById(R.id.button1);
        ib.setOnClickListener(this);
        iv.setOnClickListener(this);
        btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton1:
                ShowPickDialog();
                break;
            case R.id.imageView1:
                ShowPickDialog();
                break;
            case R.id.button1:
                ShowPickDialog();
                break;

            default:
                break;
        }
    }
    /**
     * 选择提示对话框
     */
    private void ShowPickDialog() {
        new AlertDialog.Builder(this)
                .setTitle("设置头像...")
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        /**
                         * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
                         * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
                         */
                        Intent intent = new Intent(Intent.ACTION_PICK, null);

                        /**
                         * 下面这句话，与其它方式写是一样的效果，如果：
                         * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                         * intent.setType(""image/*");设置数据类型
                         * 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"

                         */
                        intent.setDataAndType(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "image/*");
                        startActivityForResult(intent, 1);

                    }
                })
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        /**
                         * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
                         * 文档，you_sdk_path/docs/guide/topics/media/camera.html
                         *

                         */
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        //下面这句指定调用相机拍照后的照片存储的路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                .fromFile(new File(Environment
                                        .getExternalStorageDirectory(),
                                        "xiaoma.jpg")));
                        startActivityForResult(intent, 2);
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                startPhotoZoom(data.getData());
                break;
            // 如果是调用相机拍照时
            case 2:
                File temp = new File(Environment.getExternalStorageDirectory()
                        + "/xiaoma.jpg");
                startPhotoZoom(Uri.fromFile(temp));
                break;
            // 取得裁剪后的图片
            case 3:
                /**
                 * 非空判断大家一定要验证，如果不验证的话，
                 * 在剪裁之后如果发现不满意，要重新裁剪，丢弃

                 */
                if(data != null){
                    setPicToView(data);
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }
    private String pic;

    /**
     * 保存裁剪之后的图片数据
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            System.out.print("yyyyyyyyyyyyyy"+photo);
            Drawable drawable = new BitmapDrawable(photo);
            System.out.print("wwwwwwwwwwwwww"+drawable);
            /**
             * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上
             * 传到服务器，QQ头像上传采用的方法跟这个类似
             */

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.PNG, 60, stream);
			byte[] b = stream.toByteArray();
			// 将图片流以字符串形式存储下来
			tp = new String(Base64Coder.encodeLines(b));
            System.out.print("222222222222"+tp);
//			这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了，

//			如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换

		  // Bitmap dBitmap = BitmapFactory.decodeFile(tp);
//			Drawable drawable1 = new BitmapDrawable(dBitmap);

           // final  File name= getFileFromBytes(b,pic);

          final File name =new File(b.toString());
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    FileImageUpload.uploadFile(name,RequestURL);
                }
            }.start();
            System.out.print("3333333333333"+name);
            ib.setBackgroundDrawable(drawable);
            iv.setBackgroundDrawable(drawable);
        }
    }
//    public static File getFileFromBytes(byte[] b, String outputFile) {
//        BufferedOutputStream stream = null;
//        File file = null;
//        try {
//            file = new File(outputFile);
//            FileOutputStream fstream = new FileOutputStream(file);
//            stream = new BufferedOutputStream(fstream);
//            stream.write(b);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (stream != null) {
//                try {
//                    stream.close();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        }
//        return file;
//    }
}
//    int time = (int) (System.currentTimeMillis());
//    Timestamp tsTemp = new Timestamp(time);
//    String ti = tsTemp.toString();
   // 感谢您的帮助 解决的办法是：

