package com.pag.socialz;

import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class Services extends AsyncTask<Object, Integer, SoapPrimitive> {

    private final String NAMESPACE = "http://WebServices.pkg/";
    private final String URL = "http://192.168.1.254:8080/SocialZ/AndroidWS?WSDL";

    @Override
    protected SoapPrimitive doInBackground(Object... params) {
        String METHOD_NAME = (String) params[0];
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        for (int i = 1; i < params.length - 1; i += 2) {
            String paramName = (String) params[i];
            Object paramValue = params[i+1];
            PropertyInfo propInfo = new PropertyInfo();
            propInfo.setName(paramName);
            propInfo.setValue(paramValue);
            request.addProperty(propInfo);
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE ht = new HttpTransportSE(URL);
        try {
            ht.call(NAMESPACE + METHOD_NAME, envelope);
            return (SoapPrimitive) envelope.getResponse();
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
            return new SoapPrimitive("Cacca", "molla", "ops");
        }
    }
}
