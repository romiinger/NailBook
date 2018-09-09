package romiinger.nailbook;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.auth.AuthUI;

public class FirebaseUtil
{
    private static FirebaseDatabase mFirebaseDatabase;
    private static DatabaseReference mDatabaseReference;
    private static FirebaseAuth mFiebaseAuth;
    private static FirebaseUtil firebaseUtil;
    private static FirebaseAuth.AuthStateListener mAuthListener;

    private static final int RC_SIGN_IN = 123;
    private static Activity caller;
    private FirebaseUtil(){};

   public static void openFbReference(String ref , final Activity callerActivity)
   {
       if (firebaseUtil == null)
       {
           firebaseUtil = new FirebaseUtil();
           mFirebaseDatabase = FirebaseDatabase.getInstance();
           mFiebaseAuth = FirebaseAuth.getInstance();
           caller = callerActivity;
           mAuthListener = new FirebaseAuth.AuthStateListener()
           {
               @Override
               public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUtil.signIn();
                  /* Toast.makeText(callerActivity.getBaseContext(),"Welcome back!", Toast.LENGTH_LONG.show());*/
               }
           };
       }
       mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
   }

   private static void signIn()
   {
       List<AuthUI.IdpConfig> providers = Arrays.asList(
               new AuthUI.IdpConfig.EmailBuilder().build(),
               new AuthUI.IdpConfig.GoogleBuilder().build());
                          /* new AuthUI.IdpConfig.FacebookBuilder().build(),
                           new AuthUI.IdpConfig.TwitterBuilder().build())*/;
       caller.startActivityForResult(
               AuthUI.getInstance()
                       .createSignInIntentBuilder()
                       .setAvailableProviders(providers)
                       .build(),
               RC_SIGN_IN);
   }
   public static  void attachListener()
   {
       mFiebaseAuth.addAuthStateListener(mAuthListener);
   }
   public static void detachListener()
   {
       mFiebaseAuth.removeAuthStateListener(mAuthListener);
   }
}
