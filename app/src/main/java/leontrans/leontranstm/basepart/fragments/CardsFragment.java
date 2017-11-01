package leontrans.leontranstm.basepart.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import leontrans.leontranstm.R;
import leontrans.leontranstm.basepart.AdvertisementAdapter;
import leontrans.leontranstm.basepart.AdvertisementInfo;
import leontrans.leontranstm.utils.SiteDataParseUtils;


public class CardsFragment extends Fragment {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private ArrayList<MenuItem> navMenuItemList= new ArrayList<>();
    private Context context;
    private SiteDataParseUtils siteDataUtils;

    private ArrayList<JSONObject> arrayListJsonObjectAdvertisement = new ArrayList<>();
    private ArrayList<AdvertisementInfo> arrayListAdvertisement = new ArrayList<>();
    private int numbOfAdvertisement = 10;

    private ListView advertisementListView;
    private FloatingActionButton btToBottom;
    private FloatingActionButton btToTop;
    private AdvertisementAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cards, container, false);
        context = getContext();
        siteDataUtils = new SiteDataParseUtils();

        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        navView = (NavigationView) view.findViewById(R.id.nvView);
        navView.setNavigationItemSelectedListener(getNavItemSelectedListener());

        for (int i = 0; i < navView.getMenu().size(); i++){
            navMenuItemList.add(navView.getMenu().getItem(i));
        }
        setMenuItemSwitcherAction();

        advertisementListView = (ListView) view.findViewById(R.id.listView);
        adapter = new AdvertisementAdapter(getActivity(),R.layout.list_item_layout,arrayListAdvertisement);
        advertisementListView.setAdapter(adapter);
        btToBottom = (FloatingActionButton) view.findViewById(R.id.btToBottom);
        btToTop = (FloatingActionButton) view.findViewById(R.id.btToTop);

        try {

            arrayListJsonObjectAdvertisement = siteDataUtils.getCardsInformation("https://leon-trans.com/api/ver1/login.php?action=get_bids&limit=", numbOfAdvertisement);

            for(int i = 0 ; i < numbOfAdvertisement ; i ++){
                arrayListAdvertisement.add(new AdvertisementInfo(arrayListJsonObjectAdvertisement.get(i).getString("trans_capacity"),arrayListJsonObjectAdvertisement.get(i).getString("trans_weight"),arrayListJsonObjectAdvertisement.get(i).getString("goods_load_type"),arrayListJsonObjectAdvertisement.get(i).getString("goods"),arrayListJsonObjectAdvertisement.get(i).getString("pay_currency"),arrayListJsonObjectAdvertisement.get(i).getString("pay_price"),arrayListJsonObjectAdvertisement.get(i).getString("pay_type"),
                        arrayListJsonObjectAdvertisement.get(i).getString("trans_type"),makeDate(arrayListJsonObjectAdvertisement.get(i).getString("date_from")),makeDate(arrayListJsonObjectAdvertisement.get(i).getString("date_to"))
                        ,arrayListJsonObjectAdvertisement.get(i).getString("country_from_ru")
                        ,arrayListJsonObjectAdvertisement.get(i).getString("country_to_ru"),arrayListJsonObjectAdvertisement.get(i).getString("city_from_ru"),arrayListJsonObjectAdvertisement.get(i).getString("city_to_ru")
                        ,arrayListJsonObjectAdvertisement.get(i).getString("userid_creator")));
            }


            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }




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

                adapter.notifyDataSetChanged();
                numbOfAdvertisement = numbOfAdvertisement + 10 ;
                arrayListJsonObjectAdvertisement = siteDataUtils.getCardsInformation("https://leon-trans.com/api/ver1/login.php?action=get_bids&limit=",numbOfAdvertisement);
                try {
                    for(int i = numbOfAdvertisement/2 ; i < numbOfAdvertisement  ; i ++){
                        arrayListAdvertisement.add(i,new AdvertisementInfo(arrayListJsonObjectAdvertisement.get(i).getString("trans_capacity"),arrayListJsonObjectAdvertisement.get(i).getString("trans_weight"),arrayListJsonObjectAdvertisement.get(i).getString("goods_load_type"),arrayListJsonObjectAdvertisement.get(i).getString("goods"),arrayListJsonObjectAdvertisement.get(i).getString("pay_currency"),arrayListJsonObjectAdvertisement.get(i).getString("pay_price"),arrayListJsonObjectAdvertisement.get(i).getString("pay_type"),arrayListJsonObjectAdvertisement.get(i).getString("trans_type")
                                ,makeDate(arrayListJsonObjectAdvertisement.get(i).getString("date_from")),makeDate(arrayListJsonObjectAdvertisement.get(i).getString("date_to"))
                                ,arrayListJsonObjectAdvertisement.get(i).getString("country_from_ru")
                                ,arrayListJsonObjectAdvertisement.get(i).getString("country_to_ru"),arrayListJsonObjectAdvertisement.get(i).getString("city_from_ru"),arrayListJsonObjectAdvertisement.get(i).getString("city_to_ru")
                                ,arrayListJsonObjectAdvertisement.get(i).getString("userid_creator")));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advertisementListView.setSelectionAfterHeaderView();
            }
        });

        return view;
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
                    Toast.makeText(context, "" + navMenuItemList.get(menuItemPosition).getTitle() + " activated", Toast.LENGTH_SHORT).show();
                }
                else {
                    navMenuItemList.get(menuItemPosition).setIcon(getResources().getDrawable(R.drawable.icon_filter_drawer));
                    Toast.makeText(context, "" + navMenuItemList.get(menuItemPosition).getTitle() + " disabled", Toast.LENGTH_SHORT).show();
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
