package com.liamdjolly.shopme.listeners;

import android.content.DialogInterface;
import android.widget.EditText;
import com.liamdjolly.shopme.ShoppingListActivity;
import com.liamdjolly.shopme.database.DatabaseHelper;
import com.liamdjolly.shopme.entities.ShoppingList;

public class OnAddItemListener implements DialogInterface.OnClickListener {
  private final EditText input;
  private ShoppingListActivity activity;


  public OnAddItemListener(final EditText input, final ShoppingListActivity activity) {
    this.input = input;
    this.activity = activity;
  }

  @Override
  public void onClick(DialogInterface dialog, int which) {
    ShoppingList shoppingList = activity.getShoppingList();
    DatabaseHelper dbHelper = new DatabaseHelper(activity.getApplicationContext());

    shoppingList.getItems().add(dbHelper.createItem(input.getText().toString(), shoppingList.getId()));
    activity.getAdapter().notifyDataSetChanged();
  }
}
