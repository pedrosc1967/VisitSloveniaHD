package com.aplanetbit.visit.slovenia.hd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;

public class MotorrailActivity extends Activity {

    private AdView adView;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motorrail);

        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);

        adView = new AdView(this, getString(R.string.FAN_banner_id), AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle item selection
        switch (item.getItemId()) {
            case R.id.Otras_apps:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/collection/cluster?clp=igM4ChkKEzgxMzQyNTg2NTUwODQ1MTg1NzQQCBgDEhkKEzgxMzQyNTg2NTUwODQ1MTg1NzQQCBgDGAA%3D:S:ANO1ljJfH-4&gsr=CjuKAzgKGQoTODEzNDI1ODY1NTA4NDUxODU3NBAIGAMSGQoTODEzNDI1ODY1NTA4NDUxODU3NBAIGAMYAA%3D%3D:S:ANO1ljIsTuoLetalo") ) );
                return true;
            case R.id.Salir:
                this.finish();
                this.finish();
                return true;
            case R.id.Acerca:
                AlertDialog builder;
                try {
                    builder = VisitSloveniaHD.AboutDialogBuilder.create(this);
                    builder.show();
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //Para FAN
    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}


