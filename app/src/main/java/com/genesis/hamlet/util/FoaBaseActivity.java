package com.genesis.hamlet.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.genesis.hamlet.R;

/**
 * The abstract base container responsible for showing and destroying {@link Fragment} and handling
 * back and up navigation using the Fragment back stack. This is based on the
 * Fragment Oriented Architecture explained here
 * http://vinsol.com/blog/2014/09/15/advocating-fragment-oriented-applications-in-android/
 */
public abstract class FoaBaseActivity extends AppCompatActivity implements
        FragmentManager.OnBackStackChangedListener {

    Fragment lastFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    public <T extends Fragment> void showFragment(Class<T> fragmentClass, Bundle bundle,
                                                  boolean addToBackStack) {

        showFragment(fragmentClass, null, bundle, false);
    }

    public <T extends Fragment> void showFragment(Class<T> fragmentClass, String variant, Bundle bundle,
            boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                fragmentClass.getSimpleName() + "__" + variant);
        if (fragment == null) {
            try {
                fragment = fragmentClass.newInstance();
                fragment.setArguments(bundle);
            } catch (InstantiationException e) {
                throw new RuntimeException("New Fragment should have been created", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("New Fragment should have been created", e);
            }
        }
        else if (!fragment.isDetached())
            return;

        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                R.anim.slide_out_left, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        if (variant != null)
        fragmentTransaction.replace(R.id.fragmentPlaceHolder, fragment,
                fragmentClass.getSimpleName() + "__" + variant);
        else
            fragmentTransaction.replace(R.id.fragmentPlaceHolder, fragment,
                    fragmentClass.getSimpleName());

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        else {
            lastFragment = fragment;
        }
        FrameLayout container = (FrameLayout) findViewById(R.id.fragmentPlaceHolder);
        if (container != null) {
            container.removeAllViews();
        }
        fragmentTransaction.commit();
    }

    public <T extends Fragment> Fragment getFragment(Class<T> fragmentClass, String variant) {
        if (variant != null)
            return getSupportFragmentManager().findFragmentByTag(
                    fragmentClass.getSimpleName() + "__" + variant);
        else
            return getSupportFragmentManager().findFragmentByTag(
                    fragmentClass.getSimpleName());
    }

    public <T extends Fragment> void showFragment(Class<T> fragmentClass) {
        showFragment(fragmentClass, null, false);
    }

    public void popFragmentBackStack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void shouldShowActionBarUpButton() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            popFragmentBackStack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackStackChanged() {
        shouldShowActionBarUpButton();
    }

    @Override
    public void onBackPressed() {
        if (lastFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(lastFragment).commit();
            lastFragment = null;
            FrameLayout container = (FrameLayout) findViewById(R.id.fragmentPlaceHolder);
            if (container != null) {
                container.removeAllViews();
            }
        }
        super.onBackPressed();
    }
}


