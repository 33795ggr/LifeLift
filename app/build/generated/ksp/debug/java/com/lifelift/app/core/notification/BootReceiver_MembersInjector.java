package com.lifelift.app.core.notification;

import com.lifelift.app.core.data.repository.VitaminRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class BootReceiver_MembersInjector implements MembersInjector<BootReceiver> {
  private final Provider<VitaminRepository> vitaminRepositoryProvider;

  private final Provider<VitaminNotificationScheduler> startNotificationSchedulerProvider;

  public BootReceiver_MembersInjector(Provider<VitaminRepository> vitaminRepositoryProvider,
      Provider<VitaminNotificationScheduler> startNotificationSchedulerProvider) {
    this.vitaminRepositoryProvider = vitaminRepositoryProvider;
    this.startNotificationSchedulerProvider = startNotificationSchedulerProvider;
  }

  public static MembersInjector<BootReceiver> create(
      Provider<VitaminRepository> vitaminRepositoryProvider,
      Provider<VitaminNotificationScheduler> startNotificationSchedulerProvider) {
    return new BootReceiver_MembersInjector(vitaminRepositoryProvider, startNotificationSchedulerProvider);
  }

  @Override
  public void injectMembers(BootReceiver instance) {
    injectVitaminRepository(instance, vitaminRepositoryProvider.get());
    injectStartNotificationScheduler(instance, startNotificationSchedulerProvider.get());
  }

  @InjectedFieldSignature("com.lifelift.app.core.notification.BootReceiver.vitaminRepository")
  public static void injectVitaminRepository(BootReceiver instance,
      VitaminRepository vitaminRepository) {
    instance.vitaminRepository = vitaminRepository;
  }

  @InjectedFieldSignature("com.lifelift.app.core.notification.BootReceiver.startNotificationScheduler")
  public static void injectStartNotificationScheduler(BootReceiver instance,
      VitaminNotificationScheduler startNotificationScheduler) {
    instance.startNotificationScheduler = startNotificationScheduler;
  }
}
