package com.pocketwork.justinhan.pocketbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

/**
 * Created by justinhan on 5/29/17.
 */

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.password)
    EditText Password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
        ButterKnife.bind(this);

        Toolbar temp = (Toolbar) findViewById(R.id.toolbarText);
        TextView titleName = (TextView)temp.findViewById(R.id.toolbar_title);
        titleName.setText("Login");
        setSupportActionBar(temp);
    }
    @OnClick(R.id.loginSubmit)
    void onClick() {
        if(Password.getText().toString().equals(Paper.book().read("Password"))) {

            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
        } else {
            Password.setError("Incorrect!");
        }
    }
}
