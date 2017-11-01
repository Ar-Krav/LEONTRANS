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

    public UserDetailInfo getuserDetailInfo(int userID){
        String jsonStr = getJsonString("https://leon-trans.com/api/ver1/login.php?action=get_user&id=" + userID);

        String userName;
        String userCity;
        String userEmail;
        String userPhone;
        String userSkype;
        String userICQ;
        String userWebSite;
        String userOccupation;
        String userOccupationType;
        String userOccupationDescription;
        String userRegistrationDate;
        String userLastOnline;
        String userAvatarCode;

        try {
            JSONObject dataJson = new JSONObject(jsonStr);
            userName = dataJson.getString("full_name");
            userCity = dataJson.getString("location_city");
            userEmail = dataJson.getString("email");
            userPhone = dataJson.getString("phones");
            userSkype = dataJson.getString("skype");
            userICQ = dataJson.getString("icq");
            userWebSite = dataJson.getString("website");
            userOccupation = dataJson.getString("metier_type");
            userOccupationType = dataJson.getString("activity_kind");
            userOccupationDescription = dataJson.getString("activity_desc");
            userRegistrationDate = dataJson.getString("date_registry");
            userLastOnline = dataJson.getString("date_last_action");
            userAvatarCode = dataJson.getString("avatar");

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return new UserDetailInfo(userName,userCity, userEmail, userPhone,
                userSkype, userICQ, userWebSite,
                userOccupation, userOccupationType, userOccupationDescription,
                userRegistrationDate, userLastOnline, userAvatarCode);
    }


    public ArrayList<JSONObject> getGetCardsDetailInformation(String url , int numOfRequests){
        JSONObject dataJsonObj = null;
        JSONArray dataJsonArr = null ;
        ArrayList<JSONObject> names = new ArrayList<>();

        String resJson = getJsonString(url+numOfRequests);

        try{
            dataJsonArr = new JSONArray(resJson);
            for(int i = 0 ; i < numOfRequests ; i++){
                dataJsonObj = dataJsonArr.getJSONObject(i);
                names.add(dataJsonObj);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return names;
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
