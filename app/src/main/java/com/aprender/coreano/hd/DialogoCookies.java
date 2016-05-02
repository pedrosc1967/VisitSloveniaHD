package com.aprender.coreano.hd;

/**
 * Created by pedrosantangelocalvo on 12/8/15.

public class DialogoCookies {

    public void mostrarDialogo(){


        AlertDialog alertDialog = new AlertDialog.Builder(AprenderCoreanoHD.this).create();
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

*/



