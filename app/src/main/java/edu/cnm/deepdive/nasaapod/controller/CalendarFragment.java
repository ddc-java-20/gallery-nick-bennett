package edu.cnm.deepdive.nasaapod.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.kizitonwose.calendar.core.CalendarMonth;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.nasaapod.R;
import edu.cnm.deepdive.nasaapod.adapter.DayBinder;
import edu.cnm.deepdive.nasaapod.adapter.HeaderBinder;
import edu.cnm.deepdive.nasaapod.databinding.FragmentCalendarBinding;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import edu.cnm.deepdive.nasaapod.viewmodel.ApodViewModel;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import kotlin.Unit;

@AndroidEntryPoint
public class CalendarFragment extends Fragment {

  private static final String TAG = CalendarFragment.class.getSimpleName();

  @Inject
  DayBinder dayBinder;
  @Inject
  HeaderBinder headerBinder;

  private FragmentCalendarBinding binding;
  private ApodViewModel viewModel;
  private LocalDate firstApodDate;
  private YearMonth firstApodMonth;
  private DayOfWeek firstDayOfWeek;
  private YearMonth currentMonth;
  private boolean scrollingToData;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    firstApodDate = LocalDate.parse(getString(R.string.first_apod_date));
    firstApodMonth = YearMonth.from(firstApodDate);
    firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = FragmentCalendarBinding.inflate(inflater, container, false);
    currentMonth = YearMonth.now();
    dayBinder.setListener((day) -> Log.d(TAG, "day clicked = " + day));
    binding.calendar.setDayBinder(dayBinder);
    binding.calendar.setMonthHeaderBinder(headerBinder);
    binding.calendar.setup(firstApodMonth, currentMonth, firstDayOfWeek);
    binding.calendar.scrollToMonth(currentMonth);
    binding.calendar.setMonthScrollListener(this::handleScroll);
    // TODO: 2025-02-28 Initialize UI.
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity())
        .get(ApodViewModel.class);
    viewModel
        .getApodMap()
        .observe(getViewLifecycleOwner(), this::handleApods);
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  @NonNull
  private Unit handleScroll(CalendarMonth calendarMonth) {
    YearMonth yearMonth = calendarMonth.getYearMonth();
    LocalDate rangeStart = yearMonth.minusMonths(1).atDay(1);
    if (rangeStart.isBefore(firstApodDate)) {
      rangeStart = firstApodDate;
    }
    LocalDate rangeEnd = yearMonth.plusMonths(2).atDay(1);
    if (!rangeEnd.isBefore(LocalDate.now())) {
      viewModel.setRange(rangeStart);
    } else {
      viewModel.setRange(rangeStart, rangeEnd);
    }
    return Unit.INSTANCE;
  }

  private void handleApods(Map<LocalDate, Apod> apodMap) {
    dayBinder.getApodMap().clear();
    dayBinder.getApodMap().putAll(apodMap);
    apodMap
        .keySet()
        .stream()
        .map(YearMonth::from)
        .distinct()
        .forEach(binding.calendar::notifyMonthChanged);
  }

}
