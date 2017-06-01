package com.pocketwork.justinhan.PocketBook;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.pocketwork.justinhan.PocketBook.Fragment.CloudLoginFragment;
import com.pocketwork.justinhan.PocketBook.Fragment.CreditCardFragment;
import com.pocketwork.justinhan.PocketBook.Fragment.FragmentInterface;
import com.pocketwork.justinhan.PocketBook.Fragment.LoginFragment;
import com.pocketwork.justinhan.PocketBook.Fragment.NoteFragment;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements FragmentInterface{
    private int mSelectedItem;
    private FragmentTransaction transaction;
    private CreditCardFragment frag1;
    private NoteFragment frag2;
    private LoginFragment frag3;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            transaction = getFragmentManager().beginTransaction();

            switch (item.getItemId()) {

                case R.id.navigation_cards:
                    if(mSelectedItem != R.id.navigation_cards) {
                        transaction.setCustomAnimations(R.animator.pop_enter, R.animator.pop_exit);
                    }
                    mSelectedItem = item.getItemId();
                    transaction.replace(R.id.content, frag1).commit();
                    return true;
                case R.id.navigation_notes:
                    if(mSelectedItem == R.id.navigation_cards) {
                        transaction.setCustomAnimations(R.animator.enter, R.animator.exit);
                    } else if(mSelectedItem == R.id.navigation_logins || mSelectedItem == R.id.cloud) {
                        transaction.setCustomAnimations(R.animator.pop_enter, R.animator.pop_exit);
                    }
                    mSelectedItem = item.getItemId();
                    transaction.replace(R.id.content, frag2).commit();
                    return true;
                case R.id.navigation_logins:
                    if(mSelectedItem == R.id.navigation_cards || mSelectedItem == R.id.navigation_notes) {
                        transaction.setCustomAnimations(R.animator.enter, R.animator.exit);
                    } else if(mSelectedItem == R.id.cloud) {
                        transaction.setCustomAnimations(R.animator.pop_enter, R.animator.pop_exit);
                    }
                    mSelectedItem = item.getItemId();
                    transaction.replace(R.id.content, frag3).commit();
                    return true;
                case R.id.cloud:
                    if(mSelectedItem != R.id.cloud) {
                        transaction.setCustomAnimations(R.animator.enter, R.animator.exit);
                    }
                    mSelectedItem = item.getItemId();
                    transaction.replace(R.id.content, new CloudLoginFragment()).commit();
                    return true;
            }
            return false;
        }

    };

    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);

        Toolbar temp = (Toolbar) findViewById(R.id.toolbarText);
        setSupportActionBar(temp);

        frag1 = new CreditCardFragment();
        frag2 = new NoteFragment();
        frag3 = new LoginFragment();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mSelectedItem = R.id.navigation_cards;
        navigation.setSelectedItemId(R.id.navigation_cards);
    }

    @Override
    public void updatePaper() {
        frag1 = new CreditCardFragment();
        frag2 = new NoteFragment();
        frag3 = new LoginFragment();
    }
}
