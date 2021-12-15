package transfer.money.com.xpresssewa.util;


import android.content.DialogInterface;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AlertDialogs {
    public void alertDialog(AppCompatActivity appCompatActivity, String title, String message, final String positiveButton, final String negativeButton, final DialogCallBack callBacks) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(appCompatActivity);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                positiveButton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        callBacks.getDialogEvent(positiveButton);

                    }
                });

        builder1.setNegativeButton(
                negativeButton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        callBacks.getDialogEvent(negativeButton);

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
