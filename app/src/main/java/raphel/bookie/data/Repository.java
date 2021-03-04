package raphel.bookie.data;

import android.se.omapi.Session;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import raphel.bookie.data.room.Book;
import raphel.bookie.data.room.BookDao;
import raphel.bookie.data.room.DailyTarget;
import raphel.bookie.data.room.DailyTargetDao;
import raphel.bookie.data.room.Database;
import raphel.bookie.data.room.Date;
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

    private LiveData<List<ReadingSession>> sessions;
    private LiveData<List<Deadline>> deadlines;

    private LocalDate today;

    protected Repository() {
        executor = Executors.newSingleThreadExecutor();

        database = Database.getInstance();
        bookDao = database.bookDao();
        deadlineDao = database.deadlineDao();
        dailyTargetDao = database.dailyTargetDao();

        sessions = bookDao.getReadingSessions();
        deadlines = deadlineDao.getAll();

        today = LocalDate.now();
    }

    public synchronized void dayChange() {
        executor.execute(() -> {
            for (ReadingSession session : sessions.getValue()) {
                if (session.dailyTarget == null) continue;
                if (session.dailyTarget.pageReached > 0
                        && !session.dailyTarget.validFor
                        .isSameDay(today.getDayOfMonth(), today.getMonthValue(), today.getYear())) {

                    session.book.userPosition = session.dailyTarget.pageReached;
                    int highestPageToReach = calculateHighestPageToReach(session, null);
                    DailyTarget dailyTarget = createDailyTarget(session, highestPageToReach);
                    dailyTarget.id = session.dailyTarget.id;
                    updateDailyTarget(dailyTarget);
                }
            }
        });
    }

    public synchronized static Repository getInstance() {
        if (repository == null) repository = new Repository();
        return repository;
    }

    public void insertBook(@NotNull Book book) {
        executor.execute(() -> bookDao.insert(book));
    }

    public void insertDeadline(@NotNull ReadingSession session, @NotNull Deadline deadline) {
        executor.execute(() -> {
            int highestPageToReach = calculateHighestPageToReach(session, null);
            int pageToReach = calculatePageToReach(deadline, session.book.userPosition);
            if (pageToReach > highestPageToReach) highestPageToReach = pageToReach;

            if (highestPageToReach == 0) {
                if (session.dailyTarget != null) dailyTargetDao.delete(session.dailyTarget);
                deadlineDao.insert(deadline);
                return;
            }

            DailyTarget dailyTarget = createDailyTarget(session, highestPageToReach);
            if (session.dailyTarget != null) {
                dailyTarget.id = session.dailyTarget.id;
                updateDailyTarget(dailyTarget);
            }
            else {
                insertDailyTarget(dailyTarget);
            }

            deadlineDao.insert(deadline);
        });
    }
    public void updateDeadline(@NotNull ReadingSession session, @NotNull Deadline deadline) {
        executor.execute(() -> {
            int highestPageToReach = calculateHighestPageToReach(session, null);
            if (deadline.isActive) {
                int pageToReach = calculatePageToReach(deadline, session.book.userPosition);
                if (pageToReach > highestPageToReach) highestPageToReach = pageToReach;
            }

            if (highestPageToReach == 0) {
                if (session.dailyTarget != null) dailyTargetDao.delete(session.dailyTarget);
                deadlineDao.update(deadline);
                return;
            }

            DailyTarget dailyTarget = createDailyTarget(session, highestPageToReach);
            if (session.dailyTarget != null) {
                dailyTarget.id = session.dailyTarget.id;
                updateDailyTarget(dailyTarget);
            }
            else {
                insertDailyTarget(dailyTarget);
            }

            deadlineDao.update(deadline);
        });
    }
    public void deleteDeadline(@NotNull ReadingSession session, @NotNull Deadline deadline) {
        executor.execute(() -> {
            int pageToReach = calculateHighestPageToReach(session, deadline);

            if (pageToReach == 0) {
                if (session.dailyTarget != null) dailyTargetDao.delete(session.dailyTarget);
                deadlineDao.delete(deadline);
                return;
            }

            DailyTarget dailyTarget = createDailyTarget(session, pageToReach);
            if (session.dailyTarget != null) {
                dailyTarget.id = session.dailyTarget.id;
                updateDailyTarget(dailyTarget);
            }
            else {
                insertDailyTarget(dailyTarget);
            }

            deadlineDao.delete(deadline);
        });
    }

    public void insertDailyTarget(@NotNull DailyTarget dailyTarget) {
        executor.execute(() -> dailyTargetDao.insert(dailyTarget));
    }
    public void updateDailyTarget(@NotNull DailyTarget dailyTarget) {
        executor.execute(() -> dailyTargetDao.update(dailyTarget));
    }
    public void deleteDailyTarget(@NotNull DailyTarget dailyTarget) {
        executor.execute(() -> dailyTargetDao.delete(dailyTarget));
    }

    public void deleteReadingSession(@NotNull ReadingSession session) {
        executor.execute(() -> {
            bookDao.delete(session.book);
            if (session.deadlines != null && session.deadlines.size() > 0) {
                deadlineDao.deleteByBookId(session.book.id);
            }
            if (session.dailyTarget != null) {
                dailyTargetDao.delete(session.dailyTarget);
            }
        });
    }

    private ReadingSession getById(long bookId) {
        for (ReadingSession session : sessions.getValue()) {
           if (session.book.id == bookId) return session;
        }

        return null;
    }
    public synchronized LiveData<List<ReadingSession>> getSessions() {
        return sessions;
    }

    public synchronized LiveData<List<Deadline>> getDeadlines() {
        return deadlines;
    }

    private DailyTarget createDailyTarget(ReadingSession session, int pageToReach) {
        DailyTarget dailyTarget = new DailyTarget();
        dailyTarget.validFor = new Date();
        dailyTarget.validFor.year = today.getYear();
        dailyTarget.validFor.month = today.getMonthValue();
        dailyTarget.validFor.day = today.getDayOfMonth();
        dailyTarget.bookId = session.book.id;
        dailyTarget.pageToReach = pageToReach;

        return dailyTarget;
    }

    private int calculateHighestPageToReach(ReadingSession session, Deadline exclude) {
        int highestPageToReach = 0;
        for(Deadline deadline : session.deadlines) {
            if (deadline == exclude || deadline.isActive == false) continue;

            int pageToReach = calculatePageToReach(deadline, session.book.userPosition);
            if (pageToReach > highestPageToReach) highestPageToReach = pageToReach;
        }
        return highestPageToReach;
    }

    private int calculatePageToReach(@NotNull Deadline deadline, int bookUserPosition) {
        LocalDate deadlineDate = LocalDate.of(deadline.date.year,
                deadline.date.month,
                deadline.date.day);

        long days = ChronoUnit.DAYS.between(today, deadlineDate);
        int pages = deadline.pageToReach - bookUserPosition;

        double pagesPerDay = pages / days;
        return (int)pagesPerDay + bookUserPosition;
    }
}