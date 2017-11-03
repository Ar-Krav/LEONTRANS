package leontrans.leontranstm.launching;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import leontrans.leontranstm.R;
import leontrans.leontranstm.basepart.cardpart.CardsActivity;
import leontrans.leontranstm.utils.SiteDataParseUtils;

public class LauncherActivity extends AppCompatActivity {

    SiteDataParseUtils siteDataUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        siteDataUtils = new SiteDataParseUtils();
        isUserAlreadySignedin();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void isUserAlreadySignedin(){
        SharedPreferences userPasswordSharedPreferences = getSharedPreferences("hashPassword", MODE_PRIVATE);
        String userPassword = userPasswordSharedPreferences.getString("userPassword","");

        if (userPassword.isEmpty()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area, new SignInFragment()).commit();
        }
        else{
            checkUserHashPassword("https://leon-trans.com/api/ver1/login.php?action=get_hash_id&hash=" + userPassword);
        }
    }

    private void checkUserHashPassword(String urlRequest){
        int userID = siteDataUtils.getUserIdByHashpassword(urlRequest);
        if (userID > 0){
            Intent intent = new Intent(LauncherActivity.this, CardsActivity.class);
            intent.putExtra("userID",userID);
            startActivity(intent);
        }
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area, new SignInFragment()).commit();
        }
    }
}
