package leontrans.leontranstm.basepart.cardpart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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
import leontrans.leontranstm.utils.NavigationDrawerMain;
import leontrans.leontranstm.utils.SiteDataParseUtils;

public class CardsActivity extends AppCompatActivity {
    private SiteDataParseUtils siteDataUtils;

    private Toolbar toolbar;
    private Drawer.Result mainNavigationDrawer;

    private ProgressBar loaderView;
    private ConstraintLayout contentArea;

    private ArrayList<JSONObject> arrayListJsonObjectAdvertisement = new ArrayList<>();
    private ArrayList<AdvertisementInfo> arrayListAdvertisement = new ArrayList<>();
    private int numbOfAdvertisement = 10;

    private ListView advertisementListView;
    private FloatingActionButton loadNewCardsBtn;
    private FloatingActionButton btToTop;
    private AdvertisementAdapter adapter;

    private Locale locale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        contentArea = (ConstraintLayout) findViewById(R.id.content_area);
        contentArea.setVisibility(View.GONE);

        siteDataUtils = new SiteDataParseUtils();
        adapter = new AdvertisementAdapter(this,R.layout.list_item_layout,arrayListAdvertisement);

        loadNewCardsBtn = (FloatingActionButton) findViewById(R.id.btToBottom);
            loadNewCardsBtn.setOnClickListener(getLoadNewCardsBtnListener());

        btToTop = (FloatingActionButton) findViewById(R.id.btToTop);
            btToTop.setOnClickListener(getUpButtonClickListener());

        advertisementListView = (ListView)findViewById(R.id.listView);
            advertisementListView.setAdapter(adapter);
            advertisementListView.setOnScrollListener(getListScrollListener());

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
                arrayListJsonObjectAdvertisement = siteDataUtils.getCardsInformation("https://leon-trans.com/api/ver1/login.php?action=get_bids&limit=" + numbOfAdvertisement, numbOfAdvertisement);

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
                    result = advertisementOwnerInfo.getString("nomination_prefix") + " " +advertisementOwnerInfo.getString("nomination_name");
                    break;
                }
                case "fop":{
                    result = advertisementOwnerInfo.getString("nomination_prefix") + " " +advertisementOwnerInfo.getString("nomination_name");
                    break;
                }
                case "employee":{
                    userCreatorEmploeeOwner = siteDataUtils.getCardUserId("https://leon-trans.com/api/ver1/login.php?action=get_user&id=" + advertisementOwnerInfo.getString("employee_owner"));
                    result = userCreatorEmploeeOwner.getString("nomination_prefix")+ " " +userCreatorEmploeeOwner.getString("nomination_name");
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
                    loadNewCardsBtn.setVisibility(View.VISIBLE);
                    btToTop.setVisibility(View.VISIBLE);

                }else{
                    loadNewCardsBtn.setVisibility(View.INVISIBLE);
                    btToTop.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    private View.OnClickListener getLoadNewCardsBtnListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbOfAdvertisement += 10;
                new LoadCards().execute(numbOfAdvertisement-10);
            }
        };
    }

    private View.OnClickListener getUpButtonClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advertisementListView.setSelectionAfterHeaderView();
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

        Intent intent = new Intent(CardsActivity.this, FilterSwitcherDialogActivity.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
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
