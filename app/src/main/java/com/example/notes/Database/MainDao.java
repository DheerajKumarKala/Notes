package com.example.notes.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.notes.Model.Notes;

import java.util.List;

@Dao
public interface MainDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Notes notes);
    @Query("SELECT * from notes order by id DESC")
    List<Notes> getAll();

    @Query("UPDATE notes SET title=:title,notes=:notes WHERE ID=:id")
    void update(int id,String title,String notes);

    @Delete
    void delete(Notes notes);

    @Query("UPDATE notes SET pinned=:pin WHERE id=:id ")
    void pin(int id,boolean pin);

}
