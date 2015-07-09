package com.liamdjolly.shopme.listeners;

import android.view.View;
import android.widget.AdapterView;
import com.liamdjolly.shopme.MainActivity;

public class OnListClickListener extends ListListener implements AdapterView.OnItemClickListener {

  public OnListClickListener(MainActivity activity) {
    super(activity);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    String listName = activity.getListItems().get(position);
    openList(listName);
  }
}
