package com.example.smoothcardanimation;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<ExpandModel> data;
    private RecyclerView recyclerView;
    private int lastExpandedCardPosition;
    private int i=0;
    public RecyclerViewAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.card_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.headTextView.setText("Tap to expand");
        holder.expandedTextView.setText("this is sample text");
        if(data.get(position).isExpanded()){
            holder.expandableView.setVisibility(View.VISIBLE);
            holder.expandableView.setExpanded(true);
        }
        else{
            holder.expandableView.setVisibility(View.GONE);
            holder.expandableView.setExpanded(false);
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<ExpandModel> data) {
        this.data = data;
    }

    public void addItem(int i) {
        data.add(i,new ExpandModel());
        if(i<=lastExpandedCardPosition)
            lastExpandedCardPosition++;
        notifyDataSetChanged();
    }

    public void deleteItem(int i) {
        data.remove(i);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ExpandableLinearLayout expandableView;
        TextView headTextView;
        TextView expandedTextView;
        ViewHolder(View itemView) {
            super(itemView);
            headTextView = itemView.findViewById(R.id.head_textview);
            expandedTextView = itemView.findViewById(R.id.expanded_textview);
            expandableView = itemView.findViewById(R.id.expandableView);
            initializeClicks();
        }

        private void initializeClicks() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (expandableView.isExpanded()) {
                        expandableView.setExpanded(false);
                        expandableView.toggle();
                        data.get(getAdapterPosition()).setExpanded(false);
                    } else {
                        expandableView.setExpanded(true);
                        data.get(getAdapterPosition()).setExpanded(true);
                        expandableView.toggle();
                        if(lastExpandedCardPosition!=getAdapterPosition() && recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition)!=null){
                            ((ExpandableLinearLayout)recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition).itemView.findViewById(R.id.expandableView)).setExpanded(false);
                            data.get(lastExpandedCardPosition).setExpanded(false);
                            ((ExpandableLinearLayout)recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition).itemView.findViewById(R.id.expandableView)).toggle();
                        }
                        else if(lastExpandedCardPosition!=getAdapterPosition() && recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition)==null){
                            data.get(lastExpandedCardPosition).setExpanded(false);
                        }
                        lastExpandedCardPosition = getAdapterPosition();
                    }
                }
            });
        }
    }
}
