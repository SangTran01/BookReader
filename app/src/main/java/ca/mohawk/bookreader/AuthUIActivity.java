package ca.mohawk.bookreader;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AuthUIActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1000;
    private final String TAG = "AUTHUI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_ui);
    }
}
