package com.android.sdk13.craft.game;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.sdk13.craft.R;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_game );

        new AlertDialog.Builder( this )
                .setTitle( "欢迎光临" )
                .setMessage( "恭喜你发现了一个刀剑制作室，在这里你可以用最简单的方法学习刀剑的制作流程，赶快来试试吧！" )
                .setNeutralButton( "好的",null )
                .show();

        heat();

    }

    private void heat() {

    }
}
