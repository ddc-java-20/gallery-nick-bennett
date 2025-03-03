package edu.cnm.deepdive.nasaapod.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.ViewContainer;
import dagger.hilt.android.qualifiers.ActivityContext;
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
  private final String apodTooltipFormat;
  private final String[] mediaTypes;

  private OnDayClickListener listener;

  @Inject
  public DayBinder(@ActivityContext Context context) {
    this.apodMap = new HashMap<>();
    apodTooltipFormat = context.getString(R.string.apod_tooltip_format);
    mediaTypes = context
        .getResources()
        .getStringArray(R.array.media_types);
    listener = (apod) -> {};
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

  @ColorInt
  private int getThemeColor(Context context, int colorAttr) {
    TypedValue typedValue = new TypedValue();
    context.getTheme().resolveAttribute(colorAttr, typedValue, true);
    return typedValue.data;
  }

  private class DayHolder extends ViewContainer {

    private static final OnClickListener NO_OP_LISTENER = (v) -> {};

    private final DayCalendarBinding binding;
    private final Drawable clickableBackground;

    private Apod apod;

    public DayHolder(@NotNull View view) {
      super(view);
      binding = DayCalendarBinding.bind(view);
      clickableBackground = view.getBackground();
    }

    public void bind(CalendarDay calendarDay) {
      TextView dayText = binding.getRoot();
      dayText.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));
      Apod apod = apodMap.get(calendarDay.getDate());
      if (apod != null) {
        // TODO: 2025-03-02 Use color and/or icons to indicate image vs. video.
        this.apod = apod;
        dayText.setClickable(true);
        dayText.setFocusable(true);
        dayText.setSoundEffectsEnabled(true);
        dayText.setOnClickListener(this::translateClick);
        dayText.setBackground(clickableBackground);
        dayText.setTextAppearance((calendarDay.getPosition() == DayPosition.MonthDate)
            ? R.style.CalendarTextAppearance_AvailableDay
            : R.style.CalendarTextAppearance_AvailableDay_OutOfMonth);
        dayText.setTooltipText(String.format(apodTooltipFormat,
            mediaTypes[apod.getMediaType().ordinal()], apod.getTitle().strip()));
      } else {
        this.apod = null;
        dayText.setClickable(false);
        dayText.setFocusable(false);
        dayText.setSoundEffectsEnabled(false);
        dayText.setOnClickListener(NO_OP_LISTENER);
        dayText.setBackground(null);
        dayText.setTextAppearance(R.style.CalendarTextAppearance);
        dayText.setTooltipText(null);
      }
    }

    private void translateClick(View view) {
      listener.onDayClick(apod);
    }

  }

  public interface OnDayClickListener {

    void onDayClick(Apod apod);

  }

}
