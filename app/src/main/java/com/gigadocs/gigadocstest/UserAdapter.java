package com.gigadocs.gigadocstest;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gigadocs.gigadocstest.Extras.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private final List<User> users;
    public UserAdapter(List<User> users) {
        this.users = users;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User myListData = users.get(position);
        holder.textView.setText(myListData.getName());

        holder.email.setText(myListData.getEmail());
        holder.gender.setText(myListData.getGender());
        holder.mobile.setText(myListData.getMobile());
        holder.age.setText(String.valueOf(myListData.getAge()));
        holder.date.setText("Date:"+myListData.getDate());
        holder.imageView.setImageBitmap(Utils.convertBase64ToBitmap(myListData.getImage()));

    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setFilter(List<User> newList) {
        users.clear();
        users.addAll(newList);
        notifyDataSetChanged();
    }

    public void sortByDate(int type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utils.getTimeFormat()); //your own date format
        if (users != null) {
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    try {
                        Log.i("TypeAdapter",type+"");
                        if (type==0)
                            return simpleDateFormat.parse(o2.getDate()).compareTo(simpleDateFormat.parse(o1.getDate()));
                        else
                            return simpleDateFormat.parse(o1.getDate()).compareTo(simpleDateFormat.parse(o2.getDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });
            notifyDataSetChanged();
        }
    }

    public void sortByAge(int type) {
        if (users != null) {
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    if (type==0)
                        return o2.getAge()-o1.getAge();
                    else
                        return o1.getAge()-o2.getAge();
                }
            });
            notifyDataSetChanged();
        }
    }

    public List<User> getData() {
        return users;
    }

    public void removeItem(int position) {
        users.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(User item, int position) {
        users.add(position,item);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView,email,gender,age,mobile,date;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.email = (TextView) itemView.findViewById(R.id.description);
            this.gender = (TextView) itemView.findViewById(R.id.gender);
            this.age = (TextView) itemView.findViewById(R.id.age);
            this.mobile = (TextView) itemView.findViewById(R.id.mobile);
            this.date = (TextView) itemView.findViewById(R.id.date);

            linearLayout = (LinearLayout)itemView.findViewById(R.id.linearLayout);
        }
    }
}