package kr.wdream.wplusshop.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.app.dialog.CompleteDialog;
import kr.wdream.wplusshop.app.dialog.NFCDialog;
import kr.wdream.wplusshop.common.AuthTask;
import kr.wdream.wplusshop.common.Capture;
import kr.wdream.wplusshop.common.CardInfo;
import kr.wdream.wplusshop.common.util.PointUtil;
import kr.wdream.wplusshop.common.util.StoreAuthUtil;


/**
 * Created by deobeuldeulim on 2016. 10. 20..
 */
public class PayActivity extends Activity implements View.OnClickListener, DialogInterface.OnDismissListener{

    private static final String TAG = "PayActivity";

    //최초 적립, 결제 분기처리
    public static final int STATE_PAY  = 0;
    public static final int STATE_SAVE = 1;

    private int STATE;

    //결제 및 적립하기 레이아웃
    private Button btnSubmit;
    private EditText edtPoint;

    private ProgressDialog progDialog;

    private TextView txtCardNo;
    private TextView txtPoint;

    private String cardNo;
    private String cardPW;
    private String remainPoint;
    private String usePoint;

    private int intRemainPoint;
    private int intUsePoint;

    private CompleteDialog dialog;

    private HashMap<String,String> paramPay;
    private HashMap<String,String> paramSave;

    //NFC 설정을 위한 변수
    private NfcAdapter mNfcAdapter;
    private NdefMessage mNdefMessage;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    private String nfc_card="";

    private String stCode;

    private LinearLayout btnBack;
    private TextView txtTitle;

    private boolean isNFC;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        initNFC();

        getIntentData();

        getCardInfo();

        initView();
    }

    private void getCardInfo(){
        cardPW = CardInfo.getCardPw();
        cardNo = CardInfo.getCardNo();
    }

    private void getIntentData(){
        Intent getIntent = getIntent();

        STATE = getIntent.getIntExtra("STATE", 0);

        remainPoint = getIntent.getStringExtra("point");
        Log.d(TAG, "CardInfo CardNo : " + CardInfo.getCardNo());
        intRemainPoint = Integer.parseInt(remainPoint);

        Log.d(TAG, "RemainPoint : " + intRemainPoint);

    }

    private void initView(){

        btnBack = (LinearLayout)findViewById(R.id.icon_head_left);
        btnBack.setOnClickListener(this);
        txtTitle = (TextView)findViewById(R.id.txt_head_center);
        if(STATE == STATE_PAY){
            txtTitle.setText("포인트 사용하기");
        }else{
            txtTitle.setText("포인트 적립하기");
        }

        txtCardNo = (TextView)findViewById(R.id.txt_card_no);
        txtCardNo.setText("회원번호 " + Main.userInfo.get("card_no") + " 님");
        txtPoint = (TextView)findViewById(R.id.txt_point);
        txtPoint.setText(remainPoint + " Point 사용하실 수 있습니다.");
        //결제 및 적립하기 뷰 초기화
        btnSubmit = (Button)findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        if(STATE_PAY == STATE){
            btnSubmit.setText("결제하기");
        }else{
            btnSubmit.setText("적립하기");
            txtPoint.setText("사용한 금액을 입력해주세요.");
        }

        edtPoint  = (EditText)findViewById(R.id.edt_point);
    }


    @Override
    public void onClick(View view) {
        if (view == btnSubmit) {
            NFCDialog dialog = new NFCDialog(PayActivity.this);
            dialog.setOnDismissListener(this);
            dialog.show();
        }

        if (view == btnBack) {
            finish();
        }
    }

    private void startSave(){
        usePoint = edtPoint.getText().toString();

        paramSave = new HashMap<String,String>();

        paramSave.put("mode", "reserve_insert");
        paramSave.put("stcd", stCode);
            paramSave.put("trsubgbncd", "CR");
        paramSave.put("usecardno", cardNo);
        paramSave.put("paymethod","P");
        paramSave.put("payamount", usePoint);
        paramSave.put("OTYPE","J");

        new PayAsyncThread(this).execute();
    }

    private void startPay(){
        usePoint = edtPoint.getText().toString();
        Log.d(TAG, "상은 : usePoint : " + usePoint);

        if(usePoint.length() > 0) {
            intUsePoint = Integer.parseInt(usePoint);

            if (intUsePoint > intRemainPoint) {
                Toast.makeText(this, "포인트가 부족합니다\n잔여 포인트를 확인해주세요", Toast.LENGTH_SHORT).show();
            } else {
                // 결제를 타야합니다
                paramPay = new HashMap<String, String>();

                paramPay.put("mode", "consume_insert");
                paramPay.put("trsubgbncd", "CC");
                paramPay.put("paymethod", "P");
                paramPay.put("stcd", stCode);
                Log.d("상은", "상은상은 : " + stCode);
                paramPay.put("usecardno", cardNo);
                paramPay.put("usecardpw", cardPW);
                paramPay.put("payamount", usePoint);
                paramPay.put("OTYPE","J");

                new PayAsyncThread(this).execute();

            }
        }else{
            Toast.makeText(this, "결제할 금액을 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        NFCDialog dialog = (NFCDialog)dialogInterface;
        isNFC = dialog.returnValue();

        if (isNFC) {
            progDialog = new ProgressDialog(PayActivity.this);
            progDialog.setMessage("NFC 카드를 읽는 중 입니다.");
            progDialog.show();
        } else {
            IntentIntegrator integrator = new IntentIntegrator(PayActivity.this);
            integrator.setOrientationLocked(true);
            integrator.setCaptureActivity(Capture.class);
            integrator.initiateScan();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result.getContents() != null) {
            final String code = result.getContents();

            int temp = code.indexOf("@");
            Log.d("상은", "temp = " + temp);

            if (temp == -1) {
                Toast.makeText(this, "올바르지 않은 정보입니다.", Toast.LENGTH_SHORT).show();
            }else {
                String authKey = code.substring(0, temp);
                stCode = code.substring(temp + 1);



                new AuthTask(handler, authKey, stCode).execute();
            }

        }else{
            return;
        }
    }

    private class PayAsyncThread extends AsyncTask{
        private Context context;
        private ProgressDialog progDialog;

        private String code;
        private String message;

        public PayAsyncThread(Context context){
            this.context = context;
        }

        //결제 API를 타는 동안 로딩 다이얼로그 생성
        @Override
        protected void onPreExecute() {

            String dlgMsg = "적립이 진행중입니다.";

            progDialog = new ProgressDialog(context);
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            if (STATE == STATE_PAY) {
                dlgMsg = "결제가 진행중입니다.";
                progDialog.setMessage(dlgMsg);
            }else{
                progDialog.setMessage(dlgMsg);
            }

            progDialog.show();

            super.onPreExecute();
        }


        @Override
        protected Object doInBackground(Object[] objects) {
            if (STATE == STATE_PAY) {
                try {
                    HashMap<String,String> resultPay = PointUtil.selectPay(paramPay);

                    code = resultPay.get("code");
                    message = resultPay.get("message");
                    Log.e(TAG, "Pay code : " + code);


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "포인트 결제 API 실패");
                }
            }

            if (STATE == STATE_SAVE) {
                try {
                    HashMap<String,String> resultPay = PointUtil.selectSave(paramSave);

                    code = resultPay.get("code");
                    message = resultPay.get("message");


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "포인트 적립 API 실패");
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            progDialog.dismiss();

            if (STATE == STATE_PAY) {
//                if("0000".equals(code)) {
                    dialog = new CompleteDialog(PayActivity.this, Main.userInfo.get("card_no"), STATE, usePoint, remainPoint);
                    dialog.show();

                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            finish();
                        }
                    };
                    timer.schedule(task, 3000);
//                }else{
//                    Toast.makeText(context, "사용 실패\n잔여 포인트를 확인해주세요.", Toast.LENGTH_SHORT).show();
//                }
            }

            if (STATE == STATE_SAVE) {
//                if("0000".equals(code)) {
                dialog = new CompleteDialog(PayActivity.this, Main.userInfo.get("card_no"), STATE, usePoint, remainPoint);
                dialog.show();

                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        finish();
                    }
                };
                timer.schedule(task, 3000);
//                }else{
//                    Toast.makeText(context, "적립 실패\n관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
//                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initNFC(){
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
//
        mNdefMessage = new NdefMessage(createNewTextRecord(nfc_card, Locale.ENGLISH, true));
        Log.d("dev_test", "member_cardno : " + createNewTextRecord(nfc_card, Locale.ENGLISH, true));

        // create an intent with tag data and deliver to this activity
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[]{ndefIntent};
        } catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }
        mNFCTechLists = new String[][]{new String[]{NfcF.class.getName()}};
    }

    public static NdefRecord createNewTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;

        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    @Override
    public void onNewIntent(Intent intent) {
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Log.d("dev_test", "tag : " + tag);
        String s = "";

        // parse through all NDEF messages and their records and pick text type only
        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (data != null) {
            try {
                for (int i = 0; i < data.length; i++) {
                    NdefRecord[] recs = ((NdefMessage) data[i]).getRecords();
                    for (int j = 0; j < recs.length; j++) {
                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {
                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;
                            s += (new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1, textEncoding));
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }
        }

        int temp = s.indexOf("@");
        Log.d("상은", "temp = " + temp);

        if (temp == -1) {
            Toast.makeText(this, "올바르지 않은 정보입니다.", Toast.LENGTH_SHORT).show();
        }else {
            String authKey = s.substring(0, temp);
            stCode = s.substring(temp + 1);



            new AuthTask(handler, authKey, stCode).execute();
        }
        //태그 이후 원하는 동작 구성
        
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case AuthTask.DIALOG_START:
                    break;
                
                case AuthTask.DIALOG_STOP:
                    break;
                
                case AuthTask.TASK_SUCCESS:
                    String code    = msg.getData().getString("code");
                    String message = msg.getData().getString("msg");
                    if ("0000".equals(code)) {
                        stCode = msg.getData().getString("stCode");
                        if (STATE == STATE_PAY) {
                            Log.d(TAG, "BTN_STATE : " + STATE);
                            startPay();
                        }else if(STATE == STATE_SAVE){
                            Log.d(TAG, "BTN_STATE : " + STATE);
                            startSave();
                        }
                        if (progDialog != null) {
                            progDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(PayActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    
                    break;
                
                case AuthTask.TASK_FAILED:
                    Toast.makeText(PayActivity.this, "시스템 오류", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    
    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundNdefPush(this, mNdefMessage);
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundNdefPush(this);
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }
}
