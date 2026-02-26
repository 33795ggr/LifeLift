package com.lifelift.app;

import com.lifelift.app.core.notification.VitaminNotificationScheduler;
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
public final class LifeLiftApplication_MembersInjector implements MembersInjector<LifeLiftApplication> {
  private final Provider<VitaminNotificationScheduler> notificationSchedulerProvider;

  public LifeLiftApplication_MembersInjector(
      Provider<VitaminNotificationScheduler> notificationSchedulerProvider) {
    this.notificationSchedulerProvider = notificationSchedulerProvider;
  }

  public static MembersInjector<LifeLiftApplication> create(
      Provider<VitaminNotificationScheduler> notificationSchedulerProvider) {
    return new LifeLiftApplication_MembersInjector(notificationSchedulerProvider);
  }

  @Override
  public void injectMembers(LifeLiftApplication instance) {
    injectNotificationScheduler(instance, notificationSchedulerProvider.get());
  }

  @InjectedFieldSignature("com.lifelift.app.LifeLiftApplication.notificationScheduler")
  public static void injectNotificationScheduler(LifeLiftApplication instance,
      VitaminNotificationScheduler notificationScheduler) {
    instance.notificationScheduler = notificationScheduler;
  }
}
