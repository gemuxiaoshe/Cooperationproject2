package cn.gemuxiaoshe.cooperationproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import util.IdentifyingCode;
import util.Sqliteopenhelper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
private Sqliteopenhelper sqliteopenhelper;
private Spinner spinner;
private ImageView identifyingCode;
private EditText edit_name;
private EditText edit_pass;
private EditText edit_flag;
private String realCode="";
private EditText edit_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edit_name = findViewById(R.id.register_username);//用户名
       edit_pass = findViewById(R.id.register_password);//密码
        edit_flag = findViewById(R.id.register_findpwd);//找回方式
       edit_image = findViewById(R.id.register_code);//验证码输入
        Button button = findViewById(R.id.register_btn);
        spinner = findViewById(R.id.register_select);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_spinner_item,getData());
        spinner.setAdapter(adapter);
        sqliteopenhelper = new Sqliteopenhelper(this, "Users.db", null, 1);
        button.setOnClickListener(this);
        identifyingCode = (ImageView)findViewById(R.id.register_image);
        identifyingCode.setOnClickListener(this);
        identifyingCode.setImageBitmap(IdentifyingCode.getInstance().createBitmap());//初始化验证码
        realCode=IdentifyingCode.getInstance().getCode().toLowerCase();//正确验证码
}

    private List<String> getData() {
        List<String> dataList = new ArrayList<String>();
        dataList.add("phone");
        dataList.add("email");
        return dataList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn:
                {

                    if((edit_image.getText().toString())!=null) {
                       if ((edit_image.getText().toString()).equals(realCode)) {
                            SQLiteDatabase db = sqliteopenhelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("username", edit_name.getText().toString());
                            values.put("userpass", edit_pass.getText().toString());
                            values.put(spinner.getSelectedItem().toString(), edit_flag.getText().toString());
                            db.insert("User", null, values);
                            Toast.makeText(RegisterActivity.this, "成功注册", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                        }else {
                           Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
                       }
                    }

            }
            break;
            case R.id.register_image:
            {
                identifyingCode.setImageBitmap(IdentifyingCode.getInstance().createBitmap());
                realCode=IdentifyingCode.getInstance().getCode().toLowerCase();
            }
            break;
        }
    }
}
