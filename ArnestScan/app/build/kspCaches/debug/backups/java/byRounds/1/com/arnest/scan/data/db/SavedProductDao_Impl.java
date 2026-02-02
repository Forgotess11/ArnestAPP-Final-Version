package com.arnest.scan.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
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
public final class SavedProductDao_Impl implements SavedProductDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SavedProductEntity> __insertionAdapterOfSavedProductEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByBarcode;

  public SavedProductDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSavedProductEntity = new EntityInsertionAdapter<SavedProductEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `saved_products` (`barcode`,`name`,`imageUrls`,`composition`,`safetyStatus`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SavedProductEntity entity) {
        statement.bindString(1, entity.getBarcode());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getImageUrls());
        statement.bindString(4, entity.getComposition());
        statement.bindString(5, entity.getSafetyStatus());
      }
    };
    this.__preparedStmtOfDeleteByBarcode = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM saved_products WHERE barcode = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final SavedProductEntity product,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSavedProductEntity.insert(product);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByBarcode(final String barcode,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByBarcode.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, barcode);
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
          __preparedStmtOfDeleteByBarcode.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SavedProductEntity>> getAllSaved() {
    final String _sql = "SELECT * FROM saved_products";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"saved_products"}, new Callable<List<SavedProductEntity>>() {
      @Override
      @NonNull
      public List<SavedProductEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBarcode = CursorUtil.getColumnIndexOrThrow(_cursor, "barcode");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfImageUrls = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrls");
          final int _cursorIndexOfComposition = CursorUtil.getColumnIndexOrThrow(_cursor, "composition");
          final int _cursorIndexOfSafetyStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "safetyStatus");
          final List<SavedProductEntity> _result = new ArrayList<SavedProductEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SavedProductEntity _item;
            final String _tmpBarcode;
            _tmpBarcode = _cursor.getString(_cursorIndexOfBarcode);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpImageUrls;
            _tmpImageUrls = _cursor.getString(_cursorIndexOfImageUrls);
            final String _tmpComposition;
            _tmpComposition = _cursor.getString(_cursorIndexOfComposition);
            final String _tmpSafetyStatus;
            _tmpSafetyStatus = _cursor.getString(_cursorIndexOfSafetyStatus);
            _item = new SavedProductEntity(_tmpBarcode,_tmpName,_tmpImageUrls,_tmpComposition,_tmpSafetyStatus);
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
  public Object isSaved(final String barcode, final Continuation<? super Boolean> $completion) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM saved_products WHERE barcode = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, barcode);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp != 0;
          } else {
            _result = false;
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
