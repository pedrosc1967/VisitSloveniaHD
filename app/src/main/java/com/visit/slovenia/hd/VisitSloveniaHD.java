package com.visit.slovenia.hd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.apptracker.android.track.AppTracker;
import com.facebook.FacebookSdk;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.appevents.AppEventsLogger;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.ads.fullscreen.RevMobFullscreen;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.Intent.ACTION_VIEW;
import static com.visit.slovenia.hd.R.string.FacebookAudienceID;

public class VisitSloveniaHD extends Activity implements OnInitListener {


    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech mTts;
    private ShareActionProvider mShareActionProvider;

    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    private RevMob revmob;


    private RevMobFullscreen fullscreen;
    private boolean adIsLoaded = false;

    RevMobAdsListener revmobListener = new RevMobAdsListener() {

        // Required
        @Override
        public void onRevMobSessionIsStarted() {
            loadFullscreen();// pre-load it without showing it
        }

        public void onRevMobAdReceived() {
            adIsLoaded = true; // Now you can show your fullscreen whenever you want
        }
    };
    private Context context;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public void loadFullscreen() {
        fullscreen = revmob.createFullscreen(this, revmobListener);
    }

    public void showRevmobAd() {
        if (adIsLoaded) fullscreen.show(); // call it wherever you want to show the fullscreen ad
    }


    //Definici�n del men� en menu.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /** Inflating the current activity's menu with res/menu/items.xml */
        getMenuInflater().inflate(R.menu.menu, menu);

        /** Getting the actionprovider associated with the menu item whose id is share */
        mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.menu_share).getActionProvider();

        /** Setting a share intent */
        mShareActionProvider.setShareIntent(getDefaultShareIntent());

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Returns a share intent
     */
    private Intent getDefaultShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject1));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.extra_text1) + getString(R.string.paquete));
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Otras_apps:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=Aabcdata&c=apps")));
                return true;
            case R.id.Rate:
                startActivity(new Intent(ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getString(R.string.paquete))));
                return true;
            case R.id.Salir:
                FlurryAgent.onEndSession(this);
                this.finish();
                return true;
            case R.id.Acerca:
                AlertDialog builder;
                try {
                    builder = AboutDialogBuilder.create(this);
                    builder.show();
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

// Para las cookies de Google


    // Para Ogury
    @Override
    protected void onResume() {
        super.onResume();

        /*
        // Para revmob
        revmob = RevMob.startWithListener(this, revmobListener);

        Presage.getInstance().adToServe("interstitial", new IADHandler() {

            @Override
            public void onAdNotFound() {
                // Para LeadBolt
                if ((getString(R.string.LeadBoltCTRL).equalsIgnoreCase("true"))) {
                    //Toast.makeText(VisitSloveniaHD.this,
                    //        "Activado Leadbolt", Toast.LENGTH_LONG).show();
                    AppTracker.loadModule(getApplicationContext(), "inapp");
                }

                if (getString(R.string.revmobCTRL).equalsIgnoreCase("true")) {
                    //Toast.makeText(VisitSloveniaHD.this,
                    //        "Activado Revmob", Toast.LENGTH_LONG).show();
                    showRevmobAd();
                }


            }

            @Override
            public void onAdFound() {
                Log.i("PRESAGE", "ad found");
            }

            @Override
            public void onAdClosed() {
                Log.i("PRESAGE", "ad closed");
            }
        });

        */
    }
//Definicion de la interfaz de usuario

    //private AdView mAdView; // AdMob

    private AdView adView;  // Facebook ad

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        OnClickButtonListener();
        OnClickButtonMapListener();
     //   OnClickButtonMapSloveniaListener();


        // Instantiate an AdView view
        adView = new AdView(this, getString(FacebookAudienceID), AdSize.BANNER_HEIGHT_50);

        // Find the Vocabulario layout of your activity

        //RelativeLayout adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.adViewContainer);
        layout.addView(adView);
       // adViewContainer.addView(adView);

        adView.loadAd();

        // setContentView(R.layout.activity_ad_sample);

        // Instantiate an AdView view
        //adView = new AdView(this, "816639391801383_816641235134532", AdSize.BANNER_HEIGHT_50);

        // Find the Vocabulario layout of your activity
        //LinearLayout layout = (LinearLayout)findViewById(R.id.activityLayout);

        // Add the ad view to your activity layout
        //layout.addView(adView);

        // Request to load an ad
        //adView.loadAd();


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        /* This was for AdMob
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        */

        //Presage.getInstance().setContext(this.getBaseContext());
        //Presage.getInstance().start();

        revmob = RevMob.startWithListener(this, revmobListener);

        if (savedInstanceState == null) {
            // Initialize Leadbolt SDK with your api key
            AppTracker.startSession(getApplicationContext(), getString(R.string.LeadboltStr));
        }
        // cache Leadbolt Ad without showing it
        AppTracker.loadModuleToCache(getApplicationContext(), "inapp");

        // call this when you want to display the Leadbolt Interstitial
        //AppTracker.loadModule(getApplicationContext(), "inapp");

        if (getString(R.string.revmobCTRL).equalsIgnoreCase("true")) {
            //Toast.makeText(VisitSloveniaHD.this,
            //        "Activado Revmob", Toast.LENGTH_LONG).show();
            showRevmobAd();
        }

        //showRevmobAd();

        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker(getString(R.string.analyticsstr)); // Replace with actual tracker/property Id
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);


// All subsequent hits will be send with screen name = "Vocabulario screen"
        tracker.setScreenName("Vocabulario screen");

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("UX")
                .setAction("click")
                .setLabel("submit")
                .build());

// Builder parameters can overwrite the screen name set on the tracker.
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("UX")
                .setAction("click")
                .setLabel("help popup")
                //  .set(Fields.SCREEN_NAME, "help popup dialog")
                .build());


        //Esta mierda viene de Google


        boolean pertenece;
        String locale = "";

        try {
            Process exec = Runtime.getRuntime().exec(new String[]{"getprop", "persist.sys.language"});

            //String locale = new BufferedReader(new InputStreamReader(exec.getInputStream())).readLine();

            locale = Locale.getDefault().toString();

            exec.destroy();
            Log.e("", "Device locale: " + locale);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> listaUE = new ArrayList<String>();
        // Añadir los códigos de la UE
        // AT BE BG CY CZ DK EE FI FR DE GR HR HU IE IT
        // LV LT LU MT NL PL PT RO SK SI ES SE GB
        listaUE.add("at");
        listaUE.add("be");
        listaUE.add("bg");
        listaUE.add("cy");
        listaUE.add("cz");
        listaUE.add("dk");
        listaUE.add("ee");
        listaUE.add("fi");
        listaUE.add("fr");
        listaUE.add("de");

        listaUE.add("gr");
        listaUE.add("hr");
        listaUE.add("hu");
        listaUE.add("ie");
        listaUE.add("it");
        listaUE.add("lv");
        listaUE.add("lt");
        listaUE.add("lu");
        listaUE.add("mt");
        listaUE.add("nl");
        listaUE.add("pl");

        listaUE.add("pt");
        listaUE.add("ro");
        listaUE.add("sk");
        listaUE.add("si");
        listaUE.add("es");
        listaUE.add("se");
        listaUE.add("en");

        pertenece = listaUE.contains(locale);


        final SharedPreferences settings =
                getSharedPreferences("localPreferences", MODE_PRIVATE);
        if ((settings.getBoolean("isFirstRun", true)) && pertenece)
        // Mostar el diálogo y cambiar isFirstRun
        {
            // Mostar el diálogo y cambiar isFirstRun luego

            AlertDialog alertDialog = new AlertDialog.Builder(VisitSloveniaHD.this).create();
            alertDialog.setTitle(getString(R.string.cookies));

            // Esto funcionaba bien
            alertDialog.setMessage(Html.fromHtml(getString(R.string.mensaje_cookies)));

            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            // Make the textview clickable. Must be called after show()
            ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

            // cambiar isFirstRun
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("isFirstRun", false);
            editor.apply();
        }

        //Fin de la mierda de Google que no compila


//Id de Flurry
        FlurryAgent.onStartSession(this, getString(R.string.flurry));




//Aqui se ponen m�s botones

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //Lo que hace el bot�n "Vocabulario"
    public void OnClickButtonListener() {
        Button buttonVocabulary_open = (Button) findViewById(R.id.buttonVocabulary);
        buttonVocabulary_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("com.visit.slovenia.hd.Vocabulario");
                        startActivity(intent);
                    }
                }
        );

    }


    //Lo que hace el bot�n "Mapa"
    public void OnClickButtonMapListener() {
        Button buttonMap_open = (Button) findViewById(R.id.buttonMap);
        buttonMap_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentMap = new Intent("com.visit.slovenia.hd.MapActivity");
                        startActivity(intentMap);
                    }
                }
        );

    }
/*
    //Lo que hace el bot�n "Mapa de Eslovenia"
    public void OnClickButtonMapSloveniaListener() {
        Button buttonMap_open_sl = (Button) findViewById(R.id.map);
        buttonMap_open_sl.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentMap = new Intent("com.visit.slovenia.hd.SloveniaMapsActivity");
                        startActivity(intentMap);
                    }
                }
        );

    }

    */

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "VisitSloveniaHD Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.visit.slovenia.hd/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "VisitSloveniaHD Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.visit.slovenia.hd/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    public static class AboutDialogBuilder {
        public static AlertDialog create(Context context) throws PackageManager.NameNotFoundException {
            // Try to load the a package matching the name of our own package
           // PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
           // String versionInfo = pInfo.versionName;

            String aboutTitle = MessageFormat.format("Visita la Slovenia HD", new Object [] {R.string.about});
            String versionString = MessageFormat.format(String.valueOf(R.string.version), new Object [] {R.string.version});

            //String aboutTitle = String.format(context.getString(R.string.about) + " %s", context.getString(R.string.app_name));
            //String versionString = String.format(context.getString(R.string.version) + " %s", versionInfo);
            String aboutText = context.getString(R.string.textoabout);

            // Set up the TextView
            final TextView message = new TextView(context);
            // We'll use a spannablestring to be able to make links clickable
            final SpannableString s = new SpannableString(aboutText);

            // Set some padding
            message.setPadding(5, 5, 5, 5);
            // Set up the final string
            message.setText(versionString + "\n\n" + s);
            // Now linkify the text
            Linkify.addLinks(message, Linkify.ALL);
            return new AlertDialog.Builder(context).setTitle(aboutTitle).setCancelable(true).setIcon(R.mipmap.ic_launcher).setPositiveButton(
                    context.getString(android.R.string.ok), null).setView(message).create();
        }
    }

    @Override
    protected void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        // Inicializar el TTS en Italiano por ser el mas parecido al esloveno
        if (status == TextToSpeech.SUCCESS) {
            mTts.setLanguage(Locale.ITALIAN);
            //Esta forma rara es para ponerlo en Portugues
            //mTts.setLanguage(new Locale("pt", "PRT"));
            String cadena1 = getString(R.string.toast_tts_inicializado);
            Toast.makeText(VisitSloveniaHD.this,
                    cadena1, Toast.LENGTH_LONG).show();
        } else if (status == TextToSpeech.ERROR) {
            String cadena1 = getString(R.string.toast_tts_error);
            Toast.makeText(VisitSloveniaHD.this,
                    cadena1, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(this, this);

                //mTts.setLanguage(Locale.FRANCE);
                String cadena1 = getString(R.string.toast_set_language);
                Toast.makeText(VisitSloveniaHD.this,
                        cadena1, Toast.LENGTH_LONG).show();

            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
                String cadena1 = getString(R.string.toast_redir);
                Toast.makeText(VisitSloveniaHD.this,
                        cadena1, Toast.LENGTH_LONG).show();
            }
        }
    }


}
