package com.scanner.offlineqrscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.scanner.offlineqrscanner.R;

public class MainActivity extends AppCompatActivity {







    DrawerLayout drawerLayout;
    MaterialToolbar toolbar;
    NavigationView navigationView;

   CardView qr_genarate ,qr_scan;



    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        qr_genarate = findViewById(R.id.qr_genarate);
        qr_scan = findViewById(R.id.qr_scan);


        qr_genarate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,qrGenarateActivity.class));
            }
        });

        qr_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,qrScanActivity.class));
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainActivity.this,drawerLayout,toolbar,R.string.drawer_close,R.string.drawer_open
        );
        drawerLayout.addDrawerListener(toggle);









        //SearchView Call Here






        //Navigation item adds

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId==R.id.icon_share) {
                    ShareApp(MainActivity.this);
                    drawerLayout.closeDrawer(GravityCompat.START);

                } else if (itemId==R.id.icon_policy) {

                    gotoLink();

                    drawerLayout.closeDrawer(GravityCompat.START);

                }else if (itemId==R.id.icon_rate) {

                    final String appName = getPackageName();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));

                    drawerLayout.closeDrawer(GravityCompat.START);

                }

                return true;
            }
        });
        //=======================================================




    }


    //Share App code
    private void ShareApp(Context context){
        // code here
        final String appPakageName = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Download Now : https://play.google.com/store/apps/details?id=" + appPakageName );
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    //privacy_policy_link_open_code
    private void gotoLink(){
        try {

            String download_link = "https://sites.google.com/view/privacy-policy-2048-puzzle/home";
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(download_link));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


}