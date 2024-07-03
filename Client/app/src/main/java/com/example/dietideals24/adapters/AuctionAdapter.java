package com.example.dietideals24.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietideals24.R;
import com.example.dietideals24.models.Asta;
import com.example.dietideals24.models.Asta_Inversa;
import com.example.dietideals24.models.Asta_Ribasso;
import com.example.dietideals24.models.Asta_Silenziosa;

import java.util.List;

public class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.AuctionViewHolder> {

    private final List<Asta> auctionList;
    private final OnAstaListener onAstaListener;

    public AuctionAdapter(List<Asta> auctionList, OnAstaListener onAstaListener) {
        this.auctionList = auctionList;
        this.onAstaListener = onAstaListener;
    }

    @NonNull
    @Override
    public AuctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_asta, parent, false);
        return new AuctionViewHolder(view, onAstaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AuctionViewHolder holder, int position) {
        Asta asta = auctionList.get(position);
        holder.bind(asta);
    }

    @Override
    public int getItemCount() {
        return auctionList.size();
    }

    public static class AuctionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView astaFoto;
        TextView astaNome;
        ImageView astaTipoImage;
        OnAstaListener onAstaListener;

        public AuctionViewHolder(@NonNull View itemView, OnAstaListener onAstaListener) {
            super(itemView);
            astaFoto = itemView.findViewById(R.id.foto_asta_image);
            astaNome = itemView.findViewById(R.id.nome_asta);
            astaTipoImage = itemView.findViewById(R.id.tipo_asta_image);
            this.onAstaListener = onAstaListener;

            itemView.setOnClickListener(this); // This refers to AuctionViewHolder which implements OnClickListener
        }

        public void bind(Asta asta) {
            Bitmap bitmap;
            astaNome.setText(asta.getNome());
            byte[] fotoBytes = asta.getFoto();
            if (fotoBytes != null) {
                bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
                astaFoto.setImageBitmap(bitmap);
            }
            //astaTipoImage.setImageResource(R.drawable.silenziosa); //<- foto del tipo

            if (asta instanceof Asta_Ribasso) {
                Asta_Ribasso ribasso = (Asta_Ribasso) asta;
                astaTipoImage.setImageResource(R.drawable.ribasso);
                Log.d("AuctionAdapter", "Asta di tipo Ribasso: " + ribasso.getNome());
            } else if (asta instanceof Asta_Silenziosa) {
                Asta_Silenziosa silenziosa = (Asta_Silenziosa) asta;
                astaTipoImage.setImageResource(R.drawable.silenziosa);
                Log.d("AuctionAdapter", "Asta di tipo Silenziosa: " + silenziosa.getNome());
            } else if (asta instanceof Asta_Inversa) {
                Asta_Inversa inversa = (Asta_Inversa) asta;
                astaTipoImage.setImageResource(R.drawable.inversa);
                Log.d("AuctionAdapter", "Asta di tipo Inversa: " + inversa.getNome());
            } else
                Log.d("AuctionAdapter", "Tipo di asta sconosciuto: " + asta.getNome());
        }

        @Override
        public void onClick(View v) {
            onAstaListener.onAstaClick(getAdapterPosition());
        }
    }

    public interface OnAstaListener {
        void onAstaClick(int position);
    }
}
