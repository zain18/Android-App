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

    public void update (String name, String url){ // update the recycler view item with name and download url
        items.add(name); // add name
        urls.add(url); // add url
        notifyDataSetChanged(); // refresh recycler view
    }

    // firebase item constructor
    public FBItemAdapter(RecyclerView recyclerView, Context context, ArrayList<String> item, ArrayList<String> urls) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = item;
        this.urls = urls;
    }

    @NonNull
    @Override // creates the layout for item on recycler list
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.fbitem, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override // set the text for item
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.nameOfFile.setText(items.get(i));
    }

    @Override // returns the number of items
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nameOfFile; // text box for item name

        public ViewHolder(View itemView){
            super(itemView);
            nameOfFile=itemView.findViewById(R.id.fbitemtextview); // initialize text view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = recyclerView.getChildLayoutPosition(v); // retrieve index of item
                    Intent intent = new Intent(Intent.ACTION_VIEW); // ininitialize intent to connect to browser
                    intent.setData(Uri.parse(urls.get(position))); // grab url from recycler list item
                    context.startActivity(intent); // go to browser to download item
                }
            });
        }
    }
}
