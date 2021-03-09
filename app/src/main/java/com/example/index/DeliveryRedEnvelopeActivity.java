package com.example.index;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.index.ReadQRCode.QRGeo;
import com.example.index.ReadQRCode.QRURL;
import com.example.index.ReadQRCode.QRVCard;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class DeliveryRedEnvelopeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_red_envelope);
        //Init
        scannerView = (ZXingScannerView) findViewById(R.id.zxscan);
        //Request permission
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(DeliveryRedEnvelopeActivity.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(DeliveryRedEnvelopeActivity.this, "請允許此權限", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }


    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    public void handleResult(Result rawResult) {
        //Here we can receive rawRusult
       // processRawResult(rawResult.getText());
        Intent intent = new Intent(DeliveryRedEnvelopeActivity.this, FinalRedEnvelopeActivity.class);
        intent.putExtra("uid", rawResult.getText());
        startActivity(intent);
    }

   /* private void processRawResult(String text) {
        if (text.startsWith("BEGIN:")) {
            String[] tokens = text.split("\n");
            QRVCard qrvCard = new QRVCard();
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].startsWith("BEGIN:")) {
                    qrvCard.setType(tokens[i].substring("BEGIN:".length())); //Remove  BEGIN: to  get Type
                } else if (tokens[i].startsWith("N:")) {
                    qrvCard.setName(tokens[i].substring("N:".length()));
                } else if (tokens[i].startsWith("ORG:")) {
                    qrvCard.setOrg(tokens[i].substring("ORG:".length()));
                } else if (tokens[i].startsWith("TEL:")) {
                    qrvCard.setTel(tokens[i].substring("TEL:".length()));
                } else if (tokens[i].startsWith("URL:")) {
                    qrvCard.setUrl(tokens[i].substring("URL:".length()));
                } else if (tokens[i].startsWith("EMAIL:")) {
                    qrvCard.setEmail(tokens[i].substring("EMAIL:".length()));
                } else if (tokens[i].startsWith("ADR:")) {
                    qrvCard.setAddress(tokens[i].substring("ADR:".length()));
                } else if (tokens[i].startsWith("NOTE:")) {
                    qrvCard.setNote(tokens[i].substring("NOTE:".length()));
                } else if (tokens[i].startsWith("SUMMARY:")) {
                    qrvCard.setSummary(tokens[i].substring("SUMMARY:".length()));
                } else if (tokens[i].startsWith("DTSTART:")) {
                    qrvCard.setDtstart(tokens[i].substring("DTSTART:".length()));
                } else if (tokens[i].startsWith("DREND:")) {
                    qrvCard.setDtend(tokens[i].substring("DTEND".length()));
                }

                //Try to show
            }
        } else if (text.startsWith("http://") ||
                text.startsWith("http://") ||
                text.startsWith("www.")) {
            QRURL qrurl = new QRURL(text);
        } else if (text.startsWith("geo:")) {
            QRGeo qrGeo = new QRGeo();
            String delims = "[  ,  ?q=  ]+";
            String tokens[] = text.split(delims);

            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].startsWith("  geo:")) {
                    qrGeo.setLat(tokens[i].substring("geo:".length()));
                }
            }
            qrGeo.setLat(tokens[0].substring("geo:".length()));
            qrGeo.setLng(tokens[1]);
            qrGeo.setGeo_place(tokens[2]);
        } else {
        }
        scannerView.resumeCameraPreview(DeliveryRedEnvelopeActivity.this);
    }*/
}
