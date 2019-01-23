package com.example.andrioid.pracainzynierska.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrioid.pracainzynierska.R;
import com.example.andrioid.pracainzynierska.viewmodel.MainViewModel;
import com.example.andrioid.pracainzynierska.viewmodel.MainViewModelFactory;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import javax.inject.Inject;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class MainActivity extends BaseActivity implements DuoMenuView.OnMenuClickListener {
    private MenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;

    @Inject
    MainFragment mainFragment;

    @Inject
    AboutFragment aboutFragment;

    @Inject
    SettingsFragment settingsFragment;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 1;

    MainViewModel viewMode;

    @Inject
    MainViewModelFactory mainViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActivityComponent().inject(this);

        viewMode = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel.class);

        // Initialize the views
        mViewHolder = new ViewHolder();

        makeAuthFirebase();

        // Handle toolbar actions
        handleToolbar();
        // Handle menu actions
        handleMenu();
        // Handle drawer actions
        handleDrawer();


        // Show main fragment in container
        mMenuAdapter.setViewSelected(0, true);
        setTitle(mainViewModelFactory.getMainRepository().getMenuTitles().get(0));
    }

    void makeAuthFirebase() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    viewMode.addUser();
                    makeHeaderView();
                    goToFragment(mainFragment, false);
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setLogo(R.drawable.watlogo)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Check which request we're responding to
//        if (requestCode == RC_SIGN_IN) {
////            setupViewsFromDataFireBase();
//        }
//    }

    private void handleToolbar() {
        setSupportActionBar(mViewHolder.mToolbar);
    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mViewHolder.mDuoDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();
    }

    private void handleMenu() {
        mMenuAdapter = new MenuAdapter(mainViewModelFactory.getMainRepository().getMenuTitles());

        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
    }

    public void makeHeaderView() {
        TextView headerTextTitle = mViewHolder.mDuoMenuView.getHeaderView().findViewById(R.id.duo_view_header_text_title);
        headerTextTitle.setText(viewMode.getUserDisplayName());

        TextView headeTextSubTitle = mViewHolder.mDuoMenuView.getHeaderView().findViewById(R.id.duo_view_header_text_sub_title);
        headeTextSubTitle.setText(viewMode.getUserEmail());

    }

    @Override
    public void onFooterClicked() {
        AuthUI.getInstance().signOut(this).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "You have logged out", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onHeaderClicked() {
        Intent kontoIntent = new Intent(MainActivity.this, KontoActivity.class);
        // Start the new activity
        startActivity(kontoIntent);
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    private void goToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.replace(R.id.container, fragment).commit();
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
        setTitle(mainViewModelFactory.getMainRepository().getMenuTitles().get(position));

        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);

        // Navigate to the right fragment
        switch (position) {
            case 0:
                goToFragment(mainFragment, false);
                break;
            case 1:
                goToFragment(settingsFragment, false);
                break;
            case 2:
                goToFragment(aboutFragment, false);
                break;
        }

        // Close the drawer
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null)
            viewMode.getFirebaseAuth().removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewMode.getFirebaseAuth().addAuthStateListener(mAuthStateListener);
    }

    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
        }
    }
}

