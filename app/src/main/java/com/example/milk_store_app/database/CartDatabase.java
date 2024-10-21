package com.example.milk_store_app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.milk_store_app.constants.RoomDataBaseConstants;
import com.example.milk_store_app.database.dao.CartItemDao;
import com.example.milk_store_app.models.entities.CartItems;

@Database(entities = {CartItems.class}, version = RoomDataBaseConstants.DATABASE_VERSION)
public abstract class CartDatabase extends RoomDatabase {
    private static CartDatabase instance;

    public abstract CartItemDao cartItemDao();

    public static synchronized CartDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            CartDatabase.class, RoomDataBaseConstants.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
