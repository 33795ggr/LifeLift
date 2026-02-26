package com.lifelift.app.features.workouts.edit;

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
public final class EditWorkoutViewModel_Factory implements Factory<EditWorkoutViewModel> {
  private final Provider<WorkoutRepository> workoutRepositoryProvider;

  public EditWorkoutViewModel_Factory(Provider<WorkoutRepository> workoutRepositoryProvider) {
    this.workoutRepositoryProvider = workoutRepositoryProvider;
  }

  @Override
  public EditWorkoutViewModel get() {
    return newInstance(workoutRepositoryProvider.get());
  }

  public static EditWorkoutViewModel_Factory create(
      Provider<WorkoutRepository> workoutRepositoryProvider) {
    return new EditWorkoutViewModel_Factory(workoutRepositoryProvider);
  }

  public static EditWorkoutViewModel newInstance(WorkoutRepository workoutRepository) {
    return new EditWorkoutViewModel(workoutRepository);
  }
}
