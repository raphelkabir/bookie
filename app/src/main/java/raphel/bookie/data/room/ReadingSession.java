package raphel.bookie.data.room;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ReadingSession {

    @Embedded
    public Book book;

    @Relation(parentColumn = "id", entityColumn = "book_id")
    public List<Deadline> deadlines;

    @Relation(parentColumn = "id", entityColumn = "book_id")
    public DailyTarget dailyTarget;
}
