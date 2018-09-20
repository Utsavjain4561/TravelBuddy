package android.com.avishkar;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

import javax.security.auth.callback.Callback;

public class Main2Activity extends AppCompatActivity {

    private TextView tvSignupInvoker;
    private LinearLayout llSignup;
    private TextView tvSigninInvoker;
    private LinearLayout llSignin;
    private Button btnSignup;
    private Button btnSignin;
    private Button skip,skipL;
    private FirebaseAuth firebaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    private TextInputEditText memail,mpassword,signup_memail,signup_mpass,signup_mob;
    private static int RC_SIGN_IN = 1;
    LoginButton facebook;
    Button google;
    View login_view,signin_view;
    String red_em;
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("418717225322021");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main2);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("296397114320-jfk9dnk77n4n98tda8gkr338fj31f7i6.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        facebook=(LoginButton)findViewById(R.id.fblogin);
        google=(Button)findViewById(R.id.goologin);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        callbackManager = CallbackManager.Factory.create();
        facebook.setReadPermissions("email","public_profile");
        facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(Main2Activity.this, "facebook logged in", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "android.com.avishkar",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        tvSigninInvoker=(TextView) findViewById(R.id.tvSigninInvoker);
        tvSignupInvoker=(TextView) findViewById(R.id.tvSignupInvoker);
        login_view = (View)findViewById(R.id.llSigninContent);
        signin_view = (View)findViewById(R.id.llSignupContent);
        btnSignin=(Button) findViewById(R.id.btnSignin);
        btnSignup=(Button) findViewById(R.id.btnSignup);
        signup_memail = (TextInputEditText)signin_view.findViewById(R.id.signup_email);
        signup_mpass = (TextInputEditText)signin_view.findViewById(R.id.signup_password);
        signup_mob = (TextInputEditText) signin_view.findViewById(R.id.mob);
        memail=(TextInputEditText)login_view.findViewById(R.id.email);
        mpassword=(TextInputEditText)login_view.findViewById(R.id.password);
        skip=(Button)findViewById(R.id.skip);
        skipL=(Button)findViewById(R.id.skipL);
        skipL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte=new Intent(Main2Activity.this,MapsActivity.class);
                startActivity(inte);
                finish();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte=new Intent(Main2Activity.this,MapsActivity.class);
                startActivity(inte);
                finish();
            }
        });
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            firebaseAuth=FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(memail.getText().toString().trim(),mpassword.getText().toString().trim()).addOnCompleteListener(Main2Activity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful())
                        Toast.makeText(Main2Activity.this, "error logging in"+task.getException(), Toast.LENGTH_LONG).show();
                    else
                    {
                        Intent intent = new Intent (Main2Activity.this,MapsActivity.class);

                        //change it at time of merging
                        Intent temp=new Intent(Main2Activity.this,Temp.class);
                        red_em=parse(memail.getText().toString().trim());
                        temp.putExtra("email",red_em);
                        startActivity(temp);
                        finish();
                    }
                }
            });
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(signup_memail.getText().toString().trim()))
                {
                    Toast.makeText(Main2Activity.this,"Enter email address",Toast.LENGTH_LONG).show();
                    return ;
                }
                if (TextUtils.isEmpty(signup_mpass.getText().toString().trim()))
                {
                    Toast.makeText(Main2Activity.this,"Enter email password",Toast.LENGTH_LONG).show();
                    return ;
                }
                if (signup_mpass.getText().toString().trim().length()<6)
                {
                    Toast.makeText(Main2Activity.this,"Password too short",Toast.LENGTH_LONG).show();
                    return ;
                }
                if (TextUtils.isEmpty(signup_mob.getText().toString().trim()))
                {
                    Toast.makeText(Main2Activity.this,"Enter mobile number",Toast.LENGTH_LONG).show();
                    return ;
                }
                firebaseAuth=FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(signup_memail.getText().toString().trim(),signup_mpass.getText().toString()).addOnCompleteListener(Main2Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful())
                            Snackbar.make(getCurrentFocus(),"Authentication failed",Snackbar.LENGTH_LONG).show();
                        else
                        {
                            Snackbar.make(getCurrentFocus(), "User Registered", Snackbar.LENGTH_SHORT).show();
                            UserLogin user=new UserLogin(signup_memail.getText().toString(),signup_mob.getText().toString(),0,0,0);
                            red_em=parse(signup_memail.getText().toString());
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference myRef=database.getReference();
                            myRef.child("users").child(red_em).child("profile").setValue(user);
                        }
                    }
                });
            }
        });
        llSignin=(LinearLayout) findViewById(R.id.llSignin);
        llSignup=(LinearLayout) findViewById(R.id.llSignup);

        tvSigninInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSigninForm();
            }
        });

        tvSignupInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignupForm();
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Intent intent = new Intent(Main2Activity.this,MapsActivity.class);
//        startActivity(intent);
//        finish();
//    }

    public  void sing(){
        Toast.makeText(this,"oops this  sing up", Toast.LENGTH_LONG).show();
    }
    private void showSignupForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.15f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.85f;
        llSignup.requestLayout();

        tvSignupInvoker.setVisibility(View.GONE);
        tvSigninInvoker.setVisibility(View.VISIBLE);
        Animation translate= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_right_to_left);
        llSignup.startAnimation(translate);

        Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_right_to_left);
        btnSignup.startAnimation(clockwise);

    }

    private void showSigninForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.  widthPercent = 0.85f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.15f;
        llSignup.requestLayout();

        Animation translate= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_left_to_right);
        llSignin.startAnimation(translate);

        tvSignupInvoker.setVisibility(View.VISIBLE);
        tvSigninInvoker.setVisibility(View.GONE);
        Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_left_to_right);
        btnSignin.startAnimation(clockwise);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
           // Log.e("activity",resultCode+"");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("failed", "Google sign in failed", e);
                // ...
            }
        }
        else
        {
         Log.e("fb",resultCode+"");
         if (resultCode==0)
         {
             Snackbar.make(getCurrentFocus(),"Try Again Later",Snackbar.LENGTH_LONG).show();
         }
            else {
             Intent intent= new Intent(Main2Activity.this,MapsActivity.class);
             startActivity(intent);
             finish();
         }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("mailed", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Snackbar.make(getCurrentFocus(),"done",Snackbar.LENGTH_LONG).show();
                            Intent intent = new Intent(Main2Activity.this,MapsActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("failed", "signInWithCredential:failure", task.getException());
                            Snackbar.make(getCurrentFocus(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    public String parse(String email)
    {
        String red_email="";
        for (int i = 0 ;i<email.length();i++)
        {
            if ((email.charAt(i)>='A'&&email.charAt(i)<='Z')||(email.charAt(i)>='a'&&email.charAt(i)<='z')||(email.charAt(i)>='0'&&email.charAt(i)<='9'))
                red_email+=email.charAt(i);
        }
        return red_email;
    }
}
