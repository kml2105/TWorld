package com.semonics.tworld.Accounts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.semonics.tworld.Main_Menu.MainMenuActivity;
import com.semonics.tworld.R;
import com.semonics.tworld.SimpleClasses.Utils;
import com.semonics.tworld.WebService.BaseAPIService;
import com.semonics.tworld.WebService.RequestParams;
import com.semonics.tworld.WebService.ResponseListener;
import com.semonics.tworld.WebService.SessionManager;
import com.semonics.tworld.WebService.TWorld;

import org.json.JSONObject;

import static com.semonics.tworld.SimpleClasses.Utils.getDeviceId;
import static com.semonics.tworld.SimpleClasses.Utils.methodToast;
import static com.semonics.tworld.SimpleClasses.Utils.showLog;
import static com.semonics.tworld.WebService.SessionManager.PREF_IS_LOGIN;
import static com.semonics.tworld.WebService.SessionManager.PREF_TOKEN;
import static com.semonics.tworld.WebService.SessionManager.PREF_USER_ID;
import static com.semonics.tworld.WebService.WSParams.METHOD_POST;
import static com.semonics.tworld.WebService.WSParams.SERVICE_AUTHENTICATE;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_CODE;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_MSG;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_OBJ;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_TOKEN;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    TextView tvSignUp, tvForgotPw;
    private SessionManager sessionManager;
    EditText etUserName, etPassword;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    GoogleSignInClient mGoogleSignInClient;
    //a constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;
    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.activity_log_in_btn_log_in);
        tvSignUp = findViewById(R.id.tv_sign_up);
        etUserName = findViewById(R.id.activity_log_in_et_user_name);
        etPassword = findViewById(R.id.activity_log_in_et_password);
        tvForgotPw = findViewById(R.id.tv_forgot_pw);
        sessionManager = TWorld.getInstance().getSession();
        tvSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvForgotPw.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        deviceId = getDeviceId(this);
        showLog("id:", deviceId);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        FacebookSdk.sdkInitialize(this);
//        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void validation() {
        if (etUserName.getText().toString().isEmpty()) {
            methodToast(LoginActivity.this, "Please enter user name.");
        } else if (etPassword.getText().toString().isEmpty()) {
            methodToast(LoginActivity.this, "Please enter password.");
        } else {
            apiCall();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(LoginActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


    //this method is called on click
    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_log_in_btn_log_in:
                validation();
                break;

            case R.id.tv_forgot_pw:
                Intent intent1 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
                break;

            case R.id.tv_sign_up:
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                if (getIntent().getExtras() != null) {
                    intent.putExtras(getIntent().getExtras());
                    setIntent(null);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();
                break;
        }
    }


    public void apiCall() {
        try {
            new BaseAPIService(this, SERVICE_AUTHENTICATE, RequestParams.getLogin(etUserName.getText().toString().trim(), etPassword.getText().toString().trim()), false, responseListener, METHOD_POST, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                int code = jsonObject.getInt(WS_KEY_CODE);
                String msg = jsonObject.getString(WS_KEY_MSG);
                if (code == 200) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject(WS_KEY_OBJ);
                    String token = jsonObject1.getString(WS_KEY_TOKEN);
                    Log.e("token:", token);
                    sessionManager.putString(PREF_TOKEN, token);
                    sessionManager.setBoolean(PREF_IS_LOGIN, true);
                    sessionManager.putString(PREF_USER_ID, jsonObject1.getString("_id"));
                    Intent i = new Intent(LoginActivity.this, MainMenuActivity.class);
                    if (getIntent().getExtras() != null) {
                        i.putExtras(getIntent().getExtras());
                        setIntent(null);
                    }
                    startActivity(i);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    finish();
                } else {
                    methodToast(LoginActivity.this, msg);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            Utils.methodToast(LoginActivity.this, error);
        }
    };

}