package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.util.NoticeListUtil;
import kr.wdream.wplusshop.common.list.NoticeListVO;

/**
 * Created by SEO on 2016-04-22.
 */
public class NoticeList extends Activity implements View.OnClickListener{
    Animation anim;
    public String Tag = "NoticeList";
    private int pageNum = 1;     // 페이지 번호
    private int pageSize = 13;
    private static ArrayList<NoticeListVO> noticeList;
    private static int point = 0;
    public static View convertView1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_list);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Main.locationHistory.add(this);

        NoticeListUtil.inIt(this);

        ((TextView)findViewById(R.id.txt_head_center)).setText("공지사항");
        LinearLayout before_bullet = (LinearLayout)findViewById(R.id.icon_head_left);
        before_bullet.setVisibility(View.VISIBLE);
        before_bullet.setOnClickListener(this);
        ((Button)findViewById(R.id.btn_next)).setOnClickListener(this);

        HashMap<String, String> noticeParams = new HashMap<String, String>();
        noticeParams.put("cmd", "/app/notice_list.json");
        noticeParams.put("app_id", getString(R.string.app_name).toLowerCase());
        noticeParams.put("page", String.valueOf(pageNum));
        noticeParams.put("pagesize", String.valueOf(pageSize));
        noticeList = NoticeListUtil.getNoticeList(pageNum, noticeParams);

        NoticeListAdapter adapter = new NoticeListAdapter(
                this, R.layout.notice_list_item, noticeList);

        adapter.notifyDataSetChanged();
        ((ListView) findViewById(R.id.notice_list)).setAdapter(adapter);
    }


    public class NoticeListAdapter extends BaseAdapter {

        private Context context;
        private  int layout;
        private List<NoticeListVO> list;
        private LayoutInflater inflater;

        public NoticeListAdapter(
                Context context,
                int layout, List<NoticeListVO> list) {
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

            convertView1 = convertView;
            Log.d("dev_test", "convertView1 : "+ convertView);
            Log.d("dev_test", "convertView1 : "+ convertView1);

            ViewHolder holder;

            if(convertView==null)
                convertView=inflater.inflate(layout, parent,false);

            holder = new ViewHolder();
            holder.button = (LinearLayout) convertView.findViewById(R.id.lyt_btn_notice);
            final View finalConvertView = convertView;
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("dev_test", "v : "+ v);
                    Log.d("dev_test", "convertView2 : "+ convertView1);
//                    convertView1.findViewById(R.id.lyt_contents);
                    View contents = finalConvertView.findViewById(R.id.lyt_contents);
                    if(contents.getVisibility() == View.GONE){
                        final TextView txt_bullet1 = (TextView) finalConvertView.findViewById(R.id.txt_bullet);
//                        anim = AnimationUtils.loadAnimation(context, R.anim.rotate_bullet);
////                        anim.setFillAfter(true);
//                        txt_bullet1.startAnimation(anim);
//
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
                                txt_bullet1.setText("∧");
//                            }
//                        }, 650);


                        String txt_copy = "&lt;p style='text-align:right;'&gt;&lt;span style='color: rgb(169, 169, 169);'&gt;&lt;span style='font-size: 11px;'&gt;@더블드림&lt;/span&gt;&lt;/span&gt;&lt;br/&gt;";
                        String txt_date = "&lt;span style='color: rgb(169, 169, 169);'&gt;&lt;span style='font-size: 11px;'&gt;"+ formattedDate(list.get(position).get_create_dt(), "yyyyMMdd", "yyyy/MM/dd") +"&lt;/span&gt;&lt;/span&gt;&lt;/p&gt;";
                        String html =  String.valueOf(Html.fromHtml(list.get(position).get_content() + txt_copy + txt_date));

                        WebView web = (WebView)finalConvertView.findViewById(R.id.webView);
                        web.loadData(html, "text/html; charset=UTF-8", null);
                        expand(contents);
                    }
                    else {
                        final TextView txt_bullet2 = (TextView) finalConvertView.findViewById(R.id.txt_bullet);
//                        anim = AnimationUtils.loadAnimation(context, R.anim.rotate_bullet);
//                        anim.setFillAfter(true);
//                        txt_bullet2.startAnimation(anim);
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
                                txt_bullet2.setText("∨");
//                            }
//                        }, 900);
                        collapse(contents);
                    }

//                    Intent intent = new Intent(context, StoreMap.class);
//                    intent.putExtra("stnm", list.get(position).get_stnm());
//                    intent.putExtra("stphone", list.get(position).get_stphone());
//                    intent.putExtra("point_xx", list.get(position).get_point_xx());
//                    intent.putExtra("point_yy", list.get(position).get_point_yy());

//                    startActivity(intent);

                }
            });

            ((TextView)convertView.findViewById(R.id.txt_title)).setText(list.get(position).get_subject());
//            Log.d("dev_test", "get_create_dt : " + list.get(position).get_create_dt());
//            String create_dt = list.get(position).get_create_dt();
            String format_create_dt = formattedDate(list.get(position).get_create_dt(), "yyyyMMdd", "MM/dd");
            ((TextView)convertView.findViewById(R.id.txt_date)).setText(format_create_dt);

            return convertView;
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

    public static String formattedDate
            (String date, String fromFormatString, String toFormatString)
    {
        SimpleDateFormat fromFormat =
                new SimpleDateFormat(fromFormatString);
        SimpleDateFormat toFormat =
                new SimpleDateFormat(toFormatString);
        Date fromDate = null;

        try
        {
            fromDate = fromFormat.parse(date);
        }
        catch(ParseException e)
        {
            fromDate = new Date();
        }

        return toFormat.format(fromDate);
    }


    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(a);
    }
}
