package com.gigadocs.gigadocstest.RoomPersistance;

import com.gigadocs.gigadocstest.User;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


@Dao
public interface UserDao {

    @Query("SELECT * FROM user_table")
    List<User> getAllUsers();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

    @Query("DELETE FROM user_table")
    void deleteAll();

    @Query("DELETE FROM user_table WHERE Id = :id")
    void deleteUserById(int id);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM user_table WHERE Id = :id")
    User getUserById(int id);
}
