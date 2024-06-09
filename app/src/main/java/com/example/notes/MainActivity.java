package com.example.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.notes.Adapter.NoteListAdapter;
import com.example.notes.Database.RoomDb;
import com.example.notes.Interface.NoteClickListener;
import com.example.notes.Model.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    RecyclerView recyclerView;
    NoteListAdapter noteListAdapter;
    RoomDb database;
    List<Notes> notes=new ArrayList<>();

    FloatingActionButton faBtn;
    Notes selectedNote;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView =findViewById(R.id.NoteRv);
        faBtn =findViewById(R.id.addBtn);
        searchView=findViewById(R.id.searchView);
        database=RoomDb.getInstance(this);
        notes=database.mainDao().getAll();

        updateRecycle(notes);

        faBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NotesTakeActivity.class);
                startActivityForResult(intent,101);

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText)
    {
        List<Notes> filterList=new ArrayList<>();
        for(Notes singleNote:notes){
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())||singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filterList.add(singleNote);
            }
        }
        noteListAdapter.filterList(filterList);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101)
        {
            if(resultCode== Activity.RESULT_OK){
                assert data != null;
                Notes new_notes= (Notes) data.getSerializableExtra("note");

                database.mainDao().insert(new_notes);
                notes.clear();//just displayed
//                notes.add(new_notes);
                notes.addAll(database.mainDao().getAll());

                noteListAdapter.notifyDataSetChanged();
            }
        }else if(requestCode == 102)
        {
            if(resultCode== Activity.RESULT_OK){
                assert data != null;
                Notes new_notes= (Notes) data.getSerializableExtra("note");

                database.mainDao().update(new_notes.getID(), new_notes.getTitle(), new_notes.getNotes());
                notes.clear();//just displayed
//                notes.add(new_notes);
                notes.addAll(database.mainDao().getAll());

                noteListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycle(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        noteListAdapter=new NoteListAdapter(MainActivity.this,notes,noteClickListener);
        recyclerView.setAdapter(noteListAdapter);
    }
    private final NoteClickListener noteClickListener=new NoteClickListener(){
        @Override
        public void onClick(Notes notes) {
        Intent intent=new Intent(MainActivity.this,NotesTakeActivity.class);
        intent.putExtra("old_notes",notes);
        startActivityForResult(intent,102);


        }

        @Override
        public void onLongPress(Notes notes, CardView cardView) {
            selectedNote=new Notes();
            selectedNote=notes;
            showPopUp(cardView);


        }

        private void showPopUp(CardView cardView) {

            PopupMenu popupmenu;
            popupmenu = new PopupMenu(MainActivity.this,cardView);
            popupmenu.setOnMenuItemClickListener(MainActivity.this);
            popupmenu.inflate(R.menu.popup_menu);
            popupmenu.show();

        }
    };

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId()==R.id.pin)
        {
            if(selectedNote.getPinned())
            {
                database.mainDao().pin(selectedNote.getID(),false);
                Toast.makeText(this,"UnPinned",Toast.LENGTH_SHORT).show();
            }else {
                database.mainDao().pin(selectedNote.getID(),true);
                Toast.makeText(this,"Pinned",Toast.LENGTH_SHORT).show();
            }
            notes.clear();
            notes.addAll(database.mainDao().getAll());
            noteListAdapter.notifyDataSetChanged();
            return true;
        }else if(item.getItemId()==R.id.delete)
        {
            database.mainDao().delete(selectedNote);
            notes.remove(selectedNote);
            noteListAdapter.notifyDataSetChanged();
            Toast.makeText(this,"Note Deleted",Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}