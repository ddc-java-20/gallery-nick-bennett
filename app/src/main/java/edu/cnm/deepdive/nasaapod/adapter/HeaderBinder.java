package edu.cnm.deepdive.nasaapod.adapter;

import android.view.View;
import androidx.annotation.NonNull;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;
import com.kizitonwose.calendar.view.ViewContainer;

public class HeaderBinder implements MonthHeaderFooterBinder<ViewContainer> {


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

    public HeaderHolder(@NonNull View view) {
      super(view);
    }

    public void bind(CalendarMonth calendarMonth) {

    }

  }

}
