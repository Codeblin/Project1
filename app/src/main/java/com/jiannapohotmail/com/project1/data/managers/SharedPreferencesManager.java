package com.jiannapohotmail.com.project1.data.managers;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiannapohotmail.com.project1.R;
import com.jiannapohotmail.com.project1.data.models.PointModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferencesManager {

    public static final String KEY_JSON_STRING = "jsonString";
    public static final String SHARED_PREF_DATA = "local_storage";
    public  static final String KEY_DESTINATION_INDEX = "destinationIndex";
    private Context context;

    public SharedPreferencesManager(Context context){
        this.context = context;
    }

    public void InitialJsonMaping(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_JSON_STRING, ReadJsonFromRaw());
        editor.commit();

        addResourcesId();
    }

    public ArrayList<PointModel> GetListFromSharedPreferences(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_DATA, Context.MODE_PRIVATE);
        Type listType = new TypeToken<ArrayList<PointModel>>(){}.getType();
        String jsonString = sharedPreferences.getString(KEY_JSON_STRING, "empty");
        return new Gson().fromJson(jsonString, listType);
    }

    public void SaveDataToSharedPreferences(ArrayList<PointModel> pointModels, int destinationIndex){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_JSON_STRING, new Gson().toJson(pointModels));
        editor.putInt(KEY_DESTINATION_INDEX, destinationIndex);
        editor.commit();
    }

    private String ReadJsonFromRaw() {
        InputStream is = context.getResources().openRawResource(R.raw.pointdata);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            is.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return writer.toString();
    }

    private void addResourcesId(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_DATA, Context.MODE_PRIVATE);
        Type listType = new TypeToken<ArrayList<PointModel>>(){}.getType();
        String jsonString = sharedPreferences.getString(KEY_JSON_STRING, "empty");
        ArrayList<PointModel> pointModels = new Gson().fromJson(jsonString, listType);

        for (PointModel point : pointModels) {
            point.setPointPic(findRightImage(point.getPointTitle(), pointModels));
            point.setPointVideo(findRightVideo(point.getPointTitle(), pointModels));
        }

        Log.e("sd", "Dsa");
    }

    private int findRightVideo(String title, ArrayList<PointModel> pointModels){
        if (title.equals(pointModels.get(0).getPointTitle()))
            return R.raw.diplomatiki_afetiria_teliko_compressed;
        else if (title.equals(pointModels.get(1).getPointTitle()))
            return R.raw.diplomatiki_stasi_2_espo_compressed;
        else if (title.equals(pointModels.get(2).getPointTitle()))
            return R.raw.eam_theatrou_2_compressed;
        else if (title.equals(pointModels.get(3).getPointTitle()))
            return R.raw.epon_spoudazousa_compressed;
        else if (title.equals(pointModels.get(4).getPointTitle()))
            return R.raw.v5korai;
        else if (title.equals(pointModels.get(5).getPointTitle()))
            return R.raw.v6omirou;
        else if (title.equals(pointModels.get(6).getPointTitle()))
            return R.raw.v7merlin;
        else if (title.equals(pointModels.get(7).getPointTitle()))
            return R.raw.v8syntagma;
        else
            return  0;
    }

    private int findRightImage(String title, ArrayList<PointModel> pointModels){
        if (title.equals(pointModels.get(0).getPointTitle()))
            return R.drawable.i01;
        else if (title.equals(pointModels.get(1).getPointTitle()))
            return R.drawable.i02;
        else if (title.equals(pointModels.get(2).getPointTitle()))
            return R.drawable.i03;
        else if (title.equals(pointModels.get(3).getPointTitle()))
            return R.drawable.i04;
        else if (title.equals(pointModels.get(4).getPointTitle()))
            return R.drawable.i05;
        else if (title.equals(pointModels.get(5).getPointTitle()))
            return R.drawable.i06;
        else if (title.equals(pointModels.get(6).getPointTitle()))
            return R.drawable.i07;
        else if (title.equals(pointModels.get(7).getPointTitle()))
            return R.drawable.i08;
        else
            return  0;
    }

}
