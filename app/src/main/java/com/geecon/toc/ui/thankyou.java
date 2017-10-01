package com.geecon.toc.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.geecon.toc.R;

public class thankyou extends AppCompatActivity {

    private TextView txt_title,txt_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);

        String title = getIntent().getStringExtra("TITLE");
        String desc = getIntent().getStringExtra("DESC");

        txt_title = (TextView)findViewById(R.id.t1);
        txt_desc = (TextView)findViewById(R.id.t3);
        txt_title.setText(title);
        txt_desc.setText(desc);
    }

    public void info2(View view)
    {
        Intent intent ;
        switch (view.getId())
        {
            case R.id.img2:
                intent = new Intent(thankyou.this,MainActivity.class);
                startActivity(intent);
                break;

        }
    }
}
