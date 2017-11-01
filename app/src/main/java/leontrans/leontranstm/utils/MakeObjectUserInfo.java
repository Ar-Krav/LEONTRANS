package leontrans.leontranstm.utils;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MakeObjectUserInfo {
    JSONObject dataJsonArr = null ;
    ArrayList<JSONObject> names = new ArrayList<>() ;
    String resJson = "";
    public ArrayList<JSONObject> connectParseTask(String url){
        try {
            resJson = new ParseTask(url).execute().get();
            dataJsonArr = new JSONObject(resJson);
            names.add(dataJsonArr);
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