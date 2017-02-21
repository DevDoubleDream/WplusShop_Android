package kr.wdream.wplusshop.common.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.list.CouponVO;


/**
 * Created by deobeuldeulim on 2016. 10. 24..
 */

public class CouponAdapter extends BaseAdapter {
    private static final String TAG = "CouponAdapter";

    private Context context;
    private LayoutInflater inflater;

    private ArrayList<CouponVO> list;

    public CouponAdapter(Context context, ArrayList<CouponVO> resultCoupon){
        this.context = context;
        this.list = resultCoupon;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void removeAll() {
        list.clear();
    }

    public void removeItem(int i){
        list.remove(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_coupon, null);

            holder.txtName = (TextView)view.findViewById(R.id.txt_name);
            holder.txtDate = (TextView)view.findViewById(R.id.txt_date);
            holder.txtUse = (TextView)view.findViewById(R.id.txt_banner);
            holder.imgCoupon = (ImageView)view.findViewById(R.id.img_coupon);

            holder.lytBanner = (FrameLayout)view.findViewById(R.id.lyt_banner);

            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }

        CouponVO listItem = list.get(i);

        if(!"".equals(listItem.get_cp_no())) {
            String cp_nm = listItem.get_cp_nm();
            String cp_sdt = listItem.get_cp_sdt();
            cp_sdt = cp_sdt.substring(0,4) + "." + cp_sdt.substring(4,6) + "." + cp_sdt.substring(6,8);
            String cp_edt = listItem.get_cp_edt();
            cp_edt = cp_edt.substring(0,4) + "." + cp_edt.substring(4,6) + "." + cp_edt.substring(6,8);
            String cp_id = listItem.get_cp_id();
            String cp_no = listItem.get_cp_no();
            String cp_pub = listItem.get_cp_pub();
            String cp_pub_nm = listItem.get_cp_pub_nm();
            String cp_photo = listItem.get_cp_photo();

            holder.txtName.setText(cp_nm);
            holder.txtDate.setText(cp_sdt + " ~ " + cp_edt);

            try {
                URL url = new URL(cp_photo);
                Bitmap bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                holder.imgCoupon.setImageBitmap(bm);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(context, cp_nm + "의 쿠폰 이미지를 불러오지 못했습니다.\n잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "이미지를 비트맵으로 변환하지 못했습니다");
            }
        }

        return view;
    }

    private class ViewHolder{
        TextView txtName;
        TextView txtDate;
        TextView txtUse;
        ImageView imgCoupon;

        FrameLayout lytBanner;
    }
}
