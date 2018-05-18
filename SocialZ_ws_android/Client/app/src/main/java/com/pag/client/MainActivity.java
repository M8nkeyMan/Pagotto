package com.pag.client;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pag.client.entities.Result;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String IP;
    private String PORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            IP = (bundle.getString("address")!= null)?bundle.getString("address"):"192.168.1.254";
            PORT = (bundle.getString("port") != null)?bundle.getString("port"):"8080";
        }
        new Service().execute(R.id.hobbies, "getHobbies");
        ((Spinner) findViewById(R.id.hobbies)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                new Service().execute(R.id.users, "getMailingList", "idHobby", position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        ((ListView) findViewById(R.id.users)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent share = new Intent(Intent.ACTION_SENDTO);
                TextView userView = view.findViewById(R.id.text_secondary);
                Result hobby = ((Result)((Spinner) findViewById(R.id.hobbies)).getSelectedItem());
                String mailto ="mailto:"+Uri.encode(userView.getText().toString())+"?&subject="+Uri.encode(hobby.getText_main())+"&body="+Uri.encode("Troviamoci a parlare di "+(hobby.getText_main().toLowerCase()));
                share.setData(Uri.parse(mailto));
                try{
                    startActivity(share);
                }catch(ActivityNotFoundException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_toolbar_layout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("address",IP);
                extras.putString("port",PORT);
                i.putExtras(extras);
                startActivity(i);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class Service extends AsyncTask<Object, Integer, Vector<SoapObject>> {

        private ProgressBar pb;

        private int CONTAINER_ID;

        private final String NAMESPACE = "http://services/";
        private final String URL = "/Server/SOAP_Service?WSDL";
        private String METHOD_NAME = "";
        private String SOAP_ACTION = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb = findViewById(R.id.progressBar);
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Vector<SoapObject> doInBackground(Object... args) {
            CONTAINER_ID = (Integer) args[0];
            METHOD_NAME = args[1].toString();
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            for (int i = 2; i < args.length - 1; i += 2) {
                request.addProperty(args[i].toString(), args[i + 1]);
            }
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            String cos = "http://"+IP+":"+PORT+URL;
            HttpTransportSE ht = new HttpTransportSE(cos);
            try {
                SOAP_ACTION = NAMESPACE + METHOD_NAME;
                ht.call(SOAP_ACTION, envelope);
                Vector<SoapObject> response = (Vector<SoapObject>) envelope.getResponse();
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return new Vector<>();
            }
        }

        @Override
        protected void onPostExecute(Vector<SoapObject> soapObjects) {
            pb.setVisibility(View.GONE);
            if (soapObjects.size() > 0) {
                final ArrayList<Result> items = new ArrayList<>();
                try {
                    SoapObjectParser.parseList(soapObjects, items);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Result.Adapter adapter = new Result.Adapter(MainActivity.this, R.layout.listitem_layout, items);
                            switch (CONTAINER_ID) {
                                case R.id.hobbies:
                                    ((Spinner) findViewById(CONTAINER_ID)).setAdapter(adapter);
                                    break;
                                case R.id.users:
                                    ((ListView) findViewById(CONTAINER_ID)).setAdapter(adapter);
                                    break;
                            }
                        }
                    });
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MainActivity.this,R.string.error,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
