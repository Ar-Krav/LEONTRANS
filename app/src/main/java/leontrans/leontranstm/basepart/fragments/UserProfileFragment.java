package leontrans.leontranstm.basepart.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import leontrans.leontranstm.R;
import leontrans.leontranstm.utils.SiteDataListener;

public class UserProfileFragment extends Fragment {

    private int userID;

    private View fragmentLayout;
    private ScrollView mainLayoutScrolView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentLayout = inflater.inflate(R.layout.fragment_user_profile, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            userID = bundle.getInt("userID");
            Log.d("USER_TEST_LOG","userId in Fragment: " + userID);
        }

        mainLayoutScrolView = (ScrollView) fragmentLayout.findViewById(R.id.InfoScrollView);
        mainLayoutScrolView.setVisibility(View.GONE);

        if (userID > 0) new LoadFragmentData("https://leon-trans.com/api/ver1/login.php?action=get_user&id=" + userID).execute();
        else{
            fragmentLayout = inflater.inflate(R.layout.error_layout, container, false);
        }

        return fragmentLayout;
    }

    private class LoadFragmentData extends SiteDataListener{

        public LoadFragmentData(String urlAddress) {
            super(urlAddress);
        }

        @Override
        protected void onPostExecute(String jsonStr) {

            try {
                JSONObject dataJson = new JSONObject(jsonStr);

                TextView TV_name_value = (TextView) fragmentLayout.findViewById(R.id.TV_name_value);
                TextView TV_city_value = (TextView) fragmentLayout.findViewById(R.id.TV_city_value);
                TextView TV_email_value = (TextView) fragmentLayout.findViewById(R.id.TV_email_value);
                TextView TV_telephone_value = (TextView) fragmentLayout.findViewById(R.id.TV_telephone_value);
                TextView TV_skype_value = (TextView) fragmentLayout.findViewById(R.id.TV_skype_value);
                TextView TV_icq_value = (TextView) fragmentLayout.findViewById(R.id.TV_icq_value);
                TextView TV_website_value = (TextView) fragmentLayout.findViewById(R.id.TV_website_value);
                TextView TV_occupation_value = (TextView) fragmentLayout.findViewById(R.id.TV_occupation_value);
                TextView TV_occupation_type_value = (TextView) fragmentLayout.findViewById(R.id.TV_occupation_type_value);
                TextView TV_occupation_description_value = (TextView) fragmentLayout.findViewById(R.id.TV_occupation_description_value);
                TextView TV_register_date_value = (TextView) fragmentLayout.findViewById(R.id.TV_register_date_value);
                TextView TV_last_online_value = (TextView) fragmentLayout.findViewById(R.id.TV_last_online_value);
                ImageView userAvatarImageView = (ImageView) fragmentLayout.findViewById(R.id.userAvatarImageView);


                Picasso.with(getContext())
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

                ((WebView) getActivity().findViewById(R.id.loaderView)).setVisibility(View.GONE);
                mainLayoutScrolView.setVisibility(View.VISIBLE);

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
}
