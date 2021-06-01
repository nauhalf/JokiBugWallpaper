package com.vivindev.realfirewallpaper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.vivindev.realfirewallpaper.config.admob;

/**
 * Created by kingdov on 17/01/2017.
 */

public class DiscActivity extends AppCompatActivity {

    FloatingActionButton emailFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disc);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back));
        this.setTitle("Disclaimer");
        LinearLayout linearlayout = (LinearLayout)findViewById(R.id.unitads);
        admob.admobBannerCall(this, linearlayout);
        findStuff();
        setEmailFab();

    }

    private void findStuff() {
        emailFab = findViewById(R.id.report_email);
    }

    private void setEmailFab() {
        emailFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String subject = getResources().getString(R.string.app_name) + ":" + " " + getResources().getString(R.string.email_subject);
                String emailBody = getResources().getString(R.string.email_body_1) + getResources().getString(R.string.enter_links) + getResources().getString(R.string.email_body_2);

                Intent sendMail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",SettingsClasse.contactMail, null));
                sendMail.putExtra(Intent.EXTRA_SUBJECT, subject);
                sendMail.putExtra(Intent.EXTRA_TEXT, emailBody);
                startActivity(Intent.createChooser(sendMail, getResources().getString(R.string.email_client)));

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
