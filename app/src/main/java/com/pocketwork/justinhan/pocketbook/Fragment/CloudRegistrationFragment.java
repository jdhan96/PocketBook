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
import com.pocketwork.justinhan.pocketbook.R;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by justinhan on 5/30/17.
 */

public class CloudRegistrationFragment extends Fragment {
    @BindView(R.id.newEmailEditText)
    EditText Email;
    @BindView(R.id.newPassEditText)
    EditText newPass;
    @BindView(R.id.confirmPassEditText)
    EditText confPass;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cloud_fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mAuth = FirebaseAuth.getInstance();

    }

    @OnClick(R.id.submitPassButton)
    void onClick() {
        if(validateForm()) {
            if(newPass.getText().toString().equals(confPass.getText().toString())) {
                mAuth.createUserWithEmailAndPassword(Email.getText().toString(), newPass.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    CloudLoginFragment nextFrag= new CloudLoginFragment();

                                    getActivity().getFragmentManager().beginTransaction()
                                            .setCustomAnimations(R.animator.enter, R.animator.exit)
                                            .replace(R.id.content, nextFrag)
                                            .addToBackStack(null)
                                            .commit();
                                    Toast.makeText(getActivity(), "Created Successfully!", Toast.LENGTH_SHORT).show();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getActivity(), "Email already exists!", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
            } else {
                newPass.setError("Incorrect");
                confPass.setError("Incorrect");
            }
        }
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = Email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Email.setError("Required");
            valid = false;
        }

        String password = newPass.getText().toString();
        if (TextUtils.isEmpty(password)) {
            newPass.setError("Required");
            valid = false;
        }

        String confirm = confPass.getText().toString();
        if (TextUtils.isEmpty(confirm)) {
            confPass.setError("Required");
            valid = false;
        }

        return valid;
    }

}
