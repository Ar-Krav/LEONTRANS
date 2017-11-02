package leontrans.leontranstm.basepart;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import leontrans.leontranstm.R;
import leontrans.leontranstm.utils.Constants;
import leontrans.leontranstm.utils.NavigationDrawerMain;
import leontrans.leontranstm.utils.SiteDataParseUtils;

public class CardsActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private ArrayList<MenuItem> navMenuItemList= new ArrayList<>();
    private SiteDataParseUtils siteDataUtils;

    private Toolbar toolbar;
    private Drawer.Result mainNavigationDrawer;

    private ArrayList<JSONObject> arrayListJsonObjectAdvertisement = new ArrayList<>();
    private ArrayList<AdvertisementInfo> arrayListAdvertisement = new ArrayList<>();
    private int numbOfAdvertisement = 10;

    private ListView advertisementListView;
    private FloatingActionButton btToBottom;
    private FloatingActionButton btToTop;
    private AdvertisementAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainNavigationDrawer = new NavigationDrawerMain(this, toolbar, Constants.NAVMENU_CARDS).getMainNavigationDrawer();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nvView);
        navView.setNavigationItemSelectedListener(getNavItemSelectedListener());

        for (int i = 0; i < navView.getMenu().size(); i++){
            navMenuItemList.add(navView.getMenu().getItem(i));
        }
        setMenuItemSwitcherAction();

        siteDataUtils = new SiteDataParseUtils();
        advertisementListView = (ListView)findViewById(R.id.listView);
        adapter = new AdvertisementAdapter(this,R.layout.list_item_layout,arrayListAdvertisement);
        advertisementListView.setAdapter(adapter);
        btToBottom = (FloatingActionButton) findViewById(R.id.btToBottom);
        btToTop = (FloatingActionButton) findViewById(R.id.btToTop);

        LoadCards loadCards = new LoadCards();
        loadCards.doInBackground();


        advertisementListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;

                if(lastItem >= totalItemCount-1){
                    btToBottom.setVisibility(View.VISIBLE);
                    btToTop.setVisibility(View.VISIBLE);

                }else{
                    btToBottom.setVisibility(View.INVISIBLE);
                    btToTop.setVisibility(View.INVISIBLE);
                }
            }
        });

        btToBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadByButtonPress loadByButtonPress = new LoadByButtonPress();
                loadByButtonPress.doInBackground();
                adapter.notifyDataSetChanged();
            }
        });

        btToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advertisementListView.setSelectionAfterHeaderView();
            }
        });

    }

    private class LoadCards extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                arrayListJsonObjectAdvertisement = siteDataUtils.getCardsInformation("https://leon-trans.com/api/ver1/login.php?action=get_bids&limit=", numbOfAdvertisement);
                for(int i = 0 ; i < numbOfAdvertisement ; i ++){
                    arrayListAdvertisement.add(i,new AdvertisementInfo(arrayListJsonObjectAdvertisement.get(i)));
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }

    private class LoadByButtonPress extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            adapter.notifyDataSetChanged();
            numbOfAdvertisement += 10 ;
            arrayListJsonObjectAdvertisement = siteDataUtils.getCardsInformation("https://leon-trans.com/api/ver1/login.php?action=get_bids&limit=",numbOfAdvertisement);
            try {
                for(int i = numbOfAdvertisement/2 ; i < numbOfAdvertisement  ; i ++){
                    arrayListAdvertisement.add(new AdvertisementInfo(arrayListJsonObjectAdvertisement.get(i)));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
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

    public void onBackPressed(){
        if(mainNavigationDrawer.isDrawerOpen()){
            mainNavigationDrawer.closeDrawer();
        }
        else{
            super.onBackPressed();
        }
    }
}
