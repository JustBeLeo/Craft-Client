package com.android.sdk13.craft.publish;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.sdk13.craft.R;
import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.utils.ContentUriUtil;
import com.android.sdk13.craft.utils.NetConfig;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.luck.picture.lib.config.PictureMimeType.ofImage;
import static com.luck.picture.lib.config.PictureMimeType.ofVideo;

public class PublishVideoActivity extends AppCompatActivity {

    public final static int CODE_VIDEO = 10000;
    public final static int CODE_PIC = 10001;

    @BindView(R.id.iv_publish_video_cover)
    ImageView ivCover;
    @BindView(R.id.tv_publish_video_upload)
    TextView tvUpload;
    @BindView(R.id.ed_publish_video_cover)
    TextView tvVideoName;
    @BindView(R.id.ed_publish_video_title)
    EditText edTitle;
    @BindView(R.id.ed_publish_video_detail)
    EditText edDetail;
    @BindView(R.id.ed_publish_video_topic)
    EditText edTopic;
    @BindView(R.id.rl_publish_video)
    RelativeLayout rlVideo;
    @BindView(R.id.pb_upload)
    ProgressBar pbUpload;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.sp_publish_video)
    Spinner spVideo;

    File cover;
    File video;
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

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_video);
        ButterKnife.bind(this);
        spVideo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type_id = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getPicture(int type, int requestCode) {
        PictureSelector.create(PublishVideoActivity.this)
                .openGallery(type)//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            String path = selectList.get(0).getPath();
            if (requestCode == CODE_PIC) {          // 图片、视频、音频选择结果回调
                cover = new File(path);
                Glide.with(this)
                        .load(path)
                        .into(ivCover);
                tvUpload.setVisibility(View.GONE);
            } else {
                selectList.get(0).getPath();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    path = ContentUriUtil.getPath(this, Uri.parse(path));
                }
                video = new File(path);
                Toast.makeText(this, video.getName(), Toast.LENGTH_SHORT).show();
                tvVideoName.setText(video.getName());
            }
        }
    }

    public void chooseCover(View view) {
        getPicture(ofImage(), CODE_PIC);
    }

    public void publish(View view) {
        if (video == null) {
            Toast.makeText(this, "你好像忘记添加视频了", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cover == null) {
            Toast.makeText(this, "你好像没加封面诶", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edTitle.getText().toString().equals("")) {
            Toast.makeText(this, "你还没有输入标题", Toast.LENGTH_SHORT).show();
            return;
        }
        OkGo.<String>post(NetConfig.VIDEO_DEPLOY)
                .params("video", video)
                .params("name", edTitle.getText().toString())
                .params("des", edDetail.getText().toString())
                .params("user_id", UserTemp.user.getId())
                .params("craft_id", 1)
                .params("cover", cover)
                .params("type_id", type_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (NetConfig.getCode(response.body()) == 200) {
                            Toast.makeText(PublishVideoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "onVideoUpload:" + response.body());
                            handler.sendEmptyMessageDelayed(1, 1000);
                            tvVideoName.setText(getFileName(video.getName()));
                        } else {
                            JSONObject object = JSON.parseObject(response.body()).getJSONObject("msg");
                            Toast.makeText(PublishVideoActivity.this, object.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void uploadProgress(Progress progress) {
                        tvSpeed.setVisibility(View.VISIBLE);
                        pbUpload.setProgress((int) (100 * progress.fraction));
                        tvSpeed.setText(String.format("正在上传：%dkb/s", progress.speed >> 10));
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Toast.makeText(PublishVideoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void exit(View view) {
        finish();
    }

    public String getFileName(String path) {
        String regEx = ".+/(.+)$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(path);
        StringBuffer sb = new StringBuffer();
        boolean rs = m.find();
        if (rs) {
            for (int i = 1; i <= m.groupCount(); i++) {
                sb.append(m.group(i));
            }
            return sb.toString();
        }
        return null;
    }

    public void uploadVideo(View view) {
        getPicture(ofVideo(), CODE_VIDEO);
    }
}
