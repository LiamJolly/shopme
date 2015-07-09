package com.liamdjolly.shopme.listeners;

import android.content.DialogInterface;
import android.widget.EditText;
import com.liamdjolly.shopme.MainActivity;
import com.liamdjolly.shopme.database.DatabaseHelper;

public class OnAddListListener extends ListListener implements DialogInterface.OnClickListener {
  private final EditText input;

  public OnAddListListener(final EditText input, final MainActivity activity) {
    super(activity);
    this.input = input;
  }

  @Override
  public void onClick(DialogInterface dialog, int which) {
    String listName = input.getText().toString();
    DatabaseHelper dbHelper = new DatabaseHelper(activity.getContext());
    try {
      if (!dbHelper.listExists(listName)) {
        dbHelper.createNewList(listName);
        openList(listName);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
