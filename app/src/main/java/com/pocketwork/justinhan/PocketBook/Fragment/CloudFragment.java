package com.pocketwork.justinhan.PocketBook.Fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pocketwork.justinhan.PocketBook.Adapter.CloudRecyclerAdapter;
import com.pocketwork.justinhan.PocketBook.Data.Cloud;
import com.pocketwork.justinhan.PocketBook.Data.CreditCard;
import com.pocketwork.justinhan.PocketBook.Data.Login;
import com.pocketwork.justinhan.PocketBook.Data.Note;
import com.pocketwork.justinhan.PocketBook.Helper.ItemTouchHelperCloud;
import com.pocketwork.justinhan.PocketBook.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

/**
 * Created by justinhan on 5/30/17.
 */

public class CloudFragment extends Fragment {
    @BindView(R.id.cloudList)
    RecyclerView cloudList;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDB;
    private FragmentInterface listener;
    private CloudRecyclerAdapter adapter;
    private List<Cloud> backUps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cloud_fragment_list, container, false);
    }

    @Override
    public void onDestroy() {
        mAuth.signOut();
        super.onDestroy();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDB = FirebaseDatabase.getInstance().getReference()
                .child("users").child(currentUser.getUid()).child("Cloud");

        mDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                backUps = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Cloud c = ds.getValue(Cloud.class);

                    backUps.add(c);
                }
                adapter = new CloudRecyclerAdapter(backUps,cloudList);
                cloudList.setAdapter(adapter);

                ItemTouchHelper.Callback callback = new ItemTouchHelperCloud(adapter);
                ItemTouchHelper helper=new ItemTouchHelper(callback);
                helper.attachToRecyclerView(cloudList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        cloudList.setHasFixedSize(true);
        cloudList.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(cloudList.getContext(), layoutManager.getOrientation());
        cloudList.addItemDecoration(mDividerItemDecoration);

    }


    @OnClick(R.id.signOutButton)
    void onClick() {
        mAuth.signOut();
        Toast.makeText(getActivity(), "Sign out successful!", Toast.LENGTH_SHORT).show();
        getActivity().getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.pop_enter, R.animator.pop_exit)
                .replace(R.id.content, new CloudLoginFragment())
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.uploadtoCloud)
    void onUpload() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.upload_cloud);
        dialog.setTitle("Upload to Cloud");

        final EditText cardName = (EditText) dialog.findViewById(R.id.EditCardName);


        Button addButton = (Button) dialog.findViewById(R.id.addButton);
        // if button is clicked, close the custom dialog
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = cardName.getText().toString();

                if(!name.equals("")) {
                    Calendar calendar = Calendar.getInstance();
                    String hours = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
                    String minutes = Integer.toString(calendar.get(Calendar.MINUTE));
                    String seconds = Integer.toString(calendar.get(Calendar.SECOND));

                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                    List<CreditCard> cards = Paper.book().read("creditCards", new ArrayList<CreditCard>());

                    List<Note> notes = Paper.book().read("Notes", new ArrayList<Note>());

                    List<Login> logins = Paper.book().read("Logins", new ArrayList<Login>());


                    Cloud upload = new Cloud(date+" " +hours+":"+minutes+":"+seconds, name, cards, notes, logins);

                    DatabaseReference id = mDB.push();
                    id.setValue(upload);
                    listener.updatePaper();
                    dialog.dismiss();
                } else {
                    Toast.makeText(v.getContext(), "Make sure you input a name for the backup!", Toast.LENGTH_SHORT).show();
                    if(TextUtils.isEmpty(name)) {
                        cardName.setError("Required");
                    }
                }
            }
        });

        Button cancelButton = (Button) dialog.findViewById(R.id.cancelDButton);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.show();
        dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try {
            listener = (FragmentInterface) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FragmentInterface");
        }

    }
}
