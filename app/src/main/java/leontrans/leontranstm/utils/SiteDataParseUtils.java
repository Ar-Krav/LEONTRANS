package leontrans.leontranstm.utils;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SiteDataParseUtils {

    private final String LOG_TAG = "SiteDataParseUtils_log";

    public int getUserIdByHashpassword(String urlRequest){
        String strJson = getJsonString(urlRequest);
        int requestResultID = -1;

        try {
            JSONObject dataJsonObj = new JSONObject(strJson);
            requestResultID = dataJsonObj.getInt("id");
            Log.d(LOG_TAG, "" + requestResultID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return requestResultID;
    }

    public  String getUserHashPassword(String urlRequest){
        String strJson = getJsonString(urlRequest);

        try {
            JSONObject rootJsonObj = new JSONObject(strJson);
            JSONObject userInfo = rootJsonObj.getJSONObject("id");

            if (userInfo.getInt("id") != 0) {
                return userInfo.getString("password");
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<JSONObject> getCardsInformation(String jsonResult , int numOfRequests){
        JSONObject dataJsonObj = null;
        JSONArray dataJsonArr = null ;
        ArrayList<JSONObject> resultArray = new ArrayList<>();

        try{
            dataJsonArr = new JSONArray(jsonResult);
            for(int i = 0 ; i < numOfRequests ; i++){
                dataJsonObj = dataJsonArr.getJSONObject(i);
                resultArray.add(dataJsonObj);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return resultArray;
    }

    public JSONObject getCardUserId(String resJson){
        JSONObject dataJsonObj = null;

        try {
            dataJsonObj = new JSONObject(resJson);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return dataJsonObj;
    }

    private String getJsonString(String urlRequest){
        String strJson = "";

        try {
            strJson = new SiteDataListener(urlRequest).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return strJson;
    }

}
