package com.example.dietideals24.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
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

    private static class ViewHolder {
        TextView textView;
    }

    public interface OnAstaClickListener {
        void onAstaClicked(int idAsta);
    }

    private final List<NotificaDTO> notifiche;
    private final Context context;
    private OnAstaClickListener onAstaClickListener;

    public NotificaAdapter(@NonNull Context context, @NonNull List<NotificaDTO> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context = context;
        this.notifiche = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NotificaDTO notifica = getItem(position);

        // Costruisci il testo mostrato nella TextView
        String testo = notifica.getTesto() + " ";
        String nomeAsta = notifica.getNome_asta() != null ? notifica.getNome_asta() : "";
        String data = notifica.getData();
        String nomeAstaData = nomeAsta + "\n" + data;
        int nomeAstaLength = nomeAsta.length();

        // Imposta il testo nella TextView
        SpannableString spannableString = new SpannableString(testo + nomeAstaData);

        // Aggiungi il nome dell'asta come testo cliccabile e in grassetto
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (onAstaClickListener != null) {
                    onAstaClickListener.onAstaClicked(notifica.getId_Asta());
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.BLACK);
            }
        };

        int start = testo.length();
        int end = start + nomeAstaLength;

        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        viewHolder.textView.setText(spannableString);
        viewHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());

        if (!notifica.isLetta()) {
            viewHolder.textView.setTypeface(null, Typeface.BOLD);
        } else {
            viewHolder.textView.setTypeface(null, Typeface.NORMAL);
        }

        convertView.setOnClickListener(v -> {
            if (!notifica.isLetta()) {
                notifica.setLetta(true);
                notifyDataSetChanged();
            }
        });


        return convertView;
    }

    public void setOnAstaClickListener(OnAstaClickListener listener) {
        this.onAstaClickListener = listener;
    }

    public void removeNotifica(NotificaDTO notifica) {
        notifiche.remove(notifica);
        notifyDataSetChanged();
    }

    public void updateNotifica(NotificaDTO notifica) {
        notifyDataSetChanged();
    }
}
