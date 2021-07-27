package com.swift.dating.ui.createAccountScreen;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;

import com.swift.dating.R;
import com.swift.dating.data.preference.SharedPreference;
import com.swift.dating.model.responsemodel.ProfileOfUser;
import com.swift.dating.ui.addImageScreen.AddImagesFragment;
import com.swift.dating.ui.base.BaseActivity;
import com.swift.dating.ui.createAccountScreen.fragments.AboutMeFragment;
import com.swift.dating.ui.createAccountScreen.fragments.BirthdayFragment;
import com.swift.dating.ui.createAccountScreen.fragments.CityFragment;
import com.swift.dating.ui.createAccountScreen.fragments.DrinkFragment;
import com.swift.dating.ui.createAccountScreen.fragments.EducationFragment;
import com.swift.dating.ui.createAccountScreen.fragments.ExerciseFragment;
import com.swift.dating.ui.createAccountScreen.fragments.GenderFragment;
import com.swift.dating.ui.createAccountScreen.fragments.HeightFragment;
import com.swift.dating.ui.createAccountScreen.fragments.InterestAmbitionFragment;
import com.swift.dating.ui.createAccountScreen.fragments.JobFragment;
import com.swift.dating.ui.createAccountScreen.fragments.KidsFragment;
import com.swift.dating.ui.createAccountScreen.fragments.LocationFragment;
import com.swift.dating.ui.createAccountScreen.fragments.LookingForFragment;
import com.swift.dating.ui.createAccountScreen.fragments.NameFragment;
import com.swift.dating.ui.createAccountScreen.fragments.PetFragment;
import com.swift.dating.ui.createAccountScreen.fragments.PoliticsFragment;
import com.swift.dating.ui.createAccountScreen.fragments.ReligionFragment;
import com.swift.dating.ui.createAccountScreen.fragments.SchoolFragment;
import com.swift.dating.ui.createAccountScreen.fragments.ShowMeFragment;
import com.swift.dating.ui.createAccountScreen.fragments.SignFragment;
import com.swift.dating.ui.createAccountScreen.fragments.SmokeFragment;
import com.swift.dating.ui.selfieScreen.SelfieActivity;


public class CreateAccountActivity extends BaseActivity implements View.OnClickListener {

    public boolean isEdit = false;
    public String btn_text;
    public SharedPreference preference;
    Fragment fragment = null;
    private TextView tvSkip;
    private int parseCount = 1;
    public static String location = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
//        getData();
        init();
        addFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver), new IntentFilter("MyData"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * ** method to get Data from Intent
     */
    private void getData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            btn_text = extras.getBoolean("FromEdit", false) ? "Done" : "Continue";
            parseCount = extras.getInt("parseCount", 1);
            if (extras.containsKey("isEdit"))
                isEdit = extras.getBoolean("isEdit", false);
        }
    }

    /**
     * ** method to initialize the view
     */
    private void init() {
        preference = new SharedPreference(this);
        ImageView ivback = findViewById(R.id.ivback);
        tvSkip = findViewById(R.id.tvskip);
        ivback.setOnClickListener(this);
        tvSkip.setOnClickListener(this);

    }


    /**
     * ** method to fetch userData from Preference
     */
    public ProfileOfUser getUserData() {
        Gson gson = new Gson();
        String json = sp.getUser();
        ProfileOfUser obj = gson.fromJson(json, ProfileOfUser.class);
        return obj;
    }

    /**
     * ** method to set Fragment according to parse count
     */
    public void addFragment() {
        switch (parseCount) {
            case 1:
                fragment = new NameFragment();
                break;
            case 2:
                fragment = new BirthdayFragment();
                break;
            case 3:
                fragment = new GenderFragment();
                break;
            case 4:
                fragment = new ShowMeFragment();
                break;
            case 5:
                fragment = new AddImagesFragment();
                break;
            case 6:
                Intent intent = new Intent(this, SelfieActivity.class);
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivity(intent);
                finishAffinity();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 7:
                fragment = new LookingForFragment();
                break;
            case 8:
                fragment = new AboutMeFragment();
                break;
            case 9:
                fragment = new InterestAmbitionFragment();
                break;
            case 10:
                fragment = new EducationFragment();
                break;
            case 11:
                fragment = new SchoolFragment();
                break;
            case 12:
                fragment = new JobFragment();
                break;
            case 13:
                fragment = new DrinkFragment();
                break;
            case 14:
                fragment = new SmokeFragment();
                break;
            case 15:
                fragment = new ExerciseFragment();
                break;
            case 16:
                fragment = new PoliticsFragment();
                break;
            case 17:
                fragment = new CityFragment();
                break;
            case 18:
                fragment = new PetFragment();
                break;
            case 19:
                fragment = new HeightFragment();
                break;
            case 20:
                fragment = new KidsFragment();
                break;
            case 21:
                fragment = new SignFragment();
                break;
            case 22:
                fragment = new ReligionFragment();
                break;
            case 23:
                fragment = new LocationFragment();
                break;
        }
        if (fragment != null) {
            replaceFragmentWithBackStack(fragment, getSupportFragmentManager(), "", parseCount > 6);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * ** method for skip questions
     */
    private void skipFragment() {
        if (parseCount == 7) {
            ((LookingForFragment) fragment).skipFragments();
        }
        if (parseCount == 8) {
            ((AboutMeFragment) fragment).skipFragments();
        }
        if (parseCount == 9) {
            ((InterestAmbitionFragment) fragment).skipFragments();
        }
        if (parseCount == 10) {
            ((EducationFragment) fragment).skipFragments();
        }
        if (parseCount == 11) {
            ((SchoolFragment) fragment).skipFragments();
        }
        if (parseCount == 12) {
            ((JobFragment) fragment).skipFragments();
        }
        if (parseCount == 13) {
            ((DrinkFragment) fragment).skipFragments();
        } else if (parseCount == 14) {
            ((SmokeFragment) fragment).skipFragments();
        } else if (parseCount == 15) {
            ((ExerciseFragment) fragment).skipFragments();
        } else if (parseCount == 16) {
            ((PoliticsFragment) fragment).skipFragments();
        } else if (parseCount == 17) {
            ((CityFragment) fragment).skipFragments();
        } else if (parseCount == 18) {
            ((PetFragment) fragment).skipFragments();
        } else if (parseCount == 19) {
            ((HeightFragment) fragment).skipFragments();
        } else if (parseCount == 20)
            ((KidsFragment) fragment).skipFragments();
        else if (parseCount == 21)
            ((SignFragment) fragment).skipFragments();
        else if (parseCount == 22) {
            ((ReligionFragment) fragment).skipFragments();
        }
    }

    /**
     * ** method for replace fragment
     */
    public void replaceFragmentWithBackStack(Fragment fragment, FragmentManager fm, String title, boolean showSkip) {
        tvSkip.setVisibility(showSkip ? View.VISIBLE : View.INVISIBLE);
        if (parseCount == 23) tvSkip.setVisibility(View.INVISIBLE);
        if (fragment.equals(new NameFragment())) {
            if (isEdit)
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.fragments, fragment).commit();
            else
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments, fragment).commit();
        } else {
            FragmentTransaction ft = fm.beginTransaction();
            if (isEdit) {
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            ft.replace(R.id.fragments, fragment);
            if (!isEdit)
                ft.addToBackStack(fragment.getClass().getName());
            ft.commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragments);
        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ivback:
                hideKeyboard();
                onBackPressed();
                break;

            case R.id.tvskip:
                hideKeyboard();
                skipFragment();
                break;
        }
    }

    /**
     * ** method to Update Parse Count according to fragment
     */
    public void updateParseCount(int parseCount) {
        this.parseCount = parseCount;
    }

    @Override
    public void onBackPressed() {
        if (isEdit) {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            if (parseCount == 1) {
                finishAffinity();
            } else if (parseCount == 3) {
                updateParseCount(23);
            } else if (parseCount == 23) {
                updateParseCount(2);
            } else {
                updateParseCount(parseCount - 1);
            }
            addFragment();
        }
    }
}
