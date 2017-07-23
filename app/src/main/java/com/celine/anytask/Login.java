package com.celine.anytask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity{
    MySQLiteHelper helper = new MySQLiteHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void onButtonClick(View v){

        if(v.getId() == R.id.BLogin){

            EditText a = (EditText)findViewById(R.id.TFusername);
            String name = a.getText().toString();
            EditText b = (EditText)findViewById(R.id.TFpassword);
            String pass = b.getText().toString();
            String password = helper.searchPass(name);

            if(pass.equals(password)){
                Intent i = new Intent(Login.this,Homepage.class);
                i.putExtra("Username",name);
                startActivity(i);
            }
            else {
                //pop up message
                Toast skip = Toast.makeText(this, R.string.notmatch_toast, Toast.LENGTH_SHORT);
                skip.show();
            }

        }

        if(v.getId() == R.id.BSignUp){

            Intent i = new Intent(Login.this,SignUp.class);
            startActivity(i);

        }

    }

}
