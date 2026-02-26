package com.lifelift.app.core.data.repository;

import com.lifelift.app.core.data.local.dao.VitaminDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class VitaminRepository_Factory implements Factory<VitaminRepository> {
  private final Provider<VitaminDao> vitaminDaoProvider;

  public VitaminRepository_Factory(Provider<VitaminDao> vitaminDaoProvider) {
    this.vitaminDaoProvider = vitaminDaoProvider;
  }

  @Override
  public VitaminRepository get() {
    return newInstance(vitaminDaoProvider.get());
  }

  public static VitaminRepository_Factory create(Provider<VitaminDao> vitaminDaoProvider) {
    return new VitaminRepository_Factory(vitaminDaoProvider);
  }

  public static VitaminRepository newInstance(VitaminDao vitaminDao) {
    return new VitaminRepository(vitaminDao);
  }
}
