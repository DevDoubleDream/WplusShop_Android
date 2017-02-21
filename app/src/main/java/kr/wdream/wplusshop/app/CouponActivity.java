package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.CardInfo;
import kr.wdream.wplusshop.common.adapter.CouponAdapter;
import kr.wdream.wplusshop.common.list.CouponVO;
import kr.wdream.wplusshop.common.util.CouponUtil;


/**
 * Created by deobeuldeulim on 2016. 10. 20..
 */
public class CouponActivity extends Activity implements AdapterView.OnItemClickListener,
                                                        View.OnClickListener{

    private static final String TAG = "CouponActivity";

    private TextView txtTitle;
    private TextView txtCount;
    private LinearLayout btnBack;

    private ImageButton btnBefore;
    private ImageButton btnNext;

    private Button btnFirst;
    private Button btnSecond;
    private Button btnThird;
    private Button btnFourth;
    private Button btnFifth;


    private ListView listView;
    private CouponAdapter adapter;

    private ArrayList<CouponVO> resultCoupon;

    private String pageSize = "3";

    private int intPage = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        initView();

        getCoupon();

    }

    public void getCoupon(){
        resultCoupon = new ArrayList<CouponVO>();
        new GetCouponList().execute();


    }

    private void initView(){

        listView = (ListView)findViewById(R.id.listCoupon);
        listView.setOnItemClickListener(this);

        txtTitle = (TextView)findViewById(R.id.txt_head_center);
        txtTitle.setText("쿠폰목록");
        txtCount = (TextView)findViewById(R.id.txt_count);

        btnBack = (LinearLayout)findViewById(R.id.icon_head_left);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnBefore = (ImageButton)findViewById(R.id.btn_before);
        btnBefore.setOnClickListener(this);

        btnNext = (ImageButton)findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);

        btnFirst = (Button)findViewById(R.id.btn_first);
        btnFirst.setText(String.valueOf(intPage));
        btnFirst.setOnClickListener(this);

        btnSecond = (Button)findViewById(R.id.btn_second);
        btnSecond.setText(String.valueOf(intPage+1));
        btnSecond.setOnClickListener(this);


        btnThird = (Button)findViewById(R.id.btn_third);
        btnThird.setText(String.valueOf(intPage+2));
        btnThird.setOnClickListener(this);


        btnFourth = (Button)findViewById(R.id.btn_fourth);
        btnFourth.setText(String.valueOf(intPage+3));
        btnFourth.setOnClickListener(this);

        btnFifth = (Button)findViewById(R.id.btn_fifth);
        btnFifth.setText(String.valueOf(intPage+4));
        btnFifth.setOnClickListener(this);




    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CouponVO item = (CouponVO)listView.getItemAtPosition(i);

        Log.d(TAG, "item : " + item.get_cp_nm() + "\n" + item.get_cp_edt());
        Intent itemIntent = new Intent(CouponActivity.this, CouponInfoActivity.class);
        itemIntent.putExtra("cp_nm", item.get_cp_nm());
        itemIntent.putExtra("cp_edt", item.get_cp_edt());
        itemIntent.putExtra("cp_id", item.get_cp_id());
        itemIntent.putExtra("cp_no", item.get_cp_no());
        itemIntent.putExtra("cp_photo", item.get_cp_photo());
        itemIntent.putExtra("cp_pub", item.get_cp_pub());
        itemIntent.putExtra("cp_pub_nm", item.get_cp_nm());

        startActivity(itemIntent);
    }

    @Override
    public void onClick(View view) {
        if (view == btnBefore) {
            if (intPage == 1) {
                Toast.makeText(this, "첫번째 페이지 입니다.", Toast.LENGTH_SHORT).show();
            }else{
                intPage -= 5;

                setButtonNumber(intPage);
            }
        }

        if (view == btnNext) {
            intPage += 5;

            setButtonNumber(intPage);
        }

        if (view == btnFirst) {
            new SelectCouponTask(this, String.valueOf(intPage), pageSize).execute();
        }

        if (view == btnSecond) {
            new SelectCouponTask(this, String.valueOf(intPage+1), pageSize).execute();

        }

        if (view == btnThird) {
            new SelectCouponTask(this, String.valueOf(intPage+2), pageSize).execute();
        }

        if (view == btnFourth) {
            new SelectCouponTask(this, String.valueOf(intPage+3), pageSize).execute();
        }

        if (view == btnFifth) {
            new SelectCouponTask(this, String.valueOf(intPage+4), pageSize).execute();
        }
    }

    private void setButtonNumber(int page){
        btnFirst.setText(String.valueOf(page));
        btnSecond.setText(String.valueOf(page+1));
        btnThird.setText(String.valueOf(page+2));
        btnFourth.setText(String.valueOf(page+3));
        btnFifth.setText(String.valueOf(page+4));
    }

    private class GetCouponList extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDialog = new ProgressDialog(CouponActivity.this);
            progDialog.setMessage("쿠폰 목록을 받아오고 있습니다.");
            progDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String,String> paramCoupon = new HashMap<String,String>();
            paramCoupon.put("cmd", "/coupon/couponboxprcess.json");
            paramCoupon.put("mode", "L");
            paramCoupon.put("cp_buy_user_cardno", CardInfo.getCardNo());
            paramCoupon.put("use_at","Y");
            paramCoupon.put("page", String.valueOf(intPage));
            paramCoupon.put("pagesize", pageSize);

            try {
                resultCoupon = CouponUtil.getCoupon(paramCoupon);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtCount.setText("총 목록 " + CouponVO.totalCount +"개");
                    }
                });
                Log.d(TAG, "resultCoupon : " + resultCoupon.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new CouponAdapter(CouponActivity.this, resultCoupon);

            Log.d(TAG, "adapter" + adapter.getCount());

            listView.setAdapter(adapter);

            progDialog.dismiss();
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if(null!=adapter) {
                    adapter.removeAll();
                    adapter = null;
                }
                txtCount.setText(CouponVO.totalCount);

                adapter = new CouponAdapter(CouponActivity.this, resultCoupon);
                listView.setAdapter(adapter);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        intPage = 1;
        new SelectCouponTask(this, String.valueOf(intPage), pageSize).execute();

    }

    private class SelectCouponTask extends AsyncTask{
        private Context context;
        private String page;
        private String pageSize;

        public SelectCouponTask(Context context,String page, String pageSize){
            this.context = context;
            this.page = page;
            this.pageSize = pageSize;

        }

        private ProgressDialog progDialog = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDialog = new ProgressDialog(context);
            progDialog.setMessage("쿠폰 정보를 가져오는 중 입니다.\n 잠시만 기다려주세요.");
            progDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            HashMap<String,String> paramCoupon = new HashMap<String,String>();
            paramCoupon.put("cmd", "/coupon/couponboxprcess.json");
            paramCoupon.put("mode", "L");
            paramCoupon.put("cp_buy_user_cardno", CardInfo.getCardNo());
            paramCoupon.put("use_at","Y");
            paramCoupon.put("page", page);
            paramCoupon.put("pagesize", pageSize);

            try {
                resultCoupon = CouponUtil.getCoupon(paramCoupon);

                Message msg = new Message();
                msg.what = 0;

                Bundle bundle = new Bundle();
                bundle.putSerializable("resultCoupon", resultCoupon);

                msg.setData(bundle);

                handler.sendMessage(msg);

                Log.d(TAG, "resultCoupon : " + resultCoupon.size());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progDialog.dismiss();

        }
    }
}
