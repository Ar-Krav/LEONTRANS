package leontrans.leontranstm.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toolbar;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import leontrans.leontranstm.R;
import leontrans.leontranstm.basepart.CardsActivity;
import leontrans.leontranstm.basepart.FAQActivity;
import leontrans.leontranstm.basepart.FilterSettingsActivity;
import leontrans.leontranstm.basepart.UserProfileActivity;

import static android.content.Context.MODE_PRIVATE;
import static leontrans.leontranstm.utils.Constants.NAVMENU_ADMIN;
import static leontrans.leontranstm.utils.Constants.NAVMENU_CARDS;
import static leontrans.leontranstm.utils.Constants.NAVMENU_FAQ;
import static leontrans.leontranstm.utils.Constants.NAVMENU_FILTER_SETTINGS;
import static leontrans.leontranstm.utils.Constants.NAVMENU_PROFILE;

/**
 * Created by Ar-Krav on 02.11.2017.
 */

public class NavigationDrawerMain {

    Activity activity;
    android.support.v7.widget.Toolbar toolbar;
    private IDrawerItem selectedDrawerItem;
    private int idSelectedDrawerItem;

    public NavigationDrawerMain(Activity activity, android.support.v7.widget.Toolbar toolbar, int idSelectedDrawerItem) {
        this.activity = activity;
        this.toolbar = toolbar;
        this.idSelectedDrawerItem = idSelectedDrawerItem;
    }

    public Drawer.Result getMainNavigationDrawer(){
        return new Drawer()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_main_header)
                .withHeaderDivider(true)
                .withSelectedItem(idSelectedDrawerItem)
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
                        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

                        selectedDrawerItem = null;
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        if (selectedDrawerItem != null) new StartActivityInAsync().execute();
                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                        if (drawerItem.getIdentifier() == NAVMENU_ADMIN) {
                            SharedPreferences sharedPreferences = activity.getSharedPreferences("hashPassword", MODE_PRIVATE);
                            sharedPreferences.edit().clear().commit();
                        } //TODO admin exit button. Developing part only!

                        if (drawerItem.getIdentifier() == idSelectedDrawerItem) return;

                        idSelectedDrawerItem = drawerItem.getIdentifier();
                        selectedDrawerItem = drawerItem;
                    }
                })
                .build();
    }

    private class StartActivityInAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("TEST_TAG_LOG","userID " + selectedDrawerItem.getIdentifier());

            switch (selectedDrawerItem.getIdentifier()){
                case NAVMENU_PROFILE: {
                    Log.d("TEST_TAG_LOG","userID " + 101);

                    Intent intent = new Intent(activity, UserProfileActivity.class);

                    /*SharedPreferences userPasswordSharedPreferences = activity.getSharedPreferences("hashPassword", MODE_PRIVATE);
                    String userPassword = userPasswordSharedPreferences.getString("userPassword","");
                    int userID = new SiteDataParseUtils().getUserIdByHashpassword("https://leon-trans.com/api/ver1/login.php?action=get_hash_id&hash=" + userPassword);*/

                    Log.d("TEST_TAG_LOG","userID " + 101);

                    intent.putExtra("userID", 101);
                    activity.startActivity(intent);
                    break;
                }

                case NAVMENU_CARDS: {
                    activity.startActivity(new Intent(activity, CardsActivity.class));
                    Log.d("TEST_TAG_LOG","second ");
                    break;
                }

                case NAVMENU_FILTER_SETTINGS: {
                    activity.startActivity(new Intent(activity, FilterSettingsActivity.class));
                    Log.d("TEST_TAG_LOG","third ");
                    break;
                }

                case NAVMENU_FAQ: {
                    activity.startActivity(new Intent(activity, FAQActivity.class));
                    Log.d("TEST_TAG_LOG","four ");
                    break;
                }
            }

            return null;
        }
    }
}
