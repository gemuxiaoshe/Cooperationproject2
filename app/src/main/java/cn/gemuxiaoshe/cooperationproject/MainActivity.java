package cn.gemuxiaoshe.cooperationproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUtil;
import util.Imageutil;
import util.SqlUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
private  String enen="";//图片得到的地址private  String bingpic="";//最后加载图片的地址
private  String imageurl="";
private ImageView bingPicImg;
private Handler handler=new Handler(){
     @Override
     public void handleMessage(Message msg) {
         super.handleMessage(msg);
      //   Object result=msg.obj;
       //  enen=result.toString();
         final String bingPic="https://cn.bing.com"+imageurl;
         SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
         editor.putString("bing_pic", bingPic);
         editor.apply();
         runOnUiThread(new Runnable() {
             @Override
             public void run() {//使用Glide加载每日一图
//                        RequestOptions requestOptions=new RequestOptions()
//                               .centerCrop();
                 Glide.with(MainActivity.this)
                         .load(bingPic)
                         .into(bingPicImg)
                 ;
             }
         });
     }
 };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bingPicImg = findViewById(R.id.background_view);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic = prefs.getString("bing_pic", null);
        everydayimage();                                                //每日一图的方法
        TextView text_register = findViewById(R.id.signin_register);
        text_register.setOnClickListener(this);                //为注册选项添加一个事件
        Button button =findViewById(R.id.button_login);
        button.setOnClickListener(this);          //登录按钮添加事件
     //loadBingPic();
//       if (bingPic != null) {
//          Glide.with(this).load(bingPic).into(bingPicImg);
//       } else {
//            loadBingPic();
//        }

    }

    private void everydayimage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                URL url = null;
                try {
                    url = new URL("https://cn.bing.com/HPImageArchive.aspx?idx=0&n=1");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in=connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    imageurl=parseXML(response.toString());
                    Message message = Message.obtain();
                    message.what=0;
//                    message.obj=imageurl;
                    handler.sendMessage(message);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
   //解析每日一图返回的xml文件,获取图片的信息
    public  String parseXML(String xmldata) {
        String url="";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int event=xmlPullParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String nodename=xmlPullParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                    {
                        if ("url".equals(nodename)) {
                            url=xmlPullParser.nextText();
                            break;
                        }
                    }

                }
                event=xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回图片的路径
        return url;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                Toast.makeText(this, "你点击了“用户”按键！", Toast.LENGTH_SHORT).show();
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_register:
            {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
            break;
            case R.id.button_login:
            {
                EditText editText_name = findViewById(R.id.edit_id);
                EditText editText_pass = findViewById(R.id.edit_pass);
                String sqlselect="select * from User where username=? and userpass=?";
                SqlUtil sqlUtil=new SqlUtil();
                //连接sqlite数据库，如何数据存在就成功登录，否则就弹出消息框提示账号或者密码错误
                if (sqlUtil.login(this, "Users.db", sqlselect, editText_name.getText().toString(), editText_pass.getText().toString())) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    editText_name.setText(null);
                    editText_pass.setText(null);
                    Toast.makeText(MainActivity.this, "账号或者密码错误", Toast.LENGTH_SHORT).show();
                }



            }
        }
    }
}

