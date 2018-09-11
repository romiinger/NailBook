package romiinger.nailbook;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
    private static  boolean isAdmin;
    private static final int RC_SIGN_IN = 123;
    private static Activity caller;
    private FirebaseUtil(){};
    private static final String TAG = "FirebaseUtil";

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
                   if(mFiebaseAuth.getCurrentUser()==null) {
                       FirebaseUtil.signIn();
                   }
                   else
                   {
                       String userId = firebaseAuth.getUid();
                       checkAdmin(userId);
                   }
                   Toast.makeText(callerActivity.getBaseContext(),"Welcome back!", Toast.LENGTH_LONG).show();

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
                          // new AuthUI.IdpConfig.FacebookBuilder().build(),
                           //new AuthUI.IdpConfig.TwitterBuilder().build())
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
   private static void checkAdmin(String uid)
   {
       FirebaseUtil.isAdmin=false;
       DatabaseReference ref=  mFirebaseDatabase.getReference().child("administrators").child(uid);
       ChildEventListener listener= new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               FirebaseUtil.isAdmin=true;
               Log.d(TAG, "you are in administrator");
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       };
       ref.addChildEventListener(listener);
   }

    public static boolean isIsAdmin() {
        return isAdmin;
    }
}
