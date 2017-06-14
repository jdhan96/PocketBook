package com.pocketwork.justinhan.PocketBook.Fragment;

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

import com.pocketwork.justinhan.PocketBook.Adapter.CreditRecyclerAdapter;
import com.pocketwork.justinhan.PocketBook.Data.CreditCard;
import com.pocketwork.justinhan.PocketBook.Helper.ItemTouchHelperCallback;
import com.pocketwork.justinhan.PocketBook.Helper.MonthYearPicker;
import com.pocketwork.justinhan.PocketBook.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import io.paperdb.Paper;
/**
 * Created by justinhan on 5/10/17.
 */

public class CreditCardFragment extends Fragment {
    @BindView(R.id.walletRecyclerView)
    RecyclerView walletView;
    @BindView(R.id.AddNew)
    TextView addNew;

    private MonthYearPicker myp;
    private CreditRecyclerAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.creditcard_fragment, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        addNew.setText("Cards");
        initData();
    }

    @OnClick(R.id.addNew)
    public void onAdd() {
        // custom dialog

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.newcredit_card);

        final EditText cardName = (EditText) dialog.findViewById(R.id.EditCardName);
        final EditText Name = (EditText) dialog.findViewById(R.id.EditCardUser);
        final EditText cardNum = (EditText) dialog.findViewById(R.id.EditCardNum);
        final EditText securityCode = (EditText) dialog.findViewById(R.id.EditSecurityCode);
        final EditText month = (EditText) dialog.findViewById(R.id.EditMonth);
        final EditText year = (EditText) dialog.findViewById(R.id.EditYear);

        myp = new MonthYearPicker(getActivity());
        myp.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                month.setText(myp.getSelectedMonth() + "");
                year.setText(Integer.toString(myp.getSelectedYear()).substring(2));
            }
        }, null);

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myp.show();
            }
        });

        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myp.show();
            }
        });

        Button addButton = (Button) dialog.findViewById(R.id.addButton);
        // if button is clicked, close the custom dialog
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = cardName.getText().toString();
                String user = Name.getText().toString();
                String num = cardNum.getText().toString();
                String security = securityCode.getText().toString();
                String mon = month.getText().toString();
                String yr = year.getText().toString();

                if(!name.equals("") && !user.equals("") &&
                        !num.equals("") && !security.equals("")
                        && !mon.equals("") &&
                        !yr.equals("")) {
                    if(num.length() == 16) {
                        adapter.addItem(new CreditCard(name,user, mon, yr, num, security));
                        dialog.dismiss();
                    } else {
                        Toast.makeText(v.getContext(), "The length of the card number has to equal 16!!!", Toast.LENGTH_SHORT).show();
                        cardNum.setError("Has to equal 16!");
                    }
                } else {
                    Toast.makeText(v.getContext(), "Make sure all fields are entered!!!", Toast.LENGTH_SHORT).show();
                    if(TextUtils.isEmpty(name)) {
                        cardName.setError("Required");
                    }
                    if(TextUtils.isEmpty(user)) {
                        Name.setError("Required");
                    }
                    if(TextUtils.isEmpty(num)) {
                        cardNum.setError("Required");
                    }
                    if(TextUtils.isEmpty(mon)) {
                        month.setError("Required");
                    }
                    if(TextUtils.isEmpty(yr)) {
                        year.setError("Required");
                    }
                    if(TextUtils.isEmpty(security)) {
                        securityCode.setError("Required");
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
        List<CreditCard> cards = Paper.book().read("creditCards", new ArrayList<CreditCard>());

        LinearLayoutManager linearlayout = new LinearLayoutManager(getActivity());
        adapter = new CreditRecyclerAdapter(walletView, cards, getActivity());
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
