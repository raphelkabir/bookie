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
    void insertAll(DailyTarget... dailyTargets);

    @Update
    void updateAll(DailyTarget... dailyTargets);

    @Delete
    void deleteAll(DailyTarget... dailyTargets);

    @Query("SELECT * FROM table_daily_target WHERE id=(SELECT max(id) FROM table_daily_target);")
    DailyTarget getLast();

    @Query("SELECT * FROM table_daily_target")
    List<DailyTarget> getAll();
}
