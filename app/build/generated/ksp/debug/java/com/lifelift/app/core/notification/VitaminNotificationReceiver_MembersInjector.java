package com.lifelift.app.core.notification;

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
public final class VitaminNotificationReceiver_MembersInjector implements MembersInjector<VitaminNotificationReceiver> {
  private final Provider<VitaminNotificationScheduler> notificationSchedulerProvider;

  public VitaminNotificationReceiver_MembersInjector(
      Provider<VitaminNotificationScheduler> notificationSchedulerProvider) {
    this.notificationSchedulerProvider = notificationSchedulerProvider;
  }

  public static MembersInjector<VitaminNotificationReceiver> create(
      Provider<VitaminNotificationScheduler> notificationSchedulerProvider) {
    return new VitaminNotificationReceiver_MembersInjector(notificationSchedulerProvider);
  }

  @Override
  public void injectMembers(VitaminNotificationReceiver instance) {
    injectNotificationScheduler(instance, notificationSchedulerProvider.get());
  }

  @InjectedFieldSignature("com.lifelift.app.core.notification.VitaminNotificationReceiver.notificationScheduler")
  public static void injectNotificationScheduler(VitaminNotificationReceiver instance,
      VitaminNotificationScheduler notificationScheduler) {
    instance.notificationScheduler = notificationScheduler;
  }
}
