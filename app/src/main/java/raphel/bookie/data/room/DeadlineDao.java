package raphel.bookie.data.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DeadlineDao {

    @Insert
    void insert(Deadline deadline);

    @Update
    void update(Deadline deadline);

    @Delete
    void delete(Deadline deadline);

    @Query("SELECT * FROM table_deadline")
    LiveData<List<Deadline>> getAll();

    @Query("DELETE FROM table_deadline WHERE book_id=:bookId")
    void deleteByBookId(long bookId);
}
