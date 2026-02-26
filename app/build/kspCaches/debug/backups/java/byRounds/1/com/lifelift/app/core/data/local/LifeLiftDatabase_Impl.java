package com.lifelift.app.core.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.lifelift.app.core.data.local.dao.ProgramDao;
import com.lifelift.app.core.data.local.dao.ProgramDao_Impl;
import com.lifelift.app.core.data.local.dao.ProgressMetricDao;
import com.lifelift.app.core.data.local.dao.ProgressMetricDao_Impl;
import com.lifelift.app.core.data.local.dao.VitaminDao;
import com.lifelift.app.core.data.local.dao.VitaminDao_Impl;
import com.lifelift.app.core.data.local.dao.WorkoutDao;
import com.lifelift.app.core.data.local.dao.WorkoutDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class LifeLiftDatabase_Impl extends LifeLiftDatabase {
  private volatile WorkoutDao _workoutDao;

  private volatile VitaminDao _vitaminDao;

  private volatile ProgressMetricDao _progressMetricDao;

  private volatile ProgramDao _programDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `workouts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `date` TEXT NOT NULL, `durationMinutes` INTEGER NOT NULL, `notes` TEXT NOT NULL, `type` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exercises` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `workoutId` INTEGER NOT NULL, `name` TEXT NOT NULL, `orderIndex` INTEGER NOT NULL, FOREIGN KEY(`workoutId`) REFERENCES `workouts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exercises_workoutId` ON `exercises` (`workoutId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `exerciseId` INTEGER NOT NULL, `reps` INTEGER NOT NULL, `weight` REAL NOT NULL, `isWarmup` INTEGER NOT NULL, FOREIGN KEY(`exerciseId`) REFERENCES `exercises`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_sets_exerciseId` ON `sets` (`exerciseId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exercise_refs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `category` TEXT NOT NULL, `defaultRestSeconds` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `programs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `program_exercises` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `programId` INTEGER NOT NULL, `name` TEXT NOT NULL, `orderIndex` INTEGER NOT NULL, `targetSets` INTEGER NOT NULL, `targetReps` INTEGER NOT NULL, FOREIGN KEY(`programId`) REFERENCES `programs`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_program_exercises_programId` ON `program_exercises` (`programId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `vitamins` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `dosage` TEXT NOT NULL, `timeOfDay` TEXT NOT NULL, `frequency` TEXT NOT NULL, `reminderEnabled` INTEGER NOT NULL, `color` TEXT NOT NULL, `notes` TEXT NOT NULL, `createdAt` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `vitamin_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `vitaminId` INTEGER NOT NULL, `date` TEXT NOT NULL, `taken` INTEGER NOT NULL, `takenAt` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `progress_metrics` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` TEXT NOT NULL, `metricType` TEXT NOT NULL, `value` REAL NOT NULL, `unit` TEXT NOT NULL, `notes` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3949ddbd3cae71c0f992b900d8492623')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `workouts`");
        db.execSQL("DROP TABLE IF EXISTS `exercises`");
        db.execSQL("DROP TABLE IF EXISTS `sets`");
        db.execSQL("DROP TABLE IF EXISTS `exercise_refs`");
        db.execSQL("DROP TABLE IF EXISTS `programs`");
        db.execSQL("DROP TABLE IF EXISTS `program_exercises`");
        db.execSQL("DROP TABLE IF EXISTS `vitamins`");
        db.execSQL("DROP TABLE IF EXISTS `vitamin_logs`");
        db.execSQL("DROP TABLE IF EXISTS `progress_metrics`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsWorkouts = new HashMap<String, TableInfo.Column>(6);
        _columnsWorkouts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("durationMinutes", new TableInfo.Column("durationMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWorkouts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWorkouts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWorkouts = new TableInfo("workouts", _columnsWorkouts, _foreignKeysWorkouts, _indicesWorkouts);
        final TableInfo _existingWorkouts = TableInfo.read(db, "workouts");
        if (!_infoWorkouts.equals(_existingWorkouts)) {
          return new RoomOpenHelper.ValidationResult(false, "workouts(com.lifelift.app.core.data.local.entity.WorkoutEntity).\n"
                  + " Expected:\n" + _infoWorkouts + "\n"
                  + " Found:\n" + _existingWorkouts);
        }
        final HashMap<String, TableInfo.Column> _columnsExercises = new HashMap<String, TableInfo.Column>(4);
        _columnsExercises.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("workoutId", new TableInfo.Column("workoutId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("orderIndex", new TableInfo.Column("orderIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExercises = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysExercises.add(new TableInfo.ForeignKey("workouts", "CASCADE", "NO ACTION", Arrays.asList("workoutId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesExercises = new HashSet<TableInfo.Index>(1);
        _indicesExercises.add(new TableInfo.Index("index_exercises_workoutId", false, Arrays.asList("workoutId"), Arrays.asList("ASC")));
        final TableInfo _infoExercises = new TableInfo("exercises", _columnsExercises, _foreignKeysExercises, _indicesExercises);
        final TableInfo _existingExercises = TableInfo.read(db, "exercises");
        if (!_infoExercises.equals(_existingExercises)) {
          return new RoomOpenHelper.ValidationResult(false, "exercises(com.lifelift.app.core.data.local.entity.ExerciseEntity).\n"
                  + " Expected:\n" + _infoExercises + "\n"
                  + " Found:\n" + _existingExercises);
        }
        final HashMap<String, TableInfo.Column> _columnsSets = new HashMap<String, TableInfo.Column>(5);
        _columnsSets.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSets.put("exerciseId", new TableInfo.Column("exerciseId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSets.put("reps", new TableInfo.Column("reps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSets.put("weight", new TableInfo.Column("weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSets.put("isWarmup", new TableInfo.Column("isWarmup", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSets = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSets.add(new TableInfo.ForeignKey("exercises", "CASCADE", "NO ACTION", Arrays.asList("exerciseId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSets = new HashSet<TableInfo.Index>(1);
        _indicesSets.add(new TableInfo.Index("index_sets_exerciseId", false, Arrays.asList("exerciseId"), Arrays.asList("ASC")));
        final TableInfo _infoSets = new TableInfo("sets", _columnsSets, _foreignKeysSets, _indicesSets);
        final TableInfo _existingSets = TableInfo.read(db, "sets");
        if (!_infoSets.equals(_existingSets)) {
          return new RoomOpenHelper.ValidationResult(false, "sets(com.lifelift.app.core.data.local.entity.SetEntity).\n"
                  + " Expected:\n" + _infoSets + "\n"
                  + " Found:\n" + _existingSets);
        }
        final HashMap<String, TableInfo.Column> _columnsExerciseRefs = new HashMap<String, TableInfo.Column>(4);
        _columnsExerciseRefs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseRefs.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseRefs.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseRefs.put("defaultRestSeconds", new TableInfo.Column("defaultRestSeconds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExerciseRefs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExerciseRefs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExerciseRefs = new TableInfo("exercise_refs", _columnsExerciseRefs, _foreignKeysExerciseRefs, _indicesExerciseRefs);
        final TableInfo _existingExerciseRefs = TableInfo.read(db, "exercise_refs");
        if (!_infoExerciseRefs.equals(_existingExerciseRefs)) {
          return new RoomOpenHelper.ValidationResult(false, "exercise_refs(com.lifelift.app.core.data.local.entity.ExerciseRefEntity).\n"
                  + " Expected:\n" + _infoExerciseRefs + "\n"
                  + " Found:\n" + _existingExerciseRefs);
        }
        final HashMap<String, TableInfo.Column> _columnsPrograms = new HashMap<String, TableInfo.Column>(3);
        _columnsPrograms.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPrograms = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPrograms = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPrograms = new TableInfo("programs", _columnsPrograms, _foreignKeysPrograms, _indicesPrograms);
        final TableInfo _existingPrograms = TableInfo.read(db, "programs");
        if (!_infoPrograms.equals(_existingPrograms)) {
          return new RoomOpenHelper.ValidationResult(false, "programs(com.lifelift.app.core.data.local.entity.ProgramEntity).\n"
                  + " Expected:\n" + _infoPrograms + "\n"
                  + " Found:\n" + _existingPrograms);
        }
        final HashMap<String, TableInfo.Column> _columnsProgramExercises = new HashMap<String, TableInfo.Column>(6);
        _columnsProgramExercises.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("programId", new TableInfo.Column("programId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("orderIndex", new TableInfo.Column("orderIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("targetSets", new TableInfo.Column("targetSets", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("targetReps", new TableInfo.Column("targetReps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProgramExercises = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysProgramExercises.add(new TableInfo.ForeignKey("programs", "CASCADE", "NO ACTION", Arrays.asList("programId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesProgramExercises = new HashSet<TableInfo.Index>(1);
        _indicesProgramExercises.add(new TableInfo.Index("index_program_exercises_programId", false, Arrays.asList("programId"), Arrays.asList("ASC")));
        final TableInfo _infoProgramExercises = new TableInfo("program_exercises", _columnsProgramExercises, _foreignKeysProgramExercises, _indicesProgramExercises);
        final TableInfo _existingProgramExercises = TableInfo.read(db, "program_exercises");
        if (!_infoProgramExercises.equals(_existingProgramExercises)) {
          return new RoomOpenHelper.ValidationResult(false, "program_exercises(com.lifelift.app.core.data.local.entity.ProgramExerciseEntity).\n"
                  + " Expected:\n" + _infoProgramExercises + "\n"
                  + " Found:\n" + _existingProgramExercises);
        }
        final HashMap<String, TableInfo.Column> _columnsVitamins = new HashMap<String, TableInfo.Column>(9);
        _columnsVitamins.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitamins.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitamins.put("dosage", new TableInfo.Column("dosage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitamins.put("timeOfDay", new TableInfo.Column("timeOfDay", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitamins.put("frequency", new TableInfo.Column("frequency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitamins.put("reminderEnabled", new TableInfo.Column("reminderEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitamins.put("color", new TableInfo.Column("color", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitamins.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitamins.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVitamins = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVitamins = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVitamins = new TableInfo("vitamins", _columnsVitamins, _foreignKeysVitamins, _indicesVitamins);
        final TableInfo _existingVitamins = TableInfo.read(db, "vitamins");
        if (!_infoVitamins.equals(_existingVitamins)) {
          return new RoomOpenHelper.ValidationResult(false, "vitamins(com.lifelift.app.core.data.local.entity.VitaminEntity).\n"
                  + " Expected:\n" + _infoVitamins + "\n"
                  + " Found:\n" + _existingVitamins);
        }
        final HashMap<String, TableInfo.Column> _columnsVitaminLogs = new HashMap<String, TableInfo.Column>(5);
        _columnsVitaminLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitaminLogs.put("vitaminId", new TableInfo.Column("vitaminId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitaminLogs.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitaminLogs.put("taken", new TableInfo.Column("taken", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitaminLogs.put("takenAt", new TableInfo.Column("takenAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVitaminLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVitaminLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVitaminLogs = new TableInfo("vitamin_logs", _columnsVitaminLogs, _foreignKeysVitaminLogs, _indicesVitaminLogs);
        final TableInfo _existingVitaminLogs = TableInfo.read(db, "vitamin_logs");
        if (!_infoVitaminLogs.equals(_existingVitaminLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "vitamin_logs(com.lifelift.app.core.data.local.entity.VitaminLogEntity).\n"
                  + " Expected:\n" + _infoVitaminLogs + "\n"
                  + " Found:\n" + _existingVitaminLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsProgressMetrics = new HashMap<String, TableInfo.Column>(6);
        _columnsProgressMetrics.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgressMetrics.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgressMetrics.put("metricType", new TableInfo.Column("metricType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgressMetrics.put("value", new TableInfo.Column("value", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgressMetrics.put("unit", new TableInfo.Column("unit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgressMetrics.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProgressMetrics = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProgressMetrics = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProgressMetrics = new TableInfo("progress_metrics", _columnsProgressMetrics, _foreignKeysProgressMetrics, _indicesProgressMetrics);
        final TableInfo _existingProgressMetrics = TableInfo.read(db, "progress_metrics");
        if (!_infoProgressMetrics.equals(_existingProgressMetrics)) {
          return new RoomOpenHelper.ValidationResult(false, "progress_metrics(com.lifelift.app.core.data.local.entity.ProgressMetricEntity).\n"
                  + " Expected:\n" + _infoProgressMetrics + "\n"
                  + " Found:\n" + _existingProgressMetrics);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3949ddbd3cae71c0f992b900d8492623", "431b420212034c80e96b72152bbf7916");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "workouts","exercises","sets","exercise_refs","programs","program_exercises","vitamins","vitamin_logs","progress_metrics");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `workouts`");
      _db.execSQL("DELETE FROM `exercises`");
      _db.execSQL("DELETE FROM `sets`");
      _db.execSQL("DELETE FROM `exercise_refs`");
      _db.execSQL("DELETE FROM `programs`");
      _db.execSQL("DELETE FROM `program_exercises`");
      _db.execSQL("DELETE FROM `vitamins`");
      _db.execSQL("DELETE FROM `vitamin_logs`");
      _db.execSQL("DELETE FROM `progress_metrics`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(WorkoutDao.class, WorkoutDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(VitaminDao.class, VitaminDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProgressMetricDao.class, ProgressMetricDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProgramDao.class, ProgramDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public WorkoutDao workoutDao() {
    if (_workoutDao != null) {
      return _workoutDao;
    } else {
      synchronized(this) {
        if(_workoutDao == null) {
          _workoutDao = new WorkoutDao_Impl(this);
        }
        return _workoutDao;
      }
    }
  }

  @Override
  public VitaminDao vitaminDao() {
    if (_vitaminDao != null) {
      return _vitaminDao;
    } else {
      synchronized(this) {
        if(_vitaminDao == null) {
          _vitaminDao = new VitaminDao_Impl(this);
        }
        return _vitaminDao;
      }
    }
  }

  @Override
  public ProgressMetricDao progressMetricDao() {
    if (_progressMetricDao != null) {
      return _progressMetricDao;
    } else {
      synchronized(this) {
        if(_progressMetricDao == null) {
          _progressMetricDao = new ProgressMetricDao_Impl(this);
        }
        return _progressMetricDao;
      }
    }
  }

  @Override
  public ProgramDao programDao() {
    if (_programDao != null) {
      return _programDao;
    } else {
      synchronized(this) {
        if(_programDao == null) {
          _programDao = new ProgramDao_Impl(this);
        }
        return _programDao;
      }
    }
  }
}
