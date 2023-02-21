package com.example.testdbentity.homefragment.bookinghotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testdbentity.R;
import com.example.testdbentity.homefragment.bookinghotel.adapter.ItemRAdapter;
import com.example.testdbentity.homefragment.bookinghotel.adapter.OnClickRoom;
import com.example.testdbentity.homefragment.bookinghotel.database.BookingHotelViewModel;
import com.example.testdbentity.homefragment.bookinghotel.image.roomandimg.RoomWithImage;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DisplayRoomActivity extends AppCompatActivity {
    private BookingHotelViewModel bookingHotelViewModel;
    private int idHotel;
    private int idInformationUser;
    private int[] timeFrom;
    private int[] timeTo;
    private int dayFrom, monthFrom, yearFrom;
    private int dayTo, monthTo, yearTo;
    private EditText edtFromDay, edtToDay;
    private String monthMain;
    private Long date1, date2 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_room);
        //
        RecyclerView recyclerView = findViewById(R.id.recycler_room_activity);
        recyclerView.setNestedScrollingEnabled(false);
        idHotel = getIntent().getIntExtra("idHotel", 1);
        idInformationUser = getIntent().getIntExtra("idInformationUser", 0);
        timeFrom = getIntent().getIntArrayExtra("timeFrom");
        timeTo = getIntent().getIntArrayExtra("timeTo");
        String nameHotel = getIntent().getStringExtra("nameHotel");

        bookingHotelViewModel = new ViewModelProvider(this).get(BookingHotelViewModel.class);

        TextView textViewName = findViewById(R.id.name_hotel_room_display);
        textViewName.setText(nameHotel);
        bindDataTime();

        LiveData<List<RoomWithImage>> roomWithImageList = bookingHotelViewModel.getAllImageOfRoomWithIdHotel(idHotel);

        ItemRAdapter itemRAdapter = new ItemRAdapter(new ArrayList<>());
        roomWithImageList.observe(this, imRoom -> {
            itemRAdapter.setData(imRoom);
            itemRAdapter.notifyDataSetChanged();
        });

        itemRAdapter.setOnClickRoom(new OnClickRoom() {
            @Override
            public void onShowDetailRoom(int idRoom) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(date1);
                dayFrom = cal.get(Calendar.DAY_OF_MONTH);
                monthFrom = cal.get(Calendar.MONTH) + 1;
                yearFrom = cal.get(Calendar.YEAR);

                cal.setTimeInMillis(date2);
                dayTo = cal.get(Calendar.DAY_OF_MONTH);
                monthTo = cal.get(Calendar.MONTH) + 1;
                yearTo = cal.get(Calendar.YEAR);

                timeFrom[0] = dayFrom;
                timeFrom[1] = monthFrom;
                timeFrom[2] = yearFrom;

                timeTo[0] = dayTo;
                timeTo[1] = monthTo;
                timeTo[2] = yearTo;

                int time = (yearTo - yearFrom) * 12 - monthFrom + monthTo;
                if(time == 0) {
                    time = 3;
                    Toast.makeText(getApplicationContext(), "Thời gian đặt phòng trở về mặc định là 3 tháng", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(DisplayRoomActivity.this, DisplayRoomDetailActivity.class);
                intent.putExtra("idRoom", idRoom);
                intent.putExtra("idInformationUser", idInformationUser);
                intent.putExtra("timeFrom", timeFrom);
                intent.putExtra("timeTo", timeTo);
                intent.putExtra("during", time);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemRAdapter);
    }

    private void bindDataTime()
    {
        edtFromDay = findViewById(R.id.time_from_room);
        edtToDay = findViewById(R.id.time_to_room);
        monthMain = getMonth(timeFrom[1]);
        String sDateTempFrom = timeFrom[0] + "-" + monthMain + "-" + timeFrom[2];
        monthMain = getMonth(timeTo[1]);
        String sDateTempTo = timeTo[0] + "-" + monthMain + "-" + timeTo[2];
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        try {
            Date d = formatter.parse(sDateTempFrom);
            date1 = d.getTime();
            Date d2 = formatter.parse(sDateTempTo);
            date2 = d2.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        edtFromDay.setText(sDateTempFrom);
        edtToDay.setText(sDateTempTo);

        edtFromDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetFrom();
            }
        });

        edtToDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetTo();
            }
        });

    }
    private void showBottomSheetFrom() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        CalendarView calendarView = bottomSheetDialog.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month++;
                monthMain = getMonth(month);
                String sDateTemp = dayOfMonth + "-" + monthMain + "-" + year;
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

                try {
                    Date d = formatter.parse(sDateTemp);
                    date1 = d.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                getDate();
            }
        });
        bottomSheetDialog.show();
    }

    private void getDate() {
        Date dateD1 = new Date();
        Date dateD2 = new Date();
        if(date1 == null && date2 != null)
        {
            dateD2 = new Date(date2);
            DateFormat dateF = new SimpleDateFormat("dd-MMM-yyyy");
            String strDate2 = dateF.format(dateD2);
            edtToDay.setText(strDate2);
        }
        else if(date1 != null && date2 == null)
        {
            dateD1 = new Date(date1);
            DateFormat dateF = new SimpleDateFormat("dd-MMM-yyyy");
            String strDate1 = dateF.format(dateD1);
            edtFromDay.setText(strDate1);
        }
        else
        {
            if(date1 > date2)
            {
                long temp = date1;
                date1 = date2;
                date2 = temp;
            }

            dateD1 = new Date(date1);
            dateD2 = new Date(date2);

            DateFormat dateF = new SimpleDateFormat("dd-MMM-yyyy");
            String strDate1 = dateF.format(dateD1);
            String strDate2 = dateF.format(dateD2);
            edtFromDay.setText(strDate1);
            edtToDay.setText(strDate2);
        }
    }

    private String getMonth(int month) {
        switch (month)
        {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
        }
        return "January";
    }

    private void showBottomSheetTo() {
        final BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(this);
        bottomSheetDialog1.setContentView(R.layout.bottom_sheet_dialog_layout);

        CalendarView calendarView = bottomSheetDialog1.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                month++;
//                dayTo = dayOfMonth;
//                monthTo = month;
//                yearTo = year;
                monthMain = getMonth(month);
                String sDateTemp = dayOfMonth + "-" + monthMain + "-" + year;
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

                try {
                    Date d = formatter.parse(sDateTemp);
                    date2 = d.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                getDate();
            }

        });
        bottomSheetDialog1.show();
    }
}