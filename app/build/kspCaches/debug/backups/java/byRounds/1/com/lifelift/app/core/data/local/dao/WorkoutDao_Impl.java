package com.lifelift.app.core.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.lifelift.app.core.data.local.converter.Converters;
import com.lifelift.app.core.data.local.entity.ExerciseEntity;
import com.lifelift.app.core.data.local.entity.ExerciseRefEntity;
import com.lifelift.app.core.data.local.entity.ExerciseWithSets;
import com.lifelift.app.core.data.local.entity.SetEntity;
import com.lifelift.app.core.data.local.entity.WorkoutEntity;
import com.lifelift.app.core.data.local.entity.WorkoutType;
import com.lifelift.app.core.data.local.entity.WorkoutWithExercises;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class WorkoutDao_Impl implements WorkoutDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WorkoutEntity> __insertionAdapterOfWorkoutEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<ExerciseEntity> __insertionAdapterOfExerciseEntity;

  private final EntityInsertionAdapter<SetEntity> __insertionAdapterOfSetEntity;

  private final EntityInsertionAdapter<ExerciseRefEntity> __insertionAdapterOfExerciseRefEntity;

  private final EntityDeletionOrUpdateAdapter<WorkoutEntity> __deletionAdapterOfWorkoutEntity;

  private final EntityDeletionOrUpdateAdapter<ExerciseRefEntity> __deletionAdapterOfExerciseRefEntity;

  private final EntityDeletionOrUpdateAdapter<WorkoutEntity> __updateAdapterOfWorkoutEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteExercisesByWorkoutId;

  public WorkoutDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWorkoutEntity = new EntityInsertionAdapter<WorkoutEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `workouts` (`id`,`name`,`date`,`durationMinutes`,`notes`,`type`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WorkoutEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getDate());
        statement.bindLong(4, entity.getDurationMinutes());
        statement.bindString(5, entity.getNotes());
        final String _tmp = __converters.fromWorkoutType(entity.getType());
        statement.bindString(6, _tmp);
      }
    };
    this.__insertionAdapterOfExerciseEntity = new EntityInsertionAdapter<ExerciseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exercises` (`id`,`workoutId`,`name`,`orderIndex`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getWorkoutId());
        statement.bindString(3, entity.getName());
        statement.bindLong(4, entity.getOrderIndex());
      }
    };
    this.__insertionAdapterOfSetEntity = new EntityInsertionAdapter<SetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sets` (`id`,`exerciseId`,`reps`,`weight`,`isWarmup`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SetEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getExerciseId());
        statement.bindLong(3, entity.getReps());
        statement.bindDouble(4, entity.getWeight());
        final int _tmp = entity.isWarmup() ? 1 : 0;
        statement.bindLong(5, _tmp);
      }
    };
    this.__insertionAdapterOfExerciseRefEntity = new EntityInsertionAdapter<ExerciseRefEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exercise_refs` (`id`,`name`,`category`,`defaultRestSeconds`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseRefEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getCategory());
        statement.bindLong(4, entity.getDefaultRestSeconds());
      }
    };
    this.__deletionAdapterOfWorkoutEntity = new EntityDeletionOrUpdateAdapter<WorkoutEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `workouts` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WorkoutEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfExerciseRefEntity = new EntityDeletionOrUpdateAdapter<ExerciseRefEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `exercise_refs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseRefEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfWorkoutEntity = new EntityDeletionOrUpdateAdapter<WorkoutEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `workouts` SET `id` = ?,`name` = ?,`date` = ?,`durationMinutes` = ?,`notes` = ?,`type` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WorkoutEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getDate());
        statement.bindLong(4, entity.getDurationMinutes());
        statement.bindString(5, entity.getNotes());
        final String _tmp = __converters.fromWorkoutType(entity.getType());
        statement.bindString(6, _tmp);
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteExercisesByWorkoutId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exercises WHERE workoutId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertWorkout(final WorkoutEntity workout,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfWorkoutEntity.insertAndReturnId(workout);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertExercise(final ExerciseEntity exercise,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExerciseEntity.insertAndReturnId(exercise);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSet(final SetEntity set, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSetEntity.insertAndReturnId(set);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertExerciseRef(final ExerciseRefEntity exerciseRef,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExerciseRefEntity.insertAndReturnId(exerciseRef);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteWorkout(final WorkoutEntity workout,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfWorkoutEntity.handle(workout);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteExerciseRef(final ExerciseRefEntity exerciseRef,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfExerciseRefEntity.handle(exerciseRef);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateWorkout(final WorkoutEntity workout,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfWorkoutEntity.handle(workout);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteExercisesByWorkoutId(final long workoutId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteExercisesByWorkoutId.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, workoutId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteExercisesByWorkoutId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<WorkoutWithExercises>> getAllWorkouts() {
    final String _sql = "SELECT * FROM workouts ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"sets", "exercises",
        "workouts"}, new Callable<List<WorkoutWithExercises>>() {
      @Override
      @NonNull
      public List<WorkoutWithExercises> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
            final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMinutes");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
            final LongSparseArray<ArrayList<ExerciseWithSets>> _collectionExercises = new LongSparseArray<ArrayList<ExerciseWithSets>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionExercises.containsKey(_tmpKey)) {
                _collectionExercises.put(_tmpKey, new ArrayList<ExerciseWithSets>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipexercisesAscomLifeliftAppCoreDataLocalEntityExerciseWithSets(_collectionExercises);
            final List<WorkoutWithExercises> _result = new ArrayList<WorkoutWithExercises>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final WorkoutWithExercises _item;
              final WorkoutEntity _tmpWorkout;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndexOfName);
              final String _tmpDate;
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
              final int _tmpDurationMinutes;
              _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final WorkoutType _tmpType;
              final String _tmp;
              _tmp = _cursor.getString(_cursorIndexOfType);
              _tmpType = __converters.toWorkoutType(_tmp);
              _tmpWorkout = new WorkoutEntity(_tmpId,_tmpName,_tmpDate,_tmpDurationMinutes,_tmpNotes,_tmpType);
              final ArrayList<ExerciseWithSets> _tmpExercisesCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpExercisesCollection = _collectionExercises.get(_tmpKey_1);
              _item = new WorkoutWithExercises(_tmpWorkout,_tmpExercisesCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<WorkoutWithExercises>> getWorkoutsByType(final WorkoutType type) {
    final String _sql = "SELECT * FROM workouts WHERE type = ? ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromWorkoutType(type);
    _statement.bindString(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"sets", "exercises",
        "workouts"}, new Callable<List<WorkoutWithExercises>>() {
      @Override
      @NonNull
      public List<WorkoutWithExercises> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
            final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMinutes");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
            final LongSparseArray<ArrayList<ExerciseWithSets>> _collectionExercises = new LongSparseArray<ArrayList<ExerciseWithSets>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionExercises.containsKey(_tmpKey)) {
                _collectionExercises.put(_tmpKey, new ArrayList<ExerciseWithSets>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipexercisesAscomLifeliftAppCoreDataLocalEntityExerciseWithSets(_collectionExercises);
            final List<WorkoutWithExercises> _result = new ArrayList<WorkoutWithExercises>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final WorkoutWithExercises _item;
              final WorkoutEntity _tmpWorkout;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndexOfName);
              final String _tmpDate;
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
              final int _tmpDurationMinutes;
              _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final WorkoutType _tmpType;
              final String _tmp_1;
              _tmp_1 = _cursor.getString(_cursorIndexOfType);
              _tmpType = __converters.toWorkoutType(_tmp_1);
              _tmpWorkout = new WorkoutEntity(_tmpId,_tmpName,_tmpDate,_tmpDurationMinutes,_tmpNotes,_tmpType);
              final ArrayList<ExerciseWithSets> _tmpExercisesCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpExercisesCollection = _collectionExercises.get(_tmpKey_1);
              _item = new WorkoutWithExercises(_tmpWorkout,_tmpExercisesCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<WorkoutWithExercises> getWorkoutById(final long id) {
    final String _sql = "SELECT * FROM workouts WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"sets", "exercises",
        "workouts"}, new Callable<WorkoutWithExercises>() {
      @Override
      @NonNull
      public WorkoutWithExercises call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
            final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMinutes");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
            final LongSparseArray<ArrayList<ExerciseWithSets>> _collectionExercises = new LongSparseArray<ArrayList<ExerciseWithSets>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionExercises.containsKey(_tmpKey)) {
                _collectionExercises.put(_tmpKey, new ArrayList<ExerciseWithSets>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipexercisesAscomLifeliftAppCoreDataLocalEntityExerciseWithSets(_collectionExercises);
            final WorkoutWithExercises _result;
            if (_cursor.moveToFirst()) {
              final WorkoutEntity _tmpWorkout;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndexOfName);
              final String _tmpDate;
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
              final int _tmpDurationMinutes;
              _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final WorkoutType _tmpType;
              final String _tmp;
              _tmp = _cursor.getString(_cursorIndexOfType);
              _tmpType = __converters.toWorkoutType(_tmp);
              _tmpWorkout = new WorkoutEntity(_tmpId,_tmpName,_tmpDate,_tmpDurationMinutes,_tmpNotes,_tmpType);
              final ArrayList<ExerciseWithSets> _tmpExercisesCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpExercisesCollection = _collectionExercises.get(_tmpKey_1);
              _result = new WorkoutWithExercises(_tmpWorkout,_tmpExercisesCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllExerciseRefs(
      final Continuation<? super List<ExerciseRefEntity>> $completion) {
    final String _sql = "SELECT * FROM exercise_refs ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExerciseRefEntity>>() {
      @Override
      @NonNull
      public List<ExerciseRefEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfDefaultRestSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "defaultRestSeconds");
          final List<ExerciseRefEntity> _result = new ArrayList<ExerciseRefEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseRefEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final int _tmpDefaultRestSeconds;
            _tmpDefaultRestSeconds = _cursor.getInt(_cursorIndexOfDefaultRestSeconds);
            _item = new ExerciseRefEntity(_tmpId,_tmpName,_tmpCategory,_tmpDefaultRestSeconds);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRecentSetsForExercise(final String exerciseName,
      final Continuation<? super List<SetEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT sets.* FROM sets \n"
            + "        INNER JOIN exercises ON sets.exerciseId = exercises.id \n"
            + "        INNER JOIN workouts ON exercises.workoutId = workouts.id \n"
            + "        WHERE exercises.name = ? \n"
            + "        ORDER BY workouts.date DESC, exercises.id DESC\n"
            + "        LIMIT 20\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, exerciseName);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SetEntity>>() {
      @Override
      @NonNull
      public List<SetEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseId");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfIsWarmup = CursorUtil.getColumnIndexOrThrow(_cursor, "isWarmup");
          final List<SetEntity> _result = new ArrayList<SetEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SetEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpExerciseId;
            _tmpExerciseId = _cursor.getLong(_cursorIndexOfExerciseId);
            final int _tmpReps;
            _tmpReps = _cursor.getInt(_cursorIndexOfReps);
            final double _tmpWeight;
            _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            final boolean _tmpIsWarmup;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWarmup);
            _tmpIsWarmup = _tmp != 0;
            _item = new SetEntity(_tmpId,_tmpExerciseId,_tmpReps,_tmpWeight,_tmpIsWarmup);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTopExerciseNames(final int limit,
      final Continuation<? super List<String>> $completion) {
    final String _sql = "SELECT name FROM exercises GROUP BY name ORDER BY COUNT(*) DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllSetsForExercise(final String exerciseName,
      final Continuation<? super List<SetEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT sets.* FROM sets \n"
            + "        INNER JOIN exercises ON sets.exerciseId = exercises.id \n"
            + "        INNER JOIN workouts ON exercises.workoutId = workouts.id \n"
            + "        WHERE exercises.name = ? \n"
            + "        ORDER BY workouts.date ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, exerciseName);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SetEntity>>() {
      @Override
      @NonNull
      public List<SetEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseId");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfIsWarmup = CursorUtil.getColumnIndexOrThrow(_cursor, "isWarmup");
          final List<SetEntity> _result = new ArrayList<SetEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SetEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpExerciseId;
            _tmpExerciseId = _cursor.getLong(_cursorIndexOfExerciseId);
            final int _tmpReps;
            _tmpReps = _cursor.getInt(_cursorIndexOfReps);
            final double _tmpWeight;
            _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            final boolean _tmpIsWarmup;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWarmup);
            _tmpIsWarmup = _tmp != 0;
            _item = new SetEntity(_tmpId,_tmpExerciseId,_tmpReps,_tmpWeight,_tmpIsWarmup);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipsetsAscomLifeliftAppCoreDataLocalEntitySetEntity(
      @NonNull final LongSparseArray<ArrayList<SetEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipsetsAscomLifeliftAppCoreDataLocalEntitySetEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`exerciseId`,`reps`,`weight`,`isWarmup` FROM `sets` WHERE `exerciseId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "exerciseId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfExerciseId = 1;
      final int _cursorIndexOfReps = 2;
      final int _cursorIndexOfWeight = 3;
      final int _cursorIndexOfIsWarmup = 4;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<SetEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final SetEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpExerciseId;
          _tmpExerciseId = _cursor.getLong(_cursorIndexOfExerciseId);
          final int _tmpReps;
          _tmpReps = _cursor.getInt(_cursorIndexOfReps);
          final double _tmpWeight;
          _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
          final boolean _tmpIsWarmup;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsWarmup);
          _tmpIsWarmup = _tmp != 0;
          _item_1 = new SetEntity(_tmpId,_tmpExerciseId,_tmpReps,_tmpWeight,_tmpIsWarmup);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipexercisesAscomLifeliftAppCoreDataLocalEntityExerciseWithSets(
      @NonNull final LongSparseArray<ArrayList<ExerciseWithSets>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipexercisesAscomLifeliftAppCoreDataLocalEntityExerciseWithSets(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`workoutId`,`name`,`orderIndex` FROM `exercises` WHERE `workoutId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, true, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "workoutId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfWorkoutId = 1;
      final int _cursorIndexOfName = 2;
      final int _cursorIndexOfOrderIndex = 3;
      final LongSparseArray<ArrayList<SetEntity>> _collectionSets = new LongSparseArray<ArrayList<SetEntity>>();
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_cursorIndexOfId);
        if (!_collectionSets.containsKey(_tmpKey)) {
          _collectionSets.put(_tmpKey, new ArrayList<SetEntity>());
        }
      }
      _cursor.moveToPosition(-1);
      __fetchRelationshipsetsAscomLifeliftAppCoreDataLocalEntitySetEntity(_collectionSets);
      while (_cursor.moveToNext()) {
        final long _tmpKey_1;
        _tmpKey_1 = _cursor.getLong(_itemKeyIndex);
        final ArrayList<ExerciseWithSets> _tmpRelation = _map.get(_tmpKey_1);
        if (_tmpRelation != null) {
          final ExerciseWithSets _item_1;
          final ExerciseEntity _tmpExercise;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpWorkoutId;
          _tmpWorkoutId = _cursor.getLong(_cursorIndexOfWorkoutId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final int _tmpOrderIndex;
          _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
          _tmpExercise = new ExerciseEntity(_tmpId,_tmpWorkoutId,_tmpName,_tmpOrderIndex);
          final ArrayList<SetEntity> _tmpSetsCollection;
          final long _tmpKey_2;
          _tmpKey_2 = _cursor.getLong(_cursorIndexOfId);
          _tmpSetsCollection = _collectionSets.get(_tmpKey_2);
          _item_1 = new ExerciseWithSets(_tmpExercise,_tmpSetsCollection);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
