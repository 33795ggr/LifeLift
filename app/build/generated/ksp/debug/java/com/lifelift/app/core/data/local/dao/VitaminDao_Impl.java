package com.lifelift.app.core.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.lifelift.app.core.data.local.converter.Converters;
import com.lifelift.app.core.data.local.entity.VitaminEntity;
import com.lifelift.app.core.data.local.entity.VitaminFrequency;
import com.lifelift.app.core.data.local.entity.VitaminLogEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class VitaminDao_Impl implements VitaminDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<VitaminEntity> __insertionAdapterOfVitaminEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<VitaminLogEntity> __insertionAdapterOfVitaminLogEntity;

  private final EntityDeletionOrUpdateAdapter<VitaminEntity> __deletionAdapterOfVitaminEntity;

  private final EntityDeletionOrUpdateAdapter<VitaminEntity> __updateAdapterOfVitaminEntity;

  private final EntityDeletionOrUpdateAdapter<VitaminLogEntity> __updateAdapterOfVitaminLogEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteLogsForVitamin;

  public VitaminDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVitaminEntity = new EntityInsertionAdapter<VitaminEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `vitamins` (`id`,`name`,`dosage`,`timeOfDay`,`frequency`,`reminderEnabled`,`color`,`notes`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VitaminEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getDosage());
        statement.bindString(4, entity.getTimeOfDay());
        final String _tmp = __converters.fromVitaminFrequency(entity.getFrequency());
        statement.bindString(5, _tmp);
        final int _tmp_1 = entity.getReminderEnabled() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        statement.bindString(7, entity.getColor());
        statement.bindString(8, entity.getNotes());
        statement.bindString(9, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfVitaminLogEntity = new EntityInsertionAdapter<VitaminLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `vitamin_logs` (`id`,`vitaminId`,`date`,`taken`,`takenAt`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VitaminLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getVitaminId());
        statement.bindString(3, entity.getDate());
        final int _tmp = entity.getTaken() ? 1 : 0;
        statement.bindLong(4, _tmp);
        if (entity.getTakenAt() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTakenAt());
        }
      }
    };
    this.__deletionAdapterOfVitaminEntity = new EntityDeletionOrUpdateAdapter<VitaminEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `vitamins` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VitaminEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfVitaminEntity = new EntityDeletionOrUpdateAdapter<VitaminEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `vitamins` SET `id` = ?,`name` = ?,`dosage` = ?,`timeOfDay` = ?,`frequency` = ?,`reminderEnabled` = ?,`color` = ?,`notes` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VitaminEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getDosage());
        statement.bindString(4, entity.getTimeOfDay());
        final String _tmp = __converters.fromVitaminFrequency(entity.getFrequency());
        statement.bindString(5, _tmp);
        final int _tmp_1 = entity.getReminderEnabled() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        statement.bindString(7, entity.getColor());
        statement.bindString(8, entity.getNotes());
        statement.bindString(9, entity.getCreatedAt());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__updateAdapterOfVitaminLogEntity = new EntityDeletionOrUpdateAdapter<VitaminLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `vitamin_logs` SET `id` = ?,`vitaminId` = ?,`date` = ?,`taken` = ?,`takenAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VitaminLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getVitaminId());
        statement.bindString(3, entity.getDate());
        final int _tmp = entity.getTaken() ? 1 : 0;
        statement.bindLong(4, _tmp);
        if (entity.getTakenAt() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTakenAt());
        }
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteLogsForVitamin = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM vitamin_logs WHERE vitaminId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertVitamin(final VitaminEntity vitamin,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfVitaminEntity.insertAndReturnId(vitamin);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertLog(final VitaminLogEntity log,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfVitaminLogEntity.insertAndReturnId(log);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteVitamin(final VitaminEntity vitamin,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfVitaminEntity.handle(vitamin);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateVitamin(final VitaminEntity vitamin,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfVitaminEntity.handle(vitamin);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLog(final VitaminLogEntity log,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfVitaminLogEntity.handle(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteLogsForVitamin(final long vitaminId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteLogsForVitamin.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, vitaminId);
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
          __preparedStmtOfDeleteLogsForVitamin.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<VitaminEntity>> getAllVitamins() {
    final String _sql = "SELECT * FROM vitamins ORDER BY timeOfDay ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"vitamins"}, new Callable<List<VitaminEntity>>() {
      @Override
      @NonNull
      public List<VitaminEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDosage = CursorUtil.getColumnIndexOrThrow(_cursor, "dosage");
          final int _cursorIndexOfTimeOfDay = CursorUtil.getColumnIndexOrThrow(_cursor, "timeOfDay");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfReminderEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderEnabled");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<VitaminEntity> _result = new ArrayList<VitaminEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VitaminEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDosage;
            _tmpDosage = _cursor.getString(_cursorIndexOfDosage);
            final String _tmpTimeOfDay;
            _tmpTimeOfDay = _cursor.getString(_cursorIndexOfTimeOfDay);
            final VitaminFrequency _tmpFrequency;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfFrequency);
            _tmpFrequency = __converters.toVitaminFrequency(_tmp);
            final boolean _tmpReminderEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfReminderEnabled);
            _tmpReminderEnabled = _tmp_1 != 0;
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            _item = new VitaminEntity(_tmpId,_tmpName,_tmpDosage,_tmpTimeOfDay,_tmpFrequency,_tmpReminderEnabled,_tmpColor,_tmpNotes,_tmpCreatedAt);
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
  public Object getVitaminById(final long id,
      final Continuation<? super VitaminEntity> $completion) {
    final String _sql = "SELECT * FROM vitamins WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<VitaminEntity>() {
      @Override
      @Nullable
      public VitaminEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDosage = CursorUtil.getColumnIndexOrThrow(_cursor, "dosage");
          final int _cursorIndexOfTimeOfDay = CursorUtil.getColumnIndexOrThrow(_cursor, "timeOfDay");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfReminderEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderEnabled");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final VitaminEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDosage;
            _tmpDosage = _cursor.getString(_cursorIndexOfDosage);
            final String _tmpTimeOfDay;
            _tmpTimeOfDay = _cursor.getString(_cursorIndexOfTimeOfDay);
            final VitaminFrequency _tmpFrequency;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfFrequency);
            _tmpFrequency = __converters.toVitaminFrequency(_tmp);
            final boolean _tmpReminderEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfReminderEnabled);
            _tmpReminderEnabled = _tmp_1 != 0;
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            _result = new VitaminEntity(_tmpId,_tmpName,_tmpDosage,_tmpTimeOfDay,_tmpFrequency,_tmpReminderEnabled,_tmpColor,_tmpNotes,_tmpCreatedAt);
          } else {
            _result = null;
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
  public Flow<List<VitaminLogEntity>> getLogsForDate(final String date) {
    final String _sql = "SELECT * FROM vitamin_logs WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"vitamin_logs"}, new Callable<List<VitaminLogEntity>>() {
      @Override
      @NonNull
      public List<VitaminLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVitaminId = CursorUtil.getColumnIndexOrThrow(_cursor, "vitaminId");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "taken");
          final int _cursorIndexOfTakenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "takenAt");
          final List<VitaminLogEntity> _result = new ArrayList<VitaminLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VitaminLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpVitaminId;
            _tmpVitaminId = _cursor.getLong(_cursorIndexOfVitaminId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final boolean _tmpTaken;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfTaken);
            _tmpTaken = _tmp != 0;
            final String _tmpTakenAt;
            if (_cursor.isNull(_cursorIndexOfTakenAt)) {
              _tmpTakenAt = null;
            } else {
              _tmpTakenAt = _cursor.getString(_cursorIndexOfTakenAt);
            }
            _item = new VitaminLogEntity(_tmpId,_tmpVitaminId,_tmpDate,_tmpTaken,_tmpTakenAt);
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
  public Object getLogForVitaminAndDate(final long vitaminId, final String date,
      final Continuation<? super VitaminLogEntity> $completion) {
    final String _sql = "SELECT * FROM vitamin_logs WHERE vitaminId = ? AND date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, vitaminId);
    _argIndex = 2;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<VitaminLogEntity>() {
      @Override
      @Nullable
      public VitaminLogEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVitaminId = CursorUtil.getColumnIndexOrThrow(_cursor, "vitaminId");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "taken");
          final int _cursorIndexOfTakenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "takenAt");
          final VitaminLogEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpVitaminId;
            _tmpVitaminId = _cursor.getLong(_cursorIndexOfVitaminId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final boolean _tmpTaken;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfTaken);
            _tmpTaken = _tmp != 0;
            final String _tmpTakenAt;
            if (_cursor.isNull(_cursorIndexOfTakenAt)) {
              _tmpTakenAt = null;
            } else {
              _tmpTakenAt = _cursor.getString(_cursorIndexOfTakenAt);
            }
            _result = new VitaminLogEntity(_tmpId,_tmpVitaminId,_tmpDate,_tmpTaken,_tmpTakenAt);
          } else {
            _result = null;
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
  public Object getTakenCountForPeriod(final String startDate, final String endDate,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM vitamin_logs WHERE date BETWEEN ? AND ? AND taken = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
  public Object getTotalCountForPeriod(final String startDate, final String endDate,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM vitamin_logs WHERE date BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
}
