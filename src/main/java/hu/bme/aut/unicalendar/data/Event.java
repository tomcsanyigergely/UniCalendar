package hu.bme.aut.unicalendar.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName="event",
foreignKeys = {
    @ForeignKey(
            entity = Subject.class,
            parentColumns="id",
            childColumns="subjectId",
            onDelete=CASCADE),
    @ForeignKey(
            entity = Requirement.class,
            parentColumns="id",
            childColumns="requirementId",
            onDelete=CASCADE)
})
public class Event implements Comparable<Event> {

    @Override
    public int compareTo(@NonNull Event o) {
        return date.compareTo(o.date);
    }

    public static class Converters {
        @TypeConverter
        public static Date longToDate(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static Long dateToLong(Date date) {
            return date == null ? null : date.getTime();
        }
    }

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "subjectId")
    public Long subjectId;

    @ColumnInfo(name = "requirementId")
    public Long requirementId;

    @ColumnInfo(name = "date")
    public Date date;

    @ColumnInfo(name = "notification")
    public Long notification;

    public transient String requirement;

    public transient String subject;
}
