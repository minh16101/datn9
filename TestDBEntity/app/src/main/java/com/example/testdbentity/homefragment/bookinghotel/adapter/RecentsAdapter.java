package com.example.testdbentity.homefragment.bookinghotel.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testdbentity.R;
import com.example.testdbentity.homefragment.bookinghotel.image.roomandimg.RoomWithImage;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class RecentsAdapter extends RecyclerView.Adapter<RecentsAdapter.RecentsViewHolder> {
    private List<RoomWithImage> roomWithImageList;
    private OnclickToDetail onclickToDetail;
    private Context context;

    public RecentsAdapter(List<RoomWithImage> roomWithImageList) {
        this.roomWithImageList = roomWithImageList;
    }

    public void setOnclickToDetail(OnclickToDetail onclickToDetail) {
        this.onclickToDetail = onclickToDetail;
    }

    public void setRoomWithImageList(List<RoomWithImage> roomWithImageList) {
        this.roomWithImageList = roomWithImageList;
    }

    @NonNull
    @Override
    public RecentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recents, parent, false);
        return new RecentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentsViewHolder holder, int position) {
        RoomWithImage roomWithImage = roomWithImageList.get(position);
        holder.imageView.setImageResource(roomWithImage.imageRooms.get(0).srcImageRoom);
        holder.nameTv.setText(roomWithImage.bookingRoomEntity.nameBookingRoom);
        holder.placeTv.setText(formatVnCurrence(context, String.valueOf(roomWithImage.bookingRoomEntity.priceBookingRoom)));
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclickToDetail.OnClick(roomWithImage.bookingRoomEntity.idBookingRoom);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomWithImageList.size();
    }

    public String formatVnCurrence(Context context, String price) {

        NumberFormat format = new DecimalFormat("#,##0.00");// #,##0.00 ¤ (¤:// Currency symbol)
        format.setCurrency(Currency.getInstance(Locale.US));//Or default locale

        price = (!TextUtils.isEmpty(price)) ? price : "0";
        price = price.trim();
        price = format.format(Double.parseDouble(price));
        price = price.replaceAll(",", "\\.");

        if (price.endsWith(".00")) {
            int centsIndex = price.lastIndexOf(".00");
            if (centsIndex != -1) {
                price = price.substring(0, centsIndex);
            }
        }
        price = String.format("%s đ", price);
        return price;
    }
    public class RecentsViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView placeTv;
        private TextView nameTv;
        private ConstraintLayout constraintLayout;
        public RecentsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image_recent);
            placeTv = itemView.findViewById(R.id.item_name_place_recent);
            nameTv = itemView.findViewById(R.id.item_country_recent);
            constraintLayout = itemView.findViewById(R.id.item_recent_btn);

        }
    }
}
