package raphel.bookie.data.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DailyTargetDao {

    @Insert
    void insert(DailyTarget dailyTarget);

    @Update
    void update(DailyTarget dailyTarget);

    @Delete
    void delete(DailyTarget dailyTarget);

    @Query("SELECT * FROM table_daily_target WHERE id=(SELECT max(id) FROM table_daily_target);")
    DailyTarget getLast();

    @Query("SELECT * FROM table_daily_target")
    List<DailyTarget> getAll();
}
