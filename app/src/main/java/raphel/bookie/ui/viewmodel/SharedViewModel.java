package raphel.bookie.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import raphel.bookie.data.Repository;
import raphel.bookie.data.room.Book;
import raphel.bookie.data.room.DailyTarget;
import raphel.bookie.data.room.Date;
import raphel.bookie.data.room.Deadline;
import raphel.bookie.data.room.ReadingSession;

public class SharedViewModel extends ViewModel {

    private LocalDate today;
    private Repository repository;

    private LiveData<List<ReadingSession>> sessions;

    private MutableLiveData<Integer> selectedIndex;

    public SharedViewModel() {
        today = LocalDate.now();
        repository = Repository.getInstance();
        sessions = repository.getSessions();

        selectedIndex = new MutableLiveData<>(0);
    }

    public synchronized void addBook(@NotNull Book book) {
        repository.insertBook(book);
    }

    public synchronized void addDeadline(@NotNull ReadingSession session, @NotNull Deadline deadline) {
        repository.insertDeadline(session, deadline);
    }
    public synchronized void updateDeadline(@NotNull ReadingSession session, @NotNull Deadline deadline) {
        repository.updateDeadline(session, deadline);
    }
    public synchronized void deleteDeadline(@NotNull ReadingSession session, @NotNull Deadline deadline) {
        repository.deleteDeadline(session, deadline);
    }

    public synchronized void updateDailyTarget(@NotNull DailyTarget dailyTarget) {
        repository.updateDailyTarget(dailyTarget);
    }

    public synchronized void deleteReadingSession(ReadingSession session) {
        repository.deleteReadingSession(session);
    }

    public synchronized MutableLiveData<Integer> getSelectedIndex() {
        return selectedIndex;
    }
    public synchronized ReadingSession getSelected() {
        if (sessions.getValue().size() == 0
                || selectedIndex.getValue() < 0
                || selectedIndex.getValue() >= sessions.getValue().size()) return null;
        return getSessions().getValue().get(getSelectedIndex().getValue());
    }
    public synchronized LiveData<List<ReadingSession>> getSessions() {
        return sessions;
    }
}