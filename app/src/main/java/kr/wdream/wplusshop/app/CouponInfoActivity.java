package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.app.dialog.NFCDialog;
import kr.wdream.wplusshop.common.Capture;
import kr.wdream.wplusshop.common.util.CouponUtil;
import kr.wdream.wplusshop.common.util.StoreInfoUtil;

/**
 * Created by deobeuldeulim on 2016. 10. 25..
 */
public class CouponInfoActivity extends Activity implements View.OnClickListener, DialogInterface.OnDismissListener{
    private static final String TAG = "CouponInfoActivity";

    //각 뷰
    private TextView txtName;
    private TextView txtDate;
    private TextView txtRemain;
    private ImageView imgCoupon;
    private ImageView imgInfo;
    private Button btnSubmit;
    private Button btnCancel;

    private TextView txtTitle;
    private LinearLayout btnBack;

    // 쿠폰 정보 변수
    private String cp_nm;
    private String cp_edt;
    private String cp_id;
    private String cp_no;
    private String cp_pub;
    private String cp_pub_nm;
    private String cp_photo;

    //NFC 설정을 위한 변수
    private NfcAdapter mNfcAdapter;
    private NdefMessage mNdefMessage;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    private String nfc_card="";

    private String st_cd;

    private ProgressDialog progDialog;
    private DialogInterface popDialog;

    private boolean isNFC;
    private int STATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_info);

        getDataIntent();

        initView();

        new getStoreInfoTask().execute();

        initNFC();
    }

    private void initView() {
        progDialog = new ProgressDialog(this);

        txtTitle = (TextView)findViewById(R.id.txt_head_center);
        txtTitle.setText(cp_nm);
        btnBack = (LinearLayout)findViewById(R.id.icon_head_left);
        btnBack.setOnClickListener(this);

        txtName = (TextView)findViewById(R.id.txt_cp_nm);
        txtName.setText(cp_nm);
        txtDate = (TextView)findViewById(R.id.txt_cp_edt);
        txtDate.setText(cp_edt);

        txtRemain = (TextView)findViewById(R.id.txt_remain_date);
        String dday = getRemainDate(cp_edt);
        txtRemain.setText(dday);

        imgCoupon = (ImageView)findViewById(R.id.img_coupon);
        try {
            URL url = new URL(cp_photo);
            Bitmap bm = BitmapFactory.decodeStream(url.openStream());
            imgCoupon.setImageBitmap(bm);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(this, cp_nm + R.string.get_img_err , Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, cp_nm + R.string.get_img_err , Toast.LENGTH_SHORT).show();
        }

        imgInfo   = (ImageView)findViewById(R.id.img_info);

        btnSubmit = (Button)findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
    }

    private void getDataIntent(){
        Intent couponData = getIntent();

        cp_nm = couponData.getStringExtra("cp_nm");
        cp_edt = couponData.getStringExtra("cp_edt");
        cp_id = couponData.getStringExtra("cp_id");
        cp_no = couponData.getStringExtra("cp_no");
        cp_pub = couponData.getStringExtra("cp_pub");
        cp_pub_nm = couponData.getStringExtra("cp_pub_nm");
        cp_photo = couponData.getStringExtra("cp_photo");
    }

    private String getRemainDate(String edt){
        String dday = "";

        int year = Integer.parseInt(edt.substring(0,4));
        int month = Integer.parseInt(edt.substring(4,6));
        int day = Integer.parseInt(edt.substring(6,8));

        Calendar today = Calendar.getInstance();
        Calendar endDay  = Calendar.getInstance();

        endDay.set(year, month, day);

        long temp = endDay.getTimeInMillis()/86400000;
        Log.d(TAG, "temp : " + temp);
        long tday = today.getTimeInMillis()/86400000;
        Log.d(TAG, "Tday : " + tday);

        long count = temp - tday;

        dday = String.valueOf(count+1);

        return dday;
    }

    @Override
    public void onClick(View view) {
        if (view == btnSubmit) {
            NFCDialog dialog = new NFCDialog(CouponInfoActivity.this);
            dialog.setOnDismissListener(this);
            dialog.show();
        }

        if (view == btnCancel) {
            finish();
        }

        if (view == btnBack) {
            finish();
        }
    }


    public void initNFC(){
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNdefMessage = new NdefMessage(createNewTextRecord(nfc_card, Locale.ENGLISH, true));
        }
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

        st_cd = "ST" + s;
        Log.d(TAG, "s : " + s);

        progDialog.dismiss();

        if (st_cd.equals(cp_pub)) {
            new UseCouponTask().execute();
        }else{
            Toast.makeText(CouponInfoActivity.this, cp_pub_nm + "에서 발행한 쿠폰이 아닙니다.\n확인 후 이용해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        NFCDialog dialog = (NFCDialog)dialogInterface;
        isNFC = dialog.returnValue();

        if (isNFC) {
            progDialog.setTitle("NFC 카드 결제");
            progDialog.setMessage("NFC 카드를 읽는 중 입니다.");
            progDialog.show();
        } else {
            IntentIntegrator integrator = new IntentIntegrator(CouponInfoActivity.this);
            integrator.setOrientationLocked(true);
            integrator.setCaptureActivity(Capture.class);
            integrator.initiateScan();
        }
    }

    private class getStoreInfoTask extends AsyncTask<Void, Void, Void>{
        private HashMap<String,String> paramStore;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            paramStore = new HashMap<String,String>();
            paramStore.put("cmd", "/coupon/couponpubprocess.json");
            paramStore.put("mode", "R");
            paramStore.put("cp_pub", cp_pub);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final HashMap<String,String> resultStore = StoreInfoUtil.getStore(paramStore);
                String code = resultStore.get("code");

                if ("0000".equals(code)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String photo = resultStore.get("photo");
                            try {
                                URL url = new URL(photo);
                                Bitmap bm = BitmapFactory.decodeStream(url.openStream());
                                imgInfo.setImageBitmap(bm);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                                Toast.makeText(CouponInfoActivity.this, "쿠폰 정보를 이미지로 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CouponInfoActivity.this, "매장 정보를 가져오지 못했습니다.\n관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result.getContents() != null) {
            final String code = result.getContents();

            Log.d(TAG, "CODE : " + code);

            st_cd = code;

            if (st_cd.equals(cp_pub)) {
                new UseCouponTask().execute();
            }else{
                Toast.makeText(CouponInfoActivity.this, cp_pub_nm + "에서 발행한 쿠폰이 아닙니다.\n확인 후 이용해주세요.", Toast.LENGTH_SHORT).show();
            }

        }else{
            return;
        }
    }

    private class UseCouponTask extends AsyncTask<Void, Void, Void>{
        ProgressDialog progDialog = new ProgressDialog(CouponInfoActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDialog.setMessage("쿠폰 결제를 진행중입니다.");
            progDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String,String> paramCoupon = new HashMap<String,String>();

            paramCoupon.put("cp_id", cp_id);
            paramCoupon.put("cp_no", cp_no);

            HashMap<String,String> resultCoupon = null;
            try {
                resultCoupon = CouponUtil.useCouponPoint(paramCoupon);
                String code = resultCoupon.get("code");
                Log.d(TAG,"code : " + code);
                String message = resultCoupon.get("message");

                if ("0000".equals(code)) {
                    paramCoupon.clear();
                    resultCoupon.clear();

                    paramCoupon.put("cmd", "/coupon/couponnoprcess.json");
                    paramCoupon.put("mode", "H");
                    paramCoupon.put("cp_id", cp_id);
                    paramCoupon.put("cp_no", cp_no);
                    paramCoupon.put("cp_use_at", "Y");
                    paramCoupon.put("cp_use_user_no", Main.userInfo.get("user_no"));

                    try {
                        resultCoupon = CouponUtil.useCoupon(paramCoupon);

                        message = resultCoupon.get("message");
                        code = resultCoupon.get("code");
                        if("0000".equals(code)){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    popDialog = null;

                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CouponInfoActivity.this);
                                    alertDialog.setTitle("쿠폰 사용하기");
                                    alertDialog.setMessage(cp_nm + " 의 사용이 완료되었습니다.");
                                    alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            popDialog.dismiss();
                                            finish();
                                        }
                                    });
                                    alertDialog.create();

                                    popDialog = alertDialog.show();
                                }
                            });
                        }else{
                            Toast.makeText(CouponInfoActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CouponInfoActivity.this, "쿠폰을 사용하지 못했습니다.\n관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else if ("9999".equals(code)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CouponInfoActivity.this, "이미 사용한 쿠폰입니다.\n관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CouponInfoActivity.this, "쿠폰(포인트) 사용에 실패하였습니다.\n관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progDialog.dismiss();
        }
    }

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
