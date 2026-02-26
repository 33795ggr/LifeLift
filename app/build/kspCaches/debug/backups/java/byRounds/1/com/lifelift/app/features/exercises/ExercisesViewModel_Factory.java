package com.lifelift.app.features.exercises;

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
public final class ExercisesViewModel_Factory implements Factory<ExercisesViewModel> {
  private final Provider<WorkoutRepository> workoutRepositoryProvider;

  public ExercisesViewModel_Factory(Provider<WorkoutRepository> workoutRepositoryProvider) {
    this.workoutRepositoryProvider = workoutRepositoryProvider;
  }

  @Override
  public ExercisesViewModel get() {
    return newInstance(workoutRepositoryProvider.get());
  }

  public static ExercisesViewModel_Factory create(
      Provider<WorkoutRepository> workoutRepositoryProvider) {
    return new ExercisesViewModel_Factory(workoutRepositoryProvider);
  }

  public static ExercisesViewModel newInstance(WorkoutRepository workoutRepository) {
    return new ExercisesViewModel(workoutRepository);
  }
}
