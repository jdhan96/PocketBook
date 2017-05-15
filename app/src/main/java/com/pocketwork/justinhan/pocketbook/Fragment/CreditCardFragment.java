package com.pocketwork.justinhan.pocketbook.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pocketwork.justinhan.pocketbook.Adapter.RecyclerAdapter;
import com.pocketwork.justinhan.pocketbook.Data.CreditCard;
import com.pocketwork.justinhan.pocketbook.Helper.ItemTouchHelperCallback;
import com.pocketwork.justinhan.pocketbook.Helper.MonthYearPicker;
import com.pocketwork.justinhan.pocketbook.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by justinhan on 5/10/17.
 */

public class CreditCardFragment extends Fragment {
    @BindView(R.id.walletRecyclerView)
    RecyclerView walletView;
    @BindView(R.id.AddNew)
    TextView addNew;

    private MonthYearPicker myp;
    private RecyclerAdapter adapter;
    private List<CreditCard> cards;
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
        LinearLayoutManager linearlayout = new LinearLayoutManager(getActivity());
        adapter = new RecyclerAdapter(walletView, cards);
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
    }

    @OnClick(R.id.addNew)
    public void onAdd() {
        // custom dialog

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.newcredit_card);
        dialog.setTitle("New Card");

        final EditText cardName = (EditText) dialog.findViewById(R.id.EditCardName);
        final EditText Name = (EditText) dialog.findViewById(R.id.EditCardUser);
        final EditText cardNum = (EditText) dialog.findViewById(R.id.EditCardNum);
        final EditText securityCode = (EditText) dialog.findViewById(R.id.EditSecurityCode);
        final EditText zipCode = (EditText) dialog.findViewById(R.id.EditZipCode);
        final EditText month = (EditText) dialog.findViewById(R.id.EditMonth);
        final EditText year = (EditText) dialog.findViewById(R.id.EditYear);

        myp = new MonthYearPicker(getActivity());
        myp.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                month.setText(myp.getSelectedMonth() + "");
                year.setText(myp.getSelectedYear() + "");
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
                String zip = zipCode.getText().toString();
                String mon = month.getText().toString();
                String yr = year.getText().toString();

                if(!name.equals("") && !user.equals("") &&
                        !num.equals("") && !security.equals("") &&
                        !zip.equals("") && !mon.equals("") &&
                        !yr.equals("")) {
                    if(num.length() == 16) {
                        adapter.addItem(new CreditCard(name,user, Integer.parseInt(mon), Integer.parseInt(yr), num,
                                                    Integer.parseInt(security), Integer.parseInt(zip)));
                        dialog.dismiss();
                    } else {
                        Toast.makeText(v.getContext(), "The length of the card number has to equal 16!!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(v.getContext(), "Make sure all fields are entered!!!", Toast.LENGTH_SHORT).show();
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

    public void show(View view) {
        myp.show();
    }

    private void initData() {
        cards = new ArrayList<>();
        cards.add(new CreditCard("Justin Han", "Justin's Credit Card", 07,1732, "1234567891234567", 903, 90006));
        cards.add(new CreditCard("Heidi Han", "Heidi's Credit Card", 06,1237, "1234567891234567", 903, 90006));
        cards.add(new CreditCard("Jennifer Han", "Jennifer's Credit Card", 10,1327, "1234567891234567", 903, 90006));
    }

//    public static class DatePickerFragment extends DialogFragment
//            implements DatePickerDialog.OnDateSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current date as the default date in the picker
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog test = new DatePickerDialog(getActivity(), null , year, month, day);
//
//            try {
//                java.lang.reflect.Field[] datePickerDialogFields = test.getClass().getDeclaredFields();
//                for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
//                    if (datePickerDialogField.getName().equals("mDatePicker")) {
//                        datePickerDialogField.setAccessible(true);
//                        DatePicker datePicker = (DatePicker) datePickerDialogField.get(test);
//                        java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
//                        for (java.lang.reflect.Field datePickerField : datePickerFields) {
//                            Log.i("test", datePickerField.getName());
//                            if ("mDaySpinner".equals(datePickerField.getName())) {
//                                datePickerField.setAccessible(true);
//                                Object dayPicker = datePickerField.get(datePicker);
//                                ((View) dayPicker).setVisibility(View.GONE);
//                            }
//                        }
//                    }
//                }
//            }
//            catch (Exception ex) {
//            }
//
//            // Create a new instance of DatePickerDialog and return it
//            return test;
//        }
//
//        public void onDateSet(DatePicker view, int year, int month, int day) {
//            // Do something with the date chosen by the user
//        }
//    }
//
//    public void showDatePickerDialog(View v) {
//        FragmentManager fragmentmanager = getFragmentManager();
//        DialogFragment newFragment = new DatePickerFragment();
//        newFragment.show(fragmentmanager, "datePicker");
//    }
}
