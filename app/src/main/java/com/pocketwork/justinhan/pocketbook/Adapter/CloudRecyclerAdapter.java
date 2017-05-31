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

import com.pocketwork.justinhan.pocketbook.Data.Cloud;
import com.pocketwork.justinhan.pocketbook.Data.CreditCard;
import com.pocketwork.justinhan.pocketbook.Helper.ItemTouchCloudAdapter;
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
public class CloudRecyclerAdapter extends RecyclerView.Adapter<CloudRecyclerAdapter.CloudViewHolder> implements ItemTouchCloudAdapter {
    private List<Cloud> cards;
    private RecyclerView viewer;


    public CloudRecyclerAdapter(List<Cloud> cards, RecyclerView viewer) {
        this.cards = cards;
        this.viewer = viewer;
    }

    @Override
    public CloudViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_cloud, parent, false);
        CloudViewHolder pvh = new CloudViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(CloudViewHolder holder, int position) {


        Cloud card = cards.get(position);

        holder.Date.setText(card.getDate());
        holder.nameofBackUp.setText(card.getName());

    }
    public void addItem(Cloud card) {
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


    public class CloudViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.uploadDate)
        TextView Date;
        @BindView(R.id.uploadName)
        TextView nameofBackUp;

        public CloudViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.downloadBackUp)
        void onClick() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Download from Backup")
                    .setMessage("Are you sure you want to download? ")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Paper.book().write("creditCards", cards.get(getAdapterPosition()).getCards());
                            Paper.book().write("Logins", cards.get(getAdapterPosition()).getLogins());
                            Paper.book().write("Notes", cards.get(getAdapterPosition()).getNotes());
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });

            builder.create().show();
        }
    }

}