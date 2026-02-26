package com.lifelift.app.features.analytics;

import com.lifelift.app.core.data.repository.VitaminRepository;
import com.lifelift.app.core.data.repository.WorkoutRepository;
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
public final class AnalyticsViewModel_Factory implements Factory<AnalyticsViewModel> {
  private final Provider<WorkoutRepository> workoutRepositoryProvider;

  private final Provider<VitaminRepository> vitaminRepositoryProvider;

  public AnalyticsViewModel_Factory(Provider<WorkoutRepository> workoutRepositoryProvider,
      Provider<VitaminRepository> vitaminRepositoryProvider) {
    this.workoutRepositoryProvider = workoutRepositoryProvider;
    this.vitaminRepositoryProvider = vitaminRepositoryProvider;
  }

  @Override
  public AnalyticsViewModel get() {
    return newInstance(workoutRepositoryProvider.get(), vitaminRepositoryProvider.get());
  }

  public static AnalyticsViewModel_Factory create(
      Provider<WorkoutRepository> workoutRepositoryProvider,
      Provider<VitaminRepository> vitaminRepositoryProvider) {
    return new AnalyticsViewModel_Factory(workoutRepositoryProvider, vitaminRepositoryProvider);
  }

  public static AnalyticsViewModel newInstance(WorkoutRepository workoutRepository,
      VitaminRepository vitaminRepository) {
    return new AnalyticsViewModel(workoutRepository, vitaminRepository);
  }
}
