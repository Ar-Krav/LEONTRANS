package leontrans.leontranstm.basepart.cardpart;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import leontrans.leontranstm.R;
import leontrans.leontranstm.utils.Constants;
import leontrans.leontranstm.utils.NavigationDrawerMain;
import leontrans.leontranstm.utils.SiteDataParseUtils;

public class CardsActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private ArrayList<MenuItem> navMenuItemList= new ArrayList<>();
    private SiteDataParseUtils siteDataUtils;
    private ActionBarDrawerToggle drawerToggle;


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

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nvView);
        navView.setNavigationItemSelectedListener(getNavItemSelectedListener());
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,null,R.string.drawer_open,R.string.drawer_close);

        for (int i = 0; i < navView.getMenu().size(); i++){
            navMenuItemList.add(navView.getMenu().getItem(i));
        }
        setMenuItemSwitcherAction();

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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
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

        private String getSiteRequestResult(String urlAddress){
           HttpURLConnection urlConnection = null;
           BufferedReader reader = null;
           String resultJson = "";

            try {
                URL url = new URL(urlAddress);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            return resultJson;
        }
    }

    private void setMenuItemSwitcherAction(){
        MenuItem menuItem;
        Switch itemSwitcher;

        for (int i = 0; i < navMenuItemList.size(); i++){
            menuItem = navMenuItemList.get(i);
            itemSwitcher = menuItem.getActionView().findViewById(R.id.menuSwitcher);
            itemSwitcher.setOnCheckedChangeListener(getSwitcherListener(i));
        }
    }

    private CompoundButton.OnCheckedChangeListener getSwitcherListener(final int menuItemPosition){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //TODO some actions with filter status

                if (isChecked){
                    navMenuItemList.get(menuItemPosition).setIcon(getResources().getDrawable(R.drawable.icon_filter_drawer_checked));
                    Toast.makeText(CardsActivity.this, "" + navMenuItemList.get(menuItemPosition).getTitle() + " activated", Toast.LENGTH_SHORT).show();
                }
                else {
                    navMenuItemList.get(menuItemPosition).setIcon(getResources().getDrawable(R.drawable.icon_filter_drawer));
                    Toast.makeText(CardsActivity.this, "" + navMenuItemList.get(menuItemPosition).getTitle() + " disabled", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private NavigationView.OnNavigationItemSelectedListener getNavItemSelectedListener(){
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.filter1:{
                        //TODO goto filter settings
                        break;
                    }
                    case R.id.filter2:{
                        //TODO goto filter settings
                        break;
                    }
                    case R.id.filter3:{
                        //TODO goto filter settings
                        break;
                    }
                    case R.id.filter4:{
                        //TODO goto filter settings
                        break;
                    }
                    case R.id.filter5:{
                        //TODO goto filter settings
                        break;
                    }
                    case R.id.filter6:{
                        //TODO goto filter settings
                        break;
                    }
                    case R.id.filter7:{
                        //TODO goto filter settings
                        break;
                    }
                    case R.id.filter8:{
                        //TODO goto filter settings
                        break;
                    }
                    case R.id.filter9:{
                        //TODO goto filter settings
                        break;
                    }
                    case R.id.filter10:{
                        //TODO goto filter settings
                        break;
                    }
                }

                return true;
            }
        };
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
        if (drawerLayout.isDrawerOpen(navView)){
            drawerLayout.closeDrawer(navView);
        }
        else {
            drawerLayout.openDrawer(navView, false);
        }

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
