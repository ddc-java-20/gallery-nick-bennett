package edu.cnm.deepdive.nasaapod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;
import com.kizitonwose.calendar.view.ViewContainer;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.FragmentScoped;
import edu.cnm.deepdive.nasaapod.R;
import edu.cnm.deepdive.nasaapod.databinding.HeaderCalendarBinding;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.stream.IntStream;
import javax.inject.Inject;

@FragmentScoped
public class HeaderBinder implements MonthHeaderFooterBinder<ViewContainer> {

  private final LayoutInflater inflater;
  private final DateTimeFormatter formatter;

  @Inject
  public HeaderBinder(@NonNull @ActivityContext Context context) {
    inflater = LayoutInflater.from(context);
    formatter = DateTimeFormatter.ofPattern(context.getString(R.string.year_month_format));
  }

  @NonNull
  @Override
  public ViewContainer create(@NonNull View view) {
    return new HeaderHolder(view);
  }

  @Override
  public void bind(@NonNull ViewContainer container, CalendarMonth calendarMonth) {
    ((HeaderHolder) container).bind(calendarMonth);
  }

  private class HeaderHolder extends ViewContainer {

    private final HeaderCalendarBinding binding;

    private boolean bound;

    public HeaderHolder(@NonNull View view) {
      super(view);
      binding = HeaderCalendarBinding.bind(view);
    }

    public void bind(CalendarMonth calendarMonth) {
      binding.monthName.setText(calendarMonth.getYearMonth().format(formatter));
      if (!bound) {
        bound = true;
        ViewGroup headerRoot = binding.dayNames;
        headerRoot.removeAllViews();
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        IntStream.range(0, 7)
            .mapToObj(firstDayOfWeek::plus)
            .forEach((dayOfWeek) -> {
              TextView dayHeader = (TextView) inflater.inflate(R.layout.day_header, headerRoot, false);
              dayHeader.setText(dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault()));
              headerRoot.addView(dayHeader);
            });
      }
    }

  }

}
