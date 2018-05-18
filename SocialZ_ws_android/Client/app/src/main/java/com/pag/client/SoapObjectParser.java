package com.pag.client;

import com.pag.client.entities.Result;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Vector;

public class SoapObjectParser {

    public static void parseObject(SoapObject input, Result output) throws IllegalAccessException {
        String soapObject = input.toString();
        Field[] fields = Result.class.getDeclaredFields();
        for (Field field : fields) {
            Type type = field.getType();
            field.setAccessible(true);

            if (field.getType().equals(String.class)) {
                String tag = "s_" + field.getName() + "=";
                if(soapObject.contains(tag)){
                    String strValue = soapObject.substring(soapObject.indexOf(tag)+tag.length(), soapObject.indexOf(";", soapObject.indexOf(tag)));
                    if(strValue.length()!=0){
                        field.set(output, strValue);
                    }
                }
            }

            if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
                String tag = "i_" + field.getName() + "=";
                if(soapObject.contains(tag)){
                    String strValue = soapObject.substring(soapObject.indexOf(tag)+tag.length(), soapObject.indexOf(";", soapObject.indexOf(tag)));
                    if(strValue.length()!=0){
                        field.set(output, Integer.parseInt(strValue));
                    }
                }
            }
        }
    }

    public static void parseList(Vector<SoapObject> input, ArrayList<Result> output) throws IllegalAccessException {
        for (int i = 0; i < input.size(); i++) {
            Result ris = new Result();
            parseObject(input.get(i), ris);
            output.add(ris);
        }
    }
}
