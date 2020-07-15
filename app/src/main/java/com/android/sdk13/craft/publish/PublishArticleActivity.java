package com.android.sdk13.craft.publish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.utils.LoginUtils;
import com.android.sdk13.craft.utils.NetConfig;
import com.android.sdk13.craft.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.luck.picture.lib.config.PictureMimeType.ofImage;

public class PublishArticleActivity extends AppCompatActivity {

    final static int UPLOAD_COVER = 10086;
    final static int UPLOAD_VIDEO = 10087;
    final static int UPLOAD_PIC = 10088;

    @BindView(R.id.tv_publish_article_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_publish_article_confirm)
    TextView tvPConfirm;
    @BindView(R.id.iv_publish_article_cover)
    ImageView ivCover;
    @BindView(R.id.tv_publish_article_upload)
    TextView tvUpload;
    @BindView(R.id.ed_publish_article_title)
    EditText edTitle;
    @BindView(R.id.ll_publish_article_html)
    LinearLayout llHtml;
    @BindView(R.id.rl_publish)
    RelativeLayout rlPublish;
    @BindView(R.id.sp_publish_article)
    Spinner spEntry;
    @BindView(R.id.ed_item_publish)
    EditText edItemPublish;
    @BindView(R.id.iv_item_publish_part)
    ImageView ivItemPublish;

    File cover;
    File img;
    int type_id;
    String[] types = {"丝绸织染",
            "雕塑",
            "造纸与印刷",
            "金银细金工艺",
            "绘画",
            "剪纸",
            "景泰蓝",
            "陶瓷",
            "漆艺",
            "中药炮制"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_article);
        ButterKnife.bind(this);
        LoginUtils.loadLogin();
        Log.e("TAG", "onCreate: \n" + UserTemp.user);
        spEntry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type_id = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /*
     * Description 上传封面按钮
     * Date 2020/7/6 15:54
     **/
    public void uploadCover(View view) {
        getPicture(ofImage(), UPLOAD_COVER);
    }

    /*
     * Description 上传内容图片
     * Date 2020/7/6 16:05
     **/
    public void uploadImg(View view) {
        getPicture(ofImage(), UPLOAD_PIC);
    }

    /*
     * Description 获取图片
     * Date 2020/7/6 15:56
     * Param 类型，请求码
     **/
    private void getPicture(int openGallery, int requestCode) {
        PictureSelector.create(PublishArticleActivity.this)
                .openGallery(openGallery)//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(1)// 最大图片选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/MyPic")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .compress(true)// 是否压缩 true or false
                .withAspectRatio(3, 2)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .isGif(true)// 是否显示gif图片 true or false
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(requestCode);// 结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            String path = selectList.get(0).getCutPath();
            if (requestCode == UPLOAD_COVER) {          // 图片、视频、音频选择结果回调
                tvUpload.setVisibility(View.GONE);
                cover = new File(path);
                Glide.with(this)
                        .load(path)
                        .into(ivCover);
            } else {
                img = new File(path);
                Glide.with(this)
                        .load(path)
                        .into(ivItemPublish);
            }
        }
    }

    /*
     * Description 上传文章
     * Date 2020/7/6 15:58
     * Param
     **/
    public void upload(View view) {


        String title = edTitle.getText().toString();
        String text = edItemPublish.getText().toString();

        if (StringUtils.isBlank(title)) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isBlank(text)) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cover == null) {
            Toast.makeText(this, "封面还未选择", Toast.LENGTH_SHORT).show();
            return;
        }
        if (img == null) {
            Toast.makeText(this, "图片还未选择", Toast.LENGTH_SHORT).show();
            return;
        }

        OkGo.<String>post(NetConfig.ARTICLE_DEPLOY)
                .params("user_id", UserTemp.user.getId())
                .params("type_id", type_id)
                .params("craft_id", 2)
                .params("title", title)
                .params("cover", cover)
                .params("img", img)
                .params("text", text)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (NetConfig.judgeCode(response)){
                            Toast.makeText(PublishArticleActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }


    public void cancel(View view) {
        finish();
    }
}