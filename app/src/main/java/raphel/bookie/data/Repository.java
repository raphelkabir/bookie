package raphel.bookie.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import raphel.bookie.data.room.Book;
import raphel.bookie.data.room.BookDao;
import raphel.bookie.data.room.DailyTargetDao;
import raphel.bookie.data.room.Database;
import raphel.bookie.data.room.Deadline;
import raphel.bookie.data.room.DeadlineDao;
import raphel.bookie.data.room.ReadingSession;

public class Repository {

    private static Repository repository;

    private ExecutorService executor;

    private Database database;

    private BookDao bookDao;
    private DeadlineDao deadlineDao;
    private DailyTargetDao dailyTargetDao;

    private List<ReadingSession> sessions;

    protected Repository() {
        executor = Executors.newSingleThreadExecutor();

        database = Database.getInstance();
        bookDao = database.bookDao();
        deadlineDao = database.deadlineDao();
        dailyTargetDao = database.dailyTargetDao();

        sessions = bookDao.getReadingSessions();
    }

    public synchronized static Repository getInstance() {
        if (repository == null) repository = new Repository();
        return repository;
    }

    public synchronized void insertBook(@NotNull Book book) {
        ReadingSession session = new ReadingSession();
        session.book = book;
        sessions.add(session);

        executor.execute(() -> {
            bookDao.insert(book);
            synchronized (this) {
                book.id = bookDao.getLast().id;
            }
        });
    }
    public synchronized void updateBook(@NotNull Book book) {
        executor.execute(() -> bookDao.update(book));
    }

    public synchronized void insertDeadline(@NotNull ReadingSession session, @NotNull Deadline deadline) {
        if (session.deadlines == null) session.deadlines = new ArrayList<>();
        session.deadlines.add(deadline);
        executor.execute(() -> {
            deadlineDao.insert(deadline);
            synchronized (this) {
                deadline.id = deadlineDao.getLast().id;
            }
        });
    }
    public synchronized void updateDeadline(@NotNull ReadingSession session, @NotNull Deadline deadline) {
        executor.execute(() -> deadlineDao.update(deadline));
    }
    public synchronized void deleteDeadline(@NotNull ReadingSession session, @NotNull Deadline deadline) {
        session.deadlines.remove(deadline);
        executor.execute(() -> deadlineDao.delete(deadline));
    }

    public synchronized void insertDailyTarget(@NotNull ReadingSession session) {
        executor.execute(() -> {
            dailyTargetDao.insert(session.dailyTarget);
            synchronized (this) {
                session.dailyTarget.id = dailyTargetDao.getLast().id;
            }
        });
    }
    public synchronized void updateDailyTarget(@NotNull ReadingSession session) {
        executor.execute(() -> dailyTargetDao.update(session.dailyTarget));
    }
    public synchronized void deleteDailyTarget(@NotNull ReadingSession session) {
        executor.execute(() -> dailyTargetDao.delete(session.dailyTarget));
    }

    public synchronized void deleteReadingSession(@NotNull ReadingSession session) {
        sessions.remove(session);

        executor.execute(() -> {
            bookDao.delete(session.book);
            if (session.deadlines != null && session.deadlines.size() > 0) {
                deadlineDao.deleteAllByBook(session.book.id);
            }
            if (session.dailyTarget != null) {
                dailyTargetDao.delete(session.dailyTarget);
            }
        });
    }

    private ReadingSession getById(long bookId) {
        for (ReadingSession session : sessions) {
           if (session.book.id == bookId) return session;
        }

        return null;
    }

    public synchronized List<ReadingSession> getSessions() {
        return sessions;
    }
}