package leontrans.leontranstm.basepart.filters.editor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import leontrans.leontranstm.R;
import leontrans.leontranstm.basepart.filters.FilterSettingsActivity;
import leontrans.leontranstm.utils.SiteDataParseUtils;

public class FilterEditActivity extends AppCompatActivity {

    private final int REQUEST_CODE_LOAD_TYPE = 1;
    private final int REQUEST_CODE_DOCS = 2;
    private final int REQUEST_CODE_ADR = 3;

    private Toolbar toolbar;
    String notifyId;

    Spinner notifyTypeSpinner;
    Spinner carTypeSpinner;
    Spinner carKindSpinner;

    ArrayList<String> docsArrayList;
    ArrayList<String> loadTypeArrayList;
    ArrayList<String> adrArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_edit);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        notifyTypeSpinner = (Spinner) findViewById(R.id.notify_type_spinner);
        carTypeSpinner = (Spinner) findViewById(R.id.car_type);
        carKindSpinner = (Spinner) findViewById(R.id.car_kind);

        ArrayAdapter<?> notifyTypeAdapter = ArrayAdapter.createFromResource(this, R.array.notify_types, android.R.layout.simple_spinner_item);
            notifyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<?> carTypeAdapter = ArrayAdapter.createFromResource(this, R.array.car_types, android.R.layout.simple_spinner_item);
            carTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<?> carKindAdapter = ArrayAdapter.createFromResource(this, R.array.car_kind, android.R.layout.simple_spinner_item);
            carKindAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        notifyTypeSpinner.setAdapter(notifyTypeAdapter);
        carTypeSpinner.setAdapter(carTypeAdapter);
        carKindSpinner.setAdapter(carKindAdapter);

        docsArrayList = new ArrayList<>();
        loadTypeArrayList = new ArrayList<>();
        adrArrayList = new ArrayList<>();

        ((Button) findViewById(R.id.docs_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterEditActivity.this, DocsSelectorDialog.class);
                intent.putStringArrayListExtra("docsArray",docsArrayList);
                startActivityForResult(intent, REQUEST_CODE_DOCS);
            }
        });

        ((Button) findViewById(R.id.load_type_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterEditActivity.this, LoadTypeSelectorDialog.class);
                intent.putStringArrayListExtra("loadTypeArray",loadTypeArrayList);
                startActivityForResult(intent, REQUEST_CODE_LOAD_TYPE);
            }
        });

        ((Button) findViewById(R.id.adr_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterEditActivity.this, AdrSelectorDialog.class);
                intent.putStringArrayListExtra("adrArray",adrArrayList);
                startActivityForResult(intent, REQUEST_CODE_ADR);
            }
        });

        ((Button) findViewById(R.id.save_button)).setOnClickListener(getSaveBtnClickListener());
        ((Button) findViewById(R.id.cancel_button)).setOnClickListener(getCancelBtnClickListener());

        notifyId = getIntent().getStringExtra("notifyId");
        new LoadFilterInfo().execute();
    }

    private View.OnClickListener getSaveBtnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO send all result to server
                returnToFilterSettingActivity();
            }
        };
    }

    private View.OnClickListener getCancelBtnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToFilterSettingActivity();
            }
        };
    }

    private class LoadFilterInfo extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            SharedPreferences userPasswordSharedPreferences = FilterEditActivity.this.getSharedPreferences("hashPassword", MODE_PRIVATE);
            String userPassword = userPasswordSharedPreferences.getString("userPassword","");
            int userID = new SiteDataParseUtils().getUserIdByHashpassword("https://leon-trans.com/api/ver1/login.php?action=get_hash_id&hash=" + userPassword);
            return new SiteDataParseUtils().getSiteRequestResult("https://leon-trans.com/api/ver1/login.php?action=get_user&id=" + userID);
        }

        @Override
        protected void onPostExecute(String jsonStr) {

            try {
                JSONObject dataJson = new JSONObject(jsonStr);

                if (dataJson.getString(notifyId).isEmpty()) return;

                JSONObject notifyData = new JSONObject(dataJson.getString(notifyId));

                ((EditText) findViewById(R.id.country_from)).setText(notifyData.getString("country_from_name"));
                ((EditText) findViewById(R.id.country_to)).setText(notifyData.getString("country_to_name"));
                ((EditText) findViewById(R.id.city_from)).setText(notifyData.getString("city_from_name"));
                ((EditText) findViewById(R.id.city_to)).setText(notifyData.getString("city_to_name"));

                ((EditText) findViewById(R.id.capacity_from)).setText(notifyData.getString("capacity_from"));
                ((EditText) findViewById(R.id.capacity_to)).setText(notifyData.getString("capacity_to"));

                ((EditText) findViewById(R.id.weight_from)).setText(notifyData.getString("weight_from"));
                ((EditText) findViewById(R.id.weight_to)).setText(notifyData.getString("weight_to"));

                setNotifySpinnerSelection(notifyData.getString("type"));
                setCarTypeSpinnerSelection(notifyData.getString("trans_type"));
                setCarKindSpinnerSelection(notifyData.getString("trans_kind"));

                docsArrayList = getSplittedArrayList(notifyData.getString("docs"));
                loadTypeArrayList = getSplittedArrayList(notifyData.getString("load_type"));
                adrArrayList = getSplittedArrayList(notifyData.getString("adr"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private ArrayList<String> getSplittedArrayList(String data){
            ArrayList<String> resultList = new ArrayList<>();

            String[] docs = data.split(",");
            for (int i = 0; i < docs.length; i++){
                resultList.add(docs[i]);
            }

            return resultList;
        }

        private void setNotifySpinnerSelection(String notifyType){
            switch (notifyType){
                case "": {
                    notifyTypeSpinner.setSelection(0);
                    break;
                }
                case "avto": {
                    notifyTypeSpinner.setSelection(1);
                    break;
                }case "goods": {
                    notifyTypeSpinner.setSelection(2);
                    break;
                }
                default: notifyTypeSpinner.setSelection(0);
            }
        }

        private void setCarTypeSpinnerSelection(String carType){
            switch (carType){
                case "":{
                    carTypeSpinner.setSelection(0);
                    break;
                }
                case "any":{
                    carTypeSpinner.setSelection(1);
                    break;
                }
                case "bus":{
                    carTypeSpinner.setSelection(2);
                    break;
                }
                case "avto":{
                    carTypeSpinner.setSelection(3);
                    break;
                }
                case "fuel_oil":{
                    carTypeSpinner.setSelection(4);
                    break;
                }
                case "concrete":{
                    carTypeSpinner.setSelection(5);
                    break;
                }
                case "gas":{
                    carTypeSpinner.setSelection(6);
                    break;
                }
                case "hard":{
                    carTypeSpinner.setSelection(7);
                    break;
                }
                case "grain":{
                    carTypeSpinner.setSelection(8);
                    break;
                }
                case "isotherms":{
                    carTypeSpinner.setSelection(9);
                    break;
                }
                case "containertrans":{
                    carTypeSpinner.setSelection(10);
                    break;
                }
                case "tap":{
                    carTypeSpinner.setSelection(11);
                    break;
                }
                case "closed":{
                    carTypeSpinner.setSelection(12);
                    break;
                }
                case "trees":{
                    carTypeSpinner.setSelection(13);
                    break;
                }
                case "microbus":{
                    carTypeSpinner.setSelection(1);
                    break;
                }
                case "oversized":{
                    carTypeSpinner.setSelection(14);
                    break;
                }
                case "unclosed":{
                    carTypeSpinner.setSelection(15);
                    break;
                }
                case "refrigerator":{
                    carTypeSpinner.setSelection(16);
                    break;
                }
                case "tipper":{
                    carTypeSpinner.setSelection(17);
                    break;
                }
                case "animaltruck":{
                    carTypeSpinner.setSelection(18);
                    break;
                }
                case "awning":{
                    carTypeSpinner.setSelection(19);
                    break;
                }
                case "trall":{
                    carTypeSpinner.setSelection(20);
                    break;
                }
                case "avtotipper":{
                    carTypeSpinner.setSelection(21);
                    break;
                }
                case "fullmetal":{
                    carTypeSpinner.setSelection(22);
                    break;
                }
                case "fuel_oil_small":{
                    carTypeSpinner.setSelection(23);
                    break;
                }
                case "evacuator":{
                    carTypeSpinner.setSelection(24);
                    break;
                }
                default: carTypeSpinner.setSelection(0);
            }
        }

        private void setCarKindSpinnerSelection(String carKind){
            switch (carKind){
                case "":{
                    carKindSpinner.setSelection(0);
                    break;
                }
                case "truck":{
                    carKindSpinner.setSelection(1);
                    break;
                }
                case "trailer":{
                    carKindSpinner.setSelection(2);
                    break;
                }
                case "half-trailer":{
                    carKindSpinner.setSelection(3);
                    break;
                }
                default: carKindSpinner.setSelection(0);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            returnToFilterSettingActivity();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case REQUEST_CODE_LOAD_TYPE:{
                if(data != null) loadTypeArrayList = data.getStringArrayListExtra("loadTypeResult");
                break;
            }

            case REQUEST_CODE_DOCS:{
                if(data != null) docsArrayList = data.getStringArrayListExtra("docsResult");
                break;
            }

            case REQUEST_CODE_ADR:{
                if(data != null) adrArrayList = data.getStringArrayListExtra("adrResult");
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        returnToFilterSettingActivity();
    }

    private void returnToFilterSettingActivity(){
        Intent intent = new Intent(FilterEditActivity.this, FilterSettingsActivity.class);
        startActivity(intent);
    }
}
