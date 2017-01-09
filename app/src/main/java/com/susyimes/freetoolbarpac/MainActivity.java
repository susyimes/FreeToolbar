package com.susyimes.freetoolbarpac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.susyimes.freetoolbar.FreeBar;

public class MainActivity extends AppCompatActivity {
    FreeBar freeBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        freeBar= (FreeBar) findViewById(R.id.freebar);
        freeBar.move(0,100,0);
        freeBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                freeBar.hide(1);
            }
        });


    }
}
