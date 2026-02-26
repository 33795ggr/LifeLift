package com.lifelift.app.core.di;

import com.lifelift.app.core.data.local.LifeLiftDatabase;
import com.lifelift.app.core.data.local.dao.VitaminDao;
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
public final class DatabaseModule_ProvideVitaminDaoFactory implements Factory<VitaminDao> {
  private final Provider<LifeLiftDatabase> databaseProvider;

  public DatabaseModule_ProvideVitaminDaoFactory(Provider<LifeLiftDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public VitaminDao get() {
    return provideVitaminDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideVitaminDaoFactory create(
      Provider<LifeLiftDatabase> databaseProvider) {
    return new DatabaseModule_ProvideVitaminDaoFactory(databaseProvider);
  }

  public static VitaminDao provideVitaminDao(LifeLiftDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideVitaminDao(database));
  }
}
