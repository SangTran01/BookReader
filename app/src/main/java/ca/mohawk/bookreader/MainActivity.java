package ca.mohawk.bookreader;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
//                AppDatabase.class, "my_db").build();
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "my_db").build();

//        User u1 = new User("S", "Tran", "test123@test.com");
//        InsertUserTask insertTask = new InsertUserTask();
//        insertTask.execute(u1);

        QueryUserTask task = new QueryUserTask();
        task.execute();
    }

    private class QueryUserTask extends AsyncTask<Void, Void, List<User>> {
        @Override
        protected List<User> doInBackground(Void... voids) {
            return db.userDao().getAllUsers();
        }

        @Override
        protected void onPostExecute(List<User> users) {
//            super.onPostExecute(listLiveData);
            System.out.println("here");
            System.out.println(users);
            for (User user : users) {
                System.out.println(user);
                System.out.println(user.getFirstName());
                System.out.println(user.getLastName());
                System.out.println(user.getEmail());
            }
        }
    }

    private class InsertUserTask extends AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... users) {
            System.out.println("in bk");
            System.out.println(users);
            db.userDao().insertUsers(users[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
            System.out.println("inserted");
        }
    }
}
