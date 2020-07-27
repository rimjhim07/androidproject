package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ListView listView = (ListView)findViewById(R.id.userListView);
        ArrayList<String> users = new ArrayList<>();

        Cursor cursor = MainActivity.mydatabase.rawQuery("select username from login",null);
        int uIndex = cursor.getColumnIndex("username");
        while (cursor!=null && cursor.moveToFirst()) {
            users.add(cursor.getString(uIndex).toString());
            cursor.moveToNext();
        }
        cursor.close();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,users);
        listView.setAdapter(arrayAdapter);
    }
}