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
import com.pocketwork.justinhan.pocketbook.Data.Login;
import com.pocketwork.justinhan.pocketbook.Helper.ItemTouchHelperAdapter;
import com.pocketwork.justinhan.pocketbook.Helper.MonthYearPicker;
import com.pocketwork.justinhan.pocketbook.R;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

/**
 * Created by justinhan on 5/8/17.
 */
public class LoginRecyclerAdapter extends RecyclerView.Adapter<LoginRecyclerAdapter.LoginHolder> implements ItemTouchHelperAdapter{
    private List<Login> cards;
    private RecyclerView viewer;
    private int selected = 0;
    private ItemTouchHelper helper;


    public LoginRecyclerAdapter(RecyclerView viewer, List<Login> cards) {
        this.viewer = viewer;
        this.cards = cards;
    }

    public void addHelper(ItemTouchHelper helper) {
        this.helper = helper;
    }
    @Override
    public LoginHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loginview, parent, false);
        LoginHolder pvh = new LoginHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(LoginHolder holder, int position) {


        Login card = cards.get(position);
        holder.loginName.setText(card.getName());
        holder.nameofCard.setText(card.getName());
        holder.editLogin.setText(card.getLogin());
        holder.editPassword.setText(card.getPassword());

    }
    public void addItem(Login card) {
        viewer.smoothScrollToPosition(cards.size());
        cards.add(card);
        Paper.book().write("Logins", cards);

        notifyItemInserted(cards.size());

    }
    public void removeItem(int position) {
        cards.remove(position);
        Paper.book().write("Logins", cards);

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
        Paper.book().write("Logins", cards);
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


    public class LoginHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.clickableCard)
        RelativeLayout clickableCard;
        @BindView(R.id.loginName)
        TextView loginName;
        @BindView(R.id.EditCardName)
        EditText nameofCard;
        @BindView(R.id.EditLogin)
        EditText editLogin;
        @BindView(R.id.EditPassword)
        EditText editPassword;
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
        public LoginHolder(View itemView) {
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
        }
        @OnClick(R.id.cancelButton)
        void onCancel() {
            editable = false;
            editButton.setVisibility(View.VISIBLE);
            checkButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            setEditable(false);
            notifyDataSetChanged();
        }
        @OnClick(R.id.checkButton)
        void onConfirm() {

            final String name = nameofCard.getText().toString();
            final String user = editLogin.getText().toString();
            final String num = editPassword.getText().toString();

            if(!name.equals("") && !user.equals("") &&
                    !num.equals("")) {

                    cards.get(getAdapterPosition()).setName(name);
                    cards.get(getAdapterPosition()).setLogin(user);
                    cards.get(getAdapterPosition()).setPassword(num);

                    notifyDataSetChanged();

                    editButton.setVisibility(View.VISIBLE);
                    checkButton.setVisibility(View.GONE);
                    cancelButton.setVisibility(View.GONE);
                    setEditable(false);
                    editable = false;
            } else {
                Toast.makeText(itemView.getContext(), "Make sure all fields are entered!!!", Toast.LENGTH_SHORT).show();
                if(nameofCard.getText().equals("")) {
                    nameofCard.setError("Required");
                }
                if(editLogin.getText().equals("")) {
                    editLogin.setError("Required");
                }
                if(editPassword.getText().equals("")) {
                    editPassword.setError("Required");
                }
            }
        }

        public void setEditable(boolean set) {
            nameofCard.setFocusable(set);
            nameofCard.setFocusableInTouchMode(set);
            nameofCard.setCursorVisible(set);
            editLogin.setFocusable(set);
            editLogin.setCursorVisible(set);
            editLogin.setFocusableInTouchMode(set);
            editPassword.setFocusable(set);
            editPassword.setFocusableInTouchMode(set);
            editPassword.setCursorVisible(set);
        }


    }



}