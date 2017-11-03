package leontrans.leontranstm.basepart;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import leontrans.leontranstm.utils.AdvertisementOwnerInfo;
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

    public AdvertisementInfo(JSONObject list, AdvertisementOwnerInfo advertisementOwnerInfo) throws JSONException {
        this.trans_type = list.getString("trans_type");
        this.date_from = makeDate(list.getString("date_from"));
        this.date_to = makeDate(list.getString("date_to"));
        this.country_from_ru = list.getString("country_from_ru");
        this.country_to_ru = list.getString("country_to_ru");
        this.city_from_ru = list.getString("city_from_ru");
        this.city_to_ru = list.getString("city_to_ru");

        this.userid_creator = list.getString("userid_creator");
        this.pay_type = list.getString("pay_type");
        this.pay_price = list.getString("pay_price");
        this.pay_currency = list.getString("pay_currency");
        this.goods = list.getString("goods");
        this.goods_load_type = list.getString("goods_load_type");
        this.trans_weight = list.getString("trans_weight");
        this.trans_capacity = list.getString("trans_capacity");

        this.telephone = advertisementOwnerInfo.getTelephone();
        this.person_type = advertisementOwnerInfo.getPerson_type();
        this.full_name = advertisementOwnerInfo.getFull_name();
    }

    public String getTrans_type() {
        return trans_type;
    }

    public void setTrans_type(String trans_type) {
        this.trans_type = trans_type;
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

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_price() {
        return pay_price;
    }

    public void setPay_price(String pay_price) {
        this.pay_price = pay_price;
    }

    public String getPay_currency() {
        return pay_currency;
    }

    public void setPay_currency(String pay_currency) {
        this.pay_currency = pay_currency;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getGoods_load_type() {
        return goods_load_type;
    }

    public void setGoods_load_type(String goods_load_type) {
        this.goods_load_type = goods_load_type;
    }

    public String getTrans_weight() {
        return trans_weight;
    }

    public void setTrans_weight(String trans_weight) {
        this.trans_weight = trans_weight;
    }

    public String getTrans_capacity() {
        return trans_capacity;
    }

    public void setTrans_capacity(String trans_capacity) {
        this.trans_capacity = trans_capacity;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPerson_type() {
        return person_type;
    }

    public void setPerson_type(String person_type) {
        this.person_type = person_type;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    private String makeDate(String date){
        long dv;
        Date df;
        String dateFrom;
        dv = Long.valueOf(date) * 1000;
        df = new java.util.Date(dv);
        dateFrom = new SimpleDateFormat("MM.dd.yy").format(df);
        return dateFrom;
    }
}