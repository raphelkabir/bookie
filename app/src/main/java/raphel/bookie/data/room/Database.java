package raphel.bookie.data.room;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Book.class, Deadline.class, DailyTarget.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    public abstract BookDao bookDao();
    public abstract DeadlineDao deadlineDao();
    public abstract DailyTargetDao dailyTargetDao();

    private static Context context;
    public static synchronized void setApplicationContext(Context context) {
        Database.context = context;
    }

    private static Database database;
    public static synchronized Database getInstance() {
        if (database == null) {

            if (context == null) {
                throw new AssertionError("ApplicationContext cannot be null");
            }

            database = Room.databaseBuilder(context, Database.class, "database").build();
        }

        return database;
    }
}