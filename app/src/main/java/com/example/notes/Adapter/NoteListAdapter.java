package com.example.notes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.Interface.NoteClickListener;
import com.example.notes.Model.Notes;
import com.example.notes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteListAdapter extends RecyclerView.Adapter<NotesViewHolder> {
    Context context;
    List<Notes> notesList;
    NoteClickListener listener;


    public NoteListAdapter(Context context, List<Notes> notesList, NoteClickListener listener) {
        this.context = context;
        this.notesList = notesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.note_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.titleText.setText(notesList.get(position).getTitle());
        holder.notesText.setText(notesList.get(position).getNotes());
        holder.dateText.setText(notesList.get(position).getDate());
        holder.dateText.setSelected(true);

        if(notesList.get(position).getPinned()){
            holder.imageView.setImageResource(R.drawable.baseline_push_pin_24);
        }
        else {
            holder.imageView.setImageResource(0);
        }

        int colorcode=getRandomColor();
        holder.cardview.setCardBackgroundColor(holder.itemView.getResources().getColor(colorcode));
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(notesList.get(holder.getAdapterPosition()));
            }
        });

        holder.cardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongPress(notesList.get(holder.getAdapterPosition()),holder.cardview);
                return true;
            }
        });
    }

    private int getRandomColor()
    {
        List<Integer> colorCode=new ArrayList<>();
        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);
        colorCode.add(R.color.color6);
        colorCode.add(R.color.color7);
        colorCode.add(R.color.color8);
        colorCode.add(R.color.color9);

        Random random=new Random();
        int random_color=random.nextInt(colorCode.size());
        return colorCode.get(random_color);


    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }


    public void filterList(List<Notes> filterList)
    {
        notesList=filterList;
        notifyDataSetChanged();
    }
}
class NotesViewHolder extends RecyclerView.ViewHolder{
    CardView cardview;
    TextView notesText,dateText,titleText;
    ImageView imageView;
    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        cardview= itemView.findViewById(R.id.note_container);
        notesText=itemView.findViewById(R.id.notesText);
        titleText=itemView.findViewById(R.id.titleText);
        dateText=itemView.findViewById(R.id.dateText);
        imageView=itemView.findViewById(R.id.imagePin);
    }
}