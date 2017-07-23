package com.celine.anytask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by peiwe on 22/07/2017.
 */

public class SignUp extends AppCompatActivity {

    MySQLiteHelper helper = new MySQLiteHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item){ //back icon
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRegisterClick(View v){
        if(v.getId() == R.id.BRegister){
            EditText u_name = (EditText)findViewById(R.id.TFusername);
            EditText email = (EditText)findViewById(R.id.TFemail);
            EditText pass = (EditText)findViewById(R.id.TFpassword);
            EditText com_pass = (EditText)findViewById(R.id.TFcomfirmpass);

            String u_namestr = u_name.getText().toString();
            String emailstr = email.getText().toString();
            String passstr = pass.getText().toString();
            String com_passstr = com_pass.getText().toString();

            if(!(u_namestr.isEmpty() && emailstr.isEmpty() && passstr.isEmpty() && com_passstr.isEmpty())){

                if(!passstr.equals(com_passstr)){
                    //pop up message
                    Toast.makeText(SignUp.this,R.string.notmatch_toast,Toast.LENGTH_SHORT).show();
                }



                else if(passstr.isEmpty()||pass.length()!=8){
                    Toast.makeText(SignUp.this,R.string.shortpass_toast,Toast.LENGTH_SHORT).show();
                }

                else{
                    //save in database
                    Data d = new Data();
                    d.setUsername(u_namestr);
                    d.setEmail(emailstr);
                    d.setPassword(passstr);

                    helper.insertData(d);
                    Toast.makeText(SignUp.this,"Register Success",Toast.LENGTH_SHORT).show();
                }

            }
            else{
                Toast.makeText(SignUp.this,R.string.successreg_toast,Toast.LENGTH_SHORT).show();
            }

        }
    }

}