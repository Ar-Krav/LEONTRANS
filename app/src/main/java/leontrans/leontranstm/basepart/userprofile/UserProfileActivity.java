package leontrans.leontranstm.basepart.userprofile;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.mikepenz.materialdrawer.Drawer;
import leontrans.leontranstm.R;
import leontrans.leontranstm.utils.Constants;
import leontrans.leontranstm.utils.NavigationDrawerMain;

public class UserProfileActivity extends UserCardOwenerProfile {

    Drawer.Result mainNavigationDrawer;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainNavigationDrawer = new NavigationDrawerMain(this, toolbar, Constants.NAVMENU_PROFILE).getMainNavigationDrawer();
    }

    @Override
    public void onBackPressed(){
        if(mainNavigationDrawer.isDrawerOpen()){
            mainNavigationDrawer.closeDrawer();
        }
        else{
            super.onBackPressed();
        }
    }
}
