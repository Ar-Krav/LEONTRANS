package leontrans.leontranstm.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MakeObjects {
    JSONObject dataJsonObj = null;
    JSONArray dataJsonArr = null ;
    ArrayList<JSONObject> names = new ArrayList<>() ;
    String resJson = "";

    public ArrayList<JSONObject> connectParseTask(String url ,int n){

        try {
            resJson = new ParseTask(url+n).execute().get();
            dataJsonArr = new JSONArray(resJson);
            for(int i = 0 ; i < n ; i++){
                dataJsonObj = dataJsonArr.getJSONObject(i);
                names.add(dataJsonObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return names;
    }
}