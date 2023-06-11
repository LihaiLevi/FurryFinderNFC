package com.furryfindernfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class IDInputActivity extends AppCompatActivity {

    private EditText idEditText;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_input);
        idEditText = findViewById(R.id.idEditText);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not supported on this device.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Intent nfcIntent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableNfcForegroundDispatch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableNfcForegroundDispatch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String animalId = idEditText.getText().toString();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()))
        {
            if(!animalId.isEmpty()) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                writeNfcTag(tag, animalId);
            }
            else
            {
                Toast.makeText(IDInputActivity.this, "Please enter an Animal ID", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void enableNfcForegroundDispatch() {
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    private void disableNfcForegroundDispatch() {
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    private void writeNfcTag(String url) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())) {
            Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
            writeNfcTag(tag, url);
        } else {
            Toast.makeText(this, "Please bring an NFC tag close to the device.", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeNfcTag(Tag tag, String url) {
        NdefMessage ndefMessage = createNdefMessage(url);
        if (writeTag(tag, ndefMessage)) {
            Toast.makeText(this, "NFC tag written successfully!", Toast.LENGTH_SHORT).show();
            idEditText.setText("");
        } else {
            Toast.makeText(this, "Failed to write NFC tag.", Toast.LENGTH_SHORT).show();
        }
    }

    private NdefMessage createNdefMessage(String url) {
        // Create a custom NFC URI record with your app-specific scheme
        NdefRecord uriRecord = NdefRecord.createUri(Uri.parse(url));
        NdefMessage ndefMessage = new NdefMessage(uriRecord);
        return ndefMessage;
    }

    private boolean writeTag(Tag tag, NdefMessage ndefMessage) {
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (ndef.isWritable()) {
                    ndef.writeNdefMessage(ndefMessage);
                    return true;
                } else {
                    Toast.makeText(this, "NFC tag is read-only.", Toast.LENGTH_SHORT).show();
                }
                ndef.close();
            } else {
                Toast.makeText(this, "NFC tag is not supported.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Failed to write NFC tag.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
