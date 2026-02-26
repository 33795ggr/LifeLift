package com.lifelift.app.core.data.repository;

import com.lifelift.app.core.data.local.dao.WorkoutDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class WorkoutRepository_Factory implements Factory<WorkoutRepository> {
  private final Provider<WorkoutDao> workoutDaoProvider;

  public WorkoutRepository_Factory(Provider<WorkoutDao> workoutDaoProvider) {
    this.workoutDaoProvider = workoutDaoProvider;
  }

  @Override
  public WorkoutRepository get() {
    return newInstance(workoutDaoProvider.get());
  }

  public static WorkoutRepository_Factory create(Provider<WorkoutDao> workoutDaoProvider) {
    return new WorkoutRepository_Factory(workoutDaoProvider);
  }

  public static WorkoutRepository newInstance(WorkoutDao workoutDao) {
    return new WorkoutRepository(workoutDao);
  }
}
