package ca.mohawk.bookreader;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("Select * from user")
    List<User> getAllUsers();

    @Insert
    void insertUsers(User... users);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);
}
