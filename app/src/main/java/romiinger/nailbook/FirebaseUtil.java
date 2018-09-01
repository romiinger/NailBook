package romiinger.nailbook;

import android.support.annotation.NonNull;
import java.util.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil
{
    private static FirebaseDatabase mFirebaseDatabase;
    private static DatabaseReference mDatabaseReference;
    private static FirebaseAuth mFiebaseAuth;
    private static FirebaseUtil firebaseUtil;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUtil(){};

   public static void openFbReference(String user)
   {
       if (firebaseUtil == null)
       {
           firebaseUtil = new FirebaseUtil();
           mFirebaseDatabase = FirebaseDatabase.getInstance();
           mFiebaseAuth = FirebaseAuth.getInstance();
           mAuthListener = new FirebaseAuth.AuthStateListener() {
               @Override
               public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    // Choose authentication providers
                   List<AuthUI.IdpConfig> providers = Arrays.asList(
                           new AuthUI.IdpConfig.EmailBuilder().build(),
                           new AuthUI.IdpConfig.PhoneBuilder().build(),
                           new AuthUI.IdpConfig.GoogleBuilder().build(),
                           new AuthUI.IdpConfig.FacebookBuilder().build(),
                           new AuthUI.IdpConfig.TwitterBuilder().build());

// Create and launch sign-in intent
                   startActivityForResult(
                           AuthUI.getInstance()
                                   .createSignInIntentBuilder()
                                   .setAvailableProviders(providers)
                                   .build(),
                           RC_SIGN_IN);
               }
           };
       }
       mDatabaseReference = mFirebaseDatabase.getReference().child(user);
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
