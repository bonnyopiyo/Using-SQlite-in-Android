package com.example.mysqlitedatabaseapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText mFname, mLname,mId ;
    Button mSave,mView,mDelete;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFname = findViewById(R.id.editText1);
        mLname = findViewById(R.id.editText2);
        mId = findViewById(R.id.editId);

        mSave = findViewById(R.id.btnsave);
        mView = findViewById(R.id.btnview);
        mDelete = findViewById(R.id.btndelete);

        //create db

        db = openOrCreateDatabase("huduma",MODE_PRIVATE,null);

        //create a table in your datables

        db.execSQL("CREATE TABLE IF NOT EXISTS citizens(first_name VARCHAR ,last_name VARCHAR, id_number INTEGER)");

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the data from the user
                String firstName = mFname.getText().toString();
                String lastName = mLname.getText().toString();
                String Idnumber = mId.getText().toString();
                //chek if the user is inputting empty fields

                if (firstName.isEmpty()){
                    messages("First name Error","Please enter the first name");
                }else if (lastName.isEmpty()){
                    messages("last name Error","Please enter the last name");
                }else if (Idnumber.isEmpty()){
                    messages("Id number Error","Enter Id Number");
                }else {
                    //Proceed to save the received data into your db called huduma
                    db.execSQL("INSERT INTO citizens VALUES('"+firstName+"','"+lastName+"','"+Idnumber+"')");
                     messages("SUCCESS","User saved successfully");

                     //clear input fields for the next entry

                    mFname.setText("");
                    mLname.setText("");
                    mId.setText("");
                }
            }
        });
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //use a cursor to query and select data from yoir db table

                Cursor cursor = db.rawQuery("SELECT * FROM citizens",null);
                //check if the cursor found any data in the db

                if (cursor.getCount()== 0 ){
                    messages("Empty Database","Sorry no data was found");
                }else {
                    //proceed to display the selected data
                    //use the string buffer to append and display the records
                    StringBuffer buffer = new StringBuffer();

                    //loop through the selected data that is on your cursor to display

                    while (cursor.moveToNext()){
                        buffer.append(cursor.getString(0)+"\t");//zero is the column for first name
                        buffer.append(cursor.getString(1)+"\t");//one is the column for the last name
                        buffer.append(cursor.getString(2)+"\n");//two is the column for the id number

                    }
                    //Display your data using the string buffer on the message dialog
                    messages("DATABASE RECORDS",buffer.toString());
                }
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the id to use as aunique identifier  to delete any raw

                String id = mId.getText().toString().trim();
                //Check if the user id=s deleting with an empty Id field

                if (id.isEmpty()){
                    messages("ID Error","Please enter the id number");
                }else {
                    Cursor cursor = db.rawQuery("SELECT * FROM citizens WHERE id_number = '"+id+"'",null);
                    //proceed to delete

                    db.execSQL("DELETE FROM citizens WHERE id_number = '"+id+"'");
                    messages("SUCCESS","User Deleted Sucessfully");
                    mId.setText("");
                }
            }
        });
    }
    //Message display function
    public  void messages(String title , String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.create().show();
    }
}

