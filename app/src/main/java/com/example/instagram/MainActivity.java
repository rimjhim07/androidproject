package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public  static SQLiteDatabase mydatabase;
    EditText pswrd, userName;
    Boolean signupmodeActive = true;

    public void showUsers(){
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
    }

    public boolean CheckIsDataAlreadyInDBorNot(String TableName, String dbfield, String fieldValue)
    {
        String Query = "Select * from " + TableName;
        Cursor cursor = mydatabase.rawQuery(Query, null);
        cursor.moveToFirst();
        int uIndex = cursor.getColumnIndex(dbfield);
        int pIndex = cursor.getColumnIndex("password");
        while(cursor != null)
        {
            if(cursor.getString(uIndex).matches(fieldValue))
            {
                cursor.close();
                return true;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return false;
    }

    public void login(View view)
    {
        Log.i("function","in login");
        String userNameText = userName.getText().toString();
        String pwd = pswrd.getText().toString();
        Cursor cursor = mydatabase.rawQuery("select * from login", null);
        cursor.moveToFirst();
        int uIndex = cursor.getColumnIndex("username");
        int pIndex = cursor.getColumnIndex("password");
        boolean flag = false;
        if(userNameText.matches("") || pwd.matches(""))
        {
            Toast.makeText(this, "Username and Password are required", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor != null) {
                Log.i("Name",cursor.getString(uIndex));
                Log.i("Password",cursor.getString(pIndex));
                if (cursor.getString(uIndex).matches(userNameText) && cursor.getString(pIndex).matches(pwd)) {
                    flag = true;
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    break;
                }
                cursor.moveToNext();
            }
            cursor.close();
            if (!flag)
            {
                Toast.makeText(this, "Username/Password Incorrect!!", Toast.LENGTH_SHORT).show();
                userName.setText("");
                pswrd.setText("");
            }
            else
                showUsers();
        }
    }
    public void signup(View view)
    {
        Log.i("function","in signup");
        String userNameText = userName.getText().toString();
        String pwd = pswrd.getText().toString();
        if(userNameText.matches("") || pwd.matches(""))
        {
            Toast.makeText(this, "Username and Password are required", Toast.LENGTH_SHORT).show();
        }
        else
        {
                boolean f = CheckIsDataAlreadyInDBorNot("login", "username", userNameText);
                if (f==false)
                {
                    Log.i("Values","insert");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("username", userNameText);
                    contentValues.put("password", pwd);
                    long result = mydatabase.insert("login", null, contentValues);
                    if (result == -1) {
                        Toast.makeText(this, "Try Again!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                        showUsers();
                    }
                }
                else {
                    Toast.makeText(this, "User Already Exists!! Try another username", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.layout);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);

        mydatabase = this.openOrCreateDatabase("login",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS login (username VARCHAR, password VARCHAR, image BLOB)");

        userName = (EditText) findViewById(R.id.userName);
        pswrd = (EditText) findViewById(R.id.password);

        constraintLayout.setOnClickListener(this);
        imageView.setOnClickListener(this);

         Button signupButton =(Button)findViewById(R.id.signupbtn);
         Button loginButton =(Button)findViewById(R.id.loginbutton);

    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.layout || view.getId() == R.id.imageView)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
           inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }
}