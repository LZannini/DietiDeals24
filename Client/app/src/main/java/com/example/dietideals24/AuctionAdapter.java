package com.example.dietideals24;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietideals24.dto.AstaDTO;
import com.example.dietideals24.dto.Asta_RibassoDTO;
import com.example.dietideals24.dto.Asta_InversaDTO;
import com.example.dietideals24.dto.Asta_SilenziosaDTO;
import java.util.List;

public class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.AuctionViewHolder> {

    private List<AstaDTO> auctionList;

    public AuctionAdapter(List<AstaDTO> auctionList) {
        this.auctionList = auctionList;
    }

    @NonNull
    @Override
    public AuctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_asta, parent, false);
        return new AuctionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuctionViewHolder holder, int position) {
        AstaDTO asta = auctionList.get(position);
        holder.bind(asta);
    }

    @Override
    public int getItemCount() {
        return auctionList.size();
    }

    public class AuctionViewHolder extends RecyclerView.ViewHolder {

        TextView astaNome;
        TextView astaPrezzo;
        ImageView astaTipoImage;

        public AuctionViewHolder(@NonNull View itemView) {
            super(itemView);
            astaNome = itemView.findViewById(R.id.nome_asta);
            astaPrezzo = itemView.findViewById(R.id.prezzo_asta);
            astaTipoImage = itemView.findViewById(R.id.tipo_asta_image);
        }

        public void bind(AstaDTO asta) {
            astaNome.setText(asta.getNome());

            // Puoi aggiungere altre impostazioni in base al tipo di asta se necessario
            if (asta instanceof Asta_RibassoDTO) {
                Asta_RibassoDTO ribasso = (Asta_RibassoDTO) asta;
                astaPrezzo.setText(String.valueOf(ribasso.getPrezzo()));
                astaTipoImage.setImageResource(R.drawable.ribasso);
            } else if (asta instanceof Asta_SilenziosaDTO) {
                Asta_SilenziosaDTO silenziosa = (Asta_SilenziosaDTO) asta;
                astaTipoImage.setImageResource(R.drawable.silenziosa);
            } else if (asta instanceof Asta_InversaDTO) {
                Asta_InversaDTO inversa = (Asta_InversaDTO) asta;
                astaTipoImage.setImageResource(R.drawable.inversa);
            }
        }
    }
}
