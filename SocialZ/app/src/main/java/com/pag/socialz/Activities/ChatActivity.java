package com.pag.socialz.Activities;

import android.os.Bundle;

import com.pag.socialz.R;

public class ChatActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        if(!hasInternetConnection()) showSnackBar("");
    }
}
