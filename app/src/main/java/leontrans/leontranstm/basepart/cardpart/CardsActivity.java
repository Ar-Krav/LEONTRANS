package leontrans.leontranstm.basepart.cardpart;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mikepenz.materialdrawer.Drawer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import leontrans.leontranstm.R;
import leontrans.leontranstm.basepart.filters.FilterSwitcherDialogActivity;
import leontrans.leontranstm.utils.Constants;
import leontrans.leontranstm.utils.InternetStatusUtils;
import leontrans.leontranstm.utils.NavigationDrawerMain;
import leontrans.leontranstm.utils.SiteDataParseUtils;

public class CardsActivity extends AppCompatActivity {
    private SiteDataParseUtils siteDataUtils;

    private Toolbar toolbar;
    private Drawer.Result mainNavigationDrawer;

    private ProgressBar loaderView;
    private LinearLayout contentArea;
    LinearLayout.LayoutParams listViewParams;

    private ArrayList<JSONObject> arrayListJsonObjectAdvertisement = new ArrayList<>();
    private ArrayList<AdvertisementInfo> arrayListAdvertisement = new ArrayList<>();
    private int numbOfAdvertisement = 10;

    private ListView advertisementListView;
    private Button loadNewCardsBtn;
    private AdvertisementAdapter adapter;

    private Locale locale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!InternetStatusUtils.isDeviceOnline(this)){
            showConnectionAlertDialog();
            return;
        }

        setContentView(R.layout.activity_cards);

        //en ru uk
        String language = getSharedPreferences("app_language", MODE_PRIVATE).getString("language","en");
        locale = new Locale("" + language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainNavigationDrawer = new NavigationDrawerMain(this, toolbar, Constants.NAVMENU_CARDS).getMainNavigationDrawer();

        loaderView = (ProgressBar) findViewById(R.id.loading_spinner);
        contentArea = (LinearLayout) findViewById(R.id.content_area);
        contentArea.setVisibility(View.GONE);

        siteDataUtils = new SiteDataParseUtils();
        adapter = new AdvertisementAdapter(this,R.layout.list_item_layout,arrayListAdvertisement);

        loadNewCardsBtn = (Button) findViewById(R.id.show_more_bids_btn);
            loadNewCardsBtn.setOnClickListener(getLoadNewCardsBtnListener());

        listViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            listViewParams.weight = 0.0f;
        advertisementListView = (ListView)findViewById(R.id.listView);
            advertisementListView.setAdapter(adapter);
            advertisementListView.setOnScrollListener(getListScrollListener());
            advertisementListView.setLayoutParams(listViewParams);

        new LoadCards().execute(0);
    }

    private class LoadCards extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loaderView.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            try {

                SharedPreferences userPasswordSharedPreferences = CardsActivity.this.getSharedPreferences("hashPassword", MODE_PRIVATE);
                String userPassword = userPasswordSharedPreferences.getString("userPassword","");
                int userID = new SiteDataParseUtils().getUserIdByHashpassword("https://leon-trans.com/api/ver1/login.php?action=get_hash_id&hash=" + userPassword);

                arrayListJsonObjectAdvertisement = siteDataUtils.getCardsInformation("https://leon-trans.com/api/ver1/login.php?action=get_bids&limit=" + numbOfAdvertisement + "&user=" + userID, numbOfAdvertisement);

                SharedPreferences lastCardId = getSharedPreferences("lastCardInfo", MODE_PRIVATE);
                lastCardId.edit().putInt("idLastCard", Integer.parseInt(arrayListJsonObjectAdvertisement.get(0).getString("id"))).commit();

                for(int i = integers[0]; i < arrayListJsonObjectAdvertisement.size() ; i ++){
                    JSONObject advertisementOwnerInfoJSON = siteDataUtils.getCardUserId("https://leon-trans.com/api/ver1/login.php?action=get_user&id="
                            +arrayListJsonObjectAdvertisement.get(i).getString("userid_creator"));

                    AdvertisementOwnerInfo advertisementOwnerInfo = new AdvertisementOwnerInfo(advertisementOwnerInfoJSON.getString("phones"), advertisementOwnerInfoJSON.getString("person_type"), getFullName(advertisementOwnerInfoJSON));
                    arrayListAdvertisement.add(i,new AdvertisementInfo(arrayListJsonObjectAdvertisement.get(i), advertisementOwnerInfo ,getApplicationContext(),locale));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();

            loaderView.setVisibility(View.GONE);
            contentArea.setVisibility(View.VISIBLE);
        }

        private String getFullName(JSONObject advertisementOwnerInfo) throws JSONException {
            JSONObject userCreatorEmploeeOwner;
            String result = "";

            switch (advertisementOwnerInfo.getString("person_type")){
                case "individual":{
                    result = advertisementOwnerInfo.getString("full_name");
                    break;
                }
                case "entity":{
                    result = advertisementOwnerInfo.getString("nomination_prefix") + "\n" +advertisementOwnerInfo.getString("nomination_name");
                    break;
                }
                case "fop":{
                    result = advertisementOwnerInfo.getString("nomination_prefix") + " " +advertisementOwnerInfo.getString("nomination_name");
                    break;
                }
                case "employee":{
                    userCreatorEmploeeOwner = siteDataUtils.getCardUserId("https://leon-trans.com/api/ver1/login.php?action=get_user&id=" + advertisementOwnerInfo.getString("employee_owner"));
                    if(userCreatorEmploeeOwner.getString("full_name").equals("")){
                        result = "(" + userCreatorEmploeeOwner.getString("nomination_prefix")+ " " +userCreatorEmploeeOwner.getString("nomination_name") + ")\n" +  advertisementOwnerInfo.getString("nomination_prefix") + " " +advertisementOwnerInfo.getString("nomination_name");
                    }else{
                        result = "(" + userCreatorEmploeeOwner.getString("full_name")+ ")\n" + advertisementOwnerInfo.getString("full_name");
                    }

                    break;
                }
            }
            return result;
        }
    }

    private AbsListView.OnScrollListener getListScrollListener(){
        return new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;

                if(lastItem >= totalItemCount-1){
                    listViewParams.weight = 1.0f;
                    loadNewCardsBtn.setVisibility(View.VISIBLE);
                }else{
                    listViewParams.weight = 0.0f;
                    loadNewCardsBtn.setVisibility(View.GONE);
                }

                advertisementListView.setLayoutParams(listViewParams);
            }
        };
    }

    private View.OnClickListener getLoadNewCardsBtnListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbOfAdvertisement += 10;

                listViewParams.weight = 0.0f;
                advertisementListView.setLayoutParams(listViewParams);

                new LoadCards().execute(numbOfAdvertisement-10);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cards_activity_meny,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()){
            case R.id.reloadCardActivityMenuBtn:{
                intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                break;
            }
            case R.id.showFilterNavigationDrawer:{
                intent = new Intent(CardsActivity.this, FilterSwitcherDialogActivity.class);
                break;
            }

            default:{
                intent = getIntent();
            }
        }

        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    private void showConnectionAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CardsActivity.this);
        builder.setTitle("You are offline!")
                .setMessage("Check your internet connection and try again.")
                .setIcon(R.drawable.icon_internet_disabled)
                .setCancelable(false)
                .setNegativeButton("Refresh",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent;
                                intent = getIntent();
                                overridePendingTransition(0, 0);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                overridePendingTransition(0, 0);

                                dialog.cancel();
                                finish();
                                startActivity(intent);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onBackPressed(){
        if(mainNavigationDrawer.isDrawerOpen()){
            mainNavigationDrawer.closeDrawer();
        }
        else{
            super.onBackPressed();
        }
    }
}
