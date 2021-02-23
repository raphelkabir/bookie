package raphel.bookie.data.room;

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
    void insertAll(Book... books);

    @Update
    void updateAll(Book... books);

    @Delete
    void deleteAll(Book... books);

    @Query("SELECT * FROM table_book WHERE id=(SELECT max(id) FROM table_book);")
    Book getLast();

    @Query("SELECT * FROM table_book")
    List<Book> getAll();

    @Transaction
    @Query("SELECT * FROM table_book")
    List<ReadingSession> getReadingSessions();
}