package com.lifelift.app.core.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.lifelift.app.core.data.local.entity.ProgramEntity;
import com.lifelift.app.core.data.local.entity.ProgramExerciseEntity;
import com.lifelift.app.core.data.local.entity.ProgramWithExercises;
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
public final class ProgramDao_Impl implements ProgramDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ProgramEntity> __insertionAdapterOfProgramEntity;

  private final EntityInsertionAdapter<ProgramExerciseEntity> __insertionAdapterOfProgramExerciseEntity;

  private final EntityDeletionOrUpdateAdapter<ProgramEntity> __deletionAdapterOfProgramEntity;

  public ProgramDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProgramEntity = new EntityInsertionAdapter<ProgramEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `programs` (`id`,`name`,`description`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgramEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getDescription());
      }
    };
    this.__insertionAdapterOfProgramExerciseEntity = new EntityInsertionAdapter<ProgramExerciseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `program_exercises` (`id`,`programId`,`name`,`orderIndex`,`targetSets`,`targetReps`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgramExerciseEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProgramId());
        statement.bindString(3, entity.getName());
        statement.bindLong(4, entity.getOrderIndex());
        statement.bindLong(5, entity.getTargetSets());
        statement.bindLong(6, entity.getTargetReps());
      }
    };
    this.__deletionAdapterOfProgramEntity = new EntityDeletionOrUpdateAdapter<ProgramEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `programs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgramEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public Object insertProgram(final ProgramEntity program,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfProgramEntity.insertAndReturnId(program);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertProgramExercise(final ProgramExerciseEntity exercise,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfProgramExerciseEntity.insertAndReturnId(exercise);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteProgram(final ProgramEntity program,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfProgramEntity.handle(program);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ProgramWithExercises>> getAllPrograms() {
    final String _sql = "SELECT * FROM programs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"program_exercises",
        "programs"}, new Callable<List<ProgramWithExercises>>() {
      @Override
      @NonNull
      public List<ProgramWithExercises> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
            final LongSparseArray<ArrayList<ProgramExerciseEntity>> _collectionExercises = new LongSparseArray<ArrayList<ProgramExerciseEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionExercises.containsKey(_tmpKey)) {
                _collectionExercises.put(_tmpKey, new ArrayList<ProgramExerciseEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipprogramExercisesAscomLifeliftAppCoreDataLocalEntityProgramExerciseEntity(_collectionExercises);
            final List<ProgramWithExercises> _result = new ArrayList<ProgramWithExercises>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final ProgramWithExercises _item;
              final ProgramEntity _tmpProgram;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndexOfName);
              final String _tmpDescription;
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
              _tmpProgram = new ProgramEntity(_tmpId,_tmpName,_tmpDescription);
              final ArrayList<ProgramExerciseEntity> _tmpExercisesCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpExercisesCollection = _collectionExercises.get(_tmpKey_1);
              _item = new ProgramWithExercises(_tmpProgram,_tmpExercisesCollection);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipprogramExercisesAscomLifeliftAppCoreDataLocalEntityProgramExerciseEntity(
      @NonNull final LongSparseArray<ArrayList<ProgramExerciseEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipprogramExercisesAscomLifeliftAppCoreDataLocalEntityProgramExerciseEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`programId`,`name`,`orderIndex`,`targetSets`,`targetReps` FROM `program_exercises` WHERE `programId` IN (");
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
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "programId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfProgramId = 1;
      final int _cursorIndexOfName = 2;
      final int _cursorIndexOfOrderIndex = 3;
      final int _cursorIndexOfTargetSets = 4;
      final int _cursorIndexOfTargetReps = 5;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<ProgramExerciseEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final ProgramExerciseEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpProgramId;
          _tmpProgramId = _cursor.getLong(_cursorIndexOfProgramId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final int _tmpOrderIndex;
          _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
          final int _tmpTargetSets;
          _tmpTargetSets = _cursor.getInt(_cursorIndexOfTargetSets);
          final int _tmpTargetReps;
          _tmpTargetReps = _cursor.getInt(_cursorIndexOfTargetReps);
          _item_1 = new ProgramExerciseEntity(_tmpId,_tmpProgramId,_tmpName,_tmpOrderIndex,_tmpTargetSets,_tmpTargetReps);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
