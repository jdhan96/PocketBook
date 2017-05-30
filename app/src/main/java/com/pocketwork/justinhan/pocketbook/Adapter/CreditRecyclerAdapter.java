package com.pocketwork.justinhan.pocketbook.Adapter;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pocketwork.justinhan.pocketbook.Data.CreditCard;
import com.pocketwork.justinhan.pocketbook.Helper.ItemTouchHelperAdapter;
import com.pocketwork.justinhan.pocketbook.Helper.MonthYearPicker;
import com.pocketwork.justinhan.pocketbook.R;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

import static android.view.FrameMetrics.ANIMATION_DURATION;

/**
 * Created by justinhan on 5/8/17.
 */
public class CreditRecyclerAdapter extends RecyclerView.Adapter<CreditRecyclerAdapter.CreditCardHolder> implements ItemTouchHelperAdapter{
    private List<CreditCard> cards;
    private RecyclerView viewer;
    private Activity activity;
    private int selected = 0;
    private ItemTouchHelper helper;


    public CreditRecyclerAdapter(RecyclerView viewer, List<CreditCard> cards, Activity activity) {
        this.viewer = viewer;
        this.cards = cards;
        this.activity = activity;
    }

    public void addHelper(ItemTouchHelper helper) {
        this.helper = helper;
    }
    @Override
    public CreditCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.creditcardview, parent, false);
        CreditCardHolder pvh = new CreditCardHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(CreditCardHolder holder, int position) {


        CreditCard card = cards.get(position);
        holder.cardName.setText(card.getName());
        holder.cardNum.setText("XXXX XXXX XXXX " + card.getLast4Digit());


        holder.nameofCard.setText(card.getName());
        holder.nameonCard.setText(card.getNameonCard());
        holder.cardNumber.setText(card.getCardNum());
        holder.month.setText(card.getMonth() +"");
        holder.year.setText(card.getYear() +"");
        holder.securityCode.setText(card.getSecurityCode() +"");
        holder.zipCode.setText(card.getZipCode() +"");

    }
    public void addItem(CreditCard card) {
        viewer.smoothScrollToPosition(cards.size());
        cards.add(card);
        Paper.book().write("creditCards", cards);

        notifyItemInserted(cards.size());

    }
    public void removeItem(int position) {
        cards.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(cards, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(cards, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(viewer.getContext());
        builder.setTitle("Delete")
                .setMessage("Are you sure you want to delete? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notifyDataSetChanged();
                    }
                });

        builder.create().show();
    }


    public class CreditCardHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.clickableCard)
        RelativeLayout clickableCard;
        @BindView(R.id.cardName)
        TextView cardName;
        @BindView(R.id.cardNum)
        TextView cardNum;
        @BindView(R.id.EditCardName)
        EditText nameofCard;
        @BindView(R.id.EditCardUser)
        EditText nameonCard;
        @BindView(R.id.EditCardNum)
        EditText cardNumber;
        @BindView(R.id.EditMonth)
        EditText month;
        @BindView(R.id.EditYear)
        EditText year;
        @BindView(R.id.EditSecurityCode)
        EditText securityCode;
        @BindView(R.id.EditZipCode)
        EditText zipCode;
        @BindView(R.id.descriptView)
        LinearLayout descript;
        @BindView(R.id.editButton)
        ImageButton editButton;
        @BindView(R.id.checkButton)
        ImageButton checkButton;
        @BindView(R.id.cancelButton)
        ImageButton cancelButton;

        private int originalHeight = 0;
        public boolean toggle = false;
        private boolean editable = false;
        public CreditCardHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            clickableCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        LinearLayoutManager linear;
                        if ((selected == 0 || toggle) && !editable) {
                            int position = getAdapterPosition();
                            if (originalHeight == 0) {
                                originalHeight = clickableCard.getHeight();
                            }

                            // Declare a ValueAnimator object
                            ValueAnimator valueAnimator;
                            if (!toggle) {
                                helper.attachToRecyclerView(null);
                                selected = 1;
                                descript.setVisibility(View.VISIBLE);
                                descript.setEnabled(true);
                                toggle = true;

                                valueAnimator = ValueAnimator.ofInt(originalHeight, originalHeight); // These values in this method can be changed to expand however much you like

                            } else {
                                helper.attachToRecyclerView(viewer);
                                selected = 0;
                                toggle = false;
                                valueAnimator = ValueAnimator.ofInt(originalHeight, originalHeight);

                                Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out

                                a.setDuration(200);
                                // Set a listener to the animation and configure onAnimationEnd
                                a.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        descript.setVisibility(View.GONE);
                                        descript.setEnabled(false);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });

                                // Set the animation on the custom view
                                descript.startAnimation(a);

                            }
                            valueAnimator.setDuration(200);
                            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    Integer value = (Integer) animation.getAnimatedValue();
                                    clickableCard.getLayoutParams().height = value.intValue();
                                    clickableCard.requestLayout();
                                }
                            });
                            valueAnimator.start();
                            if (toggle) {
                                linear = (LinearLayoutManager) viewer.getLayoutManager();
                                if (position == getItemCount() - 1 && getItemCount() != 1) {
                                    linear.setStackFromEnd(true);
                                } else {
                                    linear.setStackFromEnd(false);
                                }
                                linear.scrollToPositionWithOffset(position, 0);
                            }
                        }
                    }


            });
        }
        @OnClick(R.id.deleteButton)
        void onDelete() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(viewer.getContext());
            builder.setTitle("Delete")
                    .setMessage("Are you sure you want to delete? ")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeItem(getAdapterPosition());
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            notifyDataSetChanged();
                        }
                    });

            builder.create().show();
        }

        @OnClick(R.id.editButton)
        void onEdit() {
            editable = true;
            editButton.setVisibility(View.GONE);
            checkButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            setEditable(true);
            final MonthYearPicker myp = new MonthYearPicker(activity);
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
        }
        @OnClick(R.id.cancelButton)
        void onCancel() {
            editable = false;
            editButton.setVisibility(View.VISIBLE);
            checkButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            setEditable(false);

            month.setOnClickListener(null);
            year.setOnClickListener(null);
            notifyDataSetChanged();
        }
        @OnClick(R.id.checkButton)
        void onConfirm() {

            final String name = nameofCard.getText().toString();
            final String user = nameonCard.getText().toString();
            final String num = cardNumber.getText().toString();
            final String security = securityCode.getText().toString();
            final String zip = zipCode.getText().toString();
            final String mon = month.getText().toString();
            final String yr = year.getText().toString();

            if(!name.equals("") && !user.equals("") &&
                    !num.equals("") && !security.equals("") &&
                    !zip.equals("") && !mon.equals("") &&
                    !yr.equals("")) {
                if(num.length() == 16) {

                    cards.get(getAdapterPosition()).setName(name);
                    cards.get(getAdapterPosition()).setCardNum(num);
                    cards.get(getAdapterPosition()).setNameonCard(user);
                    cards.get(getAdapterPosition()).setMonth(Integer.parseInt(mon));
                    cards.get(getAdapterPosition()).setYear(Integer.parseInt(yr));
                    cards.get(getAdapterPosition()).setSecurityCode(Integer.parseInt(security));
                    cards.get(getAdapterPosition()).setZipCode(Integer.parseInt(zip));

                    notifyDataSetChanged();

                    editButton.setVisibility(View.VISIBLE);
                    checkButton.setVisibility(View.GONE);
                    cancelButton.setVisibility(View.GONE);
                    setEditable(false);
                    month.setOnClickListener(null);
                    year.setOnClickListener(null);
                    editable = false;
                } else {
                    Toast.makeText(itemView.getContext(), "The length of the card number has to equal 16!!!", Toast.LENGTH_SHORT).show();
                    cardNumber.setError("Has to equal 16!");
                }
            } else {
                Toast.makeText(itemView.getContext(), "Make sure all fields are entered!!!", Toast.LENGTH_SHORT).show();
                if(nameofCard.getText().equals("")) {
                    nameofCard.setError("Required");
                } else if(nameonCard.getText().equals("")) {
                    nameonCard.setError("Required");
                } else if(cardNumber.getText().equals("")) {
                    cardNumber.setError("Required");
                } else if(month.getText().equals("")) {
                    month.setError("Required");
                } else if(year.getText().equals("")) {
                    year.setError("Required");
                } else if(securityCode.getText().equals("")) {
                    securityCode.setError("Required");
                } else if(zipCode.getText().equals("")) {
                    zipCode.setError("Required");
                }
            }
        }

        public void setEditable(boolean set) {
            nameofCard.setFocusable(set);
            nameofCard.setFocusableInTouchMode(set);
            nameofCard.setCursorVisible(set);
            nameonCard.setFocusable(set);
            nameonCard.setCursorVisible(set);
            nameonCard.setFocusableInTouchMode(set);
            cardNumber.setFocusable(set);
            cardNumber.setFocusableInTouchMode(set);
            cardNumber.setCursorVisible(set);
            securityCode.setFocusable(set);
            securityCode.setFocusableInTouchMode(set);
            securityCode.setCursorVisible(set);
            zipCode.setFocusable(set);
            zipCode.setFocusableInTouchMode(set);
            zipCode.setCursorVisible(set);
        }



    }


}