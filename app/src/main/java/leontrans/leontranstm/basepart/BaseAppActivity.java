package leontrans.leontranstm.basepart;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

    private Toolbar toolbar;
    private MenuItem filterStartMenuItem;


    private Drawer.Result mainNavigationDrawer;
    private IDrawerItem selectedDrawerItem;
    private int idSelectedDrawerItem;

    private final int NAVMENU_PROFILE = 0;
    private final int NAVMENU_CARDS = 1;
    private final int NAVMENU_FILTER_SETTINGS = 2;
    private final int NAVMENU_FAQ = 3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_app);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area, new CardsFragment()).commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                        new SecondaryDrawerItem().withName("Про програму").withIcon(FontAwesome.Icon. faw_info_circle)
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
                        switch (selectedDrawerItem.getIdentifier()){
                            case NAVMENU_PROFILE: {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area, new UserProfileFragment()).commit();
                                break;
                            }

                            case NAVMENU_CARDS: {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area, new CardsFragment()).commit();
                                filterStartMenuItem.setVisible(true);
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
                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == idSelectedDrawerItem) return;

                        filterStartMenuItem.setVisible(false);

                        idSelectedDrawerItem = drawerItem.getIdentifier();
                        selectedDrawerItem = drawerItem;


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
}
