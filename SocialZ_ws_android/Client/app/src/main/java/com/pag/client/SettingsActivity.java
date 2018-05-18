package com.pag.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Bundle extras = getIntent().getExtras();
        ((EditText)findViewById(R.id.IP)).setText(extras.getString("address"));
        ((EditText)findViewById(R.id.PORT)).setText(extras.getString("port"));
    }

    public void onClick(View v){
        String ip = ((EditText)findViewById(R.id.IP)).getText().toString();
        String port = ((EditText)findViewById(R.id.PORT)).getText().toString();
        Intent i = new Intent(this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("address",ip);
        bundle.putString("port",port);
        i.putExtras(bundle);
        startActivity(i);
        this.finish();
    }
}
