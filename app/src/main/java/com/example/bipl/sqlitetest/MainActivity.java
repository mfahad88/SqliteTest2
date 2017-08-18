package com.example.bipl.sqlitetest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btn_add,btn_edit,btn_delete;
    private EditText fname,lname;
    private Dbhelper dbhelper;
    private Spinner spinner;
    private ArrayAdapter adapter;
    private String firstname=null;
    private String lastname=null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_add=(Button)findViewById(R.id.button);
        btn_edit=(Button)findViewById(R.id.button2);
        btn_delete=(Button)findViewById(R.id.button3);
        fname=(EditText)findViewById(R.id.editText);
        lname=(EditText)findViewById(R.id.editText2);
        spinner=(Spinner)findViewById(R.id.spinner);
        dbhelper=new Dbhelper(this);


        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1);
        fillSpinner(getApplicationContext(),spinner,adapter,dbhelper,fname,lname);
        //spinner.setAdapter(adapter);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fname.getText().equals(null) && !lname.getText().equals(null)){
                    if(dbhelper.insertRecord(fname.getText().toString(),lname.getText().toString())){
                        Toast.makeText(MainActivity.this, "Record Saved...", Toast.LENGTH_SHORT).show();
                        fillSpinner(getApplicationContext(),spinner,adapter,dbhelper,fname,lname);
                        fname.setText(null);
                        lname.setText(null);
                    }
                }
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbhelper.updateRecord(spinner.getSelectedItemPosition(),fname.getText().toString(),lname.getText().toString());
                    Toast.makeText(MainActivity.this, "Record Updated", Toast.LENGTH_SHORT).show();
                    fillSpinner(getApplicationContext(),spinner,adapter,dbhelper,fname,lname);
                    fname.setText(null);
                    lname.setText(null);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbhelper.deleteRecord(spinner.getSelectedItemPosition());
                Toast.makeText(MainActivity.this, "Record Deleted...", Toast.LENGTH_SHORT).show();
                fillSpinner(getApplicationContext(),spinner,adapter,dbhelper,fname,lname);
                fname.setText(null);
                lname.setText(null);
            }
        });

    }

    protected void fillSpinner(final Context context, Spinner spn, final ArrayAdapter arrayAdapter, Dbhelper dbhelper1, final TextView fn, final TextView ln){
        if(!arrayAdapter.isEmpty()){
            arrayAdapter.clear();
        }
        for(int i=0;i<dbhelper1.getRecords().size();i++){
            arrayAdapter.addAll(dbhelper1.getRecords().get(i).getFname()+" - "+dbhelper1.getRecords().get(i).getLname());
        }
        arrayAdapter.notifyDataSetChanged();
        spn.setAdapter(arrayAdapter);
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                firstname=arrayAdapter.getItem(position).toString().substring(0,arrayAdapter.getItem(position).toString().indexOf("-")-1);
                lastname=arrayAdapter.getItem(position).toString().substring(arrayAdapter.getItem(position).toString().indexOf("-")+1,arrayAdapter.getItem(position).toString().length());
                fn.setText(firstname.trim());
                ln.setText(lastname.trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        dbhelper.close();
        Toast.makeText(this, "Db Closed", Toast.LENGTH_SHORT).show();
    }
}
