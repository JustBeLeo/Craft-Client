package com.android.sdk13.craft.independent_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.sdk13.craft.R;

public class EditEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_entry );
    }
}
