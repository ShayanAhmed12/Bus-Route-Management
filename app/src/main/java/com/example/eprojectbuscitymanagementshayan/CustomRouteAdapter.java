package com.example.eprojectbuscitymanagementshayan;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomRouteAdapter extends BaseAdapter {


    Context context;
    LayoutInflater inflator;
    ArrayList<RouteData> datalist;

    public CustomRouteAdapter(Context context, ArrayList<RouteData> datalist) {
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
        View v = inflator.inflate(R.layout.row_route,null);

        Context context = viewGroup.getContext();

        MainDB DB = new MainDB(context);

        ImageView img = v.findViewById(R.id.CustomRouteImage);
        ImageView fav = v.findViewById(R.id.CustomRouteFav);

        TextView name =  v.findViewById(R.id.CustomRouteName);
        TextView dest_from =  v.findViewById(R.id.CustomRouteDestFrom);
        TextView dest_to =  v.findViewById(R.id.CustomRouteDestTo);

        RouteData data = datalist.get(i);

        name.setText(data.Rname);
        dest_from.setText(data.Rdestfrom);
        dest_to.setText(data.Rdestto);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.updateFavRoute(String.valueOf(data.Rid));
                Toast.makeText(context, "Updated Favourite Status", Toast.LENGTH_SHORT).show();

            }
        });

        if(Integer.parseInt((data.Rfav))==1)
        {
            fav.setImageResource(R.drawable.fav_icon);;
        }



        return v;
    }
}
