package com.pocketwork.justinhan.pocketbook.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pocketwork.justinhan.pocketbook.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by justinhan on 5/30/17.
 */

public class CloudLoginFragment extends Fragment {
    @BindView(R.id.emailEditText)
    EditText Email;
    @BindView(R.id.passwordEditText)
    EditText Password;

    private FirebaseAuth mAuth;
    private CloudFragment frag;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cloud_fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        frag = new CloudFragment();
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            nextPage();
        }
    }

    @OnClick(R.id.submitButton)
    void onSubmit() {
        if(validateForm()) {
            mAuth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                nextPage();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getActivity(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                resetText();
                            }

                            // ...
                        }
                    });
        }
    }

    @OnClick(R.id.signUpButton)
    void onSignUp() {
        getActivity().getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.pop_enter, R.animator.pop_exit)
                .replace(R.id.content, new CloudRegistrationFragment())
                .addToBackStack(null)
                .commit();
    }

    public void nextPage() {
        getActivity().getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.enter, R.animator.exit)
                .replace(R.id.content, frag)
                .addToBackStack(null)
                .commit();
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = Email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Email.setError("Required");
            valid = false;
        }

        String password = Password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Password.setError("Required");
            valid = false;
        }

        return valid;
    }
    void resetText() {
        Email.setText("");
        Password.setText("");
    }
}
