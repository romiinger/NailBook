package romiinger.nailbook;
import android.content.Intent;

import java.util.concurrent.ExecutionException;
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
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.*;

import com.firebase.ui.auth.IdpResponse;
import android.support.v7.app.AppCompatActivity;
import static android.app.Activity.RESULT_OK;

public class FirebaseUtil {
    private static FirebaseDatabase mFirebaseDatabase;
    private static DatabaseReference mDatabaseReference;
    private static FirebaseAuth mFiebaseAuth;
    private static FirebaseUtil firebaseUtil;
    private static FirebaseStorage mStorage;
    private static StorageReference mStorageRef;
    private static FirebaseUser mFirebaseUser;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 123;
    private static MainActivity caller;
    private static boolean isAdmin;
    private static final String TAG = "FirebaseUtil";
    private static boolean mTaskSuccesful = false;

    public FirebaseUtil() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFiebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFiebaseAuth.getCurrentUser();
    }


    public static void openFbReference(String ref, final MainActivity callerActivity) {
        Log.d(TAG, "in openFbReference()");
        if (firebaseUtil == null) {
            Log.d(TAG, "new instance from firebase");
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFiebaseAuth = FirebaseAuth.getInstance();
            mFirebaseUser = mFiebaseAuth.getCurrentUser();
            caller = callerActivity;
            final String _message;
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (mFiebaseAuth.getCurrentUser() == null) {
                        Log.d(TAG, "Before signIn()");
                        FirebaseUtil.signIn(new FirebaseListener()
                        {
                            @Override
                            public void onComplete(String message) {
                                Log.d("TAG", "SigIn completed:" + message);
                            }
                        });
                    } else {
                        Log.d(TAG, "user is login");
                        String userId = mFiebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                    //Toast.makeText(callerActivity.getBaseContext(), "Welcome back!", Toast.LENGTH_LONG).show();
                }
            };
        }
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }
    private static void signIn(final FirebaseListener listener) {
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
        listener.onComplete("SignIn sucess");
    }


    public static void logOut() {
        isAdmin = false;
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


    public static DatabaseReference getmDatabaseReference() {
        Log.d(TAG, "mDatabaseReference=" + mDatabaseReference);
        return mDatabaseReference;
    }


    public static FirebaseUser getmFirebaseUser() {
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return mFirebaseUser;
    }

    private static void connectStorage(String folderName) {
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference().child(folderName);
    }

    private static void checkAdmin(String uid) {
        Log.d(TAG, "in checkAdmin");
        FirebaseUtil.isAdmin = false;
        DatabaseReference ref = mFirebaseDatabase.getReference().child("administrator").child(uid);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtil.isAdmin = true;
                caller.showMenu();
                Log.d("Admin:", "you are in administrator");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtil.isAdmin = true;
                caller.showMenu();
                Log.d("Admin:", "you are in administrator");
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
        Log.d(TAG, "isAdmin=" + isAdmin);
        return isAdmin;
    }



    private boolean reauthenticateUser(String oldPassword, String email)
    {
        final boolean[] isReautherntica = new boolean[]{false};
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email,oldPassword);
        mFirebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User is re-authenticated.");
                    isReautherntica[0] = true;
                }
                else
                {
                    Log.d(TAG,"User is not re-authenticated ");
                }
            }
        });
        Log.d(TAG, "isReautherntica= " + isReautherntica);
        return isReautherntica[0];

    }
    public interface FirebaseListener{
        void onComplete(String message);
    }
    public void resetPassword(final String currrentPassoword, final String newPassoword, final FirebaseListener listener) {

        String email = mFirebaseUser.getEmail();
        Log.d(TAG, "in resetPassword() ");
       if( reauthenticateUser(currrentPassoword,email))
       {
           mFirebaseUser.updatePassword(newPassoword)
                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()) {
                               Log.d("FirebaseUtil:","in onCompleteListener update password  ");
                               listener.onComplete("password is update");
                           }
                           else
                           {

                               Log.d("FirebaseUtil:","in onCompleteListener failed password  "+ task.getResult());

                               listener.onComplete("failed");
                           }
                       }
                   });
       }
       else
       {
           listener.onComplete("Incorrect Current Password");
       }

    }
}