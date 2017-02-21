package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.DBManager;

/**
 * Created by SEO on 2015-12-18.
 */
public class UserSetting extends Activity implements View.OnClickListener{
    public Intent intent = null;
    public String DBName = "WPLUSSHOP.db";
    public String tableName = "USER_SETTING";
    public String txtDomain = "";
    public EditText txtInputDomain;
    public DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting);

        ((TextView)findViewById(R.id.txt_head_center)).setText("쇼핑몰 설정");
        LinearLayout before_bullet = (LinearLayout)findViewById(R.id.icon_head_left);
        before_bullet.setVisibility(View.VISIBLE);
        before_bullet.setOnClickListener(this);


        dbManager = new DBManager(getApplicationContext(), DBName, null, 4);
        String[] selectParams = {"LOCATION_DOMAIN"};    // select columns
        HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
        HashMap<String, String> userSetRst = null;
        try {
            userSetRst = dbManager.select(tableName, selectParams, whereParams);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!userSetRst.isEmpty()) {
            if(!userSetRst.get("LOCATION_DOMAIN").equals(""))
                txtDomain = userSetRst.get("LOCATION_DOMAIN");
        }

        txtInputDomain = (EditText)findViewById(R.id.txt_domain);
        txtInputDomain.setText(txtDomain);

        Main.locationHistory.add(this);

    }

    public void btnClick(View v){
        switch (v.getId()) {
            case R.id.btn_ok:
                HashMap<String, String> updateParams = new HashMap<String, String>();    // update columns
                updateParams.put("LOCATION_DOMAIN", "'"+ txtInputDomain.getText().toString() +"'");
                HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
//                whereParams.put("user_no", user_no);

                try {
                    dbManager.update(tableName, updateParams, whereParams);
                    Toast.makeText(this, "쇼핑몰 설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_head_left:
                finish();
                break;
        }
    }
}
