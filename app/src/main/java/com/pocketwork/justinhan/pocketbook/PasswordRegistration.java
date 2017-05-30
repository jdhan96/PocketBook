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

public class PasswordRegistration extends AppCompatActivity{
    @BindView(R.id.newPassEditText)
    EditText newEditText;
    @BindView(R.id.confirmPassEditText)
    EditText confirmEditText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Paper.book().read("firstTime", false)) {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
        }

        setContentView(R.layout.registration);
        ButterKnife.bind(this);

        Toolbar temp = (Toolbar) findViewById(R.id.toolbarText);
        TextView titleName = (TextView)temp.findViewById(R.id.toolbar_title);
        titleName.setText("Sign Up");
        setSupportActionBar(temp);

    }

    @OnClick(R.id.submitPassButton)
    void onClick() {
        if(checkifCompleted()) {
            if(newEditText.getText().toString().equals(confirmEditText.getText().toString())) {
                Paper.book().write("Password", newEditText.getText().toString());
                Paper.book().write("firstTime", true);
                Intent myIntent = new Intent(this, LoginActivity.class);
                startActivity(myIntent);
            } else {
                newEditText.setError("Incorrect!");
                confirmEditText.setError("Incorrect!");
            }
        }
    }

    private boolean checkifCompleted() {
        boolean check = true;
        if(newEditText.getText().length() == 0) {
            newEditText.setError("Required!");
            check = false;
        }
        if(confirmEditText.getText().length() == 0) {
            confirmEditText.setError("Required!");
            check = false;
        }
        return check;
    }
}
