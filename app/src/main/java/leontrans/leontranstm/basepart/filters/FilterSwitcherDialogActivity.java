package leontrans.leontranstm.basepart.filters;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import leontrans.leontranstm.R;
import leontrans.leontranstm.basepart.userprofile.UserCardOwenerProfile;
import leontrans.leontranstm.utils.SiteDataParseUtils;
import leontrans.leontranstm.utils.SystemServicesUtils;

public class FilterSwitcherDialogActivity extends AppCompatActivity {

    private ArrayList<Switch> filterSwitchersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_filter_switcher_dialog);

        filterSwitchersList = getSwitcherArrayList();

        ((Button) findViewById(R.id.save_button)).setOnClickListener(getSaveBtnClickListener());

        new LoadUserFiltersInfo().execute();
    }

    private View.OnClickListener getSaveBtnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, String> filterStatMap = new HashMap<>();
                int filterNumber = 1;

                for (Switch switcher : filterSwitchersList){
                    filterStatMap.put("b" + filterNumber, switcher.isChecked() ? "" + 1 : "" + 0);
                }


                //TODO send result to web site

                FilterSwitcherDialogActivity.this.finish();
            }
        };
    }

    private ArrayList<Switch> getSwitcherArrayList(){
        ArrayList<Switch> switcherArray = new ArrayList<>();

        switcherArray.add((Switch) findViewById(R.id.filter_switcher_1));
        switcherArray.add((Switch) findViewById(R.id.filter_switcher_2));
        switcherArray.add((Switch) findViewById(R.id.filter_switcher_3));
        switcherArray.add((Switch) findViewById(R.id.filter_switcher_4));
        switcherArray.add((Switch) findViewById(R.id.filter_switcher_5));
        switcherArray.add((Switch) findViewById(R.id.filter_switcher_6));
        switcherArray.add((Switch) findViewById(R.id.filter_switcher_7));
        switcherArray.add((Switch) findViewById(R.id.filter_switcher_8));
        switcherArray.add((Switch) findViewById(R.id.filter_switcher_9));
        switcherArray.add((Switch) findViewById(R.id.filter_switcher_10));

        return  switcherArray;
    }

    private class LoadUserFiltersInfo extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            SharedPreferences userPasswordSharedPreferences = FilterSwitcherDialogActivity.this.getSharedPreferences("hashPassword", MODE_PRIVATE);
            String userPassword = userPasswordSharedPreferences.getString("userPassword","");
            int userID = new SiteDataParseUtils().getUserIdByHashpassword("https://leon-trans.com/api/ver1/login.php?action=get_hash_id&hash=" + userPassword);
            return new SiteDataParseUtils().getSiteRequestResult("https://leon-trans.com/api/ver1/login.php?action=get_user&id=" + userID); //TODO
        }

        @Override
        protected void onPostExecute(String jsonStr) {

            try {
                JSONObject dataJson = new JSONObject(jsonStr);
                JSONObject filterData = new JSONObject(dataJson.getString("on_off"));

                int filterNumber = 1;
                for (Switch switcher : filterSwitchersList){
                    switcher.setChecked(filterData.getString("b" + filterNumber).equals("1"));
                    filterNumber++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
