package leontrans.leontranstm.basepart.filters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import leontrans.leontranstm.R;

public class FilterSwitcherDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_switcher_dialog);

        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FilterSwitcherDialogActivity.this, "some txt", Toast.LENGTH_SHORT).show();
                FilterSwitcherDialogActivity.this.finish();
            }
        });
    }
}
