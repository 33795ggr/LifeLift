package com.lifelift.app.core.notification;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class VitaminNotificationScheduler_Factory implements Factory<VitaminNotificationScheduler> {
  private final Provider<Context> contextProvider;

  public VitaminNotificationScheduler_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public VitaminNotificationScheduler get() {
    return newInstance(contextProvider.get());
  }

  public static VitaminNotificationScheduler_Factory create(Provider<Context> contextProvider) {
    return new VitaminNotificationScheduler_Factory(contextProvider);
  }

  public static VitaminNotificationScheduler newInstance(Context context) {
    return new VitaminNotificationScheduler(context);
  }
}
