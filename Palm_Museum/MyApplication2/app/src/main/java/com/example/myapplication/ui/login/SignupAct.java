package com.example.myapplication.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.DB.DBConnect;
import com.example.myapplication.R;
import com.example.myapplication.ui.home.HomeAct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SignupAct extends AppCompatActivity {
    private Button backToLogin;
    private Button signUpAct;
    private EditText userName;
    private EditText password;
    private EditText rePassword;
    String userNameString = null;
    String passwordString = null;
    public int flag=1;
    private final String words= new String("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        backToLogin = (Button)findViewById(R.id.signUpBackToLogIn);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignupAct.this,LoginAct.class);
                startActivity(intent);
                finish();
            }
        });
        flag=1;
        signUpAct = (Button)findViewById(R.id.signup);
        signUpAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = (EditText)findViewById(R.id.signUpUsername);
                password = (EditText)findViewById(R.id.signUpPassword);
                rePassword = (EditText)findViewById(R.id.signUpRePassword);
                userNameString = new String(userName.getText().toString().trim());
                passwordString = new String(password.getText().toString().trim());
                String rePassWord = new String(rePassword.getText().toString());
                if(userNameString.equals(passwordString)){
                    Toast.makeText(getApplicationContext(),"??????????????????????????????!" ,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!passwordString.equals(rePassWord)) {
                    Toast.makeText(getApplicationContext(), "???????????????????????????!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(userNameString.isEmpty()){
                    Toast.makeText(getApplicationContext(), "?????????????????????!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwordString.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "??????????????????!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwordString.length()<6){
                    Toast.makeText(getApplicationContext(), "??????????????????6???!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwordString.length()>16){
                    Toast.makeText(getApplicationContext(), "??????????????????16???!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(userNameString.length()>10){
                    Toast.makeText(getApplicationContext(), "?????????????????????10???!", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable(){
                    @Override
                    public void run(){
                        try {
                            Connection cn = DBConnect.GetConnection();
                            String sql = "select username from users where username = '" + userNameString + "'";
                            Statement stmt = null;
                            stmt = cn.createStatement();
                            ResultSet rs=stmt.executeQuery(sql);
                            if(rs.next()){
                                myhandler.sendEmptyMessage(0);
                                flag=0;
                            }
                            else flag=2;
                            stmt.close();
                            cn.close();
                        }catch(SQLException e){
                            e.printStackTrace();
                        }
                    }
                }).start();
                new Thread(new Runnable(){
                    @Override
                    public void run(){
                        while(flag==1)continue;
                        if(flag==0)return;

                        int u=0;
                        try{
                            Connection cn=DBConnect.GetConnection();
                            String sql="insert into users(username,pwd) values('"+userNameString+"',MD5('"+passwordString+"'))";
                            Log.v("test",sql);
                            PreparedStatement pst;
                            pst=(PreparedStatement)cn.prepareStatement(sql);
                            u=pst.executeUpdate();
                            pst.close();
                            cn.close();
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                        myhandler.sendEmptyMessage(1);
                    }
                }).start();
                /*if(){
                    Toast.makeText(getApplicationContext(), "??????????????????!", Toast.LENGTH_SHORT).show();
                    return;
                }
                 */

            }
        });
    }
    private Handler myhandler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                    Intent intent=new Intent(SignupAct.this,LoginAct.class);
                    intent.putExtra("signup",true);
                    startActivity(intent);
                    finish();
                case 0:
                    userName.setError("??????????????????");
                    break;
            }
        }
    };
}