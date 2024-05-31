package com.example.dietideals24.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.dietideals24.dto.NotificaDTO;
import java.util.List;

public class NotificaAdapter extends ArrayAdapter<NotificaDTO> {

    private final List<NotificaDTO> notifiche;

    public NotificaAdapter(@NonNull Context context, @NonNull List<NotificaDTO> objects) {
        super(context, 0, objects);
        this.notifiche = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        NotificaDTO notifica = getItem(position);

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(notifica.getTesto() + "\n" + notifica.getData());
        if (!notifica.isLetta()) {
            textView.setTypeface(null, Typeface.BOLD);
        } else {
            textView.setTypeface(null, Typeface.NORMAL);
        }

        return convertView;
    }

    public void removeNotifica(NotificaDTO notifica) {
        notifiche.remove(notifica);
        notifyDataSetChanged();
    }

    public void updateNotifica(NotificaDTO notifica) {
        notifyDataSetChanged();
    }
}


