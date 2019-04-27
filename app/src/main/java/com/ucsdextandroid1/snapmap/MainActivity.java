package com.ucsdextandroid1.snapmap;

import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ucsdextandroid1.snapmap.util.WindowUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.am_toolbar);
        WindowUtil.doOnApplyWindowInsetsToMargins(toolbar, true, false);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.am_frame, MapFragment.create())
                .commit();

        DataSources.getInstance().getAppName(new DataSources.Callback<String>() {
            @Override
            public void onDataFetched(String data) {
                toolbar.setTitle(data);
            }
        });
    }
}
