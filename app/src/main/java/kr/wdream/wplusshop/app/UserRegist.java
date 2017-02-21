package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.DBManager;

/**
 * Created by SEO on 2015-12-28.
 */
public class UserRegist extends Activity implements View.OnClickListener{
    public String DBName = "WPLUSSHOP.db";
    public String tableName = "USER_LIST";

    public Intent intent;
    String user_mobile1 = "";
    String user_sex = "";
    String user_idx = "";
    String proc_str = "등록";
    DBManager dbManager = null;

    public int user_sex_num = 0;
    public int user_mobile1_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_regist);


        intent = getIntent();
        try {
            user_idx = intent.getExtras().getString("USER_IDX");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        dbManager = new DBManager(this.getApplicationContext(), DBName, null, 4);
        if(!user_idx.equals("")){
            String[] selectParams = {"IDX", "USER_NAME", "USER_PHONE", "USER_PHONE1", "USER_PHONE2", "USER_PHONE3", "USER_SEX", "USER_CARDNO"};    // select columns
            HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
            whereParams.put("IDX", user_idx);
            HashMap<String, String> userSetRst = null;
            try {
                userSetRst = dbManager.select(tableName, selectParams, whereParams);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (userSetRst.size() > 0) {
                ((EditText) findViewById(R.id.txt_user_name)).setText(userSetRst.get("USER_NAME"));

                Resources res = getResources();
                String[] user_sex= res.getStringArray(R.array.user_sex);
                for(int i = 0; i < user_sex.length; i++) {
                    if (user_sex[i].equals(userSetRst.get("USER_SEX").equals("F") ? "여자" : "남자" )) {
                        user_sex_num = i;
                        break;
                    }
                }

                String[] user_mobile1= res.getStringArray(R.array.user_mobile1);
                for(int i = 0; i < user_mobile1.length; i++) {
                    if (user_mobile1[i].equals(userSetRst.get("USER_PHONE1"))){
                        user_mobile1_num = i;
                        break;
                    }
                }

                ((EditText) findViewById(R.id.txt_user_mobile2)).setText(userSetRst.get("USER_PHONE2"));
                ((EditText) findViewById(R.id.txt_user_mobile3)).setText(userSetRst.get("USER_PHONE3"));
                ((EditText) findViewById(R.id.txt_user_cardno)).setText(userSetRst.get("USER_CARDNO"));
            }
            proc_str = "수정";
        }

        ((TextView) findViewById(R.id.txt_head_center)).setText("친구 "+ proc_str);
        LinearLayout before_bullet = (LinearLayout)findViewById(R.id.icon_head_left);
        before_bullet.setVisibility(View.VISIBLE);
        before_bullet.setOnClickListener(this);

        ((TextView)findViewById(R.id.txt_btn_action)).setText(proc_str +"하기");

        final Spinner spinner = (Spinner)findViewById(R.id.spinner_user_sex);
        spinner.setPrompt("성별을 선택하세요.");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user_sex,
                R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(user_sex_num);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_sex = (String)spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final Spinner spinner2 = (Spinner)findViewById(R.id.spinner_user_mobile1);
        spinner2.setPrompt("휴대폰번호 앞자리를 선택하세요.");
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.user_mobile1,
                R.layout.spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(user_mobile1_num);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_mobile1 = (String)spinner2.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void btnClick(View v){
        switch (v.getId()){
            case R.id.btn_user_regist:
                String user_name    = ((EditText) findViewById(R.id.txt_user_name)).getText().toString();
                String user_mobile2 = ((EditText) findViewById(R.id.txt_user_mobile2)).getText().toString();
                String user_mobile3 = ((EditText) findViewById(R.id.txt_user_mobile3)).getText().toString();
                String user_cardno  = ((EditText) findViewById(R.id.txt_user_cardno)).getText().toString();

                if(user_name.equals(""))
                    Toast.makeText(this, "이름을 입력해 주세요.",Toast.LENGTH_SHORT).show();
                else if(user_mobile2.equals("") || user_mobile3.equals(""))
                    Toast.makeText(this, "휴대폰 번호를 입력해 주세요.",Toast.LENGTH_SHORT).show();
                else if(user_cardno.equals("") || user_cardno.length() < 16)
                    Toast.makeText(this, "카드 번호를 정확히 입력해 주세요.",Toast.LENGTH_SHORT).show();
                else{
                    String user_mobile  = user_mobile1 +"-"+ user_mobile2 +"-"+ user_mobile3;
                    String user_sex_str = "";
                    user_sex_str = user_sex.equals("남자") ? "M" : "F";
                    String sql;
                    if(user_idx.equals("")) {
                        sql = "insert into "+ tableName +"('USER_NAME', 'USER_PHONE', 'USER_PHONE1', 'USER_PHONE2', 'USER_PHONE3', 'USER_SEX', 'USER_CARDNO') " +
                                "values('" + user_name + "', '" + user_mobile + "', '" + user_mobile1 + "', '" + user_mobile2 + "', '" + user_mobile3 + "', '" + user_sex_str + "', '" + user_cardno + "');";
                        dbManager.insert(sql);
                    }
                    else {
                        HashMap<String, String> updateParams = new HashMap<String, String>();    // update columns
                        updateParams.put("USER_NAME"    , "'"+ user_name +"'");
                        updateParams.put("USER_PHONE"   , "'"+ user_mobile +"'");
                        updateParams.put("USER_PHONE1"  , "'"+ user_mobile1 +"'");
                        updateParams.put("USER_PHONE2"  , "'"+ user_mobile2 +"'");
                        updateParams.put("USER_PHONE3"  , "'"+ user_mobile3 +"'");
                        updateParams.put("USER_SEX"     , "'"+ user_sex_str +"'");
                        updateParams.put("USER_CARDNO"  , "'"+ user_cardno +"'");

                        HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
                        whereParams.put("IDX", user_idx);
                        try {
                            dbManager.update(tableName, updateParams, whereParams);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(this, proc_str +"이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }

                Log.d("test_wplus", "user_name : "+ user_name);
                Log.d("test_wplus", "user_sex : "+ user_sex);
                Log.d("test_wplus", "user_mobile1 : "+ user_mobile1);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon_head_left:
                finish();
                break;
        }
    }
}
