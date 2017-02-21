package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.BackPressCloseHandler;
import kr.wdream.wplusshop.common.DBManager;

/**
 * Created by SEO on 2016-01-06.
 */
public class PassLockNumber extends Activity {
    public String DBName = "WPLUSSHOP.db";
    public String tableName = "USER_SETTING";
    public DBManager dbManager = null;
    public static String proc_type;
    public static String check_pass;
    public static String user_no;
    public String txtComment = "";

    private BackPressCloseHandler backPressCloseHandler;
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.set_pass_lock_number);
//        Intro.at.add(this);
        Intent intent = getIntent();
        proc_type = intent.getExtras().getString("proc_type");
        check_pass = intent.getExtras().getString("check_pass");
        if(proc_type.equals("lock"))            txtComment = "암호 잠금 비밀번호를 입력해주세요.";
        else if(proc_type.equals("lock_check")) txtComment = "확인을 위해 한번 더 입력해 주세요.";
        else if(proc_type.equals("check")) txtComment = "wplus 암호 잠금 비밀번호를 입력해주세요.";

        ((TextView) findViewById(R.id.txt_comment)).setText(txtComment);
        dbManager = new DBManager(getApplicationContext(), DBName, null, 4);

        backPressCloseHandler = new BackPressCloseHandler(this);

        Main.locationHistory.add(this);
    }

    public void btnClick(View view) {
        String clickStr = (String) view.getTag();
        if (clickStr.equals("back")) {
            String passwd = ((EditText) findViewById(R.id.txt_pass)).getText().toString();
            if (passwd.length() > 0) {
                passwd = passwd.substring(0, passwd.length() - 1);
                ((EditText) findViewById(R.id.txt_pass)).setText(passwd);
            }
        }
        else{
            String passwd = ((EditText) findViewById(R.id.txt_pass)).getText().toString();
            if (passwd.length() < 4) {
                passwd += clickStr;
                if(proc_type.equals("check") && passwd.length() == 4){
                    String[][] userData;
//                    userData = dbManager.selectQuery("select lock_pw from user_setting where user_no = '"+ user_no +"'");
                    userData = dbManager.selectQuery("SELECT LOCK_PW FROM USER_SETTING");
                    if(!userData[0][0].equals(passwd)) {
                        Toast.makeText(this, "암호 잠금 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        passwd = "";
                    }else{
                        HashMap<String, String> loginInfo = (HashMap<String, String>) getIntent().getSerializableExtra("LOGININFO");
                        Intent intent = new Intent(this, Main.class);
                        intent.putExtra("LOGININFO", loginInfo);
                        startActivity(intent);
                        finish();
                    }
                }
                else if((!proc_type.equals("check")) && passwd.length() == 4){
                    if(proc_type.equals("lock")){
                        Intent intent = new Intent(this, PassLockNumber.class);
                        intent.putExtra("proc_type", "lock_check");
                        intent.putExtra("check_pass", passwd);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        if(check_pass.equals(passwd)){
                            HashMap<String, String> updateParams = new HashMap<String, String>();    // update columns
                            updateParams.put("LOCK_YN", "'Y'");
                            updateParams.put("LOCK_PW", "'"+ passwd +"'");
                            HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
                            try {
                                dbManager.update(tableName, updateParams, whereParams);
                                Toast.makeText(getApplicationContext(), "암호 잠금 설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            finish();
                        }
                        else{
                            ((EditText) findViewById(R.id.txt_pass)).setText("");
                            Toast.makeText(this, "암호 잠금 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                ((EditText) findViewById(R.id.txt_pass)).setText(passwd);
            } else {
                Toast.makeText(this, "4자 이상 입력할수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if(proc_type.equals("check"))   backPressCloseHandler.onBackPressed();
        else                            finish();
    }
}
