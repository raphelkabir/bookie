package raphel.bookie.data.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DeadlineDao {

    @Insert
    void insertAll(Deadline... deadlines);

    @Update
    void updateAll(Deadline... deadlines);

    @Delete
    void deleteAll(Deadline... deadlines);

    @Query("SELECT * FROM table_deadline WHERE id=(SELECT max(id) FROM table_deadline);")
    Deadline getLast();

    @Query("SELECT * FROM table_deadline")
    List<Deadline> getAll();

    @Query("DELETE FROM table_deadline WHERE book_id=:bookId")
    void deleteAllByBook(long bookId);
}
