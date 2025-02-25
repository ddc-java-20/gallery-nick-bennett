package edu.cnm.deepdive.nasaapod.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import io.reactivex.rxjava3.core.Single;
import java.util.Iterator;
import java.util.List;

@Dao
public interface ApodDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<List<Long>> insert(List<Apod> apods);

  default Single<List<Apod>> insertAndUpdate(List<Apod> apods) {
    return insert(apods)
        .map((ids) -> {
          Iterator<Apod> apodIter = apods.iterator();
          Iterator<Long> idIter = ids.iterator();
          while (apodIter.hasNext() && idIter.hasNext()) {
            apodIter
                .next()
                .setId(idIter.next());
          }
          apods.removeIf((apod) -> apod.getId() < 0);
          return apods;
        });
  }
  
}
