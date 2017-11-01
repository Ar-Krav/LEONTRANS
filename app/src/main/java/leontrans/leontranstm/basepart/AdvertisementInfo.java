package leontrans.leontranstm.basepart;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import leontrans.leontranstm.utils.SiteDataParseUtils;

public class AdvertisementInfo {
    private String trans_type;
    private String date_from;
    private String date_to;
    private String country_from_ru;
    private String country_to_ru;
    private String city_from_ru;
    private String city_to_ru;
    private String userid_creator;
    private String pay_type;
    private String pay_price;
    private String pay_currency;
    private String goods;
    private String goods_load_type;
    private String trans_weight;
    private String trans_capacity;
    private String telephone;
    private String person_type;
    private String full_name;
    private String nomination;
    private ArrayList<JSONObject> arrayListJsonObjectUsers;

    public AdvertisementInfo(String trans_capacity, String trans_weight, String goods_load_type, String goods, String pay_currency, String pay_price, String pay_type, String trans_type, String date_from, String date_to, String country_from_ru, String country_to_ru, String city_from_ru, String city_to_ru, String userid_creator) throws JSONException {
        this.trans_type = trans_type;
        this.date_from = date_from;
        this.date_to = date_to;
        this.country_from_ru = country_from_ru;
        this.country_to_ru = country_to_ru;
        this.city_from_ru = city_from_ru;
        this.city_to_ru = city_to_ru;
        this.userid_creator = userid_creator;
        this.pay_type = pay_type;
        this.pay_price = pay_price;
        this.pay_currency = pay_currency;
        this.goods = goods;
        this.goods_load_type = goods_load_type;
        this.trans_weight = trans_weight ;
        this.trans_capacity = trans_capacity;
        this.arrayListJsonObjectUsers = makeTunell(userid_creator);
        this.telephone = arrayListJsonObjectUsers.get(0).getString("phones");
        this.person_type = arrayListJsonObjectUsers.get(0).getString("person_type");
        this.full_name = arrayListJsonObjectUsers.get(0).getString("full_name");
        this.nomination = arrayListJsonObjectUsers.get(0).getString("nomination_prefix") + " " +arrayListJsonObjectUsers.get(0).getString("nomination_name");
    }

    public String getPerson_type() {
        return person_type;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getNomination() {
        return nomination;
    }

    public String getTelephone() {
        return telephone;
    }

    private ArrayList<JSONObject> makeTunell(String id){
        arrayListJsonObjectUsers = new SiteDataParseUtils().getCardUserId("https://leon-trans.com/api/ver1/login.php?action=get_user&id="+id);
        return arrayListJsonObjectUsers;
    }
    public String getTrans_capacity() {
        return trans_capacity;
    }

    public String getTrans_weight() {
        return trans_weight;
    }

    public String getGoods_load_type() {
        return goods_load_type;
    }

    public String getGoods() {
        return goods;
    }

    public String getPay_currency() {
        return pay_currency;
    }

    public String getPay_price() {
        return pay_price;
    }

    public String getPay_type() {
        return pay_type;
    }

    public String getTrans_type() {
        return trans_type;
    }

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    public String getCountry_from_ru() {
        return country_from_ru;
    }

    public void setCountry_from_ru(String country_from_ru) {
        this.country_from_ru = country_from_ru;
    }

    public String getCountry_to_ru() {
        return country_to_ru;
    }

    public void setCountry_to_ru(String country_to_ru) {
        this.country_to_ru = country_to_ru;
    }

    public String getCity_from_ru() {
        return city_from_ru;
    }

    public void setCity_from_ru(String city_from_ru) {
        this.city_from_ru = city_from_ru;
    }

    public String getCity_to_ru() {
        return city_to_ru;
    }

    public void setCity_to_ru(String city_to_ru) {
        this.city_to_ru = city_to_ru;
    }

    public String getUserid_creator() {
        return userid_creator;
    }

    public void setUserid_creator(String userid_creator) {
        this.userid_creator = userid_creator;
    }
}