package leontrans.leontranstm.basepart.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.concurrent.TimeUnit;

import leontrans.leontranstm.R;
import leontrans.leontranstm.utils.SiteDataListener;

public class UserProfileFragment extends Fragment {

    private int userID = 101; //TODO change this parametr to Variable sending from external part of program.

    View fragmentLayout;
    WebView loaderGIF;
    ScrollView mainLayoutScrolView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentLayout = inflater.inflate(R.layout.fragment_user_profile, container, false);

        loaderGIF = (WebView) fragmentLayout.findViewById(R.id.loaderGIF);
        loaderGIF.setBackgroundColor(Color.TRANSPARENT);
        loaderGIF.loadUrl("file:///android_asset/gif_html.html");

        mainLayoutScrolView = (ScrollView) fragmentLayout.findViewById(R.id.InfoScrollView);
        mainLayoutScrolView.setVisibility(View.GONE);

        new LoadFragmentData("https://leon-trans.com/api/ver1/login.php?action=get_user&id=" + userID).execute();

        return fragmentLayout;
    }

    class LoadFragmentData extends SiteDataListener{

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
                TV_name_value.setText(dataJson.getString("full_name"));
                TV_city_value.setText(dataJson.getString("location_city"));
                TV_email_value.setText(dataJson.getString("email"));
                TV_telephone_value.setText(dataJson.getString("phones"));
                TV_skype_value.setText(dataJson.getString("skype"));
                TV_icq_value.setText(dataJson.getString("icq"));
                TV_website_value.setText(dataJson.getString("website"));
                TV_occupation_value.setText(dataJson.getString("metier_type"));
                TV_occupation_type_value.setText(dataJson.getString("activity_kind"));
                TV_occupation_description_value.setText(dataJson.getString("activity_desc"));
                TV_register_date_value.setText(dataJson.getString("date_registry"));
                TV_last_online_value.setText(dataJson.getString("date_last_action"));

                loaderGIF.setVisibility(View.GONE);
                mainLayoutScrolView.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
