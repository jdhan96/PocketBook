package com.pocketwork.justinhan.PocketBook.Helper;

/**
 * Created by justinhan on 5/13/17.
 */

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);

}
