package com.furryfindernfc;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button scanNfc;
    private Button snycNfc;
    private ImageButton infoButton;
    private ImageButton homeButton;

    private static final String HOME_URL = "https://furryfinder2.azurewebsites.net/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanNfc = findViewById(R.id.scanNfcButton);
        snycNfc = findViewById(R.id.writeUrlButton);
        infoButton = findViewById(R.id.infoButton);
        homeButton = findViewById(R.id.homeButton);

        scanNfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NfcScanActivity.class);
                startActivity(intent);
            }
        });

        snycNfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the IDInputActivity
                Intent intent = new Intent(MainActivity.this, IDInputActivity.class);
                startActivity(intent);
            }
        });
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog();
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(HOME_URL);

            }
        });
    }
    private void showInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("App Info")
                .setMessage("Furry-Finder provides a service for locating lost animals. Used by pet owners to locate their animal by scanning an NFC tag, that will be found on the animal's collar. This tag will contain the animal's and owner's details. That way Furry-Finder is a bridge between a person who found a lost animal and the pet owners. In addition, Furry-Finder will allows to users to find a list of nearby veterinarians and an option to contact them.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
