package leontrans.leontranstm.launching;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import leontrans.leontranstm.R;
import leontrans.leontranstm.utils.SiteDataParseUtils;

import static android.content.Context.MODE_PRIVATE;

public class SignInFragment extends Fragment {

    SiteDataParseUtils siteDtataUstils;

    EditText loginInputField;
    EditText passwordInputField;
    Button signinBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        loginInputField = (EditText) view.findViewById(R.id.login_input_field);
        passwordInputField = (EditText) view.findViewById(R.id.password_input_field);

        signinBtn = (Button) view.findViewById(R.id.signin_btn);
        signinBtn.setOnClickListener(getSigninBtnClickListener());

        siteDtataUstils = new SiteDataParseUtils();

        return view;
    }

    private View.OnClickListener getSigninBtnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginInputField.getText().toString();
                String password = passwordInputField.getText().toString();

                String requestResult = siteDtataUstils.getUserHashPassword("https://leon-trans.com/api/ver1/login.php?action=login&login=" + login + "&password=" + password);

                if (requestResult != null){
                    SharedPreferences signinSharedPrefernences = getContext().getSharedPreferences("hashPassword", MODE_PRIVATE);
                    signinSharedPrefernences.edit().putString("userPassword", requestResult).commit();
                    startActivity(new Intent(getContext(), LauncherActivity.class));
                }
                else{
                    Toast.makeText(getContext(), "Login or Password is uncorrect", Toast.LENGTH_SHORT).show();
                    loginInputField.setText("");
                    passwordInputField.setText("");
                }
            }
        };
    }

}
