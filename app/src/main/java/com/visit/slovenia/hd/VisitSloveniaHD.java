package com.visit.slovenia.hd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.apptracker.android.track.AppTracker;
import com.facebook.FacebookSdk;
import com.facebook.ads.AdSize;
import com.facebook.appevents.AppEventsLogger;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.ads.fullscreen.RevMobFullscreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import io.presage.Presage;
import io.presage.utils.IADHandler;

import com.facebook.FacebookActivity;
import static android.content.Intent.ACTION_VIEW;

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
    private android.content.Context context;

    public void loadFullscreen() {
        fullscreen = revmob.createFullscreen(this, revmobListener);
    }

    public void showRevmobAd() {
        if(adIsLoaded) fullscreen.show(); // call it wherever you want to show the fullscreen ad
    }




    //Definici�n del men� en menu.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        /** Inflating the current activity's menu with res/menu/items.xml */
        getMenuInflater().inflate(R.menu.menu, menu);

        /** Getting the actionprovider associated with the menu item whose id is share */
        mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.menu_share).getActionProvider();

        /** Setting a share intent */
        mShareActionProvider.setShareIntent(getDefaultShareIntent());

        return super.onCreateOptionsMenu(menu);
    }

    /** Returns a share intent */
    private Intent getDefaultShareIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject1));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.extra_text1)+getString(R.string.paquete));
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Otras_apps:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:\"Aabcdata\"") ) );
                return true;
            case R.id.Rate:
                startActivity(new Intent(ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getString(R.string.paquete)) ) );
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

        // Para revmob
        revmob = RevMob.startWithListener(this, revmobListener);

        Presage.getInstance().adToServe("interstitial", new IADHandler() {

            @Override
            public void onAdNotFound() {
                // Para LeadBolt
                if((getString(R.string.LeadBoltCTRL).equalsIgnoreCase("true"))) {
                    //Toast.makeText(VisitSloveniaHD.this,
                    //        "Activado Leadbolt", Toast.LENGTH_LONG).show();
                    AppTracker.loadModule(getApplicationContext(), "inapp");
                }

                if (getString(R.string.revmobCTRL).equalsIgnoreCase("true")){
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
    }
//Definicion de la interfaz de usuario

    //private AdView mAdView; // AdMob

    private AdView adView;  // Facebook ad

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);



        RelativeLayout adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);

        adView = new AdView(this, getString(R.string.FacebookAudienceID), AdSize.BANNER_320_50);
        adViewContainer.addView(adView);
        adView.loadAd();

       // setContentView(R.layout.activity_ad_sample);

        // Instantiate an AdView view
        //adView = new AdView(this, "816639391801383_816641235134532", AdSize.BANNER_HEIGHT_50);

        // Find the main layout of your activity
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

        Presage.getInstance().setContext(this.getBaseContext());
        Presage.getInstance().start();

        revmob = RevMob.startWithListener(this, revmobListener);

        if(savedInstanceState == null) {
            // Initialize Leadbolt SDK with your api key
            AppTracker.startSession(getApplicationContext(), getString(R.string.LeadboltStr));
        }
        // cache Leadbolt Ad without showing it
        AppTracker.loadModuleToCache(getApplicationContext(), "inapp");

        // call this when you want to display the Leadbolt Interstitial
        //AppTracker.loadModule(getApplicationContext(), "inapp");

        if (getString(R.string.revmobCTRL).equalsIgnoreCase("true")){
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


// All subsequent hits will be send with screen name = "main screen"
        tracker.setScreenName("main screen");

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
            ((TextView)alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

            // cambiar isFirstRun
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        }

        //Fin de la mierda de Google que no compila


        Button holaPlayerBtn = (Button)findViewById(R.id.holaPlayerBtn);
        Button buenosdiasPlayerBtn = (Button)findViewById(R.id.buenosdiasPlayerBtn);
        Button adiosPlayerBtn = (Button)findViewById(R.id.adiosPlayerBtn);
        Button buenastardesPlayerBtn = (Button)findViewById(R.id.buenastardesPlayerBtn);
        Button buenasnochePlayerBtn = (Button)findViewById(R.id.buenasnochesPlayerBtn);
        Button hastaluegoPlayerBtn = (Button)findViewById(R.id.hastaluegoPlayerBtn);
        Button hastamananaPlayerBtn = (Button)findViewById(R.id.hastamananaPlayerBtn);
        Button quetalPlayerBtn = (Button)findViewById(R.id.quetalPlayerBtn);
        Button biengraciasPlayerBtn = (Button)findViewById(R.id.biengraciasPlayerBtn);
        Button buenprovechoPlayerBtn = (Button)findViewById(R.id.buenprovechoPlayerBtn);
        Button mepuedeayudarPlayerBtn = (Button)findViewById(R.id.mepuedeayudarPlayerBtn);
        Button cuantocuestaPlayerBtn = (Button)findViewById(R.id.cuantocuestaPlayerBtn);
        Button nohabloespanolPlayerBtn = (Button)findViewById(R.id.nohabloespanolPlayerBtn);
        Button necesitounmedicoPlayerBtn = (Button)findViewById(R.id.necesitounmedicoPlayerBtn);
        Button soydeeeuuPlayerBtn = (Button)findViewById(R.id.soydeeeuuPlayerBtn);
        Button dedondeeresPlayerBtn = (Button)findViewById(R.id.dedondeeresPlayerBtn);
        Button mellamopaulPlayerBtn = (Button)findViewById(R.id.mellamopaulPlayerBtn);
        Button comosellamaPlayerBtn = (Button)findViewById(R.id.comosellamaPlayerBtn);
        Button arribaPlayerBtn = (Button)findViewById(R.id.arribaPlayerBtn);
        Button abajoPlayerBtn = (Button)findViewById(R.id.abajoPlayerBtn);
        Button izquierdaPlayerBtn = (Button)findViewById(R.id.izquierdaPlayerBtn);
        Button derechaPlayerBtn = (Button)findViewById(R.id.derechaPlayerBtn);
        Button centroPlayerBtn = (Button)findViewById(R.id.centroPlayerBtn);
        Button delantePlayerBtn = (Button)findViewById(R.id.delantePlayerBtn);
        Button detrasPlayerBtn = (Button)findViewById(R.id.detrasPlayerBtn);
        Button encimaPlayerBtn = (Button)findViewById(R.id.encimaPlayerBtn);
        Button debajoPlayerBtn = (Button)findViewById(R.id.debajoPlayerBtn);
        Button losientoPlayerBtn = (Button)findViewById(R.id.losientoPlayerBtn);
        Button porfavorPlayerBtn = (Button)findViewById(R.id.porfavorPlayerBtn);
        Button graciasPlayerBtn = (Button)findViewById(R.id.graciasPlayerBtn);
        Button denadaPlayerBtn = (Button)findViewById(R.id.denadaPlayerBtn);
        Button muchasgraciasPlayerBtn = (Button)findViewById(R.id.muchasgraciasPlayerBtn);
        Button nograciasPlayerBtn = (Button)findViewById(R.id.nograciasPlayerBtn);
        Button encantadoPlayerBtn = (Button)findViewById(R.id.encantadoPlayerBtn);
        Button estabienPlayerBtn = (Button)findViewById(R.id.estabienPlayerBtn);
        Button uncafePlayerBtn = (Button)findViewById(R.id.uncafePlayerBtn);
        Button unacervezaPlayerBtn = (Button)findViewById(R.id.unacervezaPlayerBtn);
        Button elbanoPlayerBtn = (Button)findViewById(R.id.elbanoPlayerBtn);
        Button lacartaPlayerBtn = (Button)findViewById(R.id.lacartaPlayerBtn);
        Button unatortillaPlayerBtn = (Button)findViewById(R.id.unatortillaPlayerBtn);
        Button unapaellaPlayerBtn = (Button)findViewById(R.id.unapaellaPlayerBtn);
        Button lacuentaPlayerBtn = (Button)findViewById(R.id.lacuentaPlayerBtn);
        Button desayunoPlayerBtn = (Button)findViewById(R.id.desayunoPlayerBtn);
        Button comidaPlayerBtn = (Button)findViewById(R.id.comidaPlayerBtn);
        Button cenaPlayerBtn = (Button)findViewById(R.id.cenaPlayerBtn);
        Button cucharaPlayerBtn = (Button)findViewById(R.id.cucharaPlayerBtn);
        Button tenedorPlayerBtn = (Button)findViewById(R.id.tenedorPlayerBtn);
        Button cuchilloPlayerBtn = (Button)findViewById(R.id.cuchilloPlayerBtn);
        Button panPlayerBtn = (Button)findViewById(R.id.panPlayerBtn);
        Button vasoPlayerBtn = (Button)findViewById(R.id.vasoPlayerBtn);
        Button aguaPlayerBtn = (Button)findViewById(R.id.aguaPlayerBtn);
        Button servilletaPlayerBtn = (Button)findViewById(R.id.servilletaPlayerBtn);
        Button camareroPlayerBtn = (Button)findViewById(R.id.camareroPlayerBtn);
        Button megustaPlayerBtn = (Button)findViewById(R.id.megustaPlayerBtn);
        Button siPlayerBtn = (Button)findViewById(R.id.siPlayerBtn);
        Button noPlayerBtn = (Button)findViewById(R.id.noPlayerBtn);
        Button esposiblePlayerBtn = (Button)findViewById(R.id.esposiblePlayerBtn);
        Button nuncaPlayerBtn = (Button)findViewById(R.id.nuncaPlayerBtn);
        Button siemprePlayerBtn = (Button)findViewById(R.id.siemprePlayerBtn);
        Button avecesPlayerBtn = (Button)findViewById(R.id.avecesPlayerBtn);
        Button hoyPlayerBtn = (Button)findViewById(R.id.hoyPlayerBtn);
        Button ayerPlayerBtn = (Button)findViewById(R.id.ayerPlayerBtn);
        Button mananaPlayerBtn = (Button)findViewById(R.id.mananaPlayerBtn);
        Button cochePlayerBtn = (Button)findViewById(R.id.cochePlayerBtn);
        Button avionPlayerBtn = (Button)findViewById(R.id.avionPlayerBtn);
        Button trenPlayerBtn = (Button)findViewById(R.id.trenPlayerBtn);
        Button autobusPlayerBtn = (Button)findViewById(R.id.autobusPlayerBtn);
        Button bicicletaPlayerBtn = (Button)findViewById(R.id.bicicletaPlayerBtn);
        Button hotelPlayerBtn = (Button)findViewById(R.id.hotelPlayerBtn);
        Button habitacionPlayerBtn = (Button)findViewById(R.id.habitacionPlayerBtn);
        Button habitacionreservadaPlayerBtn = (Button)findViewById(R.id.habitacionreservadaPlayerBtn);
        Button hastaprontoPlayerBtn = (Button)findViewById(R.id.hastaprontoPlayerBtn);

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

//Id de Flurry	
        FlurryAgent.onStartSession(this, getString(R.string.flurry));


//Lo que hace el bot�n "Hola"
        holaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {

                String cadena = getString(R.string.di_hola);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
//Est funciona bien	playLocalAudio(R.raw.hola);
// playLocalAudio();
// playLocalAudio_UsingDescriptor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Buenos D�as"
        buenosdiasPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {


                String cadena = getString(R.string.di_buenosdias);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                    //playLocalAudio(R.raw.buenosdias);
                    // playLocalAudio();
                    // playLocalAudio_UsingDescriptor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Adios"
        adiosPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_adios);

                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                    //playLocalAudio(R.raw.adios);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Buenas Tardes"
        buenastardesPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_buenas_tardes);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                    //playLocalAudio(R.raw.buenosdias);
                    // playLocalAudio();
                    // playLocalAudio_UsingDescriptor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Buenas Noches"
        buenasnochePlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_buenas_noches);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                    //playLocalAudio(R.raw.buenosdias);
                    // playLocalAudio();
                    // playLocalAudio_UsingDescriptor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Hasta Luego"
        hastaluegoPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_hasta_luego);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                    //playLocalAudio(R.raw.buenosdias);
                    // playLocalAudio();
                    // playLocalAudio_UsingDescriptor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Hasta Ma�ana"
        hastamananaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_hasta_manana);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Qu� tal"
        quetalPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_quetal);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Bien, gracias"
        biengraciasPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_bien_gracias);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Buen provecho"
        buenprovechoPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_buen_provecho);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "�Me puede ayudar?"
        mepuedeayudarPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_me_puede_ayudar);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "�Cuanto cuesta?"
        cuantocuestaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_cuanto_cuesta);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "No hablo espa�ol"
        nohabloespanolPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_no_hablo_espanol);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Necesito un m�dico"
        necesitounmedicoPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_necesito_un_medico);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Soy de EEUU"
        soydeeeuuPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_soy_de_eeuu);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "�De donde eres?"
        dedondeeresPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_de_donde_eres);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Me llamo Paul"
        mellamopaulPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_me_llamo_paul);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "�Como se llama?"
        comosellamaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_como_se_llama);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Arriba"
        arribaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_arriba);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Abajo"
        abajoPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_abajo);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Izquierda"
        izquierdaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_izquierda);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Derecha"
        derechaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_derecha);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});


//Lo que hace el bot�n "Centro"
        centroPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_centro);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Delante"
        delantePlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_delante);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Detr�s"
        detrasPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_detras);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Encima"
        encimaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_encima);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Debajo"
        debajoPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_debajo);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Lo siento"
        losientoPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_lo_siento);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Por favor"
        porfavorPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_por_favor);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Gracias"
        graciasPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_gracias);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "De nada"
        denadaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_de_nada);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Muchas gracias"
        muchasgraciasPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_muchas_gracias);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "No gracias"
        nograciasPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_no_gracias);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Encantado"
        encantadoPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_encantado);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Est� bien"
        estabienPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_esta_bien);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Un cafe"
        uncafePlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_un_cafe);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Una cerveza"
        unacervezaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_una_cerveza);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "El ba�o"
        elbanoPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_el_bano);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "La carta"
        lacartaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_la_carta);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Una tortilla"
        unatortillaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_una_tortilla);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Una paella"
        unapaellaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_una_paella);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "La cuenta"
        lacuentaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_la_cuenta);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Desayuno"
        desayunoPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_desayuno);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Comida"
        comidaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_comida);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Cena"
        cenaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_cena);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Cuchara"
        cucharaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_cuchara);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Tenedor"
        tenedorPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_tenedor);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Cuchillo"
        cuchilloPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_cuchillo);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Pan"
        panPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_pan);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Vaso"
        vasoPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_vaso);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Agua"
        aguaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_agua);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Servilleta"
        servilletaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_servilleta);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Camarero"
        camareroPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_camarero);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Me gusta"
        megustaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_me_gusta);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "S�"
        siPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_si);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "No"
        noPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_no);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Es posible"
        esposiblePlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_es_posible);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Nunca"
        nuncaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_nunca);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Siempre"
        siemprePlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_siempre);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "A veces"
        avecesPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_a_veces);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Hoy"
        hoyPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_hoy);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Ayer"
        ayerPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_ayer);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Ma�ana"
        mananaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_manana);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Coche"
        cochePlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_coche);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Avi�n"
        avionPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_avion);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Tren"
        trenPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_tren);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Autobus"
        autobusPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_autobus);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Bicicleta"
        bicicletaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_bicicleta);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Hotel"
        hotelPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_hotel);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Habitaci�n"
        habitacionPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_habitacion);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Habitaci�n reservada"
        habitacionreservadaPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_habitacion_reservada);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Lo que hace el bot�n "Hasta pronto"
        hastaprontoPlayerBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String cadena = getString(R.string.di_hasta_pronto);
                try {
                    mTts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

//Aqui se ponen m�s botones

    }


    public static class AboutDialogBuilder {
        public static AlertDialog create(Context context) throws PackageManager.NameNotFoundException {
            // Try to load the a package matching the name of our own package
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String versionInfo = pInfo.versionName;

            String aboutTitle = String.format(context.getString(R.string.about) + " %s", context.getString(R.string.app_name));
            String versionString = String.format(context.getString(R.string.version) + " %s", versionInfo);
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

            //mTts.setLanguage(new Locale("sl","SLO"));
            //Esta forma rara es para ponerlo en Portugues
            //mTts.setLanguage(new Locale("pt", "PRT"));
            String cadena1 = getString(R.string.toast_tts_inicializado);
            Toast.makeText(VisitSloveniaHD.this,
                    cadena1, Toast.LENGTH_LONG).show();
        }
        else if (status == TextToSpeech.ERROR) {
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

            }
            else {
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
