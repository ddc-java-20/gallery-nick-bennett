package edu.cnm.deepdive.nasaapod.adapter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.ViewContainer;
import edu.cnm.deepdive.nasaapod.databinding.DayCalendarBinding;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import java.time.LocalDate;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class DayBinder implements MonthDayBinder<ViewContainer> {

  private static final String TAG = DayBinder.class.getSimpleName();

  private final Map<LocalDate, Apod> apodMap;
  private final OnDayClickListener listener;

  public DayBinder(Map<LocalDate, Apod> apodMap, OnDayClickListener listener) {
    this.apodMap = apodMap; // TODO: 2025-02-28 Consider creating a copy of map. 
    this.listener = listener;
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

  private class DayHolder extends ViewContainer {

    private static final OnClickListener NO_OP_LISTENER = (v) -> {};

    private final DayCalendarBinding binding;
    private final Drawable clickableBackground;

    private CalendarDay calendarDay;

    public DayHolder(@NotNull View view) {
      super(view);
      binding = DayCalendarBinding.bind(view);
      clickableBackground = view.getBackground();
    }

    public void bind(CalendarDay calendarDay) {
      this.calendarDay = calendarDay;
      TextView root = binding.getRoot();
      root.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));
      Apod apod = apodMap.get(calendarDay.getDate());
      if (apod != null) {
        root.setClickable(true);
        root.setSoundEffectsEnabled(true);
        root.setOnClickListener(this::translateClick);
        root.setBackground(clickableBackground);
      } else {
        root.setClickable(false);
        root.setSoundEffectsEnabled(false);
        root.setOnClickListener(NO_OP_LISTENER);
        root.setBackground(null);
      }
      // TODO: 2025-02-28 Use information from apodMap to modify style/content of widgets.
    }

    private void translateClick(View view) {
      listener.onDayClick(calendarDay);
    }

  }

  public interface OnDayClickListener {

    void onDayClick(CalendarDay day);

  }

}
