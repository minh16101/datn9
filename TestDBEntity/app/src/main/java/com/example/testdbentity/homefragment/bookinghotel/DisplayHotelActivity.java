package com.example.testdbentity.homefragment.bookinghotel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.testdbentity.R;
import com.example.testdbentity.homefragment.bookinghotel.adapter.ItemHAdapter;
import com.example.testdbentity.homefragment.bookinghotel.adapter.ItemSAdapter;
import com.example.testdbentity.homefragment.bookinghotel.adapter.OnClickHotel;
import com.example.testdbentity.homefragment.bookinghotel.adapter.OnclickToDetail;
import com.example.testdbentity.homefragment.bookinghotel.adapter.RecentsAdapter;
import com.example.testdbentity.homefragment.bookinghotel.database.BookingHotelViewModel;
import com.example.testdbentity.homefragment.bookinghotel.image.hotelandimg.HotelWithImage;
import com.example.testdbentity.homefragment.bookinghotel.image.roomandimg.RoomWithImage;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DisplayHotelActivity extends AppCompatActivity {
    BookingHotelViewModel bookingHotelViewModel;
    ItemHAdapter itemHAdapter;
    int[] timeFrom;
    int[] timeTo;
    String place = "";
    LiveData<List<HotelWithImage>> imageHotelList;

    private static final String[] cityNames = new String[]{
            "Hoàn Kiếm", "Hai Bà Trưng", "Đống Đa", "Ba Đình",
            "Tây Hồ", "Cầu Giấy", "Bắc Từ Liêm", "Nam Từ Liêm"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_hotel);

        bookingHotelViewModel = new ViewModelProvider(this).get(BookingHotelViewModel.class);
        String city = "";

        AutoCompleteTextView searchCity = findViewById(R.id.editTextTextPersonName);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, cityNames);
        searchCity.setAdapter(adapter);

        //get intent
        int idInformationUser = getIntent().getIntExtra("idInformationUser",0);
        city = getIntent().getStringExtra("cityHotel");
        //lay ra thoi gian
        timeFrom = getIntent().getIntArrayExtra("timeFrom");
        timeTo = getIntent().getIntArrayExtra("toFrom");
//        getTime();

        recyclerViewSave(idInformationUser);


        RecyclerView recyclerView = findViewById(R.id.recycler_h);
        if(city == null)
        {
            //start 1 activity pick room
        }
        if(city.equals(""))
        {
            //lay ra anh cua hotel o tat ca cac quan
            imageHotelList = bookingHotelViewModel.getAllImageOfHotel();
        }
        else
        {
            //lay ra anh cua hotel cua quan cu the
            imageHotelList = bookingHotelViewModel.getAllImageOfHotelWithDistrict(city);
        }
        itemHAdapter = new ItemHAdapter(new ArrayList<>());
        imageHotelList.observe(this, imHotel -> {
            itemHAdapter.setData(imHotel);
            itemHAdapter.notifyDataSetChanged();
        });
        itemHAdapter.setOnClickHotel(new OnClickHotel() {
            @Override
            public void onClickAHotel(int idHotel) {
                //mo activity detail hotel
                Intent intent = new Intent(DisplayHotelActivity.this, DisplayDetailHotelActivity.class);
                intent.putExtra("idHotel", idHotel);
                intent.putExtra("idInformationUser", idInformationUser);
                intent.putExtra("timeFrom", timeFrom);
                intent.putExtra("timeTo", timeTo);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemHAdapter);

        searchCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                place = adapterView.getItemAtPosition(i).toString();

            }
        });
        Button btn = findViewById(R.id.button_search_in_display_hotel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageHotelList.observe(DisplayHotelActivity.this, imHotel -> {
                    itemHAdapter.setData(imHotel);
                    itemHAdapter.notifyDataSetChanged();
                });
                imageHotelList = bookingHotelViewModel.getAllImageOfHotelWithDistrict(place);
            }
        });
    }
    private void getTime()
    {
        if(timeFrom == null)
        {
            Calendar calendar = Calendar.getInstance();
            int dayFrom = calendar.get(Calendar.DAY_OF_WEEK);
            int monthFrom = calendar.get(Calendar.MONTH);
            int yearFrom = calendar.get(Calendar.YEAR);

            int dayTo = dayFrom;
            int monthTo = monthFrom + 1;
            int yearTo = yearFrom;

            timeFrom[0] = dayFrom;
            timeFrom[1] = monthFrom;
            timeFrom[2] = yearFrom;

            timeTo[0] = dayTo;
            timeTo[1] = monthTo;
            timeTo[2] = yearTo;
        }
    }
    private void recyclerViewSave(int idInformationUser)
    {
        RecentsAdapter recentsAdapter = new RecentsAdapter(new ArrayList<>());
        recentsAdapter.setOnclickToDetail(new OnclickToDetail() {
            @Override
            public void OnClick(int idRoom) {
                //mo activity detail hotel
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView_recent);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(recentsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        LiveData<List<RoomWithImage>> roomWithImageList = bookingHotelViewModel.getRoomWithUserId(idInformationUser);
        roomWithImageList.observe(this, new Observer<List<RoomWithImage>>() {
            @Override
            public void onChanged(List<RoomWithImage> roomWithImages) {
                recentsAdapter.setRoomWithImageList(roomWithImages);
                recentsAdapter.notifyDataSetChanged();
            }
        });
    }
}
