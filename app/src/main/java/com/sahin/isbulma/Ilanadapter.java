package com.sahin.isbulma;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sahin.isbulma.databinding.RecyclerRowBinding;

import java.util.ArrayList;

public class Ilanadapter extends RecyclerView.Adapter<Ilanadapter.IlanHolder> {
    private ArrayList<Ilan> ilanlist;

    public Ilanadapter(ArrayList<Ilan> ilanlist) {
        this.ilanlist = ilanlist;
    }

    @Override
    public IlanHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new IlanHolder(recyclerRowBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull Ilanadapter.IlanHolder holder, int position) {
        holder.recyclerRowBinding.recyclerViewbaslKtext.setText(ilanlist.get(position).ilanbaslıgı);
        holder.recyclerRowBinding.recyclerViewfirmatext.setText(ilanlist.get(position).firmadı);
        holder.recyclerRowBinding.kritertextforrow.setText(ilanlist.get(position).kriterler);
    }

    @Override
    public int getItemCount() {
        return ilanlist.size();
    }


    public  class IlanHolder extends RecyclerView.ViewHolder {
        RecyclerRowBinding recyclerRowBinding;
        public IlanHolder(RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding=recyclerRowBinding;

        }

    }


}
