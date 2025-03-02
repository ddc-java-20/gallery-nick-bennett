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
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.nasaapod.R;
import edu.cnm.deepdive.nasaapod.adapter.DayBinder;
import edu.cnm.deepdive.nasaapod.databinding.FragmentCalendarBinding;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import edu.cnm.deepdive.nasaapod.viewmodel.ApodViewModel;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Map;

@AndroidEntryPoint
public class CalendarFragment extends Fragment {

  private static final String TAG = CalendarFragment.class.getSimpleName();

  private FragmentCalendarBinding binding;
  private ApodViewModel viewModel;
  private LocalDate firstApodDate;
  private YearMonth firstApodMonth;
  private DayOfWeek firstDayOfWeek;

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
    // TODO: 2025-02-28 Initialize UI.
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity())
        .get(ApodViewModel.class);
    LocalDate today = LocalDate.now();
    YearMonth currentMonth = YearMonth.from(today);
    viewModel
        .getApodMap()
        .observe(getViewLifecycleOwner(), (apodMap) -> {
          binding.calendar.setDayBinder(new DayBinder(apodMap, (day) -> {
            Log.d(TAG, "day clicked = " + day);
          }));
          binding.calendar.setup(firstApodMonth, currentMonth, firstDayOfWeek);
          binding.calendar.scrollToMonth(currentMonth);
        });
    viewModel.setRange(currentMonth.minusMonths(1).atDay(1));
    // TODO: 2025-02-28 Observe livedata and start asynchronous processes, as necessary.
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

}
