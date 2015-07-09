package com.liamdjolly.shopme;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.liamdjolly.shopme.database.DatabaseHelper;
import com.liamdjolly.shopme.entities.Item;

import java.util.List;

public class ItemAdapter extends ArrayAdapter {
  private Context context;
  private int layoutResourceId;
  private List<Item> items;

  public ItemAdapter(Context context, int layoutResourceId, List<Item> items) {
    super(context, layoutResourceId, items);
    this.layoutResourceId = layoutResourceId;
    this.context = context;
    this.items = items;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    LayoutInflater inflater = ((Activity) context).getLayoutInflater();
    View row = inflater.inflate(layoutResourceId, parent, false);

    ItemHolder holder = new ItemHolder();
    Item item = items.get(position);
    holder.item = item;

    // grab elements
    holder.itemText = (TextView) row.findViewById(R.id.itemText);
    holder.removeButton = (Button) row.findViewById(R.id.removeItemButton);
    holder.checkBox = (CheckBox) row.findViewById(R.id.toggleIsDone);


    // setup item text
    holder.itemText.setText(item.getValue());

    //set up is done checkbox
    holder.checkBox.setTag(holder);
    holder.checkBox.setChecked(item.isDone());
    holder.checkBox.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ItemHolder itemHolder = (ItemHolder) v.getTag();
        itemHolder.item.setDone(((CheckBox) v).isChecked());
        try {
          new DatabaseHelper(context).updateItem(itemHolder.item);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    // setup remove button
    holder.removeButton.setTag(holder);
    holder.removeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ItemHolder itemHolder = (ItemHolder) v.getTag();

        //delete it from db
        new DatabaseHelper(context).deleteItem(itemHolder.item);

        // remove from list
        items.remove(itemHolder.item);

        // update activity
        ShoppingListActivity activity = (ShoppingListActivity) v.getContext();
        activity.getAdapter().notifyDataSetChanged();
      }
    });


    row.setTag(holder);
    return row;
  }

  static class ItemHolder {
    TextView itemText;
    Button removeButton;
    Item item;
    CheckBox checkBox;
  }
}
