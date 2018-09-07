package com.example.mohamed.git_task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.mohamed.git_task.R.color.forkMissed;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
        {
    private ArrayList<RepoModel> mDataset;
    private Context context;



    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        ItemClickListener itemClickListener;
        public TextView owner,desc,name;
        public MyViewHolder(View v) {
            super(v);
            owner = v.findViewById(R.id.repOwner);
            desc=v.findViewById(R.id.repDesc);
            name=v.findViewById(R.id.repName);

            v.setOnLongClickListener(this);
        }
        void setItemLongClickListener(ItemClickListener itemLongClickListener){
            this.itemClickListener=itemLongClickListener;
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return true;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context, ArrayList<RepoModel> mDataset) {
        this.mDataset=mDataset;
        this.context=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String owner=this.mDataset.get(position).getRepOwner();
        String desc=this.mDataset.get(position).getRepDesc();
        String name=this.mDataset.get(position).getRepName();


        holder.owner.setText("Owner: "+owner);
        holder.desc.setText("Description: "+desc);
        holder.name.setText("Name: "+name);

        boolean fork=this.mDataset.get(position).isForkFlag();
        if(!fork){
            holder.itemView.setBackgroundColor(Color.GREEN);
        }

        holder.setItemLongClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                if(isLongClick){
                    showDialog(position);
                }
            }
        });
    }

    private void showDialog(final int position) {
        final AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setTitle("Select One....");
        //dialog.setMessage("Please use email to sign in");

        LayoutInflater inflater=LayoutInflater.from(context);
        final View dialog_layout=inflater.inflate(R.layout.dialog,null);
        dialog.setView(dialog_layout);

        dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            RadioGroup radioGroup=dialog_layout.findViewById(R.id.rg);
                int id = radioGroup.getCheckedRadioButtonId();
                String url="";
                switch (id){
                    case R.id.rd_rep:
                        url=mDataset.get(position).getRep_url();
                        break;
                    case R.id.rd_owner:
                        url=mDataset.get(position).getOwner_url();
                        break;
                }

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));

                context.startActivity(i);

            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

            // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.mDataset.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}