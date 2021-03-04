package raphel.bookie.data.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.List;

public class ReadingSession {

    @Embedded
    public Book book;

    @Relation(parentColumn = "id", entityColumn = "book_id")
    public List<Deadline> deadlines;

    @Relation(parentColumn = "id", entityColumn = "book_id")
    public DailyTarget dailyTarget;

    public boolean areItemsTheSame(Object obj) {
        ReadingSession session = (ReadingSession) obj;
        if (session == null) return false;

        return this.book.id == session.book.id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        ReadingSession session = (ReadingSession) obj;
        if (session == null) return false;

        if (this.book != null && !this.book.equals(session.book)) return false;
        if (this.dailyTarget != null &&!this.dailyTarget.equals(session.dailyTarget)) return false;
        if (this.deadlines != null && this.deadlines.size() != session.deadlines.size()) return false;

        for (int i = 0; i < session.deadlines.size(); i++) {
            Deadline deadline = session.deadlines.get(i);

            if (this.deadlines.get(i).areItemsTheSame(deadline)
                    && this.deadlines.get(i).equals(deadline)) return false;
        }
        return true;
    }
}
