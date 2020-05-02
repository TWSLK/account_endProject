package com.account.work.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.account.work.R;
import com.account.work.activity.base.BaseActivity;
import com.account.work.utils.SPUtils;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText nick;
    private EditText pwd;
    private Button login;
    private Button reg;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SPUtils.init(this);
        nick = (EditText) findViewById(R.id.nick);
        pwd = (EditText) findViewById(R.id.pwd);
        login = (Button) findViewById(R.id.login);
        reg = (Button) findViewById(R.id.reg);
        login.setOnClickListener(this);
        reg.setOnClickListener(this);
        initToolbar();
        getSupportActionBar().setTitle("登陆注册");
        if(!TextUtils.isEmpty(SPUtils.getString("user")) && !getIntent().getBooleanExtra("nojump",false)){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    private void initToolbar(){
        //更严谨是通过反射判断是否存在Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null){
            supportActionBar(mToolbar);
            setSupportActionBar(mToolbar);
        }
    }

    protected ActionBar supportActionBar(Toolbar toolbar){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return actionBar;
    }

    @Override
    public void onClick(View v) {
        String n = nick.getText().toString();
        String p = pwd.getText().toString();
        if(TextUtils.isEmpty(n) || TextUtils.isEmpty(p)){
            Toast.makeText(this, "密码或用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(v == login){
            String pn = SPUtils.getString(n);
            if(TextUtils.isEmpty(pn)){
                Toast.makeText(this, "用户不存在", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!pn.equalsIgnoreCase(p)){
                Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                return;
            }
            SPUtils.putString("user",n);
            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }else if(v == reg){
            SPUtils.putString(n,p);
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
        }
    }
}
