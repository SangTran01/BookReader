package ca.mohawk.bookreader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1000;
    private final String TAG = "main";
    ArrayList<City> cities = new ArrayList<City>();

    //FirebaseAuth
    private FirebaseAuth mAuth;
    private List<AuthUI.IdpConfig> providers;
    private Toolbar myToolbar;
    Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Create Main");
        setContentView(R.layout.activity_main);
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()
        );

        btnSignIn = findViewById(R.id.btn_signin);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
//                                .setIsSmartLockEnabled(true)
                                .setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */)
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN
                );
            }
        });

        //---------------
        //Firebase Auth
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseApp.initializeApp(this);

        FirebaseFirestore firedb = FirebaseFirestore.getInstance();

//        City city = new City("Los Angeles", "CA", "USA",
//                false, 5000000L, Arrays.asList("west_coast", "sorcal"));
//
//        firedb.collection("cities")
//                .add(city)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });

        //Get data
//        firedb.collection("cities")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                City city = document.toObject(City.class);
//                                cities.add(city);
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "START");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG, "user: " + currentUser);
        if (mAuth.getCurrentUser() != null) {
            //Go to other activity
            Intent accountIntent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(accountIntent);
            finish();
        }
    }

    // When the sign-in flow is complete, you will receive the result in onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                startActivity(AccountActivity.createIntent(this, response));
                finish();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                showSnackbar(R.string.unknown_error);
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(getCurrentFocus(), errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}
