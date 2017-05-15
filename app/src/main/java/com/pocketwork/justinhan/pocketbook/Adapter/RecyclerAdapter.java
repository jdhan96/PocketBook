package com.pocketwork.justinhan.pocketbook.Adapter;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pocketwork.justinhan.pocketbook.Data.CreditCard;
import com.pocketwork.justinhan.pocketbook.Helper.ItemTouchHelperAdapter;
import com.pocketwork.justinhan.pocketbook.R;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by justinhan on 5/8/17.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CreditCardHolder> implements ItemTouchHelperAdapter{
    private List<CreditCard> cards;
    private RecyclerView viewer;
    private int selected = 0;

    public RecyclerAdapter(RecyclerView viewer, List<CreditCard> cards) {
        this.viewer = viewer;
        this.cards = cards;
    }

    @Override
    public CreditCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.creditcardview, parent, false);
        CreditCardHolder pvh = new CreditCardHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final CreditCardHolder holder, final int position) {


        CreditCard card = cards.get(position);
        holder.cardName.setText(card.getName());
        holder.cardNum.setText(card.getLast4Digit());


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
        if(selected == 0) {
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
    }

    public class CreditCardHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.creditCardView)
        CardView creditCard;
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

        private int originalHeight = 0;
        public boolean toggle = false;

        public CreditCardHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            clickableCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        LinearLayoutManager linear;
                        if (selected == 0 || toggle == true) {
                            int position = getAdapterPosition();
                            if (originalHeight == 0) {
                                originalHeight = clickableCard.getHeight();
                            }

                            // Declare a ValueAnimator object
                            ValueAnimator valueAnimator;
                            if (!toggle) {
                                descript.setVisibility(View.VISIBLE);
                                descript.setEnabled(true);
                                toggle = true;
                                selected = 1;
                                valueAnimator = ValueAnimator.ofInt(originalHeight, originalHeight); // These values in this method can be changed to expand however much you like

                            } else {
                                toggle = false;
                                selected = 0;

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
                                if (position == getItemCount() - 1) {
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

    }



}