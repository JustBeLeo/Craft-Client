package com.android.sdk13.craft.publish;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.utils.NetConfig;
import com.android.sdk13.craft.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("SimpleDateFormat")
public class PublishActivity_Activity extends AppCompatActivity {
    // 常量
    final static int UPLOAD_COVER = 10086;
    final static int UPLOAD_VIDEO = 10087;
    final static int UPLOAD_PIC = 10088;
    // 组件
    @BindView(R.id.iv_activity_cover)
    ImageView ivCover;
    @BindView(R.id.tv_activity_upload)
    TextView tvUpload;
    @BindView(R.id.ed_activity_title)
    EditText edTitle;
    @BindView(R.id.tv_activity_region)
    TextView tvRegion;
    @BindView(R.id.ed_publish_activity_content)
    EditText edContent;
    @BindView(R.id.tv_activity_start_date)
    TextView tvStartDate;
    @BindView(R.id.tv_activity_end_date)
    TextView tvEndDate;
    CityPicker cityPicker;
    // 变量
    File cover;
    Calendar start_date = Calendar.getInstance(Locale.CHINA);//获取日期格式器对象
    Calendar end_date = Calendar.getInstance(Locale.CHINA);//获取日期格式器对象
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_activity);
        ButterKnife.bind(this);
        initCityPicker();
    }

    public void uploadCover(View view) {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
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
                .forResult(UPLOAD_COVER);
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
            }
        }
    }

    public void initCityPicker() {
        cityPicker = new CityPicker.Builder(this)
                .textSize(20)//滚轮文字的大小
                .title("地址选择")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#efb336")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province(UserTemp.user.getProvince())
                .city(UserTemp.user.getCity())
                .textColor(Color.parseColor("#000000"))//滚轮文字的颜色
                .provinceCyclic(false)//省份滚轮是否循环显示
                .cityCyclic(false)//城市滚轮是否循环显示
                .districtCyclic(false)//地区（县）滚轮是否循环显示
                .visibleItemsCount(7)//滚轮显示的item个数
                .itemPadding(10)//滚轮item间距
                .onlyShowProvinceAndCity(false)
                .build();

        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //城市
                String n = citySelected[2];
                //邮编
                String code = citySelected[3];

                //地区text赋值
                tvRegion.setText(String.format("%s %s %s", province, city, n));
            }

            @Override
            public void onCancel() {
            }
        });
    }

    public void region(View view) {
        cityPicker.show();
    }

    public void start_date(View view) {
        new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
            start_date.set(Calendar.YEAR, i);
            start_date.set(Calendar.MONTH, i1);
            start_date.set(Calendar.DAY_OF_MONTH, i2);
            tvStartDate.setText(sdf1.format(start_date.getTime()));
        }, start_date.get(Calendar.YEAR), start_date.get(Calendar.MONTH), start_date.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void end_date(View view) {
        new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
            end_date.set(Calendar.YEAR, i);
            end_date.set(Calendar.MONTH, i1);
            end_date.set(Calendar.DAY_OF_MONTH, i2);
            tvEndDate.setText(sdf1.format(end_date.getTime()));
        }, end_date.get(Calendar.YEAR), end_date.get(Calendar.MONTH), end_date.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void deploy(View view) {
        String name = edTitle.getText().toString();
        String site = tvRegion.getText().toString();
        String content = edContent.getText().toString();
        Date start = start_date.getTime();
        Date end = end_date.getTime();

        if (cover == null) {
            Toast.makeText(this, "您还没有选择封面呢", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isBlank(name)) {
            Toast.makeText(this, "标题！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isBlank(site)) {
            Toast.makeText(this, "地点好像没选呢", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isBlank(content)) {
            Toast.makeText(this, "还没输入简介哦", Toast.LENGTH_SHORT).show();
            return;
        }
        if (start == null) {
            Toast.makeText(this, "没有开始时间呢", Toast.LENGTH_SHORT).show();
            return;
        }
        if (end == null) {
            Toast.makeText(this, "没有结束时间呢", Toast.LENGTH_SHORT).show();
            return;
        }
        /*
        * @RequestParam(value = "user_id") Long user_id,
        * @RequestParam(value = "name") String name,
        * @RequestParam(value = "site") String site,
        * @RequestParam(value = "cover") MultipartFile cover,
        * @RequestParam(value = "content") String content,
        * @RequestParam(value = "start_time") Date start_time,
        * @RequestParam(value = "end_time") Date end_time,
        * */
        OkGo.<String>post(NetConfig.ACTIVITY_DEPLOY)
                .params("user_id", UserTemp.user.getId())
                .params("name", name)
                .params("site", site)
                .params("cover", cover)
                .params("content", content)
                .params("start_time", start.getTime())
                .params("end_time", end.getTime())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (NetConfig.judgeCode(response)){
                            Toast.makeText(PublishActivity_Activity.this,"上传成功！",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    public void cancel(View view) {
        finish();
    }


}