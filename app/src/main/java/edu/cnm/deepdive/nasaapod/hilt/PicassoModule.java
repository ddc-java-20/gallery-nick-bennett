package edu.cnm.deepdive.nasaapod.hilt;

import android.app.Application;
import android.content.Context;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityScoped;
import javax.inject.Singleton;

@Module
@InstallIn(ActivityComponent.class)
public class PicassoModule {

  @Provides
  @ActivityScoped
  Picasso providePicasso(@ApplicationContext Context context) {
    return new Picasso.Builder(context)
        .build();
  }

}
