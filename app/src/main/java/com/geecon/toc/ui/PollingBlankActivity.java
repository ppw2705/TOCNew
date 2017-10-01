package com.geecon.toc.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.geecon.toc.R;

public class PollingBlankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polling_blank);
    }

    public void home(View view)
    {
        Intent intent ;
        switch (view.getId())
        {
            case R.id.img_home:
                intent = new Intent(PollingBlankActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }
}
