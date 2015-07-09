package com.liamdjolly.shopme;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.liamdjolly.shopme.database.DatabaseHelper;
import com.liamdjolly.shopme.entities.ShoppingList;
import com.liamdjolly.shopme.listeners.OnAddItemListener;


public class ShoppingListActivity extends ListActivity {

  private ItemAdapter adapter;
  private ShoppingList shoppingList;
  private Context context = this;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shopping_list);

    // set up adapter
    try {
      shoppingList = new DatabaseHelper(context).getShoppingList(getIntent().getStringExtra("listName"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    adapter = new ItemAdapter(this, R.layout.listview_item_row, shoppingList.getItems());
    setListAdapter(adapter);
    adapter.notifyDataSetChanged();

    // setup button
    Button button = (Button) findViewById(R.id.addButtonShoppingList);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        createAddItemDialog().show();
      }
    });
  }

  public ItemAdapter getAdapter() {
    return this.adapter;
  }

  public ShoppingList getShoppingList() {
    return this.shoppingList;
  }

  /**
   * Create dialog
   *
   * @return
   */
  private AlertDialog createAddItemDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);

    builder.setTitle(R.string.dialog_add_item_title);
    builder.setMessage(R.string.dialog_add_item_message);

    final EditText input = new EditText(context);
    input.setMaxLines(1);
    input.setSingleLine(true);
    builder.setView(input);

    builder.setPositiveButton(R.string.ok, new OnAddItemListener(input, this));

    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        // Canceled.
      }
    });

    return builder.create();
  }
}
