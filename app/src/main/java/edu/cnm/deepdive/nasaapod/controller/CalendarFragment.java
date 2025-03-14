package edu.cnm.deepdive.nasaapod.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.kizitonwose.calendar.core.CalendarMonth;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.nasaapod.R;
import edu.cnm.deepdive.nasaapod.adapter.DayBinder;
import edu.cnm.deepdive.nasaapod.adapter.HeaderBinder;
import edu.cnm.deepdive.nasaapod.databinding.FragmentCalendarBinding;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import edu.cnm.deepdive.nasaapod.model.entity.Apod.MediaType;
import edu.cnm.deepdive.nasaapod.viewmodel.ApodViewModel;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import kotlin.Unit;
import org.jetbrains.annotations.NotNull;

@AndroidEntryPoint
public class CalendarFragment extends Fragment {

  private static final String TAG = CalendarFragment.class.getSimpleName();

  @Inject
  DayBinder dayBinder;
  @Inject
  HeaderBinder headerBinder;

  private FragmentCalendarBinding binding;
  private ApodViewModel viewModel;
  private YearMonth selectedMonth;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    LocalDate firstApodDate = LocalDate.parse(getString(R.string.first_apod_date));
    YearMonth firstApodMonth = YearMonth.from(firstApodDate);
    DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault())
        .getFirstDayOfWeek();
    YearMonth currentMonth = YearMonth.now();
    dayBinder.setListener(this::showApod);
    binding = FragmentCalendarBinding.inflate(inflater, container, false);
    binding.calendar.setDayBinder(dayBinder);
    binding.calendar.setMonthHeaderBinder(headerBinder);
    binding.calendar.setup(firstApodMonth, currentMonth, firstDayOfWeek);
    binding.calendar.setMonthScrollListener(this::handleMonthScroll);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity())
        .get(ApodViewModel.class);
    LifecycleOwner owner = getViewLifecycleOwner();
    viewModel
        .getApodMap()
        .observe(owner, this::handleApods);
    viewModel
        .getYearMonth()
        .observe(owner, this::handleYearMonth);
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  @NotNull
  private Unit handleMonthScroll(CalendarMonth calendarMonth) {
    viewModel.setYearMonth(calendarMonth.getYearMonth());
    return Unit.INSTANCE;
  }

  private void showApod(Apod apod) {
    if (apod.getMediaType() == MediaType.IMAGE) {
      Navigation.findNavController(binding.getRoot())
          .navigate(CalendarFragmentDirections.showImage(apod.getId()));
    } else {
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(apod.getLowDefUrl().toString()));
      startActivity(intent);
    }
  }

  private void handleApods(Map<LocalDate, Apod> apodMap) {
    Map<LocalDate, Apod> binderMap = dayBinder.getApodMap();
    binderMap.clear();
    binderMap.putAll(apodMap);
    apodMap
        .keySet()
        .stream()
        .map(YearMonth::from)
        .distinct()
        .forEach(binding.calendar::notifyMonthChanged);
  }

  private void handleYearMonth(YearMonth yearMonth) {
    if (!yearMonth.equals(selectedMonth)) {
      binding.calendar.scrollToMonth(yearMonth);
      selectedMonth = yearMonth;
    }
  }

}
