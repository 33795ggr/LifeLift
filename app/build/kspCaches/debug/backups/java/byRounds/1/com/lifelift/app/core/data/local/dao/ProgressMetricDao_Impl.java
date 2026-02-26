package com.lifelift.app.core.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.lifelift.app.core.data.local.entity.MetricType;
import com.lifelift.app.core.data.local.entity.ProgressMetricEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
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
public final class ProgressMetricDao_Impl implements ProgressMetricDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ProgressMetricEntity> __insertionAdapterOfProgressMetricEntity;

  private final EntityDeletionOrUpdateAdapter<ProgressMetricEntity> __deletionAdapterOfProgressMetricEntity;

  private final EntityDeletionOrUpdateAdapter<ProgressMetricEntity> __updateAdapterOfProgressMetricEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMetricById;

  public ProgressMetricDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProgressMetricEntity = new EntityInsertionAdapter<ProgressMetricEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `progress_metrics` (`id`,`date`,`metricType`,`value`,`unit`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgressMetricEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDate());
        statement.bindString(3, __MetricType_enumToString(entity.getMetricType()));
        statement.bindDouble(4, entity.getValue());
        statement.bindString(5, entity.getUnit());
        statement.bindString(6, entity.getNotes());
      }
    };
    this.__deletionAdapterOfProgressMetricEntity = new EntityDeletionOrUpdateAdapter<ProgressMetricEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `progress_metrics` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgressMetricEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfProgressMetricEntity = new EntityDeletionOrUpdateAdapter<ProgressMetricEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `progress_metrics` SET `id` = ?,`date` = ?,`metricType` = ?,`value` = ?,`unit` = ?,`notes` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgressMetricEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDate());
        statement.bindString(3, __MetricType_enumToString(entity.getMetricType()));
        statement.bindDouble(4, entity.getValue());
        statement.bindString(5, entity.getUnit());
        statement.bindString(6, entity.getNotes());
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteMetricById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM progress_metrics WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertMetric(final ProgressMetricEntity metric,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfProgressMetricEntity.insertAndReturnId(metric);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMetric(final ProgressMetricEntity metric,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfProgressMetricEntity.handle(metric);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMetric(final ProgressMetricEntity metric,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfProgressMetricEntity.handle(metric);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMetricById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMetricById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfDeleteMetricById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ProgressMetricEntity>> getAllMetrics() {
    final String _sql = "SELECT * FROM progress_metrics ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"progress_metrics"}, new Callable<List<ProgressMetricEntity>>() {
      @Override
      @NonNull
      public List<ProgressMetricEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMetricType = CursorUtil.getColumnIndexOrThrow(_cursor, "metricType");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<ProgressMetricEntity> _result = new ArrayList<ProgressMetricEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProgressMetricEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final MetricType _tmpMetricType;
            _tmpMetricType = __MetricType_stringToEnum(_cursor.getString(_cursorIndexOfMetricType));
            final double _tmpValue;
            _tmpValue = _cursor.getDouble(_cursorIndexOfValue);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _item = new ProgressMetricEntity(_tmpId,_tmpDate,_tmpMetricType,_tmpValue,_tmpUnit,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ProgressMetricEntity>> getMetricsByType(final MetricType type) {
    final String _sql = "SELECT * FROM progress_metrics WHERE metricType = ? ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, __MetricType_enumToString(type));
    return CoroutinesRoom.createFlow(__db, false, new String[] {"progress_metrics"}, new Callable<List<ProgressMetricEntity>>() {
      @Override
      @NonNull
      public List<ProgressMetricEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMetricType = CursorUtil.getColumnIndexOrThrow(_cursor, "metricType");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<ProgressMetricEntity> _result = new ArrayList<ProgressMetricEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProgressMetricEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final MetricType _tmpMetricType;
            _tmpMetricType = __MetricType_stringToEnum(_cursor.getString(_cursorIndexOfMetricType));
            final double _tmpValue;
            _tmpValue = _cursor.getDouble(_cursorIndexOfValue);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _item = new ProgressMetricEntity(_tmpId,_tmpDate,_tmpMetricType,_tmpValue,_tmpUnit,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ProgressMetricEntity>> getMetricsForPeriod(final String startDate,
      final String endDate) {
    final String _sql = "SELECT * FROM progress_metrics WHERE date BETWEEN ? AND ? ORDER BY date ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"progress_metrics"}, new Callable<List<ProgressMetricEntity>>() {
      @Override
      @NonNull
      public List<ProgressMetricEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMetricType = CursorUtil.getColumnIndexOrThrow(_cursor, "metricType");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<ProgressMetricEntity> _result = new ArrayList<ProgressMetricEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProgressMetricEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final MetricType _tmpMetricType;
            _tmpMetricType = __MetricType_stringToEnum(_cursor.getString(_cursorIndexOfMetricType));
            final double _tmpValue;
            _tmpValue = _cursor.getDouble(_cursorIndexOfValue);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _item = new ProgressMetricEntity(_tmpId,_tmpDate,_tmpMetricType,_tmpValue,_tmpUnit,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ProgressMetricEntity>> getMetricsByTypeForPeriod(final MetricType type,
      final String startDate, final String endDate) {
    final String _sql = "SELECT * FROM progress_metrics WHERE metricType = ? AND date BETWEEN ? AND ? ORDER BY date ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindString(_argIndex, __MetricType_enumToString(type));
    _argIndex = 2;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 3;
    _statement.bindString(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"progress_metrics"}, new Callable<List<ProgressMetricEntity>>() {
      @Override
      @NonNull
      public List<ProgressMetricEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMetricType = CursorUtil.getColumnIndexOrThrow(_cursor, "metricType");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<ProgressMetricEntity> _result = new ArrayList<ProgressMetricEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProgressMetricEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final MetricType _tmpMetricType;
            _tmpMetricType = __MetricType_stringToEnum(_cursor.getString(_cursorIndexOfMetricType));
            final double _tmpValue;
            _tmpValue = _cursor.getDouble(_cursorIndexOfValue);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _item = new ProgressMetricEntity(_tmpId,_tmpDate,_tmpMetricType,_tmpValue,_tmpUnit,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
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

  private String __MetricType_enumToString(@NonNull final MetricType _value) {
    switch (_value) {
      case BODY_WEIGHT: return "BODY_WEIGHT";
      case BODY_FAT_PERCENTAGE: return "BODY_FAT_PERCENTAGE";
      case MUSCLE_MASS: return "MUSCLE_MASS";
      case BENCH_PRESS_MAX: return "BENCH_PRESS_MAX";
      case SQUAT_MAX: return "SQUAT_MAX";
      case DEADLIFT_MAX: return "DEADLIFT_MAX";
      case WEEKLY_VOLUME: return "WEEKLY_VOLUME";
      case MONTHLY_VOLUME: return "MONTHLY_VOLUME";
      case CUSTOM: return "CUSTOM";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private MetricType __MetricType_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "BODY_WEIGHT": return MetricType.BODY_WEIGHT;
      case "BODY_FAT_PERCENTAGE": return MetricType.BODY_FAT_PERCENTAGE;
      case "MUSCLE_MASS": return MetricType.MUSCLE_MASS;
      case "BENCH_PRESS_MAX": return MetricType.BENCH_PRESS_MAX;
      case "SQUAT_MAX": return MetricType.SQUAT_MAX;
      case "DEADLIFT_MAX": return MetricType.DEADLIFT_MAX;
      case "WEEKLY_VOLUME": return MetricType.WEEKLY_VOLUME;
      case "MONTHLY_VOLUME": return MetricType.MONTHLY_VOLUME;
      case "CUSTOM": return MetricType.CUSTOM;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
