package leontrans.leontranstm.basepart;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import leontrans.leontranstm.R;
import leontrans.leontranstm.basepart.fragments.CardsFragment;
import leontrans.leontranstm.basepart.fragments.FAQFragment;
import leontrans.leontranstm.basepart.fragments.FilterSettingsFragment;
import leontrans.leontranstm.basepart.fragments.UserProfileFragment;

public class BaseAppActivity extends AppCompatActivity {

    public static BaseAppActivity baseAppActivity;

    private Toolbar toolbar;
    private MenuItem filterStartMenuItem;
    WebView loaderView;


    private Drawer.Result mainNavigationDrawer;
    private IDrawerItem selectedDrawerItem;
    private int idSelectedDrawerItem;

    private final int NAVMENU_PROFILE = 0;
    private final int NAVMENU_CARDS = 1;
    private final int NAVMENU_FILTER_SETTINGS = 2;
    private final int NAVMENU_FAQ = 3;
    private final int NAVMENU_ADMIN = 4; //TODO admin exit button. Developing part only!

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseAppActivity = this;

        setContentView(R.layout.activity_base_app);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area, new CardsFragment()).commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loaderView = (WebView) findViewById(R.id.loaderView);
        loaderView.setBackgroundColor(Color.TRANSPARENT);
        loaderView.setVisibility(View.GONE);

        mainNavigationDrawer =  getMainNavigationDrawer();
        idSelectedDrawerItem = NAVMENU_CARDS;
        mainNavigationDrawer.setSelection(NAVMENU_CARDS);
    }

    private Drawer.Result getMainNavigationDrawer(){
        return new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_main_header)
                .withHeaderDivider(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Мій профіль").withIcon(FontAwesome.Icon.faw_user).withIdentifier(NAVMENU_PROFILE),
                        new PrimaryDrawerItem().withName("Список оголошень").withIcon(FontAwesome.Icon.faw_list_alt).withIdentifier(NAVMENU_CARDS),
                        new PrimaryDrawerItem().withName("Налаштування фільтрів").withIcon(FontAwesome.Icon.faw_cogs).withIdentifier(NAVMENU_FILTER_SETTINGS),
                        new PrimaryDrawerItem().withName("F.A.Q.").withIcon(FontAwesome.Icon.faw_question_circle).withIdentifier(NAVMENU_FAQ),

                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Про програму").withIcon(FontAwesome.Icon. faw_info_circle),
                        new PrimaryDrawerItem().withName("admin exit").withIcon(FontAwesome.Icon.faw_medkit).withIdentifier(NAVMENU_ADMIN) //TODO admin exit button. Developing part only!
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                        selectedDrawerItem = null;
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        if (selectedDrawerItem != null) new Async().execute();
                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                        if (drawerItem.getIdentifier() == NAVMENU_ADMIN) {
                            SharedPreferences sharedPreferences = getSharedPreferences("hashPassword", MODE_PRIVATE);
                            sharedPreferences.edit().clear().commit();
                        } //TODO admin exit button. Developing part only!

                        if (drawerItem.getIdentifier() == idSelectedDrawerItem) return;

                        filterStartMenuItem.setVisible(false);

                        idSelectedDrawerItem = drawerItem.getIdentifier();
                        selectedDrawerItem = drawerItem;

                        loaderView.setVisibility(View.VISIBLE);
                    }
                })
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_switchon_menu,menu);
        filterStartMenuItem = menu.getItem(0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment cardFragmen = getSupportFragmentManager().findFragmentById(R.id.fragment_area);
        DrawerLayout fragmentDrawerLayout = (DrawerLayout) cardFragmen.getView().findViewById(R.id.drawer_layout);

        if (fragmentDrawerLayout.isDrawerOpen(GravityCompat.END)){
            fragmentDrawerLayout.closeDrawer(GravityCompat.END);
        }
        else {
            fragmentDrawerLayout.openDrawer(GravityCompat.END);
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

    public static BaseAppActivity getBaseAppActivity() {
        return baseAppActivity;
    }

    private class Async extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loaderView = (WebView) findViewById(R.id.loaderView);
            loaderView.setVisibility(View.VISIBLE);
            loaderView.loadUrl("file:///android_asset/gif_html.html");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            switch (selectedDrawerItem.getIdentifier()){
                case NAVMENU_PROFILE: {
                    Bundle bundle = new Bundle();
                    bundle.putInt("userID", getIntent().getIntExtra("userID",-1));
                    Fragment userProfileFragment = new UserProfileFragment();
                    userProfileFragment.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area, userProfileFragment).commit();
                    break;
                }

                case NAVMENU_CARDS: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area, new CardsFragment()).commit();
                    break;
                }

                case NAVMENU_FILTER_SETTINGS: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area, new FilterSettingsFragment()).commit();
                    break;
                }

                case NAVMENU_FAQ: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area, new FAQFragment()).commit();
                    break;
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            loaderView.setVisibility(View.VISIBLE);
            loaderView.loadUrl("file:///android_asset/gif_html.html");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (selectedDrawerItem.getIdentifier() == NAVMENU_CARDS) filterStartMenuItem.setVisible(true);
        }
    }
}
