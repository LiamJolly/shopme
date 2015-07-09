package com.liamdjolly.shopme.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.liamdjolly.shopme.entities.Item;
import com.liamdjolly.shopme.entities.ShoppingList;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

  private static final int DATABASE_VERSION = 1;

  private static final String DATABASE_KEY = "SHOPPING_LIST";

  private static final String TABLE_ITEMS = "listItems";
  private static final String ITEMS_KEY_ID = "ID";
  private static final String ITEMS_KEY_ITEM = "ITEM";
  private static final String ITEMS_KEY_LIST_ID = "LISTID";
  private static final String ITEMS_KEY_DONE_KEY = "DONE";

  private static final String TABLE_LISTS = "lists";
  private static final String LISTS_KEY_ID = "ID";
  private static final String LISTS_KEY_NAME = "NAME";

  public DatabaseHelper(Context context) {
    super(context, DATABASE_KEY, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // Construct a table for lists
    String CREATE_LISTS_TABLE = "CREATE TABLE " + TABLE_LISTS + "(" + LISTS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            LISTS_KEY_NAME + " TEXT);";
    db.execSQL(CREATE_LISTS_TABLE);

    // create item table
    String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEMS + "(" + ITEMS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ITEMS_KEY_ITEM + " TEXT," + ITEMS_KEY_LIST_ID + " INTEGER, " + ITEMS_KEY_DONE_KEY + " INTEGER, FOREIGN KEY (" +
            ITEMS_KEY_LIST_ID + ") REFERENCES " + TABLE_LISTS + "(" + LISTS_KEY_ID + "));";
    db.execSQL(CREATE_ITEM_TABLE);

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //nothing to do yet
  }

  public ShoppingList getShoppingList(final String listName) throws Exception {
    ShoppingList list = new ShoppingList();

    SQLiteDatabase db = this.getReadableDatabase();
    String rawQuery = "SELECT " + TABLE_ITEMS + "." + ITEMS_KEY_ID + "," + TABLE_ITEMS + "." + ITEMS_KEY_ITEM + ", " + TABLE_ITEMS + "."
            + ITEMS_KEY_DONE_KEY + " FROM " + TABLE_ITEMS + " JOIN " + TABLE_LISTS + " on " + TABLE_ITEMS + "." + ITEMS_KEY_LIST_ID + "="
            + TABLE_LISTS + "." + LISTS_KEY_ID + " WHERE " + TABLE_LISTS + "." + LISTS_KEY_NAME + "=?";


    Cursor cursor = null;
    List<Item> items = new ArrayList<>();
    try {
      cursor = db.rawQuery(rawQuery, new String[]{listName});

      if (cursor != null) {
        while (cursor.moveToNext()) {
          items.add(new Item(cursor.getInt(0), cursor.getString(1), (cursor.getInt(2) == 1)));
        }
        list = new ShoppingList(getListId(listName), listName, items);
      }
    } finally {
      assert cursor != null;
      cursor.close();
    }

    return list;
  }

  public void createNewList(String name) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    values.put(LISTS_KEY_NAME, name);

    db.insert(TABLE_LISTS, null, values);
    db.close();
  }

  public int getListId(final String listName) throws Exception {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = null;

    try {
      cursor = db.rawQuery("SELECT " + TABLE_LISTS + "." + LISTS_KEY_ID + " FROM " + TABLE_LISTS + " WHERE " + TABLE_LISTS + "." +
              LISTS_KEY_NAME + "=?", new String[]{listName});

      if (cursor != null && cursor.moveToFirst()) {
        return cursor.getInt(0);
      } else {
        //TODO proper exceptions!
        throw new Exception("Couldn't find table.");
      }
    } finally {
      assert cursor != null;
      cursor.close();
    }
  }


  public Item createItem(String item, final int listId) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(ITEMS_KEY_ITEM, item);
    values.put(ITEMS_KEY_DONE_KEY, 0);
    values.put(ITEMS_KEY_LIST_ID, listId);
    long id = db.insert(TABLE_ITEMS, null, values);

    return new Item(id, item, false);
  }


  public void updateItem(final Item item) throws Exception {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(ITEMS_KEY_ITEM, item.getValue());
    values.put(ITEMS_KEY_DONE_KEY, (item.isDone()) ? 1 : 0);
    db.update(TABLE_ITEMS, values, ITEMS_KEY_ID + " = ?", new String[]{String.valueOf(item.getId())});
  }

  public void deleteItem(final Item item) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_ITEMS, ITEMS_KEY_ID + " = ?", new String[]{String.valueOf(item.getId())});
  }

  public boolean listExists(String name) {
    try {
      return getShoppingList(name) != null;
    } catch (Exception e) {
      return false;
    }
  }

  public List<String> getListNames() {
    List<String> lists = new ArrayList<>();

    String selectQuery = "SELECT " + LISTS_KEY_NAME + " FROM " + TABLE_LISTS + ";";

    SQLiteDatabase db = this.getWritableDatabase();

    Cursor cursor = null;
    try {
      cursor = db.rawQuery(selectQuery, null);
      // looping through all rows and adding to list
      if (cursor.moveToFirst()) {
        do {
          String listName = (cursor.getString(0));
          lists.add(listName);
        } while (cursor.moveToNext());
      }
    } finally {
      assert cursor != null;
      cursor.close();
    }


    // return contact list
    return lists;
  }
}
