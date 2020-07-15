package com.android.sdk13.craft.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.sdk13.craft.R;
import com.android.sdk13.craft.entity.User;
import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.utils.NetConfig;
import com.bumptech.glide.Glide;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.luck.picture.lib.config.PictureMimeType.ofImage;
import static java.lang.String.format;

/*
 * @Author sdk13
 * @Description 用户个人信息的查看界面，点击条目可以修改
 * @Date 13:43 2019/9/5
 **/
public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.iv_edit_profile_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_edit_profile_username)
    TextView tvUsername;
    @BindView(R.id.tv_edit_profile_gender)
    TextView tvGender;
    @BindView(R.id.tv_edit_profile_email)
    TextView tvEmail;
    @BindView(R.id.tv_edit_profile_mtto)
    TextView tvMtto;
    @BindView(R.id.tv_edit_profile_id)
    TextView tvId;
    @BindView(R.id.tv_edit_profile_regTime)
    TextView tvRegTime;
    @BindView(R.id.tv_edit_profile_region)
    TextView tvRegion;
    @BindView(R.id.tv_edit_profile_isCraft)
    TextView tvIsCraft;

    View view;
    User user = UserTemp.user;
    String result = "";

    String mDiquText="";
    CityPicker cityPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_profile );
        ButterKnife.bind( this );
        getUserData();
        initCityPicker();
    }

    /*
     * @Description 更新用户的信息
     * @Date 14:27 2019/9/7
     **/
    private void getUserData() {
        OkGo.<String>get( NetConfig.GET_USER_INFO )
                .params( "id", user.getId() )
                .execute( new StringCallback() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject data = JSON.parseObject( response.body() ).getJSONObject( "data" ).getJSONObject( "user" );
                        UserTemp.user = data.toJavaObject( User.class );
                        user = UserTemp.user;
                        Glide.with( EditProfileActivity.this )
                                .load( NetConfig.URL+user.getAvatar_url() )
                                .placeholder( R.mipmap.avatar_non_login )
                                .circleCrop()
                                .into( ivAvatar );
                        // 加载普通TextView
                        tvUsername.setText( user.getUsername() );
                        tvId.setText( format( "%d", user.getId() ) );

                        String str="";
                        @SuppressLint("SimpleDateFormat")
                        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try{
                            str=sdf.format(user.getReg_time());
                            tvRegTime.setText( str );
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        // 加载需要判断的TextView
                        tvGender.setText( user.getGender() ? "男" : "女" );
                        tvIsCraft.setText( user.getLevel() == 1 ? "通过" : user.getLevel() == 2 ? "待审核" : "快去审核吧" );
                        judgeString(tvEmail,user.getEmail());
                        judgeString(tvMtto,user.getMotto());
                        if(user.getProvince()==null || user.getCity()==null){
                            tvRegion.setText( "还没有哦" );
                        }else {
                            tvRegion.setText( format( "%s %s", user.getProvince(), user.getCity() ) );
                        }
                    }
                } );
    }



    private void judgeString(TextView view, String str) {
        if(str!=null){
            view.setText( str );
        }else {
            view.setText( "还没有哦" );
        }
    }

    public void avatar(View view) {
        PictureSelector.create( this )
                .openGallery( ofImage() )//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
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
                .forResult( 1 );// 结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult( data );
            HttpParams params = new HttpParams();
            File file = new File( selectList.get( 0 ).getCutPath(), "" );
            params.put( "avatar", file );
            update( params );
        }
    }

    public void username(View view) {
        showMyDialog( "姓名", "username" );
    }

    public void gender(View view) {
        String[] genders = {"女", "男"};
        HttpParams params = new HttpParams();

        new AlertDialog.Builder( this )
                .setSingleChoiceItems( genders, user.getGender() ? 1 : 0, (dialog, which) -> result = which + "" )
                .setTitle( "修改性别" )
                .setNegativeButton( "取消", null )
                .setPositiveButton( "确认", (dialog, which) -> {
                    switch (result) {
                        case "0":
                            params.put( "gender", 0 );
                            update( params );
                            break;
                        case "1":
                            params.put( "gender", 1 );
                            update( params );
                            break;
                    }
                } )
                .show();
    }

    public void email(View view) {
        showMyDialog( "电子邮箱", "e_mail" );
    }

    public void region(View view) {
        initCityPicker();
        cityPicker.show();
    }

    public void mtto(View view) {
        showMyDialog( "个性签名","motto" );
    }

    private void showMyDialog(String text, String type) {
        view = LayoutInflater.from( this ).inflate( R.layout.content_dialog, null );
        TextView textView = view.findViewById( R.id.tv_dialog_name );
        EditText editText = view.findViewById( R.id.et_dialog_text );
        textView.setText( text );
        editText.setHint( "请输入" + text );
        HttpParams params = new HttpParams();
        new AlertDialog.Builder( this )
                .setTitle( "修改信息" )
                .setView( view )
                .setNegativeButton( "取消", null )
                .setNeutralButton( "确认修改", (dialog, which) -> {
                    result = editText.getText().toString();
                    if (!result.equals( "" )) {
                        params.put( type, result );
                        update( params );
                    }
                } )
                .show();
    }

    private void update(HttpParams params) {
        Log.e( "TAG", "update: " + params );
        OkGo.<String>post( NetConfig.UPLOAD_USER_INFO )
                .params( "id", user.getId() )
                .params( params )
                .execute( new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        int code = NetConfig.getCode( response.body() );
                        Log.e( "TAG", "onSuccess: " + response.body() );
                        if (code == 200) {
                            Toast.makeText( EditProfileActivity.this, "修改成功", Toast.LENGTH_SHORT ).show();
                            getUserData();
                        } else {
                            Toast.makeText( EditProfileActivity.this, "出现未知错误", Toast.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.e( "TAG", "onError: " + response.body() );
                    }
                } );
        result = "";
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
                .province(user.getProvince())
                .city(user.getCity())
                .textColor(Color.parseColor("#000000"))//滚轮文字的颜色
                .provinceCyclic(false)//省份滚轮是否循环显示
                .cityCyclic(false)//城市滚轮是否循环显示
                .districtCyclic(false)//地区（县）滚轮是否循环显示
                .visibleItemsCount(7)//滚轮显示的item个数
                .itemPadding(10)//滚轮item间距
                .onlyShowProvinceAndCity(true)
                .build();

        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //邮编
                String code = citySelected[3];

                //地区text赋值
                HttpParams params = new HttpParams();
                params.put("province",province);
                params.put("city",city);
                update(params);
            }

            @Override
            public void onCancel() {


            }
        });
    }

    /*
     * @Description 返回按钮
     * @Date 13:45 2019/9/5
     **/
    public void back(View view) {
        finish();
    }

}
