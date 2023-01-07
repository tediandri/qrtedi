package www.golng.qrtedii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //view object
    private Button buttonScanning;
    private TextView textViewName,textViewClass,textViewId;
    //qr scanning object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //view object
        buttonScanning = (Button) findViewById(R.id.buttonscan);
        textViewName = (TextView) findViewById(R.id.textViewNama);
        textViewId = (TextView) findViewById(R.id.textViewNim);
        textViewClass = (TextView) findViewById(R.id.textViewKelas);
        //inisialisasi scan object
        qrScan = new IntentIntegrator(this);

        //implementasi onclick listener
        buttonScanning.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,
                resultCode,data);
        if (result != null){
            //jika qr code tidak ada sama sekali
            if (result.getContents() == null){
                Toast.makeText(this,"hasil scanning tidak ada",Toast.LENGTH_LONG);
            }else if(Patterns.WEB_URL.matcher(result.getContents()).matches()){
                Intent visitUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(visitUrl);
            }else if(Patterns.PHONE.matcher(result.getContents()).matches()){
                String telp = String.valueOf(result.getContents());
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telp));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                try{
                    startActivity(Intent.createChooser(intent, "waiting"));
                }catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(MainActivity.this, "no phone apk installed", Toast.LENGTH_SHORT).show();
                }
            }

            else {
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    //diset
                    textViewName.setText(obj.getString("nama"));
                    textViewClass.setText(obj.getString("kelas"));
                    textViewId.setText(obj.getString("nim"));

                }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this,result.getContents(),
                            Toast.LENGTH_LONG).show();
                }
            } }{
            try {
                String geoUri=result.getContents();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                //Set Package
                intent.setPackage("com.google.android.apps.maps");

                //Set Flag
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }finally {

            }

        } {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        //inisialisasi qrcode view
        qrScan.initiateScan();
    }
}