package com.baidu.duer.dcs.androidapp;


import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.IconMarginSpan;
import android.util.Base64;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.duer.dcs.Fragment.HiChinaCombineBeforeFragment;
import com.baidu.duer.dcs.Fragment.HiChinaXihuSelectPhotoFragment;
import com.baidu.duer.dcs.Fragment.SelectDollsFragment;
import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.util.FaceRectangle;
import com.baidu.duer.dcs.util.Faces;
import com.baidu.duer.dcs.util.Image;
import com.baidu.duer.dcs.util.MergeFace;
import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class XihuTakePhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private HiChinaXihuSelectPhotoFragment mPhotoFragment;
    private SelectDollsFragment mDollsFragment;
    private HiChinaCombineBeforeFragment mCombineFragment;
    private ImageView mSelected, mTitle,mEndPhoto;
    private int CURRENTSTEP = 1;
    private int mBackGround, mDolls;
    private FragmentManager fm;
    private FragmentTransaction tx;
    private Uri imageUri;
    public static final int TAKE_PHOTO = 1;
    private String responseData, responseData2;
    private FrameLayout mFl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xihu_take_photo);

        mPhotoFragment = new HiChinaXihuSelectPhotoFragment();
        mDollsFragment = new SelectDollsFragment();
        mCombineFragment = new HiChinaCombineBeforeFragment();

        fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.add(R.id.hichina_secenery_xihu_fl, mPhotoFragment, "ONE");
        tx.commit();

        mSelected = (ImageView) findViewById(R.id.hichina_secenery_xihu_selected);
        mTitle = (ImageView) findViewById(R.id.jichina_secenery_xihu_title);
        mFl = (FrameLayout) findViewById(R.id.hichina_secenery_xihu_fl);
        mEndPhoto= (ImageView) findViewById(R.id.hichina_secenery_endphoto);
        mSelected.setOnClickListener(this);


    }

    public void back(View view) {
        this.finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hichina_secenery_xihu_selected:
                if (CURRENTSTEP == 1) {
                    int backgroundNumber = mPhotoFragment.getCurrentIndex();
                    getBackground(backgroundNumber);

//                    FragmentTransaction tx = fm.beginTransaction();
//                    tx.replace(R.id.hichina_secenery_xihu_fl, mDollsFragment, "TWO");
//                    tx.commit();
                    //mTitle.setImageResource(R.mipmap.ic_selectdolls_title);
                    mSelected.setImageResource(R.mipmap.ic_takephoto);
                    //Toast.makeText(XihuTakePhotoActivity.this, "index=" + backgroundNumber + " ", Toast.LENGTH_SHORT).show();
                    CURRENTSTEP = 2;
                } else if (CURRENTSTEP == 2) {
                    takePhoto();

//                    int dollsNumber = mDollsFragment.getmDollsNumber();
//                    getDolls(dollsNumber);//得到了人物和背景
//
//
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("Bg", mBackGround);
//                    bundle.putInt("Dolls", mDolls);
//
//                    mCombineFragment.setArguments(bundle);
//
//                    FragmentTransaction tx = fm.beginTransaction();
//                    tx.replace(R.id.hichina_secenery_xihu_fl, mCombineFragment, "THREE");
//                    tx.commit();
//
////
//
//                    mSelected.setImageResource(R.mipmap.ic_takephoto);
//                    CURRENTSTEP = 3;
                }
//                else if (CURRENTSTEP == 3) {
//                    takePhoto();
//
//                }
                break;
            default:
                break;
        }

    }

    private void takePhoto() {
        File outputImage = new File(Environment.getExternalStorageDirectory() + "/Pictures/output.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(XihuTakePhotoActivity.this,
                    "com.example.zhang.cameraalbumtest.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //把拍摄的照片显示出来

                    faceMerge();


                }
                break;
            default:
                Toast.makeText(this, "switch出错", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void faceMerge() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                //首先合成背景和选中的人物
//                Resources res = XihuTakePhotoActivity.this.getResources();
//                Bitmap bg = BitmapFactory.decodeResource(res, mBackGround);
//                Resources res2 = XihuTakePhotoActivity.this.getResources();
//                Bitmap dolls = BitmapFactory.decodeResource(res2, mDolls);
//                Bitmap combineBefore = CombineImage(bg, dolls);


                //选中的人物
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                //获取资源图片
                InputStream is = getResources().openRawResource(mBackGround);
                Bitmap yishuzhao= BitmapFactory.decodeStream(is,null,opt);


//                Resources res3 = XihuTakePhotoActivity.this.getResources();
//                Bitmap yishuzhao = BitmapFactory.decodeResource(res, R.drawable.yishuzhao);


                File file = new File(Environment.getExternalStorageDirectory() + "/Pictures/bainiangzijpg.jpg");
                try {
                    file.createNewFile();
                    BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    yishuzhao.compress(Bitmap.CompressFormat.JPEG, 100, os);//100 是压缩率,100表示不压缩 os.flush();
                    os.close();
                    //Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // File file = new File(Environment.getExternalStorageDirectory() + "/Pictures/bainiangzijpg.jpg");
                File file2 = new File(Environment.getExternalStorageDirectory() + "/Pictures/output.jpg");


                OkHttpClient client = new OkHttpClient();

                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

                builder.addFormDataPart("api_key", "qVZJUO28yKUKnRQylNonSf7iIZi2Q2YH");
                builder.addFormDataPart("api_secret", "R7rqY-u-lyR4nq3ivQDe9tq0gPE7BQ2D");
                builder.addFormDataPart("image_file", file.getName(), requestBody);

                Request request = new Request.Builder()
                        .url("https://api-cn.faceplusplus.com/facepp/v3/detect")
                        .post(builder.build())
                        .build();


                try {
                    Response response = client.newCall(request).execute();
                    responseData = response.body().string();
                    Message message = Message.obtain();
                    message.what = 1;
                    myhandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //接下来进行人脸融合,file1经过解析得到人脸的坐标，作为模板图（背景）
                //file2进行融合，脸为file2
                Gson gson = new Gson();
                Image image = gson.fromJson(responseData, Image.class);
                List<Faces> faces = image.getFaces();
                FaceRectangle rectangle = faces.get(0).getFace_rectangle();

                int top = rectangle.getTop();
                int left = rectangle.getLeft();
                int height = rectangle.getHeight();
                int width = rectangle.getWidth();
                String template_rectangle = top + "," + left + "," + height + "," + width;

                MultipartBody.Builder builder2 = new MultipartBody.Builder().setType(MultipartBody.FORM);
                RequestBody requestBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), file2);

                builder2.addFormDataPart("api_key", "qVZJUO28yKUKnRQylNonSf7iIZi2Q2YH");
                builder2.addFormDataPart("api_secret", "R7rqY-u-lyR4nq3ivQDe9tq0gPE7BQ2D");
                //模板图，使用requestBody
                builder2.addFormDataPart("template_file", file.getName(), requestBody);
                builder2.addFormDataPart("template_rectangle", template_rectangle);
                builder2.addFormDataPart("merge_file", file2.getName(), requestBody2);

                Request request2 = new Request.Builder()
                        .url("https://api-cn.faceplusplus.com/imagepp/v1/mergeface")
                        .post(builder2.build())
                        .build();
                try {
                    Response response = client.newCall(request2).execute();
                    responseData2 = response.body().string();
                    Message message = Message.obtain();
                    message.what = 2;
                    myhandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    Gson gson = new Gson();

                    Image image = gson.fromJson(responseData, Image.class);
                    List<Faces> faces = image.getFaces();
                    FaceRectangle rectangle = faces.get(0).getFace_rectangle();
//                    mTv.setText(responseData+" 接下来是解析   "+rectangle.getLeft()+"  "+rectangle.getTop()+"  "
//                                +rectangle.getWidth()+"  "+rectangle.getHeight());

                    break;
                case 2:
                    Gson gson2 = new Gson();
                    MergeFace mergeFace = gson2.fromJson(responseData2, MergeFace.class);

                    String mergeImageString = mergeFace.getResult();

                    Bitmap bitmap = stringtoBitmap(mergeImageString);

//                    Resources res = XihuTakePhotoActivity.this.getResources();
//                    Bitmap bainiangzi= BitmapFactory.decodeResource(res, R.drawable.bainiangzipng);
//
//                    Bitmap afterCrop = ImageCrop(bitmap);
//
//                    Bitmap combineImage=CombineImage(bainiangzi,afterCrop);

                 //   Drawable drawable = new BitmapDrawable(bitmap);

                    mEndPhoto.setImageBitmap(bitmap);
                 //   mFl.setBackground(drawable);
                    FragmentTransaction tx = fm.beginTransaction();
                    tx.hide(mPhotoFragment);
                    tx.commit();


                    break;

            }
        }


    };

    public Bitmap CombineImage(Bitmap b1, Bitmap b2) {
        int bgWidth = b1.getWidth();
        int bgHeight = b1.getHeight();
        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.RGB_565);


        Matrix martrix = new Matrix();
        float scaleWidth = (float) (450.0 / b2.getWidth());
        float scaleHeight = (float) (450.0 / b2.getHeight());
        martrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(b2, 0, 0, b2.getWidth(), b2.getHeight(), martrix, true);


        Canvas cv = new Canvas(newbmp);

        cv.drawBitmap(b1, 0, 0, null);
        cv.drawBitmap(resizedBitmap, 300, 480, null);

        return newbmp;
    }

    public Bitmap ImageCrop(Bitmap bitmap) {

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Bitmap afterCrop = Bitmap.createBitmap(bitmap, 222, 117, 171, 219, null, false);
        return afterCrop;

    }


    public Bitmap stringtoBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    private void getDolls(int dollsNumber) {

        switch (dollsNumber) {
            case 1:
                mDolls = R.drawable.xuxianpng;
                break;
            case 2:
                mDolls = R.drawable.bainiangzipng;
                break;
            case 3:
                mDolls = R.drawable.fahaipng;
                break;
            case 4:
                mDolls = R.drawable.xiaoqingpng;
                break;
        }
    }

    private void getBackground(int backgroundNumber) {
        switch (backgroundNumber) {
            case 0:
                mBackGround = R.drawable.yishuzhao;
                break;

            case 1:
                mBackGround = R.drawable.yishuzhao2;
                break;
        }
    }
}
