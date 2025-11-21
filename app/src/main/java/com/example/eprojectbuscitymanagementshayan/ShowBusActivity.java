package com.example.eprojectbuscitymanagementshayan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ShowBusActivity extends AppCompatActivity {

    UserDB userdb = new UserDB(this);
    MainDB DB = new MainDB(this);

    ArrayList<BusData> busList = new ArrayList<>();
    ArrayList<String> busID = new ArrayList<>();
    ListView busesListView;
    TextView showbustext;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bus);

        setTitle("");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        showbustext = findViewById(R.id.showbustext);
        busesListView = findViewById(R.id.busesList);


        displayBusData();
        CustomBusAdapter busadapter = new CustomBusAdapter(this,busList);
        busesListView.setAdapter(busadapter);
        busadapter.notifyDataSetChanged();
        registerForContextMenu(busesListView);

    }

    void displayBusData()
    {
        Intent intent = getIntent();
        Bundle b = intent.getExtras();


        int my_routeid  = Integer.parseInt(b.getString("route_id"));
        String my_routename  = b.getString("route_name");

        showbustext.setText("Buses running on "+my_routename);

        Cursor cursor = DB.getAllBuses(userdb.getUserID(),my_routeid);

        if(cursor.getCount()==0)
        {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while(cursor.moveToNext()){
                busList.add(new BusData(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(6),cursor.getString(7)));
                busID.add(cursor.getString(0));
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu,v,menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bus_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.contextMenuBusRemove)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(ShowBusActivity.this);

            builder.setTitle("Delete");
            builder.setMessage("Are you sure you want to remove this Bus?");
            builder.setIcon(R.drawable.ic_delete);


            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                    String ID = String.valueOf(busID.get(menuInfo.position));

                    DB.deleteBus(ID);
                    busList.clear();
                    displayBusData();
                    CustomBusAdapter busAdapter = new CustomBusAdapter(ShowBusActivity.this,busList);
                    busesListView.setAdapter(busAdapter);
                    busAdapter.notifyDataSetChanged();

                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog alert = builder.create();
            alert.show();


            return true;
        }

        if(item.getItemId() == R.id.contextMenuBusEdit)
        {
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            Intent intent = new Intent(ShowBusActivity.this, EditBusActivity.class);

            ArrayList<BusData> datalist = busList;
            BusData data = datalist.get(menuInfo.position);



            intent.putExtra("bus_id", data.Bid);
            intent.putExtra("bus_name", data.Bname);
            intent.putExtra("bus_number", data.Bnumber);
            intent.putExtra("bus_color", data.Bcolor);
            intent.putExtra("bus_fare", data.Bfare);
            intent.putExtra("belongsToRoute", data.Broute);


            startActivity(intent);


        }
        return false;
    }




}