package com.furryfindernfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class NfcScanActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_scan);

        // Get the NFC adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            // NFC is not available on the device
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Check if NFC is enabled
        if (!nfcAdapter.isEnabled()) {
            // NFC is disabled, prompt the user to enable it
            Toast.makeText(this, "Please enable NFC", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
            finish();
            return;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null && rawMessages.length > 0) {
                NdefMessage message = (NdefMessage) rawMessages[0];
                NdefRecord record = message.getRecords()[0];
                if (record != null && record.getTnf() == NdefRecord.TNF_WELL_KNOWN
                        && Arrays.equals(record.getType(), NdefRecord.RTD_URI)) {
                    Uri uri = record.toUri();
                    if (uri != null) {
                        // Open the URL in the default phone browser
                        Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, uri);
                        openUrlIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(openUrlIntent);
                        finish(); // Optional: Close the current activity if needed
                    }
                }
            }
        }
    }

}
