package ca.mohawk.bookreader;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
