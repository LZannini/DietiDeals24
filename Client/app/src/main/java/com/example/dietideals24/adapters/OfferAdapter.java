package com.example.dietideals24.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietideals24.R;
import com.example.dietideals24.models.Offerta;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder>{
    private List<Offerta> offerList;
    private final OnOffertaListener onOffertaListener;

    public OfferAdapter(List<Offerta> offerList, OnOffertaListener onOffertaListener) {
        this.offerList = offerList;
        this.onOffertaListener = onOffertaListener;
        Log.d("OfferAdapter", "Adapter initialized with " + offerList.size() + " items.");
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offerta, parent, false);
        return new OfferViewHolder(view, onOffertaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferAdapter.OfferViewHolder holder, int position) {
        Offerta offerta = offerList.get(position);
        Log.d("OfferAdapter", "Binding offer at position " + position + " with value " + offerta.getValore());
        holder.bind(offerta);
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView valOfferta;
        Button btnAccept, btnReject;
        OnOffertaListener onOffertaListener;

        public OfferViewHolder(@NonNull View itemView, OnOffertaListener onOffertaListener) {
            super(itemView);
            valOfferta = itemView.findViewById(R.id.valOfferta);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
            this.onOffertaListener = onOffertaListener;

            btnAccept.setOnClickListener(this);
            btnReject.setOnClickListener(this);
        }

        public void bind(Offerta offerta) {
            Log.d("OfferAdapter", "Valore dell'offerta" + offerta.getValore());
            valOfferta.setText(NumberFormat.getCurrencyInstance(Locale.ITALY).format(offerta.getValore()) + " da " + offerta.getOfferente());
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btnAccept) {
                onOffertaListener.onAcceptClick(getAdapterPosition());
            } else if (view.getId() == R.id.btnReject) {
                onOffertaListener.onRejectClick(getAdapterPosition());
            }
        }
    }

    public interface OnOffertaListener {
        void onAcceptClick(int position);
        void onRejectClick(int position);
    }
}
