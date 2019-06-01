package hu.bme.aut.unicalendar.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RequirementDao {

    @Insert
    long insert(Requirement requirement);

    @Update
    void update(Requirement requirement);

    @Delete
    void delete(Requirement requirement);

    @Query("DELETE FROM requirement")
    public void nukeTable();

    @Query("SELECT * FROM requirement")
    List<Requirement> getAll();

    @Query("SELECT * FROM requirement WHERE name = :name")
    List<Requirement> getRequirementByName(String name);

    @Query("SELECT * FROM requirement WHERE id = :id")
    List<Requirement> getRequirementById(Long id);

    @Query("SELECT name FROM requirement")
    List<String> getRequirementNames();

    @Query("UPDATE requirement SET visible=:visible")
    public void updateVisibility(Boolean visible);

    @Query("SELECT visible FROM requirement WHERE id = :requirementId")
    Boolean getVisibility(Long requirementId);
}
