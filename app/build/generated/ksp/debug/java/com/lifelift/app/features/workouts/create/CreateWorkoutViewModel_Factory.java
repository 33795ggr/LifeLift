package com.lifelift.app.features.workouts.create;

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
public final class CreateWorkoutViewModel_Factory implements Factory<CreateWorkoutViewModel> {
  private final Provider<WorkoutRepository> workoutRepositoryProvider;

  public CreateWorkoutViewModel_Factory(Provider<WorkoutRepository> workoutRepositoryProvider) {
    this.workoutRepositoryProvider = workoutRepositoryProvider;
  }

  @Override
  public CreateWorkoutViewModel get() {
    return newInstance(workoutRepositoryProvider.get());
  }

  public static CreateWorkoutViewModel_Factory create(
      Provider<WorkoutRepository> workoutRepositoryProvider) {
    return new CreateWorkoutViewModel_Factory(workoutRepositoryProvider);
  }

  public static CreateWorkoutViewModel newInstance(WorkoutRepository workoutRepository) {
    return new CreateWorkoutViewModel(workoutRepository);
  }
}
