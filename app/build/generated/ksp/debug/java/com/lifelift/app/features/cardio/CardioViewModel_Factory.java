package com.lifelift.app.features.cardio;

import com.lifelift.app.core.data.preferences.UserPreferencesManager;
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
public final class CardioViewModel_Factory implements Factory<CardioViewModel> {
  private final Provider<WorkoutRepository> workoutRepositoryProvider;

  private final Provider<UserPreferencesManager> userPreferencesManagerProvider;

  public CardioViewModel_Factory(Provider<WorkoutRepository> workoutRepositoryProvider,
      Provider<UserPreferencesManager> userPreferencesManagerProvider) {
    this.workoutRepositoryProvider = workoutRepositoryProvider;
    this.userPreferencesManagerProvider = userPreferencesManagerProvider;
  }

  @Override
  public CardioViewModel get() {
    return newInstance(workoutRepositoryProvider.get(), userPreferencesManagerProvider.get());
  }

  public static CardioViewModel_Factory create(
      Provider<WorkoutRepository> workoutRepositoryProvider,
      Provider<UserPreferencesManager> userPreferencesManagerProvider) {
    return new CardioViewModel_Factory(workoutRepositoryProvider, userPreferencesManagerProvider);
  }

  public static CardioViewModel newInstance(WorkoutRepository workoutRepository,
      UserPreferencesManager userPreferencesManager) {
    return new CardioViewModel(workoutRepository, userPreferencesManager);
  }
}
