package leontrans.leontranstm.basepart.filters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.mikepenz.materialdrawer.Drawer;

import leontrans.leontranstm.R;
import leontrans.leontranstm.basepart.cardpart.CardsActivity;
import leontrans.leontranstm.utils.Constants;
import leontrans.leontranstm.utils.NavigationDrawerMain;

public class FilterSettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Drawer.Result mainNavigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_settings);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainNavigationDrawer = new NavigationDrawerMain(this, toolbar, Constants.NAVMENU_FILTER_SETTINGS).getMainNavigationDrawer();

        ((Button) findViewById(R.id.filter_1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterSettingsActivity.this, FilterEditActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onBackPressed(){
        if(mainNavigationDrawer.isDrawerOpen()){
            mainNavigationDrawer.closeDrawer();
        }
        else{
            startActivity(new Intent(FilterSettingsActivity.this, CardsActivity.class));
        }
    }
}
