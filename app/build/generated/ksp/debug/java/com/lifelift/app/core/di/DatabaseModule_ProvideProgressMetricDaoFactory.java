package com.lifelift.app.core.di;

import com.lifelift.app.core.data.local.LifeLiftDatabase;
import com.lifelift.app.core.data.local.dao.ProgressMetricDao;
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
public final class DatabaseModule_ProvideProgressMetricDaoFactory implements Factory<ProgressMetricDao> {
  private final Provider<LifeLiftDatabase> databaseProvider;

  public DatabaseModule_ProvideProgressMetricDaoFactory(
      Provider<LifeLiftDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ProgressMetricDao get() {
    return provideProgressMetricDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideProgressMetricDaoFactory create(
      Provider<LifeLiftDatabase> databaseProvider) {
    return new DatabaseModule_ProvideProgressMetricDaoFactory(databaseProvider);
  }

  public static ProgressMetricDao provideProgressMetricDao(LifeLiftDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideProgressMetricDao(database));
  }
}
