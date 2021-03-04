package raphel.bookie.data.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDao {

    @Insert
    void insert(Book book);

    @Update
    void update(Book book);

    @Delete
    void delete(Book book);

    @Query("SELECT * FROM table_book")
    LiveData<List<Book>> getAll();

    @Transaction
    @Query("SELECT * FROM table_book")
    abstract LiveData<List<ReadingSession>> getReadingSessions();
}