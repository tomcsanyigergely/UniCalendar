package hu.bme.aut.unicalendar.data;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = { Event.class, Subject.class, Requirement.class }, version = 1)
@TypeConverters(Event.Converters.class)
public abstract class CalendarDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
    public abstract SubjectDao subjectDao();
    public abstract RequirementDao requirementDao();
}
