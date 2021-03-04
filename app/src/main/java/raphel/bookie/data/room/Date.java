package raphel.bookie.data.room;

import androidx.annotation.Nullable;

public class Date {

    public int day, month, year;

    @Override
    public boolean equals(@Nullable Object obj) {
        Date date = (Date) obj;
        if (date == null) return false;

        if (this.year == date.year
                && this.month == date.month
                && this.day == date.day) return true;

        return false;
    }

    public boolean isSameDay(int day, int month, int year) {

        if (this.year == year
                && this.month == month
                && this.day == day) return true;

        return false;
    }
}
