package com.pocketwork.justinhan.pocketbook.Fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.pocketwork.justinhan.pocketbook.Adapter.CreditRecyclerAdapter;
import com.pocketwork.justinhan.pocketbook.Adapter.NoteRecyclerAdapter;
import com.pocketwork.justinhan.pocketbook.Data.CreditCard;
import com.pocketwork.justinhan.pocketbook.Data.Note;
import com.pocketwork.justinhan.pocketbook.Helper.ItemTouchHelperCallback;
import com.pocketwork.justinhan.pocketbook.Helper.MonthYearPicker;
import com.pocketwork.justinhan.pocketbook.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

/**
 * Created by justinhan on 5/10/17.
 */

public class NoteFragment extends Fragment {
    @BindView(R.id.walletRecyclerView)
    RecyclerView walletView;
    @BindView(R.id.AddNew)
    TextView addNew;

    private NoteRecyclerAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.note_fragment, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        addNew.setText("Notes");
        initData();
    }

    @OnClick(R.id.addNew)
    public void onAdd() {
        // custom dialog

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.new_note);
        dialog.setTitle("New Card");

        final EditText cardName = (EditText) dialog.findViewById(R.id.EditCardName);
        final EditText Name = (EditText) dialog.findViewById(R.id.EditCardUser);


        Button addButton = (Button) dialog.findViewById(R.id.addButton);
        // if button is clicked, close the custom dialog
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = cardName.getText().toString();
                String user = Name.getText().toString();

                if(!name.equals("") && !user.equals("")) {
                    if(adapter == null) {
                        List<Note> cards = new ArrayList<>();
                        cards.add(new Note(name, user));
                        Paper.book().write("Notes", cards);
                        initData();
                    } else {
                        adapter.addItem(new Note(name, user));
                    }
                    dialog.dismiss();
                } else {
                    Toast.makeText(v.getContext(), "Make sure all fields are entered!!!", Toast.LENGTH_SHORT).show();
                    if(TextUtils.isEmpty(name)) {
                        cardName.setError("Required");
                    }
                    if(TextUtils.isEmpty(user)) {
                        Name.setError("Required");
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


    private void initData() {
        List<Note> cards = Paper.book().read("Notes");

        if(cards != null) {
            LinearLayoutManager linearlayout = new LinearLayoutManager(getActivity());
            adapter = new NoteRecyclerAdapter(walletView, cards);
            walletView.setAdapter(adapter);
            adapter.onAttachedToRecyclerView(walletView);
            walletView.setLayoutManager(linearlayout);
            walletView.setHasFixedSize(true);
            DividerItemDecoration decor = new DividerItemDecoration(walletView.getContext(), linearlayout.getOrientation());
            walletView.addItemDecoration(decor);
            ItemTouchHelper.Callback callback =
                    new ItemTouchHelperCallback(adapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(walletView);
            adapter.addHelper(touchHelper);
        }
    }
}
