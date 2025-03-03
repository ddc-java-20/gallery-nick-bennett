package edu.cnm.deepdive.nasaapod.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import edu.cnm.deepdive.nasaapod.service.ApodRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;

@HiltViewModel
public class ApodViewModel extends ViewModel {

  private static final String TAG = ApodViewModel.class.getSimpleName();

  private final ApodRepository repository;
  private final MutableLiveData<YearMonth> yearMonth;
  private final LiveData<List<Apod>> apods;
  private final MutableLiveData<Long> apodId;
  private final MutableLiveData<Throwable> throwable;

  @Inject
  ApodViewModel(ApodRepository repository) {
    this.repository = repository;
    yearMonth = new MutableLiveData<>(YearMonth.now());
    apodId = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    apods = Transformations.switchMap(yearMonth, this::transformYearMonthToQuery);
  }

  public LiveData<List<Apod>> getApods() {
    return apods;
  }

  public void setApodId(long apodId) {
    this.apodId.setValue(apodId);
  }

  public LiveData<Apod> getApod() {
    return Transformations.switchMap(Transformations.distinctUntilChanged(apodId), repository::get);
  }

  public LiveData<Map<LocalDate, Apod>> getApodMap() {
    return Transformations.map(apods, (apodList) -> apodList
        .stream()
        .collect(Collectors.toMap(Apod::getDate, Function.identity())));
  }
  
  public void setYearMonth(YearMonth yearMonth) {
    this.yearMonth.setValue(yearMonth);
  }

  public LiveData<YearMonth> getYearMonth() {
    return yearMonth;
  }

  private LiveData<List<Apod>> transformYearMonthToQuery(YearMonth month) {
    LocalDate startDate = month.minusMonths(1).atDay(1);
    LocalDate endDate = month.plusMonths(2).atDay(1);
    fetch(startDate, endDate);
    return repository.get(startDate, endDate);
  }

  /** @noinspection ResultOfMethodCallIgnored*/
  @SuppressLint("CheckResult")
  private void fetch(LocalDate startDate, LocalDate endDate) {
    throwable.setValue(null);
    repository
        .fetch(startDate, endDate)
        .subscribe(
            () -> {},
            this::postThrowable
        );
  }

  private void postThrowable(Throwable throwable) {
    Log.e(TAG, throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

}
