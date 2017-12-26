package leontrans.leontranstm.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import leontrans.leontranstm.R;
import leontrans.leontranstm.basepart.cardpart.CardsActivity;
import leontrans.leontranstm.basepart.favouritespart.FavouriteCardsActivity;
import leontrans.leontranstm.basepart.filters.FilterSettingsActivity;
import leontrans.leontranstm.basepart.userprofile.UserProfileActivity;

import static android.content.Context.MODE_PRIVATE;
import static leontrans.leontranstm.utils.Constants.NAVMENU_ADMIN;
import static leontrans.leontranstm.utils.Constants.NAVMENU_CARDS;
import static leontrans.leontranstm.utils.Constants.NAVMENU_FAQ;
import static leontrans.leontranstm.utils.Constants.NAVMENU_FILTER_SETTINGS;
import static leontrans.leontranstm.utils.Constants.NAVMENU_PROFILE;

public class NavigationDrawerMain {

    Activity activity;
    android.support.v7.widget.Toolbar toolbar;
    private IDrawerItem selectedDrawerItem;
    private int idSelectedDrawerItem;

    private ProgressBar loaderSpinner;

    public NavigationDrawerMain(Activity activity, android.support.v7.widget.Toolbar toolbar, int idSelectedDrawerItem) {
        this.activity = activity;
        this.toolbar = toolbar;
        this.idSelectedDrawerItem = idSelectedDrawerItem;
        loaderSpinner = activity.findViewById(R.id.loading_spinner);
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
                        new PrimaryDrawerItem().withName(R.string.my_profile).withIcon(FontAwesome.Icon.faw_user).withIdentifier(NAVMENU_PROFILE),
                        new PrimaryDrawerItem().withName(R.string.advertisement_list).withIcon(FontAwesome.Icon.faw_list_alt).withIdentifier(NAVMENU_CARDS),
                        new PrimaryDrawerItem().withName(R.string.filter_settings).withIcon(FontAwesome.Icon.faw_cogs).withIdentifier(NAVMENU_FILTER_SETTINGS),
                        new PrimaryDrawerItem().withName(R.string.favourite_cards_activity).withIcon(FontAwesome.Icon.faw_heart).withIdentifier(NAVMENU_FAQ),

                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.app_settings).withIcon(FontAwesome.Icon. faw_info_circle).withIdentifier(7),
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

                        if (drawerItem != null && drawerItem.getIdentifier() == NAVMENU_ADMIN) {
                            SharedPreferences sharedPreferences = activity.getSharedPreferences("hashPassword", MODE_PRIVATE);
                            sharedPreferences.edit().clear().commit();
                        } //TODO admin exit button. Developing part only!

                        if (drawerItem == null || drawerItem.getIdentifier() == idSelectedDrawerItem) return;

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
                    Intent intent = new Intent(activity, UserProfileActivity.class);

                    SharedPreferences userPasswordSharedPreferences = activity.getSharedPreferences("hashPassword", MODE_PRIVATE);
                    String userPassword = userPasswordSharedPreferences.getString("userPassword","");
                    int userID = new SiteDataParseUtils().getUserIdByHashpassword("https://leon-trans.com/api/ver1/login.php?action=get_hash_id&hash=" + userPassword);

                    intent.putExtra("userID", userID);
                    activity.startActivity(intent);
                    break;
                }

                case NAVMENU_CARDS: {
                    activity.startActivity(new Intent(activity, CardsActivity.class));
                    break;
                }

                case NAVMENU_FILTER_SETTINGS: {
                    activity.startActivity(new Intent(activity, FilterSettingsActivity.class));
                    break;
                }

                case NAVMENU_FAQ: {
                    activity.startActivity(new Intent(activity, FavouriteCardsActivity.class));
                    break;
                }
                case 7:{
                    activity.startActivity(new Intent(activity, LanguageDialogActivity.class));
                    break;
                }
            }

            return null;
        }
    }
}
