package com.example.myapplication.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.DB.DBConnect;
import com.example.myapplication.R;
import com.example.myapplication.ui.home.search.search;
import com.example.myapplication.ui.wenwuList.Wenwu;
import com.example.myapplication.ui.wenwuList.WenwuAdapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HomeAct extends Activity {
    private List<Wenwu> wenwuList=new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private WenwuAdapter mWenwuAdapter;
    private Context con;
    private Button searchButton;
    private EditText et;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Log.v("test","1");
        con=this;
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        Log.v("test","1");
        layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        Log.v("test","1");
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Log.v("test","1");
        initWenWuList();
        Log.v("test","1");
        searchButton=(Button)findViewById(R.id.search_btn_back);
        et=(EditText)findViewById(R.id.search_et_input);
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeAct.this, search.class);
                intent.putExtra("word",et.getText().toString().trim());
                startActivity(intent);
            }
        });
    }
    public void initWenWuList(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    Connection conn = DBConnect.GetConnection();
                    String sql = "select * from wenwu";
                    Statement st = (Statement) conn.createStatement();
                    ResultSet rs=st.executeQuery(sql);
                    while(rs.next()){
                        Wenwu wenwu=new Wenwu(rs.getString("wname"),Integer.parseInt(rs.getString("visnum")));
                        wenwuList.add(wenwu);
                    }
                    myhandler.sendEmptyMessage(1);
                    st.close();
                    conn.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }).start();
    };
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Handler myhandler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                    mWenwuAdapter=new WenwuAdapter(con,wenwuList);
                    Log.v("test","1");
                    recyclerView.setAdapter(mWenwuAdapter);
                    Log.v("test","1");
                    break;
                case 0:
                    break;
            }
        }
    };
}
