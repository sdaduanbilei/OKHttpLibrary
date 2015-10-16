package com.sda.myokhttp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.sda.library.HttpCore.DataResponse;
import com.sda.library.Util.Tools;

public class MainActivity extends AppCompatActivity {

    Context context;
    TextView title;

    String url = "http://m.ch999.com/app/ProductSearch.aspx?act=GetHome&cityid=39";

    DataHandler datahandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = (TextView) findViewById(R.id.title);
        context = MainActivity.this;
        datahandler = new DataHandler();
        loadData();


    }

    class DataHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                DataControl.Test1(context, url, new DataResponse() {
                    @Override
                    public void onSucc(Object o) {
                        title.setText(title.getText() + "\n" + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + o.toString());
                    }

                    @Override
                    public void onFail(String error) {
                        Tools.msgbox(context, error);
                    }
                });
            }
        }
    }


    private void loadData() {
        for (int i = 0; i < 1; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        datahandler.obtainMessage(1).sendToTarget();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
