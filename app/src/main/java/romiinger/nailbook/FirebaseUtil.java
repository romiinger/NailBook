package romiinger.nailbook;
import android.content.Intent;
import java.util.concurrent.TimeUnit;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import android.net.Uri;
import java.util.*;
import android.text.TextUtils;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.*;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.ui.auth.IdpResponse;
import android.support.v7.app.AppCompatActivity;
import static android.app.Activity.RESULT_OK;

public class FirebaseUtil extends AppCompatActivity{
    private static FirebaseDatabase mFirebaseDatabase;
    private static DatabaseReference mDatabaseReference;
    private static FirebaseAuth mFiebaseAuth;
    private static FirebaseUtil firebaseUtil;
    private static FirebaseStorage mStorage;
    private static StorageReference mStorageRef;
    private static FirebaseUser mFirebaseUser;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private static boolean isAdmin;
    private static final int RC_SIGN_IN = 123;
    private static Activity caller;
    private static MyUser mUser;
    private static boolean isSignIn=false;

    public FirebaseUtil()
    {

    }

    ;
    private static final String TAG = "FirebaseUtil";

    public static void openFbReference(String ref, final Activity callerActivity) {
        Log.d(TAG,"in openFbReference()");
        if (firebaseUtil == null) {
            Log.d(TAG,"new instance from firebase");
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFiebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (mFiebaseAuth.getCurrentUser() == null) {
                        Log.d(TAG,"Before signIn()");
                        FirebaseUtil.signIn();
                    } else {
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                    Toast.makeText(callerActivity.getBaseContext(), "Welcome back!", Toast.LENGTH_LONG).show();

                }
            };
        }
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
       // getUserProfile();

    }

    private static void signIn() {
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
        Log.d(TAG,"SignIn sucess!!");
        Log.d(TAG,"before get user profile");


    }

    public static  void logOut()
    {
        AuthUI.getInstance()
                .signOut(caller)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User logged out");
                        FirebaseUtil.attachListener();
                    }
                });
        FirebaseUtil.detachListener();

    }
    public static void attachListener() {
        mFiebaseAuth.addAuthStateListener(mAuthListener);
    }

    public static void detachListener() {
        mFiebaseAuth.removeAuthStateListener(mAuthListener);
    }
    public static boolean getUserProfile()
    {
        Log.d(TAG,"in getUserProfile()");

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG,"mFirebaseUser="+ mFirebaseUser);
        Log.d(TAG,"mFirebaseUser.getUid()" + mFirebaseUser.getUid());
        if(mUser==null || mUser.getStId()==null)
        {
            mUser= new MyUser(mFirebaseUser.getUid());
            DatabaseReference ref = mDatabaseReference.push();
             ref.setValue(mUser.getStId());
           return false;
       }
       return true;
    }
    public static void setUserProfile()
    {
        Log.d(TAG,"in setUserProfile()");
        Log.d(TAG,"mUser.getStId()= "+mUser.getStId());
        mDatabaseReference.child(mUser.getStId()).setValue(mUser);
    }
    private static void checkAdmin(String uid) {
        FirebaseUtil.isAdmin = false;
        DatabaseReference ref = mFirebaseDatabase.getReference().child("administrators").child(uid);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtil.isAdmin = true;
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

    private static void connectStorage(String folderName)
    {
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference().child(folderName);
    }
    public static boolean isIsAdmin() {
        return isAdmin;
    }

    public static DatabaseReference getmDatabaseReference() {
        return mDatabaseReference;
    }


}
