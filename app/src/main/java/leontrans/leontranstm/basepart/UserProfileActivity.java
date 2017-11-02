package leontrans.leontranstm.basepart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import leontrans.leontranstm.R;
import leontrans.leontranstm.utils.Constants;
import leontrans.leontranstm.utils.SiteDataListener;
import leontrans.leontranstm.utils.NavigationDrawerMain;

public class UserProfileActivity extends AppCompatActivity {

    private int userID;

    private Toolbar toolbar;
    private ProgressBar loaderView;
    private ScrollView scrollView;
    private int animationDuration;
    private Drawer.Result mainNavigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainNavigationDrawer = new NavigationDrawerMain(this, toolbar, Constants.NAVMENU_PROFILE).getMainNavigationDrawer();

        loaderView = (ProgressBar) findViewById(R.id.loading_spinner);
        scrollView = (ScrollView) findViewById(R.id.infoScrollView);
        scrollView.setVisibility(View.GONE);
        animationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        userID = getIntent().getIntExtra("userID",-1);
        if (userID >= 0) new LoadFragmentData("https://leon-trans.com/api/ver1/login.php?action=get_user&id=" + userID).execute();
    }

    private class LoadFragmentData extends SiteDataListener {

        public LoadFragmentData(String urlAddress) {
            super(urlAddress);
        }

        @Override
        protected void onPostExecute(String jsonStr) {

            try {
                JSONObject dataJson = new JSONObject(jsonStr);

                TextView TV_name_value = (TextView) findViewById(R.id.TV_name_value);
                TextView TV_city_value = (TextView) findViewById(R.id.TV_city_value);
                TextView TV_email_value = (TextView) findViewById(R.id.TV_email_value);
                TextView TV_telephone_value = (TextView) findViewById(R.id.TV_telephone_value);
                TextView TV_skype_value = (TextView) findViewById(R.id.TV_skype_value);
                TextView TV_icq_value = (TextView) findViewById(R.id.TV_icq_value);
                TextView TV_website_value = (TextView) findViewById(R.id.TV_website_value);
                TextView TV_occupation_value = (TextView) findViewById(R.id.TV_occupation_value);
                TextView TV_occupation_type_value = (TextView) findViewById(R.id.TV_occupation_type_value);
                TextView TV_occupation_description_value = (TextView) findViewById(R.id.TV_occupation_description_value);
                TextView TV_register_date_value = (TextView) findViewById(R.id.TV_register_date_value);
                TextView TV_last_online_value = (TextView) findViewById(R.id.TV_last_online_value);
                ImageView userAvatarImageView = (ImageView) findViewById(R.id.userAvatarImageView);


                Picasso.with(UserProfileActivity.this)
                        .load("https://leon-trans.com/uploads/user-avatars/" + dataJson.getString("avatar"))
                        .error(R.drawable.default_avatar)
                        .into(userAvatarImageView);

                if (dataJson.getString("person_type").equals("individual")){
                    TV_name_value.setText(dataJson.getString("full_name"));
                }
                else {
                    TV_name_value.setText(dataJson.getString("nomination_prefix") + dataJson.getString("nomination_name"));
                }

                TV_city_value.setText(dataJson.getString("location_city"));
                TV_email_value.setText(dataJson.getString("email"));
                TV_telephone_value.setText(dataJson.getString("phones"));
                TV_skype_value.setText(dataJson.getString("skype"));
                TV_icq_value.setText(dataJson.getString("icq"));
                TV_website_value.setText(dataJson.getString("website"));
                TV_occupation_value.setText(dataJson.getString("metier_type"));
                TV_occupation_type_value.setText(dataJson.getString("activity_kind"));
                TV_occupation_description_value.setText(dataJson.getString("activity_desc"));
                TV_register_date_value.setText(makeDate(dataJson.getString("date_registry")));
                TV_last_online_value.setText(makeDate(dataJson.getString("date_last_action")));

                crossfade();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String makeDate(String date){
        long dv;
        Date df;
        String dateFrom;
        dv = Long.valueOf(date) * 1000;
        df = new java.util.Date(dv);
        dateFrom = new SimpleDateFormat("MM.dd.yyyy").format(df);
        return dateFrom;
    }

    private void crossfade() {
        scrollView.setAlpha(0f);
        scrollView.setVisibility(View.VISIBLE);

        scrollView.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null);

        loaderView.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loaderView.setVisibility(View.GONE);
                    }
                });
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
