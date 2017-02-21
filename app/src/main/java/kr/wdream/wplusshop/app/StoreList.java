package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.StoreListUtil;
import kr.wdream.wplusshop.common.list.StoreListVO;

/**
 * Created by SEO on 2015-12-17.
 */
public class StoreList extends Activity implements View.OnClickListener{

    public String Tag = "StoreList";
    private int pageNum = 1;     // 페이지 번호
    private int pageSize = 13;
    private static ArrayList<StoreListVO> storeList;
    private static int point = 0;
    private static String storeNM = "";
    private static int no = 0;
    DecimalFormat df = new DecimalFormat("#,##0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storelist);

        Intent intent = getIntent();
        storeNM = intent.getExtras().getString("storeNM");

        Main.locationHistory.add(this);

        StoreListUtil.inIt(this);

        ((TextView)findViewById(R.id.txt_head_center)).setText("가맹점 찾기");
        ((TextView)findViewById(R.id.txt_area)).setText(storeNM);
        LinearLayout before_bullet = (LinearLayout)findViewById(R.id.icon_head_left);
        before_bullet.setVisibility(View.VISIBLE);
        before_bullet.setOnClickListener(this);
        ((Button)findViewById(R.id.btn_next)).setOnClickListener(this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // 포인트 내역 조회
        HashMap<String, String> storeParams = new HashMap<String, String>();
        storeParams.put("cmd", "list");
        storeParams.put("region", storeNM);
        storeParams.put("page", String.valueOf(pageNum));
        storeParams.put("pagesize", String.valueOf(pageSize));
        storeList = StoreListUtil.getStoreList(pageNum, storeParams);

        StoreAdapter adapter = new StoreAdapter(
                this, R.layout.storelist_item,storeList);

        adapter.notifyDataSetChanged();
        ((ListView) findViewById(R.id.storelist)).setAdapter(adapter); // ListView 강의 목록 연동

//        ButtonCommon.setClick(this);

    }



    public class StoreAdapter extends BaseAdapter {

        private Context context;
        private  int layout;
        private List<StoreListVO> list;
        private LayoutInflater inflater;

        public StoreAdapter(
                Context context,
                int layout, List<StoreListVO> list) {
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
            TextView button;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            ViewHolder holder;

            if(convertView==null)
                convertView=inflater.inflate(layout, parent,false);

            holder = new ViewHolder();
            holder.button = (TextView) convertView.findViewById(R.id.txt_staddr);
            // ListView(lecture_lst_item.xml) '강의보기' 버튼 클릭
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, StoreMap.class);
                    intent.putExtra("stnm", list.get(position).get_stnm());
                    intent.putExtra("stphone", list.get(position).get_stphone());
                    intent.putExtra("point_xx", list.get(position).get_point_xx());
                    intent.putExtra("point_yy", list.get(position).get_point_yy());

                    startActivity(intent);

                }
            });
//
//
            no = list.get(position).get_totalCNT() - (((list.get(position).get_pageNo()-1) * pageSize) + position);
            no = list.get(position).get_totalCNT() - position;


            String stbiz = "";
            stbiz = list.get(position).get_stbiztype();
            if(stbiz.equals(""))    stbiz = list.get(position).get_stbizitem();
            else    stbiz += "\n"+ list.get(position).get_stbizitem();

            if(!stbiz.equals(""))    stbiz = "\n("+ list.get(position).get_stbizitem() +")";

//            ((TextView) convertView.findViewById(R.id.txt_stbiztype)).setText(stbiz);
            ((TextView)convertView.findViewById(R.id.txt_stnm)).setText(list.get(position).get_stnm() + stbiz);
            ((TextView)convertView.findViewById(R.id.txt_staddr)).setText(list.get(position).get_staddr1() +"\n"+ list.get(position).get_staddr2());
            ((TextView)convertView.findViewById(R.id.txt_stphone)).setText(list.get(position).get_stphone());
            ((TextView)convertView.findViewById(R.id.txt_no)).setText(String.valueOf(no));


            return convertView;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon_head_left:
                finish();
                break;

            case R.id.btn_next: // 더보기
                pageNum += 1;
                HashMap<String, String> storeParams = new HashMap<String, String>();
                storeParams.put("cmd", "list");
                storeParams.put("region", storeNM);
                storeParams.put("page", String.valueOf(pageNum));
                storeParams.put("pagesize", String.valueOf(pageSize));
                storeList.addAll(StoreListUtil.getStoreList(pageNum, storeParams));

                StoreAdapter adapter = new StoreAdapter(
                        this, R.layout.storelist_item,storeList);

                ((ListView) findViewById(R.id.storelist)).setAdapter(adapter); // ListView 강의 목록 연동
                ((ListView) findViewById(R.id.storelist)).setSelection(adapter.getCount() - 1);
                break;
        }
    }
}
