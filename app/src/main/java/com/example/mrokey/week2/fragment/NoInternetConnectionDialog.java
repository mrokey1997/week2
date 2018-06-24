package com.example.mrokey.week2.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.mrokey.week2.R;
import com.example.mrokey.week2.article.repository.DataListener;

public class NoInternetConnectionDialog {
    public static void show(Context context, final DataListener dataListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.connection_failed));
        builder.setMessage(context.getString(R.string.no_internet));
        builder.setPositiveButton(context.getString(R.string.try_again), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataListener.onTryAngain();
            }
        });
        builder.create().show();
    }
}
