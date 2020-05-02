package com.account.work.note.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.text.Spannable;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.account.work.R;
import com.account.work.note.db.DBManager;
import com.account.work.note.model.Note;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText titleEt;
    private EditText contentEt;
    private FloatingActionButton saveBtn;
    private int noteID = -1;
    private DBManager dbManager;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_text);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //初始化
    private void init() {
        dbManager = new DBManager(this);
        titleEt = (EditText) findViewById(R.id.note_title);
        contentEt = (EditText) findViewById(R.id.note_content);
        saveBtn = (FloatingActionButton) findViewById(R.id.save);
        saveBtn.setOnClickListener(this);
        //name，defaultValue
        noteID = getIntent().getIntExtra("id", -1);
        if (noteID != -1) {
            showNoteData(noteID);
        }
        setStatusBarColor();
    }

    //设置状态栏同色
    public void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#ff303F9F"));
    }

    //显示更新的数据
    private void showNoteData(int id) {
        Note note = dbManager.readData(id);
        titleEt.setText(note.getTitle());
        contentEt.setText(note.getContent());
        //控制光标
        Spannable spannable = titleEt.getText();
        Selection.setSelection(spannable, titleEt.getText().length());
    }

    @Override
    public void onClick(View view) {
        String title = titleEt.getText().toString();
        String content = contentEt.getText().toString();
        String time = getTime();
        if (noteID == -1) {
            //默认添加
            dbManager.addToDB(title, content, time);
        } else {
            //更新
            dbManager.updateNote(noteID, title, content, time);
        }
        Intent i = new Intent(EditNoteActivity.this, MainActivity.class);
        startActivity(i);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.finish();
    }

    //得到时间
    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm E");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

    //按返回键时
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditNoteActivity.this, MainActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.finish();
    }


}
