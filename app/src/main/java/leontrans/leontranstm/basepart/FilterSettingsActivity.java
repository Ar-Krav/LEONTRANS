package leontrans.leontranstm.basepart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.mikepenz.materialdrawer.Drawer;

import leontrans.leontranstm.R;
import leontrans.leontranstm.utils.Constants;
import leontrans.leontranstm.utils.NavigationDrawerMain;

public class FilterSettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Drawer.Result mainNavigationDrawer;

    private ProgressBar loaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_settings);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainNavigationDrawer = new NavigationDrawerMain(this, toolbar, Constants.NAVMENU_FILTER_SETTINGS).getMainNavigationDrawer();

        loaderView = (ProgressBar) findViewById(R.id.loading_spinner);
        loaderView.setVisibility(View.GONE);
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
