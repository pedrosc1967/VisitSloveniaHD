
package com.visit.slovenia.hd;
/*
 * Created by pedrosantangelocalvo on 12/8/15.
*** /

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class DialogoCookies {

    public void mostrarDialogo(){


        AlertDialog alertDialog = new AlertDialog.Builder(VisitSloveniaHD.this).create();
        alertDialog.setTitle(toString(R.string.cookies));

        // Esto funcionaba bien
        alertDialog.setMessage(Html.fromHtml(toString(R.string.mensaje_cookies)));
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