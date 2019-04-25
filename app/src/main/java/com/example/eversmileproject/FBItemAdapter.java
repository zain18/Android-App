package com.example.eversmileproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FBItemAdapter extends RecyclerView.Adapter<FBItemAdapter.ViewHolder> {

    RecyclerView recyclerView;
    Context context;
    ArrayList<String> items = new ArrayList<String>();
    ArrayList<String> urls = new ArrayList<String>();

    public void update (String name, String url){
        items.add(name);
        urls.add(url);
        notifyDataSetChanged(); // refresh recycler view
    }

    public FBItemAdapter(RecyclerView recyclerView, Context context, ArrayList<String> item, ArrayList<String> urls) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = item;
        this.urls = urls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.fbitem, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.nameOfFile.setText(items.get(i));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nameOfFile;

        public ViewHolder(View itemView){
            super(itemView);
            nameOfFile=itemView.findViewById(R.id.fbitemtextview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = recyclerView.getChildLayoutPosition(v);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urls.get(position)));
                    context.startActivity(intent);


                }
            });
        }
    }
}
