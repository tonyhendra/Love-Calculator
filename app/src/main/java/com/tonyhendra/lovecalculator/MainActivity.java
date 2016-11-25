package com.tonyhendra.lovecalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final EditText edt_first = (EditText)findViewById(R.id.edt_first);
        final EditText edt_second = (EditText)findViewById(R.id.edt_second);
        Button btn_check = (Button)findViewById(R.id.btn_check);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(edt_first.getText().toString().trim().equals("")){
                        edt_first.setError("First name is required");
                    }else if(edt_second.getText().toString().trim().equals("")){
                        edt_second.setError("Second name is required");
                    }else{
                        Bundle b = new Bundle();
                        b.putString("first",edt_first.getText().toString().trim());
                        b.putString("second",edt_second.getText().toString().trim());
                        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Cek Inputan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnAbout:
                Intent i = new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
