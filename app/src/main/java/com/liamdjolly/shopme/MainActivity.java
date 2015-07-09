package com.liamdjolly.shopme;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.liamdjolly.shopme.database.DatabaseHelper;
import com.liamdjolly.shopme.listeners.OnAddListListener;
import com.liamdjolly.shopme.listeners.OnListClickListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ListActivity {

  private final Context context = this;
  private List<String> listItems = new ArrayList<>();

  private ArrayAdapter<String> adapter;

  private DatabaseHelper dbHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //get items from db
    dbHelper = new DatabaseHelper(context);
    listItems = dbHelper.getListNames();

    // set up adapter
    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
    setListAdapter(adapter);
    adapter.notifyDataSetChanged();


    // add button listener
    Button newButton = (Button) findViewById(R.id.newButton);
    newButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        createNewListAlertDialog().show();
      }
    });

    // add listener to list
    ListView listView = getListView();
    listView.setOnItemClickListener(new OnListClickListener(this));
  }

  /**
   * Method to create list dialog
   *
   * @return
   */
  private AlertDialog createNewListAlertDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);

    builder.setTitle(R.string.dialog_add_list_title);
    builder.setMessage(R.string.dialog_add_list_message);

    final EditText input = new EditText(context);
    input.setId(R.id.edittext_newListDialog);
    input.setMaxLines(1);
    input.setSingleLine(true);
    builder.setView(input);

    builder.setPositiveButton(R.string.ok, new OnAddListListener(input, this));
    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        // Canceled.
      }
    });

    return builder.create();
  }


  @Override
  public void onResume() {
    super.onResume();
    listItems.clear();
    listItems.addAll(dbHelper.getListNames());
    adapter.notifyDataSetChanged();
  }

  public Context getContext() {
    return context;
  }

  public List<String> getListItems() {
    return listItems;
  }
}
