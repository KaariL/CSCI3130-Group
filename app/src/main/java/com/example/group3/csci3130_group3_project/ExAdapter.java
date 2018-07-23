package com.example.group3.csci3130_group3_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ExAdapter extends RecyclerView.Adapter<ExAdapter.ExViewHolder> {
    private  ArrayList<ExItem> exItems;
    public static class ExViewHolder extends RecyclerView.ViewHolder {
        public ImageView mima;
        public TextView text1;
        public ExViewHolder(final View itemView) {
            super(itemView);
            mima=itemView.findViewById(R.id.imageView);
            text1=itemView.findViewById(R.id.text1);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(v.getContext(),DalServicesActivity.class);
                    intent.putExtra("select",text1.getText().toString());
                    v.getContext().startActivity(intent);

                    /*******************************************************************************
                     *                       choose CardView
                     ******************************************************************************/
                }
            });

        }

    }

    public  ExAdapter(ArrayList<ExItem> exItemArrayList)
    {
        exItems=exItemArrayList;
    }
    @Override
    public ExViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.ex_item,parent,false);
        ExViewHolder evh=new ExViewHolder(v);

        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExViewHolder holder, int position) {
        ExItem curritem=exItems.get(position);
        holder.mima.setImageResource(curritem.getmImaSource());
        holder.text1.setText(curritem.getmText());

    }

    @Override
    public int getItemCount() {
        return exItems.size();
    }
}
