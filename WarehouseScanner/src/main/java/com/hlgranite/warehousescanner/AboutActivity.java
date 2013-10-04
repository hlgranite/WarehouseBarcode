package com.hlgranite.warehousescanner;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        try {
            TextView textView2 = (TextView)findViewById(R.id.textView2);
            String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            textView2.setText(version);

            // NOT required. Done at layout directly.
            // Add a mailto developer link
//            TextView textView6 = (TextView)findViewById(R.id.textView6);
//            String email = getResources().getString(R.string.contact_email);
//            textView6.setText(Html.fromHtml("<a href='mailto:"+email+"'>"+email+"</a>"));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        }
    }

// disable menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.about, menu);
//        return true;
//    }
    
}
