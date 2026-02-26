package com.lifelift.app;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.lifelift.app.core.data.local.LifeLiftDatabase;
import com.lifelift.app.core.data.local.dao.VitaminDao;
import com.lifelift.app.core.data.local.dao.WorkoutDao;
import com.lifelift.app.core.data.preferences.UserPreferencesManager;
import com.lifelift.app.core.data.repository.VitaminRepository;
import com.lifelift.app.core.data.repository.WorkoutRepository;
import com.lifelift.app.core.di.DatabaseModule_ProvideDatabaseFactory;
import com.lifelift.app.core.di.DatabaseModule_ProvideVitaminDaoFactory;
import com.lifelift.app.core.di.DatabaseModule_ProvideWorkoutDaoFactory;
import com.lifelift.app.core.notification.BootReceiver;
import com.lifelift.app.core.notification.BootReceiver_MembersInjector;
import com.lifelift.app.core.notification.VitaminNotificationReceiver;
import com.lifelift.app.core.notification.VitaminNotificationReceiver_MembersInjector;
import com.lifelift.app.core.notification.VitaminNotificationScheduler;
import com.lifelift.app.features.analytics.AnalyticsViewModel;
import com.lifelift.app.features.analytics.AnalyticsViewModel_HiltModules;
import com.lifelift.app.features.cardio.CardioViewModel;
import com.lifelift.app.features.cardio.CardioViewModel_HiltModules;
import com.lifelift.app.features.exercises.ExercisesViewModel;
import com.lifelift.app.features.exercises.ExercisesViewModel_HiltModules;
import com.lifelift.app.features.settings.SettingsViewModel;
import com.lifelift.app.features.settings.SettingsViewModel_HiltModules;
import com.lifelift.app.features.vitamins.VitaminViewModel;
import com.lifelift.app.features.vitamins.VitaminViewModel_HiltModules;
import com.lifelift.app.features.workouts.WorkoutViewModel;
import com.lifelift.app.features.workouts.WorkoutViewModel_HiltModules;
import com.lifelift.app.features.workouts.create.CreateWorkoutViewModel;
import com.lifelift.app.features.workouts.create.CreateWorkoutViewModel_HiltModules;
import com.lifelift.app.features.workouts.edit.EditWorkoutViewModel;
import com.lifelift.app.features.workouts.edit.EditWorkoutViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerLifeLiftApplication_HiltComponents_SingletonC {
  private DaggerLifeLiftApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public LifeLiftApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements LifeLiftApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public LifeLiftApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements LifeLiftApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public LifeLiftApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements LifeLiftApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public LifeLiftApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements LifeLiftApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public LifeLiftApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements LifeLiftApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public LifeLiftApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements LifeLiftApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public LifeLiftApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements LifeLiftApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public LifeLiftApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends LifeLiftApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends LifeLiftApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends LifeLiftApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends LifeLiftApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(8).put(LazyClassKeyProvider.com_lifelift_app_features_analytics_AnalyticsViewModel, AnalyticsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_lifelift_app_features_cardio_CardioViewModel, CardioViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_lifelift_app_features_workouts_create_CreateWorkoutViewModel, CreateWorkoutViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_lifelift_app_features_workouts_edit_EditWorkoutViewModel, EditWorkoutViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_lifelift_app_features_exercises_ExercisesViewModel, ExercisesViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_lifelift_app_features_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_lifelift_app_features_vitamins_VitaminViewModel, VitaminViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_lifelift_app_features_workouts_WorkoutViewModel, WorkoutViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectPreferencesManager(instance, singletonCImpl.userPreferencesManagerProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_lifelift_app_features_workouts_WorkoutViewModel = "com.lifelift.app.features.workouts.WorkoutViewModel";

      static String com_lifelift_app_features_analytics_AnalyticsViewModel = "com.lifelift.app.features.analytics.AnalyticsViewModel";

      static String com_lifelift_app_features_cardio_CardioViewModel = "com.lifelift.app.features.cardio.CardioViewModel";

      static String com_lifelift_app_features_vitamins_VitaminViewModel = "com.lifelift.app.features.vitamins.VitaminViewModel";

      static String com_lifelift_app_features_settings_SettingsViewModel = "com.lifelift.app.features.settings.SettingsViewModel";

      static String com_lifelift_app_features_exercises_ExercisesViewModel = "com.lifelift.app.features.exercises.ExercisesViewModel";

      static String com_lifelift_app_features_workouts_create_CreateWorkoutViewModel = "com.lifelift.app.features.workouts.create.CreateWorkoutViewModel";

      static String com_lifelift_app_features_workouts_edit_EditWorkoutViewModel = "com.lifelift.app.features.workouts.edit.EditWorkoutViewModel";

      @KeepFieldType
      WorkoutViewModel com_lifelift_app_features_workouts_WorkoutViewModel2;

      @KeepFieldType
      AnalyticsViewModel com_lifelift_app_features_analytics_AnalyticsViewModel2;

      @KeepFieldType
      CardioViewModel com_lifelift_app_features_cardio_CardioViewModel2;

      @KeepFieldType
      VitaminViewModel com_lifelift_app_features_vitamins_VitaminViewModel2;

      @KeepFieldType
      SettingsViewModel com_lifelift_app_features_settings_SettingsViewModel2;

      @KeepFieldType
      ExercisesViewModel com_lifelift_app_features_exercises_ExercisesViewModel2;

      @KeepFieldType
      CreateWorkoutViewModel com_lifelift_app_features_workouts_create_CreateWorkoutViewModel2;

      @KeepFieldType
      EditWorkoutViewModel com_lifelift_app_features_workouts_edit_EditWorkoutViewModel2;
    }
  }

  private static final class ViewModelCImpl extends LifeLiftApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AnalyticsViewModel> analyticsViewModelProvider;

    private Provider<CardioViewModel> cardioViewModelProvider;

    private Provider<CreateWorkoutViewModel> createWorkoutViewModelProvider;

    private Provider<EditWorkoutViewModel> editWorkoutViewModelProvider;

    private Provider<ExercisesViewModel> exercisesViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<VitaminViewModel> vitaminViewModelProvider;

    private Provider<WorkoutViewModel> workoutViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.analyticsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.cardioViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.createWorkoutViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.editWorkoutViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.exercisesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.vitaminViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.workoutViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(8).put(LazyClassKeyProvider.com_lifelift_app_features_analytics_AnalyticsViewModel, ((Provider) analyticsViewModelProvider)).put(LazyClassKeyProvider.com_lifelift_app_features_cardio_CardioViewModel, ((Provider) cardioViewModelProvider)).put(LazyClassKeyProvider.com_lifelift_app_features_workouts_create_CreateWorkoutViewModel, ((Provider) createWorkoutViewModelProvider)).put(LazyClassKeyProvider.com_lifelift_app_features_workouts_edit_EditWorkoutViewModel, ((Provider) editWorkoutViewModelProvider)).put(LazyClassKeyProvider.com_lifelift_app_features_exercises_ExercisesViewModel, ((Provider) exercisesViewModelProvider)).put(LazyClassKeyProvider.com_lifelift_app_features_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).put(LazyClassKeyProvider.com_lifelift_app_features_vitamins_VitaminViewModel, ((Provider) vitaminViewModelProvider)).put(LazyClassKeyProvider.com_lifelift_app_features_workouts_WorkoutViewModel, ((Provider) workoutViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_lifelift_app_features_settings_SettingsViewModel = "com.lifelift.app.features.settings.SettingsViewModel";

      static String com_lifelift_app_features_analytics_AnalyticsViewModel = "com.lifelift.app.features.analytics.AnalyticsViewModel";

      static String com_lifelift_app_features_workouts_create_CreateWorkoutViewModel = "com.lifelift.app.features.workouts.create.CreateWorkoutViewModel";

      static String com_lifelift_app_features_workouts_edit_EditWorkoutViewModel = "com.lifelift.app.features.workouts.edit.EditWorkoutViewModel";

      static String com_lifelift_app_features_exercises_ExercisesViewModel = "com.lifelift.app.features.exercises.ExercisesViewModel";

      static String com_lifelift_app_features_vitamins_VitaminViewModel = "com.lifelift.app.features.vitamins.VitaminViewModel";

      static String com_lifelift_app_features_workouts_WorkoutViewModel = "com.lifelift.app.features.workouts.WorkoutViewModel";

      static String com_lifelift_app_features_cardio_CardioViewModel = "com.lifelift.app.features.cardio.CardioViewModel";

      @KeepFieldType
      SettingsViewModel com_lifelift_app_features_settings_SettingsViewModel2;

      @KeepFieldType
      AnalyticsViewModel com_lifelift_app_features_analytics_AnalyticsViewModel2;

      @KeepFieldType
      CreateWorkoutViewModel com_lifelift_app_features_workouts_create_CreateWorkoutViewModel2;

      @KeepFieldType
      EditWorkoutViewModel com_lifelift_app_features_workouts_edit_EditWorkoutViewModel2;

      @KeepFieldType
      ExercisesViewModel com_lifelift_app_features_exercises_ExercisesViewModel2;

      @KeepFieldType
      VitaminViewModel com_lifelift_app_features_vitamins_VitaminViewModel2;

      @KeepFieldType
      WorkoutViewModel com_lifelift_app_features_workouts_WorkoutViewModel2;

      @KeepFieldType
      CardioViewModel com_lifelift_app_features_cardio_CardioViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.lifelift.app.features.analytics.AnalyticsViewModel 
          return (T) new AnalyticsViewModel(singletonCImpl.workoutRepositoryProvider.get(), singletonCImpl.vitaminRepositoryProvider.get());

          case 1: // com.lifelift.app.features.cardio.CardioViewModel 
          return (T) new CardioViewModel(singletonCImpl.workoutRepositoryProvider.get(), singletonCImpl.userPreferencesManagerProvider.get());

          case 2: // com.lifelift.app.features.workouts.create.CreateWorkoutViewModel 
          return (T) new CreateWorkoutViewModel(singletonCImpl.workoutRepositoryProvider.get());

          case 3: // com.lifelift.app.features.workouts.edit.EditWorkoutViewModel 
          return (T) new EditWorkoutViewModel(singletonCImpl.workoutRepositoryProvider.get());

          case 4: // com.lifelift.app.features.exercises.ExercisesViewModel 
          return (T) new ExercisesViewModel(singletonCImpl.workoutRepositoryProvider.get());

          case 5: // com.lifelift.app.features.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.userPreferencesManagerProvider.get());

          case 6: // com.lifelift.app.features.vitamins.VitaminViewModel 
          return (T) new VitaminViewModel(singletonCImpl.vitaminRepositoryProvider.get(), singletonCImpl.vitaminNotificationSchedulerProvider.get());

          case 7: // com.lifelift.app.features.workouts.WorkoutViewModel 
          return (T) new WorkoutViewModel(singletonCImpl.workoutRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends LifeLiftApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends LifeLiftApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends LifeLiftApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<VitaminNotificationScheduler> vitaminNotificationSchedulerProvider;

    private Provider<LifeLiftDatabase> provideDatabaseProvider;

    private Provider<VitaminRepository> vitaminRepositoryProvider;

    private Provider<UserPreferencesManager> userPreferencesManagerProvider;

    private Provider<WorkoutRepository> workoutRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private VitaminDao vitaminDao() {
      return DatabaseModule_ProvideVitaminDaoFactory.provideVitaminDao(provideDatabaseProvider.get());
    }

    private WorkoutDao workoutDao() {
      return DatabaseModule_ProvideWorkoutDaoFactory.provideWorkoutDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.vitaminNotificationSchedulerProvider = DoubleCheck.provider(new SwitchingProvider<VitaminNotificationScheduler>(singletonCImpl, 0));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<LifeLiftDatabase>(singletonCImpl, 2));
      this.vitaminRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<VitaminRepository>(singletonCImpl, 1));
      this.userPreferencesManagerProvider = DoubleCheck.provider(new SwitchingProvider<UserPreferencesManager>(singletonCImpl, 3));
      this.workoutRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<WorkoutRepository>(singletonCImpl, 4));
    }

    @Override
    public void injectLifeLiftApplication(LifeLiftApplication lifeLiftApplication) {
      injectLifeLiftApplication2(lifeLiftApplication);
    }

    @Override
    public void injectBootReceiver(BootReceiver bootReceiver) {
      injectBootReceiver2(bootReceiver);
    }

    @Override
    public void injectVitaminNotificationReceiver(
        VitaminNotificationReceiver vitaminNotificationReceiver) {
      injectVitaminNotificationReceiver2(vitaminNotificationReceiver);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private LifeLiftApplication injectLifeLiftApplication2(LifeLiftApplication instance) {
      LifeLiftApplication_MembersInjector.injectNotificationScheduler(instance, vitaminNotificationSchedulerProvider.get());
      return instance;
    }

    private BootReceiver injectBootReceiver2(BootReceiver instance) {
      BootReceiver_MembersInjector.injectVitaminRepository(instance, vitaminRepositoryProvider.get());
      BootReceiver_MembersInjector.injectStartNotificationScheduler(instance, vitaminNotificationSchedulerProvider.get());
      return instance;
    }

    private VitaminNotificationReceiver injectVitaminNotificationReceiver2(
        VitaminNotificationReceiver instance) {
      VitaminNotificationReceiver_MembersInjector.injectNotificationScheduler(instance, vitaminNotificationSchedulerProvider.get());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.lifelift.app.core.notification.VitaminNotificationScheduler 
          return (T) new VitaminNotificationScheduler(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // com.lifelift.app.core.data.repository.VitaminRepository 
          return (T) new VitaminRepository(singletonCImpl.vitaminDao());

          case 2: // com.lifelift.app.core.data.local.LifeLiftDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.lifelift.app.core.data.preferences.UserPreferencesManager 
          return (T) new UserPreferencesManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.lifelift.app.core.data.repository.WorkoutRepository 
          return (T) new WorkoutRepository(singletonCImpl.workoutDao());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
