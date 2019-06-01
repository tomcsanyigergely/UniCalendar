package hu.bme.aut.unicalendar.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SubjectDao {

    @Insert
    long insert(Subject subject);

    @Update
    void update(Subject subject);

    @Delete
    void delete(Subject subject);

    @Query("DELETE FROM subject")
    public void nukeTable();

    @Query("SELECT * FROM subject")
    List<Subject> getAll();

    @Query("SELECT * FROM subject WHERE name = :name")
    List<Subject> getSubjectByName(String name);

    @Query("SELECT * FROM subject WHERE id = :id")
    List<Subject> getSubjectById(Long id);

    @Query("SELECT name FROM subject")
    List<String> getSubjectNames();

    @Query("UPDATE subject SET visible=:visible")
    public void updateVisibility(Boolean visible);

    @Query("SELECT visible FROM subject WHERE id = :subjectId")
    Boolean getVisibility(Long subjectId);
}
