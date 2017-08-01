package com.example.administrator.musicstart.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.administrator.musicstart.R;

public class TestActivity extends Activity {

    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        init();


    }

    private void init() {

       /* navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){

                    case R.id.test1:
                        Toast.makeText(TestActivity.this, "test1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.test2:
                        Toast.makeText(TestActivity.this, "test2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.test3:
                        Toast.makeText(TestActivity.this, "test3", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.test4:
                        Toast.makeText(TestActivity.this, "test4", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.test5:
                        Toast.makeText(TestActivity.this, "test5", Toast.LENGTH_SHORT).show();
                        break;


                }
                return false;
            }

        });
*/
    }
}
