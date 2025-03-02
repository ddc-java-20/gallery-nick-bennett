package edu.cnm.deepdive.nasaapod.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.ViewContainer;
import dagger.hilt.android.scopes.FragmentScoped;
import edu.cnm.deepdive.nasaapod.R;
import edu.cnm.deepdive.nasaapod.databinding.DayCalendarBinding;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

@FragmentScoped
public class DayBinder implements MonthDayBinder<ViewContainer> {

  private static final String TAG = DayBinder.class.getSimpleName();

  private final Map<LocalDate, Apod> apodMap;

  private OnDayClickListener listener;

  @Inject
  public DayBinder() {
    this.apodMap = new HashMap<>();
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

  public Map<LocalDate, Apod> getApodMap() {
    return apodMap;
  }

  public void setListener(OnDayClickListener listener) {
    this.listener = listener;
  }

  private class DayHolder extends ViewContainer {

    private static final OnClickListener NO_OP_LISTENER = (v) -> {
    };

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
      TextView dayText = binding.getRoot();
      dayText.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));
      Apod apod = apodMap.get(calendarDay.getDate());
      boolean clickEnabled = (apod != null);
      dayText.setClickable(clickEnabled);
      dayText.setFocusable(clickEnabled);
      dayText.setSoundEffectsEnabled(clickEnabled);
      dayText.setOnClickListener(clickEnabled ? this::translateClick : NO_OP_LISTENER);
      dayText.setBackground(clickEnabled ? clickableBackground : null);
      dayText.setTextAppearance(
          !clickEnabled
              ? R.style.CalendarTextAppearance
              : calendarDay.getPosition() == DayPosition.MonthDate
                  ? R.style.CalendarTextAppearance_AvailableDay
                  : R.style.CalendarTextAppearance_AvailableDay_OutOfMonth
      );
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
