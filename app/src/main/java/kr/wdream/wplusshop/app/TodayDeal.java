package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.CDialog;
import kr.wdream.wplusshop.common.CoverFlowAdapter;
import kr.wdream.wplusshop.common.DBManager;
import kr.wdream.wplusshop.common.util.HotDealUtil;
import kr.wdream.wplusshop.common.list.HotDealVO;
import kr.wdream.wplusshop.common.util.TodayDealUtil;
import kr.wdream.wplusshop.common.list.TodayDealVO;

/**
 * Created by SEO on 2015-12-18.
 */
public class TodayDeal extends Activity implements View.OnTouchListener{
    Handler handler = new Handler();
    View convertView;
    DecimalFormat df = new DecimalFormat("#,##0");
    String MallDomain = "";
    AdapterViewFlipper avf;
    float xAtDown;
    float xAtUp;

    static String locationURI = "";
    static String title = "";
    static String summary = "";
    static String picture = "";

    private FeatureCoverFlow mCoverFlow;
    private CoverFlowAdapter mAdapter;
    private TextSwitcher mTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_deal);

        for (int i = 0; i < Main.locationHistory.size(); i++) {
//            Main.locationHistory.get(i).finish();  //List가 Static 이므로, Class명.변수명.get으로 접근
            Log.d("test_wplus", "locationHistory : "+ Main.locationHistory.get(i));
        }

        ((TextView)findViewById(R.id.txt_head_center)).setText("TODAY DEAL");
        LinearLayout before_bullet = (LinearLayout)findViewById(R.id.icon_head_left);
        before_bullet.setVisibility(View.VISIBLE);
        before_bullet.setOnTouchListener(this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Today Deal 조회
        mTitle = (TextSwitcher) findViewById(R.id.title);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(TodayDeal.this);
                TextView textView = (TextView) inflater.inflate(R.layout.item_title, null);
                return textView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);

        HashMap<String, String> todayDealParams = new HashMap<String, String>();
        todayDealParams.put("cmd", "today");
        todayDealParams.put("dist_dmn_nm", "www.wpoint.co.kr");
        final ArrayList<TodayDealVO> todayDealList = TodayDealUtil.getTodayDealList(todayDealParams);

        mAdapter = new CoverFlowAdapter(this);
        mAdapter.setData(todayDealList);
        mCoverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);
        mCoverFlow.setAdapter(mAdapter);

        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(TodayDeal.this,
//                        todayDealList.get(position).get_prdt_nm(),
//                        Toast.LENGTH_SHORT).show();
                locationURI = MallDomain + todayDealList.get(position).get_prdt_path();
                title       = todayDealList.get(position).get_prdt_nm();

                gotoWebView(locationURI, title, summary, picture);
            }
        });

        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle.setText(todayDealList.get(position).get_prdt_nm());
            }

            @Override
            public void onScrolling() {
                mTitle.setText("");
            }
        });

        // Hot Deal 조회
        HashMap<String, String> hotDealParams = new HashMap<String, String>();
        hotDealParams.put("cmd", "hotdeal");
        hotDealParams.put("dist_dmn_nm", "www.wpoint.co.kr");
        ArrayList<HotDealVO> hotDealList = HotDealUtil.getHotDealList(hotDealParams);

        HotDealAdapter adapterHodeal = new HotDealAdapter(
                this, R.layout.hot_deal_item,hotDealList);

        Log.d("test_wplus", "SIZE : "+ hotDealList.size());
        int gridViewHeight = hotDealList.size() / 2;
        if(hotDealList.size() % 2 > 0) gridViewHeight += 1;
        Log.d("test_wplus", "gridViewHeight : " + gridViewHeight);

        ((GridView) findViewById(R.id.gridview_hotdeal)).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, gridViewHeight * 660));
        adapterHodeal.notifyDataSetChanged();
        ((GridView) findViewById(R.id.gridview_hotdeal)).setAdapter(adapterHodeal); // GridView 상품 목록 연동

        MallDomain = getMallDomain();

        Main.locationHistory.add(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.icon_head_left:
                finish();
                break;
//            case R.id.avf_today_deal:
//                if(event.getAction() == MotionEvent.ACTION_DOWN) {
//                    xAtDown = event.getX();
//                }
//                else if(event.getAction() == MotionEvent.ACTION_UP){
//                    xAtUp = event.getX();
//
//                    if( xAtUp < xAtDown )       avf.showNext();
//                    else if (xAtUp > xAtDown)   avf.showPrevious();
//                    else                        gotoWebView(locationURI, title, summary, picture);
//                }
//                break;
        }
        return true;
    }

    // Hot Deal Adapter
    public class HotDealAdapter extends BaseAdapter {

        private Context context;
        private  int layout;
        private List<HotDealVO> list;
        private LayoutInflater inflater;

        public HotDealAdapter(
                Context context,
                int layout, List<HotDealVO> list) {
            this.context = context;
            this.layout = layout;
            this.list = list;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            LinearLayout button;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            ViewHolder holder;

            if(convertView==null)
                convertView=inflater.inflate(layout, parent,false);

            holder = new ViewHolder();
            holder.button = (LinearLayout) convertView.findViewById(R.id.lyt_btn_prdt);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    locationURI = MallDomain + list.get(position).get_prdt_path();
                    title       = list.get(position).get_prdt_nm();
                    picture     = list.get(position).get_mdl_img_path();

                    gotoWebView(locationURI, title, summary, picture);
                }
            });

            URL url = null;
            try {
                url = new URL(list.get(position).get_mdl_img_path());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

//                 입력스트림으로 해당 URL의 사진정보 가져오기 -> try⁄catch
            InputStream is = null;
            try {
                is = url.openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

//                        ⁄⁄ BitmapFactory 클래스를 통해 해당 스트림을 그림으로 읽어오기
            final Bitmap bm = BitmapFactory.decodeStream(is);
            ((ImageView) convertView.findViewById(R.id.img_prdt)).setImageBitmap(bm);

            ((TextView) convertView.findViewById(R.id.txt_prdt_title)).setText(list.get(position).get_prdt_nm());
            ((TextView) convertView.findViewById(R.id.txt_prdt_price)).setText(df.format(list.get(position).get_sll_mny()) +"원");
            int prdt_discountrate = getPercent(list.get(position).get_cty_mny(), list.get(position).get_sll_mny());
            ((TextView) convertView.findViewById(R.id.txt_prdt_discountrate)).setText("["+Integer.toString(prdt_discountrate)+"%]");
            return convertView;
        }
    }

    public int getPercent(int num1, int num2){
        int resultNum = 100 - Math.round(((float) num2 / ((float) num1 / 100)));
        return resultNum;
    }

    public String getMallDomain(){
        String returnDomain;
        String DBName = "WPLUSSHOP.db";
        String tableName = "USER_SETTING";

        DBManager dbManager = new DBManager(getApplicationContext(), DBName, null, 4);
        String[] selectParams = {"LOCATION_DOMAIN"};    // select columns
        HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
        HashMap<String, String> userSetRst = null;
        try {
            userSetRst = dbManager.select(tableName, selectParams, whereParams);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (userSetRst.isEmpty())   returnDomain = "http://www.wpoint.co.kr";
        else{
            if(userSetRst.get("LOCATION_DOMAIN").equals(""))    returnDomain = "http://www.wpoint.co.kr";
            else                                                returnDomain = "http://"+ userSetRst.get("LOCATION_DOMAIN");
        }

        return returnDomain;
    }

    public void setTodayCntIcon(int totCnt, int Cnt){
        LinearLayout lyt_todaydeal_menu = (LinearLayout) findViewById(R.id.btn_todaydeal_menu);
        lyt_todaydeal_menu.removeAllViews();
        for(int i = 0; i < totCnt; i++){
            int leftMargin = i > 0 ? 6 : 0;
            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            param.leftMargin = leftMargin;
            iv.setLayoutParams(param);
            iv.setImageResource(Cnt == i ? R.drawable.slide_btn_on : R.drawable.slide_btn_off);
            iv.setId(100 + i);
            lyt_todaydeal_menu.addView(iv);
        }
    }

    public void gotoWebView(String locationURI, String title, String summary, String picture){
        Intent intent = new Intent(this, PushWebView.class);
        intent.putExtra("LOCATION_URL", locationURI);
        intent.putExtra("TITLE", title);
        intent.putExtra("SUMMARY", summary);
        intent.putExtra("PICTURE", picture);
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(locationURI));
        CDialog.showLoading(this);
        startActivity(intent);
    }
}
