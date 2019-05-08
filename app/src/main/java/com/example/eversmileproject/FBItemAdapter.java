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

// This class is the FireBase item adapter for the recycler views
public class FBItemAdapter extends RecyclerView.Adapter<FBItemAdapter.ViewHolder> {

    RecyclerView recyclerView;
    Context context;
    ArrayList<String> items = new ArrayList<String>();
    ArrayList<String> urls = new ArrayList<String>();

    // update the recycler view item with name and download url
    public void update (String name, String url){
        items.add(name); // add name
        urls.add(url); // add url
        notifyDataSetChanged(); // refresh recycler view
    }

    // firebase item constructor, pass in the recycler, context, item and url lists
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
            // initialize text view
            nameOfFile=itemView.findViewById(R.id.fbitemtextview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // retrieve index of item
                    int position = recyclerView.getChildLayoutPosition(v);
                    // ininitialize intent to connect to browser
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    // grab url from recycler list item
                    intent.setData(Uri.parse(urls.get(position)));
                    // go to browser to download item
                    context.startActivity(intent);
                }
            });
        }
    }
}
