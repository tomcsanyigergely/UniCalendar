package hu.bme.aut.unicalendar.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface EventDao {
    @Insert
    long insert(Event event);

    @Update
    void update(Event event);

    @Delete
    void delete(Event event);

    @Query("DELETE FROM event")
    public void nukeTable();

    @Query("SELECT * FROM event")
    List<Event> getAll();
}
