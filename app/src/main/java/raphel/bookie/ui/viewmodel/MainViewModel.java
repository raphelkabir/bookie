package raphel.bookie.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import raphel.bookie.data.Repository;
import raphel.bookie.data.room.Book;
import raphel.bookie.data.room.DailyTarget;
import raphel.bookie.data.room.Date;
import raphel.bookie.data.room.Deadline;
import raphel.bookie.data.room.DeadlineDao;
import raphel.bookie.data.room.ReadingSession;

public class MainViewModel extends ViewModel {

    private LocalDate today;
    private Repository repository;

    private MutableLiveData<List<ReadingSession>> sessions;

    private MutableLiveData<ReadingSession> selected;

    public MainViewModel() {
        today = LocalDate.now();
        repository = Repository.getInstance();

        sessions = new MutableLiveData<>(repository.getSessions());

        new Thread(() -> {
            synchronized (this) {
                for (ReadingSession session : sessions.getValue()) {
                    if (session.dailyTarget == null) continue;
                    if (session.dailyTarget.validFor.year == today.getYear()
                            && session.dailyTarget.validFor.month == today.getMonthValue()
                            && session.dailyTarget.validFor.day == today.getDayOfMonth()) continue;

                    session.book.userPosition = session.dailyTarget.pageReached;
                    refreshDailyTarget(session);
                    repository.updateDailyTarget(session);
                }
            }
        }).start();

        selected = new MutableLiveData<>();
    }

    public synchronized void addBook(@NotNull Book book) {
        repository.insertBook(book);
        notifyObservers();
    }
    public synchronized void updateBook(@NotNull Book book) {
        repository.updateBook(book);
        notifyObservers();
    }

    public synchronized void addDeadline(@NotNull ReadingSession session, @NotNull Deadline deadline) {
        repository.insertDeadline(session, deadline);
        refreshDailyTarget(session);
        notifyObservers();
    }
    public synchronized void updateDeadline(@NotNull ReadingSession session, @NotNull Deadline deadline) {
        repository.updateDeadline(session, deadline);
        refreshDailyTarget(session);
        notifyObservers();
    }
    public synchronized void deleteDeadline(@NotNull ReadingSession session, @NotNull Deadline deadline) {
        repository.deleteDeadline(session, deadline);
        refreshDailyTarget(session);
        notifyObservers();
    }

    public synchronized void updateDailyTarget(@NotNull ReadingSession session) {
        repository.updateDailyTarget(session);
    }

    private void refreshDailyTarget(@NotNull ReadingSession session) {
        DailyTarget target = new DailyTarget();
        target.validFor = new Date();
        target.validFor.day = today.getDayOfMonth();
        target.validFor.month = today.getMonthValue();
        target.validFor.year = today.getYear();
        target.bookId = session.book.id;

        double highestPagesPerDay = 0.0;
        for(Deadline deadline : session.deadlines) {
            LocalDate deadlineDate = LocalDate.of(deadline.date.year,
                    deadline.date.month,
                    deadline.date.day);

            long days = ChronoUnit.DAYS.between(today, deadlineDate);
            int pages = deadline.pageToReach - session.book.userPosition;

            double pagesPerDay = pages / days;
            if (pagesPerDay > highestPagesPerDay) highestPagesPerDay = pagesPerDay;
        }

        if (highestPagesPerDay > 0.0
                && session.book.userPosition + highestPagesPerDay + 1 < session.book.length) highestPagesPerDay += 1.0;
        target.pageToReach = (int)(session.book.userPosition + highestPagesPerDay);

        if (session.dailyTarget != null) {
            target.id = session.dailyTarget.id;
            session.dailyTarget = target;
            repository.updateDailyTarget(session);
        }
        else {
            session.dailyTarget = target;
            repository.insertDailyTarget(session);
        }
    }

    public synchronized void deleteReadingSession(ReadingSession session) {
        repository.deleteReadingSession(session);
        notifyObservers();
    }

    private synchronized void notifyObservers() {
        sessions.setValue(repository.getSessions());
    }

    public synchronized MutableLiveData<ReadingSession> getSelectedSession() {
        return selected;
    }
    public synchronized MutableLiveData<List<ReadingSession>> getSessions() {
        return sessions;
    }
}