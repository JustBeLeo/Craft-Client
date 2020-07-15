package com.android.sdk13.craft;

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
import android.widget.RelativeLayout;

import com.android.sdk13.craft.utils.HtmlUtils;
import com.android.sdk13.craft.utils.UIUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    ArrayList<String> imageUrls = new ArrayList<>();
    ArrayList<Object> HtmlList = new ArrayList<>();
    @BindView(R.id.ll_test)
    LinearLayout llTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_test );
        ButterKnife.bind( this );
//        JSONObject object = JSON.parseObject( response.body() );
//        String path = object.getString( "path" );
        String path = "http://img1.gtimg.com/sports/pics/hv1/239/178/1582/102915179.jpg";
        for (int i = 0 ; i< 10 ;i++){

            ImageView iv = (ImageView) LayoutInflater.from( this ).inflate( R.layout.content_publish_image,null );
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT,UIUtils.dip2px( 200 ) );
            lp.setMargins(10, UIUtils.dip2px( 15 ), 10, UIUtils.dip2px( 15 ));
            lp.gravity = Gravity.CENTER_HORIZONTAL;
            iv.setLayoutParams(lp);

            EditText editText = (EditText) LayoutInflater.from( this ).inflate( R.layout.content_publish_edit,null );
            llTest.addView( iv );
            llTest.addView( editText );
            Glide.with( this )
                    .load( path ).into( iv );
            HtmlList.add( path );
            HtmlList.add(editText);
        }

    }

    public void run(View view) {
        Log.e( "TAG", "onCreate: " + HtmlUtils.createHtmlFromObjects(HtmlList) );
    }
}
