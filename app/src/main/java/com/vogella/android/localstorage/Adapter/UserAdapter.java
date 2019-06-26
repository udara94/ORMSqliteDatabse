package com.vogella.android.localstorage.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vogella.android.localstorage.Database.DatabaseManager;
import com.vogella.android.localstorage.ListActivity;
import com.vogella.android.localstorage.R;
import com.vogella.android.localstorage.UserItemDB;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private  List<UserItemDB> userItemDBList;
    private Context mContext;


    public class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView name, age, index;
        public LinearLayout editLayout, deleteLayout;

        public UserViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            age = (TextView) view.findViewById(R.id.age);
            index = (TextView) view.findViewById(R.id.index);
            editLayout = (LinearLayout) view.findViewById(R.id.edit_layout);
            deleteLayout = (LinearLayout) view.findViewById(R.id.delete_layout);
        }
    }


    public UserAdapter(List<UserItemDB> userItemDBList,Context context) {

        this.userItemDBList = userItemDBList;
        this.mContext = context;
    }


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_list_row, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        final UserItemDB user = userItemDBList.get(position);

        holder.name.setText(user.getName());
        holder.age.setText(user.getAge());
        holder.index.setText(user.getIndex());

        holder.editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof ListActivity) {
                    ((ListActivity)mContext).presentAlert(user.getName(), user.getAge(), user.getIndex());
                    notifyDataSetChanged();
                }
            }
        });

        holder.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               presentAlertDelete(holder.getAdapterPosition(), user.getIndex());
            }
        });


    }

    public void updateUserList(List<UserItemDB> newlist) {
        userItemDBList.clear();
        userItemDBList.addAll(newlist);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return userItemDBList.size();
    }

    public void presentAlertDelete(final int position, final String index){

        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("Delete User")
                .setMessage("Are you sure to delete this user?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseManager.getInstance(mContext).deleteUser(index);
                       deleteUser(position);
                    }
                })
                .setNegativeButton("No", null)
                .create();
        dialog.show();

    }

    private void deleteUser(int position) {
        userItemDBList.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position,userItemDBList.size());

    }


}
