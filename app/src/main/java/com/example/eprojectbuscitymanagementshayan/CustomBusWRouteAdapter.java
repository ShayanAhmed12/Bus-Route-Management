package com.example.eprojectbuscitymanagementshayan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomBusWRouteAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflator;
    ArrayList<BusData> datalist;

    public CustomBusWRouteAdapter(Context context, ArrayList<BusData> datalist) {
        this.context = context;
        this.datalist = datalist;

        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflator.inflate(R.layout.row_buswroute, null);

        Context context = viewGroup.getContext();

        MainDB DB = new MainDB(context);

        ImageView img = v.findViewById(R.id.CustomBusImage);
        ImageView fav = v.findViewById(R.id.CustomBusFav);

        TextView name = v.findViewById(R.id.CustomBusName);
        TextView routename = v.findViewById(R.id.CustomBusRouteName);
        TextView number = v.findViewById(R.id.CustomBusNumber);
        TextView fare = v.findViewById(R.id.CustomBusFare);

        BusData data = datalist.get(i);

        name.setText(data.Bname);
        routename.setText(DB.editBusGetRouteName(data.Broute));
        number.setText(data.Bnumber);
        fare.setText(data.Bfare);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.updateFavBus(String.valueOf(data.Bid));
                Toast.makeText(context, "Updated Favourite Status", Toast.LENGTH_SHORT).show();

            }
        });

        if (Integer.parseInt((data.Bfav)) == 1) {
            fav.setImageResource(R.drawable.fav_icon);

        }

        switch (data.Bcolor) {
            case "Red":
                img.setImageResource(R.drawable.redbus);
                break;
            case "Green":
                img.setImageResource(R.drawable.greenbus);
                break;
            case "Blue":
                img.setImageResource(R.drawable.bluebus);
                break;
            case "Yellow":
                img.setImageResource(R.drawable.yellowbus);
                break;
            case "Orange":
                img.setImageResource(R.drawable.orangebus);
                break;
            case "White":
                img.setImageResource(R.drawable.whitebus);
                break;
            case "Pink":
                img.setImageResource(R.drawable.pinkbus);
                break;
            case "Other":
                img.setImageResource(R.drawable.otherbus);
                break;

        }

        return v;
    }
}
