package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.DBManager;
import kr.wdream.wplusshop.common.util.UserListUtil;
import kr.wdream.wplusshop.common.list.UserListVO;

/**
 * Created by SEO on 2015-12-28.
 */
public class UserList extends Activity implements View.OnClickListener{
    public String DBName = "WPLUSSHOP.db";
    public String tableName = "USER_LIST";

    public String Tag = "UserList";
    private int pageNum = 1;     // 페이지 번호
    private int pageSize = 13;
    private static ArrayList<UserListVO> userList;
    private static int point = 0;
    private static String storeNM = "";
    private static int no = 0;
    DecimalFormat df = new DecimalFormat("#,##0");

    DBManager dbManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlist);

        Intent intent = getIntent();

        Main.locationHistory.add(this);

        UserListUtil.inIt(this);

        dbManager = new DBManager(this.getApplicationContext(), DBName, null, 4);

        ((TextView)findViewById(R.id.txt_head_center)).setText("친구 목록");
        LinearLayout before_bullet = (LinearLayout)findViewById(R.id.icon_head_left);
        before_bullet.setVisibility(View.VISIBLE);
        before_bullet.setOnClickListener(this);
        ((Button)findViewById(R.id.btn_next)).setOnClickListener(this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    public void getUserList(){
        HashMap<String, String> userParams = new HashMap<String, String>();
        userParams.put("page", String.valueOf(pageNum));
        userParams.put("pagesize", String.valueOf(pageSize));
        userList = UserListUtil.getUserList(pageNum, userParams);

        if(userList != null) {
            UserListAdapter adapter = new UserListAdapter(
                    this, R.layout.userlist_item, userList);

            adapter.notifyDataSetChanged();
            ((ListView) findViewById(R.id.userlist)).setAdapter(adapter); // ListView 강의 목록 연동
        }
    }

    public class UserListAdapter extends BaseAdapter {

        private Context context;
        private  int layout;
        private List<UserListVO> list;
        private LayoutInflater inflater;

        public UserListAdapter(
                Context context,
                int layout, List<UserListVO> list) {
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
            ViewHolder holder, holder1, holder2;

            if(convertView==null)
                convertView=inflater.inflate(layout, parent,false);

            holder = new ViewHolder();
            holder.button = (LinearLayout) convertView.findViewById(R.id.lyt_ul_btn);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Use.card_no = list.get(position).get_member_cardno();
//                    Intent intent = new Intent(context, Use.class);
//                    intent.putExtra("CARD_NO", list.get(position).get_member_cardno());
//                    startActivity(intent);
                    finish();
                }
            });

            holder1 = new ViewHolder();
            holder1.button = (LinearLayout) convertView.findViewById(R.id.lyt_edit_btn);
            holder1.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserRegist.class);
                    intent.putExtra("USER_IDX", list.get(position).get_member_idx());
                    startActivity(intent);
                }
            });

            holder2 = new ViewHolder();
            holder2.button = (LinearLayout) convertView.findViewById(R.id.lyt_del_btn);
            holder2.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(context);
                    ab.setTitle("친구 삭제");
                    ab.setMessage("'"+ list.get(position).get_member_name()  +"'님을 삭제 하시겠습니까?");
                    ab.setCancelable(false);
                    ab.setIcon(getResources().getDrawable(R.drawable.icon));

                    ab.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
                            whereParams.put("IDX", list.get(position).get_member_idx());
                            try {
                                dbManager.delete("USER_LIST", whereParams);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            getUserList();
                        }
                    });

                    ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
                    AlertDialog mDialog = ab.create();
                    mDialog.setCanceledOnTouchOutside(true);
                    mDialog.show();
                }
            });
//
//
            no = list.get(position).get_totalCNT() - (((list.get(position).get_pageNo()-1) * pageSize) + position);
            no = list.get(position).get_totalCNT() - position;

            ((TextView)convertView.findViewById(R.id.txt_ul_name)).setText(list.get(position).get_member_name());

            String strSex = "";
            strSex = list.get(position).get_member_sex().equals("M") ? "(남자)" : "(여자)";
                    ((TextView) convertView.findViewById(R.id.txt_sex)).setText(strSex);
            ((TextView)convertView.findViewById(R.id.txt_ul_phone)).setText(list.get(position).get_member_hphone());

            StringBuffer sb = new StringBuffer(list.get(position).get_member_cardno());
            sb.insert(4, "-").insert(9, "-").insert(14, "-");
            String card_no = sb.toString();
            ((TextView)convertView.findViewById(R.id.txt_ul_cardno)).setText(card_no);

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
                HashMap<String, String> userParams = new HashMap<String, String>();
                userParams.put("page", String.valueOf(pageNum));
                userParams.put("pagesize", String.valueOf(pageSize));
                userList.addAll(UserListUtil.getUserList(pageNum, userParams));

                UserListAdapter adapter = new UserListAdapter(
                        this, R.layout.userlist_item,userList);

                ((ListView) findViewById(R.id.userlist)).setAdapter(adapter); // ListView 강의 목록 연동
                ((ListView) findViewById(R.id.userlist)).setSelection(adapter.getCount() - 1);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserList();
    }
}
