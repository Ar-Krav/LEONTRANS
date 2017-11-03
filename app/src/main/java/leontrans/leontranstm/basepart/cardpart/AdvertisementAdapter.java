package leontrans.leontranstm.basepart.cardpart;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

import leontrans.leontranstm.R;
import leontrans.leontranstm.basepart.userprofile.UserCardOwenerProfile;


public class AdvertisementAdapter extends ArrayAdapter<AdvertisementInfo> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<AdvertisementInfo> advertisementInfoList;

    public AdvertisementAdapter(Context context, int resource, ArrayList<AdvertisementInfo> advertisementInfoList) {
        super(context, resource, advertisementInfoList);
        this.advertisementInfoList = advertisementInfoList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null){
            view = inflater.inflate(R.layout.list_item_layout, parent, false);
        }

        TextView trans_type = (TextView) view.findViewById(R.id.trans_type);
        TextView date_from = (TextView) view.findViewById(R.id.date_from);
        TextView telephone = (TextView) view.findViewById(R.id.telephone);
        TextView date_to = (TextView) view.findViewById(R.id.date_to);
        Button country_from_ru = (Button) view.findViewById(R.id.country_from_ru);

        Button country_to_ru = (Button) view.findViewById(R.id.country_to_ru);
        Button city_to_ru = (Button) view.findViewById(R.id.city_to_ru);

        Button city_from_ru = (Button) view.findViewById(R.id.city_from_ru);

        Button pay_type = (Button) view.findViewById(R.id.pay_type);
        if(advertisementInfoList.get(position).getPay_type().equals("request")){
            pay_type.setText(advertisementInfoList.get(position).getPay_type());
        }else{
            pay_type.setText(advertisementInfoList.get(position).getPay_price() + " " + advertisementInfoList.get(position).getPay_currency());
        }
        Button goods = (Button) view.findViewById(R.id.goods);
        if((advertisementInfoList.get(position).getGoods().equals("0"))&&(advertisementInfoList.get(position).getTrans_capacity().equals("0"))){
            goods.setText(advertisementInfoList.get(position).getGoods() + " " + advertisementInfoList.get(position).getGoods_load_type() + " ");
        }else if(advertisementInfoList.get(position).getTrans_weight().equals("0")){
            goods.setText(advertisementInfoList.get(position).getGoods() + " " + advertisementInfoList.get(position).getGoods_load_type() + " "
                    +advertisementInfoList.get(position).getTrans_capacity()+"м");
        }else if(advertisementInfoList.get(position).getTrans_capacity().equals("0")){
            goods.setText(advertisementInfoList.get(position).getGoods() + " " + advertisementInfoList.get(position).getGoods_load_type());
        }else{
            goods.setText(advertisementInfoList.get(position).getGoods() + " " + advertisementInfoList.get(position).getGoods_load_type() + " "+ advertisementInfoList.get(position).getTrans_weight()+"т"
                    +advertisementInfoList.get(position).getTrans_capacity()+"м");
        }

        telephone.setText(advertisementInfoList.get(position).getTelephone());


        trans_type.setText(advertisementInfoList.get(position).getTrans_type());
        date_from.setText(advertisementInfoList.get(position).getDate_from());
        date_to.setText(advertisementInfoList.get(position).getDate_to());

        country_from_ru.setText(advertisementInfoList.get(position).getCountry_from_ru());
        country_to_ru.setText(advertisementInfoList.get(position).getCountry_to_ru());

        city_from_ru.setText(advertisementInfoList.get(position).getCity_from_ru());
        city_to_ru.setText(advertisementInfoList.get(position).getCity_to_ru());


        Button name = (Button) view.findViewById(R.id.name);
        name.setText(advertisementInfoList.get(position).getFull_name());
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,UserCardOwenerProfile.class);
                intent.putExtra("userID",Integer.parseInt(advertisementInfoList.get(position).getUserid_creator()));
                context.startActivity(intent);
            }
        });

        return view;
    }




}