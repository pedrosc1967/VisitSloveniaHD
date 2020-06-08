package com.aplanetbit.visit.slovenia.hd;

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
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.Intent.ACTION_VIEW;

public class VisitSloveniaHD extends Activity implements OnInitListener {


    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech mTts;
    private ShareActionProvider mShareActionProvider;

    private Context context;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


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
    }

//Definicion de la interfaz de usuario

    // AdMob
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Initalize AdMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //Request AdMob ad
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        OnClickButtonListener();
        OnClickButtonMapListener();
        OnClickBasicDataListener();
        OnClickComoLlegarListener();
        OnClickClimateListener();
        OnClickAtractionsListener();
        OnClickBledListener();
        OnClickPostoinaistener();
        OnClickTriglavistener();
        OnClickIsonzoistener();
        OnClickMotorrailListener();
        OnClickPiranListener();
        OnClickVelikaPlaninaListener();
        OnClickLogarListener();
        OnClickVideListener();
        OnClickRegionesListener();
        OnClickVacacionesListener();
        OnClickCiclismoListener();
        OnClickLagoBohinjListener();
        OnClickSpaListener();
     //   OnClickButtonMapSloveniaListener();


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
        listaUE.add("de_AT");
        listaUE.add("nl_BE");
        listaUE.add("fr_BE");
        listaUE.add("bg_BG");
        listaUE.add("el_CY");
        listaUE.add("tr_CY");
        listaUE.add("cs_CZ");
        listaUE.add("da_DK");
        listaUE.add("fi_FI");
        listaUE.add("fr_FR");
        listaUE.add("de_DE");
        listaUE.add("el_GR");
        listaUE.add("hr_HR");
        listaUE.add("hu_HU");
        listaUE.add("en_IE");
        listaUE.add("ga_IE");
        listaUE.add("it_IT");
        listaUE.add("lv_LV");
        listaUE.add("lt_LT");
        listaUE.add("de_LU");
        listaUE.add("fr_LU");
        listaUE.add("mt_MT");
        listaUE.add("en_MT");
        listaUE.add("nl_NL");
        listaUE.add("pl_PL");
        listaUE.add("pt_PT");
        listaUE.add("ro_RO");
        listaUE.add("sk_SK");
        listaUE.add("es_ES");
        listaUE.add("sv_SE");
        // listaUE.add("en_GB"); --> Brexit Mode
        listaUE.add("sl_SI");

        pertenece = listaUE.contains(locale);

        final SharedPreferences settings =
                getSharedPreferences("localPreferences", MODE_PRIVATE);
        if ((settings.getBoolean("isFirstRun", true)) && pertenece)
          //  if (pertenece)

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


//Aqui se ponen m�s botones

    }

    //Lo que hace el bot�n "Vocabulario"
    public void OnClickButtonListener() {
        Button buttonVocabulary_open = (Button) findViewById(R.id.buttonVocabulary);
        buttonVocabulary_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("com.aplanetbit.visit.slovenia.hd.Vocabulario");
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
                        Intent intentMap = new Intent("com.aplanetbit.visit.slovenia.hd.MapActivity");
                        startActivity(intentMap);
                    }
                }
        );

    }

    //Lo que hace el bot�n "Datos Básicos"
    public void OnClickBasicDataListener() {
        Button buttonBasicData_open = (Button) findViewById(R.id.basicData);
        buttonBasicData_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentBasicData = new Intent("com.aplanetbit.visit.slovenia.hd.BasicDataActivity");
                        startActivity(intentBasicData);
                    }
                }
        );

    }

    //Lo que hace el bot�n "Como Llegar"
    public void OnClickComoLlegarListener() {
        Button buttonBasicData_open = (Button) findViewById(R.id.comoLLegar);
        buttonBasicData_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentComoLlegar = new Intent("com.aplanetbit.visit.slovenia.hd.ComoLlegarActivity");
                        startActivity(intentComoLlegar);
                    }
                }
        );

    }

    //Lo que hace el bot�n "Clima"
    public void OnClickClimateListener() {
        Button buttonBasicData_open = (Button) findViewById(R.id.clima);
        buttonBasicData_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentClimate = new Intent("com.aplanetbit.visit.slovenia.hd.ClimateActivity");
                        startActivity(intentClimate);
                    }
                }
        );

    }

    //Lo que hace el bot�n "Atracciones Turísticas"
    public void OnClickAtractionsListener() {
        Button buttonAtractions_open = (Button) findViewById(R.id.atracciones);
        buttonAtractions_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentAtractions = new Intent("com.aplanetbit.visit.slovenia.hd.AtractionsActivity");
                        startActivity(intentAtractions);
                    }
                }
        );

    }

    //Lo que hace el bot�n "Lago Bled"
    public void OnClickBledListener() {
        Button buttonBled_open = (Button) findViewById(R.id.bled);
        buttonBled_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentBled = new Intent("com.aplanetbit.visit.slovenia.hd.BledActivity");
                        startActivity(intentBled);
                    }
                }
        );

    }

    //Lo que hace el bot�n "Postoina"
    public void OnClickPostoinaistener() {
        Button buttonPostoina_open = (Button) findViewById(R.id.postoina);
        buttonPostoina_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentPostoina = new Intent("com.aplanetbit.visit.slovenia.hd.PostoinaActivity");
                        startActivity(intentPostoina);
                    }
                }
        );
    }

    //Lo que hace el bot�n "Triglav"
    public void OnClickTriglavistener() {
        Button buttonTriglav_open = (Button) findViewById(R.id.triglav);
        buttonTriglav_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentTriglav = new Intent("com.aplanetbit.visit.slovenia.hd.TriglavActivity");
                        startActivity(intentTriglav);
                    }
                }
        );
    }

    //Lo que hace el bot�n "Isonzo"
    public void OnClickIsonzoistener() {
        Button buttonTriglav_open = (Button) findViewById(R.id.isonzo);
        buttonTriglav_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentIsonzo = new Intent("com.aplanetbit.visit.slovenia.hd.IsonzoActivity");
                        startActivity(intentIsonzo);
                    }
                }
        );
    }

    //Lo que hace el bot�n "Motorrail"
    public void OnClickMotorrailListener() {
        Button buttonMotorrail_open = (Button) findViewById(R.id.motorrail);
        buttonMotorrail_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentMotorrail = new Intent("com.aplanetbit.visit.slovenia.hd.MotorrailActivity");
                        startActivity(intentMotorrail);
                    }
                }
        );
    }

    //Lo que hace el bot�n "Piran"
    public void OnClickPiranListener() {
        Button buttonPiran_open = (Button) findViewById(R.id.piran);
        buttonPiran_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentPiran = new Intent("com.aplanetbit.visit.slovenia.hd.PiranActivity");
                        startActivity(intentPiran);
                    }
                }
        );
    }

    //Lo que hace el bot�n "Veika Planina"
    public void OnClickVelikaPlaninaListener() {
        Button buttonPiran_open = (Button) findViewById(R.id.velika);
        buttonPiran_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentPiran = new Intent("com.aplanetbit.visit.slovenia.hd.VelikaPlaninaActivity");
                        startActivity(intentPiran);
                    }
                }
        );
    }

    //Lo que hace el bot�n "Logar"
    public void OnClickLogarListener() {
        Button buttonLogar_open = (Button) findViewById(R.id.logar);
        buttonLogar_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentLogar = new Intent("com.aplanetbit.visit.slovenia.hd.LogarActivity");
                        startActivity(intentLogar);
                    }
                }
        );
    }

    //Lo que hace el bot�n "Vide"
    public void OnClickVideListener() {
        Button buttonVide_open = (Button) findViewById(R.id.vine);
        buttonVide_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentVide = new Intent("com.aplanetbit.visit.slovenia.hd.VideActivity");
                        startActivity(intentVide);
                    }
                }
        );
    }

    //Lo que hace el bot�n "Regiones"
    public void OnClickRegionesListener() {
        Button buttonRegiones_open = (Button) findViewById(R.id.regiones);
        buttonRegiones_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentRegiones = new Intent("com.aplanetbit.visit.slovenia.hd.RegionesActivity");
                        startActivity(intentRegiones);
                    }
                }
        );
    }

    //Lo que hace el bot�n "Vacaciones"
    public void OnClickVacacionesListener() {
        Button buttonVacaciones_open = (Button) findViewById(R.id.active);
        buttonVacaciones_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentVacaciones = new Intent("com.aplanetbit.visit.slovenia.hd.VacacionesActivity");
                        startActivity(intentVacaciones);
                    }
                }
        );
    }

    //Lo que hace el bot�n "Ciclismo"
    public void OnClickCiclismoListener() {
        Button buttonCiclismo_open = (Button) findViewById(R.id.biking);
        buttonCiclismo_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentCiclismo = new Intent("com.aplanetbit.visit.slovenia.hd.CiclismoActivity");
                        startActivity(intentCiclismo);
                    }
                }
        );
    }

    //Lo que hace el bot�n "Lago Bohinj"
    public void OnClickLagoBohinjListener() {
        Button buttonBohinj_open = (Button) findViewById(R.id.bohinj);
        buttonBohinj_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentBohinj = new Intent("com.aplanetbit.visit.slovenia.hd.LagoBohiniActivity");
                        startActivity(intentBohinj);
                    }
                }
        );
    }

    //Lo que hace el bot�n "SPA"
    public void OnClickSpaListener() {
        Button buttonSpa_open = (Button) findViewById(R.id.spa);
        buttonSpa_open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentSpa = new Intent("com.aplanetbit.visit.slovenia.hd.SpaActivity");
                        startActivity(intentSpa);
                    }
                }
        );
    }

    // Encima de aquí se meten nuevos Listener de nuevos botones

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

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
