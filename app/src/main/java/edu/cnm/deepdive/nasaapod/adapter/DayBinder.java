package edu.cnm.deepdive.nasaapod.adapter;

import android.view.View;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.ViewContainer;
import edu.cnm.deepdive.nasaapod.databinding.DayCalendarBinding;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import java.time.LocalDate;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class DayBinder implements MonthDayBinder<ViewContainer> {

  private final Map<LocalDate, Apod> apodMap;

  public DayBinder(Map<LocalDate, Apod> apodMap) {
    this.apodMap = apodMap; // TODO: 2025-02-28 Consider creating a copy of map. 
  }

  @NotNull 
  @Override
  public ViewContainer create(@NotNull View view) {
    return new DayHolder(view);
  }

  @Override
  public void bind(@NotNull ViewContainer holder, CalendarDay calendarDay) {
    ((DayHolder) holder).bind(calendarDay);
  }

  public class DayHolder extends ViewContainer {

    private final DayCalendarBinding binding;

    public DayHolder(@NotNull View view) {
      super(view);
      binding = DayCalendarBinding.bind(view);
    }

    public void bind(CalendarDay calendarDay) {
      binding.getRoot().setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));
      // TODO: 2025-02-28 Use information from apodMap to modify style/content of widgets. 
    }

  }

}
