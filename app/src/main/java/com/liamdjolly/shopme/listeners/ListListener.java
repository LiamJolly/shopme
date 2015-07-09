package com.liamdjolly.shopme.listeners;

import android.content.Context;
import android.content.Intent;
import com.liamdjolly.shopme.MainActivity;
import com.liamdjolly.shopme.ShoppingListActivity;

public class ListListener {
  protected final MainActivity activity;

  public ListListener(final MainActivity activity) {
    this.activity = activity;
  }

  /**
   * Called when the user clicks the Send button
   */
  public void openList(String listName) {
    Context context = activity.getContext();
    Intent intent = new Intent(context, ShoppingListActivity.class);
    intent.putExtra("listName", listName);
    context.startActivity(intent);
  }
}
