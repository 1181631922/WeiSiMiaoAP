package com.xj.af.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xj.af.R;
import com.xj.af.common.BaseBackActivity;
import com.xj.af.util.http.PostUtil;
import com.xj.af.util.http.UploadUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddNewsActivity extends BaseBackActivity implements View.OnClickListener{
    private Handler handler;
    private EditText titleEt;
    private EditText contentEt;
    private Button submitBtn;
    private Button insertPicBtn;
    private String enName ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_add_news);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title_bar);
        title="新闻发布";
        init();
    }
    private void init(){
        titleEt = (EditText)findViewById(R.id.addnews_title_editText1);
        contentEt = (EditText)findViewById(R.id.addnews_content_editText2);
        submitBtn = (Button)findViewById(R.id.addnews_submit_button1);
        insertPicBtn = (Button)findViewById(R.id.addnews_insertPic_Button);
        enName = this.getIntent().getStringExtra("enName");
        handler = new MyHandler();
        submitBtn.setOnClickListener(this);
        insertPicBtn.setOnClickListener(this);
    }
    private static final int PHOTO_SUCCESS = 1;
    private static final int CAMERA_SUCCESS = 2;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addnews_submit_button1://提交信息
                submitBtn.setEnabled(false);
                MyThread mt = new MyThread();
                mt.start();
                break;
            case R.id.addnews_insertPic_Button://插入图片
                final CharSequence[] items = { "手机相册", "相机拍摄" };
                AlertDialog dlg = new AlertDialog.Builder(AddNewsActivity.this).setTitle("选择图片").setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int item) {
                                if(item==1){
                                    Intent getImageByCamera= new Intent("android.media.action.IMAGE_CAPTURE");
                                    startActivityForResult(getImageByCamera, CAMERA_SUCCESS);
                                }else{
                                    Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                                    getImage.addCategory(Intent.CATEGORY_OPENABLE);
                                    getImage.setType("image/*");
                                    startActivityForResult(getImage, PHOTO_SUCCESS);
                                }
                            }
                        }).create();
                dlg.show();
                break;
            default:
                break;
        }
    }

    private Map <String,InputStream> mapis = new HashMap<String, InputStream>();
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ContentResolver resolver = getContentResolver();
        String fileName = getServerURL()+"/ueditor/jsp/upload/image/m/"+ getUnitId()+System.currentTimeMillis()+".jpg";
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_SUCCESS:
                    //获得图片的uri
                    Uri originalUri = intent.getData();
                    Bitmap bitmap = null;
                    Bitmap originalBitmap = null;
                    try {
                        originalBitmap = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));
                        int height = originalBitmap.getHeight();
                        int width = originalBitmap.getWidth();
                        if(width>200){
                            bitmap = resizeImage(originalBitmap, 200);
                        }else{
                            bitmap = resizeImage(originalBitmap, originalBitmap.getWidth());
                        }
                        if(height>500 || width>500){
                            originalBitmap = resizeImage(originalBitmap, 500);
                        }
                        mapis.put(fileName , resolver.openInputStream(originalUri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(bitmap != null){
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(AddNewsActivity.this, bitmap);
                        String str = "<center><img width='100%' src='" + fileName +"'/></center>";
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                        SpannableString spannableString = new SpannableString(str);
                        //  用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText中光标所在位置
                        int index = contentEt.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = contentEt.getEditableText();
                        if(index <0 || index >= edit_text.length()){
                            edit_text.append(spannableString);
                        }else{
                            edit_text.insert(index, spannableString);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CAMERA_SUCCESS:
                    Bundle extras = intent.getExtras();
                    Bitmap originalBitmap1 = (Bitmap) extras.get("data");
                    if(originalBitmap1 != null){
                        bitmap = resizeImage(originalBitmap1, 200);
                        originalBitmap1 = resizeImage(originalBitmap1, 500);
                        String str = "<center><img width='100%' src='" + fileName +"'/></center>";
                        mapis.put(fileName ,Bitmap2IS(originalBitmap1));
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(AddNewsActivity.this, bitmap);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                        SpannableString spannableString = new SpannableString(str);
                        //  用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0,str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText中光标所在位置
                        int index = contentEt.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = contentEt.getEditableText();
                        if(index <0 || index >= edit_text.length()){
                            edit_text.append(spannableString);
                            //edit_text.append(Html.fromHtml("<imgsrc='"+ faces[new Random().nextInt(6)] +"'/>", imageGetter, null));
                        }else{
                            edit_text.insert(index, spannableString);
                        }
                    }else{
                        Toast.makeText(AddNewsActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }
    private InputStream  Bitmap2IS(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        return sbs;
    }
    /**
     * 图片缩放
     * @param originalBitmap 原始的Bitmap
     * @param newWidth 自定义宽度
     * @return 缩放后的Bitmap
     */
    private Bitmap resizeImage(Bitmap originalBitmap, int newWidth){
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        //定义欲转换成的宽、高
//			int newWidth = 200;
//			int newHeight = 200;
        //计算宽、高缩放率
        float scanleWidth = (float)newWidth/width;
        //float scanleHeight = (float)newHeight/height;
        //创建操作图片用的matrix对象 Matrix
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scanleWidth,scanleWidth);
        //旋转图片 动作
        //matrix.postRotate(45);
        // 创建新的图片Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap,0,0,width,height,matrix,true);
        return resizedBitmap;
    }



    class MyHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bd = msg.getData();
            String message = bd.getString("msg");
            if(msg.what == 100){
                submitBtn.setEnabled(true);
                titleEt.setText("");
                contentEt.setText("");
                Toast.makeText(AddNewsActivity.this, message, Toast.LENGTH_SHORT).show();
            }
            if(msg.what>0 && msg.what<100){
                Toast.makeText(getApplicationContext(), "第"+msg.what+"张图片上传成功",Toast.LENGTH_SHORT);
            }else if(msg.what==-1){
                Toast.makeText(getApplicationContext(), "发布失败："+message, Toast.LENGTH_SHORT);
            }
        }

    }

    class MyThread extends Thread{
        @Override
        public void run() {
            Message message = new Message();
            Bundle bd = new Bundle();
            PostUtil pu = new PostUtil();
            Map<String,String> map = new HashMap<String,String>();
            map.put("title", titleEt.getText().toString());
            map.put("content", contentEt.getText().toString());
            String msg = "";
            message.what = 0;
            //上传图片
            int i = 1;
            for(Map.Entry<String, InputStream> ise:mapis.entrySet()){
                Message m1 = new Message();
                i++;
                try {
                    String uploadUrl = getServerURL()+"/api/news/upload";
                    msg = UploadUtil.uploadFile(null, ise.getValue(), uploadUrl, ise.getKey());
                    Log.d("xj", msg);
                    m1.what = i;
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        ise.getValue().close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    m1.what = -1;
                }
                handler.sendMessage(m1);
            }
            //发布文字内容
            try {
                String url = getServerURL()+"/api/news/add/"+getUnitId()+"/"+enName;
                msg = pu.postData(url, map);
                message.what = 100;
            } catch (IOException e) {
                e.printStackTrace();
                message.what = -1;
                msg ="信息发布失败："+e.getMessage();
            }
            bd.putString("msg", msg);
            message.setData(bd);
            handler.sendMessage(message);
        }
    }
}
