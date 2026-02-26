package com.lifelift.app.features.vitamins;

import com.lifelift.app.core.data.repository.VitaminRepository;
import com.lifelift.app.core.notification.VitaminNotificationScheduler;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class VitaminViewModel_Factory implements Factory<VitaminViewModel> {
  private final Provider<VitaminRepository> vitaminRepositoryProvider;

  private final Provider<VitaminNotificationScheduler> notificationSchedulerProvider;

  public VitaminViewModel_Factory(Provider<VitaminRepository> vitaminRepositoryProvider,
      Provider<VitaminNotificationScheduler> notificationSchedulerProvider) {
    this.vitaminRepositoryProvider = vitaminRepositoryProvider;
    this.notificationSchedulerProvider = notificationSchedulerProvider;
  }

  @Override
  public VitaminViewModel get() {
    return newInstance(vitaminRepositoryProvider.get(), notificationSchedulerProvider.get());
  }

  public static VitaminViewModel_Factory create(
      Provider<VitaminRepository> vitaminRepositoryProvider,
      Provider<VitaminNotificationScheduler> notificationSchedulerProvider) {
    return new VitaminViewModel_Factory(vitaminRepositoryProvider, notificationSchedulerProvider);
  }

  public static VitaminViewModel newInstance(VitaminRepository vitaminRepository,
      VitaminNotificationScheduler notificationScheduler) {
    return new VitaminViewModel(vitaminRepository, notificationScheduler);
  }
}
