package com.android.sdk13.craft.publish;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.sdk13.craft.R;
import com.android.sdk13.craft.utils.HtmlUtils;
import com.android.sdk13.craft.utils.NetConfig;
import com.android.sdk13.craft.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.luck.picture.lib.config.PictureMimeType.ofImage;

/*
 * @Author
 * @Description 用于词条内容的发布
 * @Date 14:20 2020/6/24
 **/
public class PublishTutorialActivity extends AppCompatActivity {

    final static int UPLOAD_COVER = 10086;
    final static int UPLOAD_VIDEO = 10087;
    final static int UPLOAD_PIC = 10088;

    @BindView(R.id.tv_publish_cancel)
    TextView tvCancel;                  // 取消按钮
    @BindView(R.id.tv_publish_confirm)
    TextView tvConfirm;                 // 确认按钮

    @BindView(R.id.tv_publish_upload)
    TextView tvUpload;                  // 上传按钮
    @BindView(R.id.iv_publish_cover)
    ImageView ivCover;                  // 封面图片
    @BindView(R.id.ed_publish_title)
    EditText edTitle;                   // 标题
    @BindView(R.id.ll_publish_html)
    LinearLayout llHtml;                // 用于装载图文视频内容
    @BindView(R.id.ed_publish_text1)
    EditText edText1;                   // 初始文字


    String cover_url = "";
    String user_id = "1";
    String title;
    String text = "";
    int type_id = 1;
    ArrayList<Object> HtmlList = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_publish );
        ButterKnife.bind( this );
        HtmlList.add( edText1 );
        initOtherListener();
    }

    private void initOtherListener() {
        tvUpload.setOnClickListener( (v) -> {
            getPicture( ofImage(), UPLOAD_COVER );
        } );
        tvCancel.setOnClickListener( v -> {
            finish();
        } );
        tvConfirm.setOnClickListener( v -> {
            title = edTitle.getText().toString();
            text = HtmlUtils.createHtmlFromObjects( HtmlList );
            Log.e( "TAG", "initOtherListener: " + text );
            OkGo.<String>post( NetConfig.UPLOAD_ARTICLE )
                    .params( "user_id", user_id )
                    .params( "cover",cover_url )
                    .params( "title",title )
                    .params( "type_id",type_id + "" )
                    .params( "text",text )
                    .execute( new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.e( "TAG", "onSuccess: " + response.body() );
                        }

                        @Override
                        public void onError(Response<String> response) {
                            Log.e( "TAG", "onError: " + response.body() );
                        }
                    } );
        } );
    }

    /*
     * @Description 用于开启图片选择界面
     * @Date 9:32 2020/6/24
     * @Param [openGallery, maxSelectNum, requestCode]
     **/
    private void getPicture(int openGallery, int requestCode) {
        PictureSelector.create( PublishTutorialActivity.this )
                .openGallery( openGallery )//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum( 1 )// 最大图片选择数量 int
                .imageSpanCount( 4 )// 每行显示个数 int
                .selectionMode( PictureConfig.SINGLE )// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage( true )// 是否可预览图片 true or false
                .isCamera( true )// 是否显示拍照按钮 true or false
                .imageFormat( PictureMimeType.PNG )// 拍照保存图片格式后缀,默认jpeg
                .sizeMultiplier( 0.5f )// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath( "/MyPic" )// 自定义拍照保存路径,可不填
                .enableCrop( true )// 是否裁剪 true or false
                .freeStyleCropEnabled( true )// 裁剪框是否可拖拽 true or false
                .rotateEnabled( true ) // 裁剪是否可旋转图片 true or false
                .scaleEnabled( true )// 裁剪是否可放大缩小图片 true or false
                .isDragFrame( true )// 是否可拖动裁剪框(固定)
                .compress( true )// 是否压缩 true or false
                .withAspectRatio( 3, 2 )// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .isGif( true )// 是否显示gif图片 true or false
                .freeStyleCropEnabled( true )// 裁剪框是否可拖拽 true or false
                .previewEggs( true )// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .minimumCompressSize( 100 )// 小于100kb的图片不压缩
                .forResult( requestCode );// 结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult( data );
            if (requestCode == UPLOAD_COVER) {          // 图片、视频、音频选择结果回调
                tvUpload.setVisibility( View.GONE );
            }
            uploadCover( selectList.get( 0 ).getCutPath(), requestCode );
        }
    }

    /*
     * @Description 用于上传媒体到服务器
     * @Date 10:02 2020/6/24
     **/
    private void uploadCover(String path, int requestCode) {
        File file = new File( path, "" );
        String url = "";
        if (requestCode == UPLOAD_COVER) {
            url = NetConfig.UPLOAD_COVER;
        } else {
            url = NetConfig.UPLOAD_PIC;
        }

        OkGo.<String>post( url )
                .params( "file", file )
                .tag( this )
                .execute( new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e( "TAG", "onSuccess: " + response.body() );
                        Log.e( "TAG", "UPLOAD_COVER: " + response.body() );
                        JSONObject object = JSON.parseObject( response.body() );
                        String path = object.getJSONObject( "data" ).getString( "path" );
                        Log.e( "TAG", "取出字符串path: " + path );
                        // 根据请求不同做不同的操作
                        if (requestCode == UPLOAD_COVER) {
                            cover_url = path;
                            //加载封面图片
                            Glide.with( PublishTutorialActivity.this )
                                    .load( path )
                                    .into( ivCover );
                        } else if (requestCode == UPLOAD_PIC) {

                            ImageView iv = (ImageView) LayoutInflater.from( PublishTutorialActivity.this ).inflate( R.layout.content_publish_image, null );
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, UIUtils.dip2px( 200 ) );
                            lp.setMargins( 10, UIUtils.dip2px( 15 ), 10, UIUtils.dip2px( 15 ) );
                            lp.gravity = Gravity.CENTER_HORIZONTAL;
                            iv.setLayoutParams( lp );
                            EditText editText = (EditText) LayoutInflater.from( PublishTutorialActivity.this ).inflate( R.layout.content_publish_edit, null );
                            llHtml.addView( iv );
                            llHtml.addView( editText );
                            Glide.with( PublishTutorialActivity.this )
                                    .load( path ).into( iv );
                            HtmlList.add( path );
                            HtmlList.add( editText );
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.e( "TAG", "onError: " + response.body() );
                    }
                } );
    }

    public String getFileName(String path) {
        String regEx = ".+/(.+)$";
        Pattern p = Pattern.compile( regEx );
        Matcher m = p.matcher( path );
        StringBuffer sb = new StringBuffer();
        boolean rs = m.find();
        if (rs) {
            for (int i = 1; i <= m.groupCount(); i++) {
                sb.append( m.group( i ) );
            }
            return sb.toString();
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HtmlList.clear();
    }
}
