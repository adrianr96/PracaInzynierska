package com.example.andrioid.pracainzynierska.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrioid.pracainzynierska.R;
import com.example.andrioid.pracainzynierska.model.User;
import com.example.andrioid.pracainzynierska.model.UserItem;

import java.util.List;


public class UserAdapter extends ArrayAdapter<UserItem> {

    public UserAdapter(@NonNull Context context, @NonNull List<UserItem> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.user_spinner_row, parent, false
            );
        }

//        ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);
        TextView textViewName = convertView.findViewById(R.id.text_view_name);
        TextView textViewNumber = convertView.findViewById(R.id.text_view_number);
        TextView textViewEmail = convertView.findViewById(R.id.text_view_email);

        UserItem currentItem = getItem(position);

        if (currentItem != null) {
//            imageViewFlag.setImageResource(currentItem.getFlagImage());
            textViewName.setText(currentItem.getUserName());
            textViewNumber.setText(currentItem.getUserNumber());
            textViewEmail.setText(currentItem.getUserEmail());
        }

        return convertView;
    }
}
