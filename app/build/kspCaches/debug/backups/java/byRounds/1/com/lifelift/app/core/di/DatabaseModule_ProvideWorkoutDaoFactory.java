package com.lifelift.app.core.di;

import com.lifelift.app.core.data.local.LifeLiftDatabase;
import com.lifelift.app.core.data.local.dao.WorkoutDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class DatabaseModule_ProvideWorkoutDaoFactory implements Factory<WorkoutDao> {
  private final Provider<LifeLiftDatabase> databaseProvider;

  public DatabaseModule_ProvideWorkoutDaoFactory(Provider<LifeLiftDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public WorkoutDao get() {
    return provideWorkoutDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideWorkoutDaoFactory create(
      Provider<LifeLiftDatabase> databaseProvider) {
    return new DatabaseModule_ProvideWorkoutDaoFactory(databaseProvider);
  }

  public static WorkoutDao provideWorkoutDao(LifeLiftDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideWorkoutDao(database));
  }
}
