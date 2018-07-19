package br.org.sidia.mymovies.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class TooltipUtils {

    public static void exibeMensagem(final Context contexto,
                                     final String mensagem) {
        final AlertDialog.Builder editalert = new AlertDialog.Builder(contexto);
        editalert.setMessage(mensagem);

        editalert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                                        final int whichButton) {

                    }
                });

        editalert.show();
    }
}
