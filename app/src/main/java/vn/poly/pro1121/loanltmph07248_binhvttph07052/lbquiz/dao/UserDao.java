package vn.poly.pro1121.loanltmph07248_binhvttph07052.lbquiz.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.poly.pro1121.loanltmph07248_binhvttph07052.lbquiz.database.DatabaseHelper;
import vn.poly.pro1121.loanltmph07248_binhvttph07052.lbquiz.model.User;

public class UserDao {
    public static final String TABLE_USERS = "Users";

    //column
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_FULLNAME = "fullname";

    DatabaseHelper databaseHelper;

    public UserDao(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    //them sua xoa, getAll, get by id
    public List<User> getAllUser() {
        List<User> users = new ArrayList<>();
        //xin quyen
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        //cau lenh select
        String selectQuery = "SELECT * FROM "+ TABLE_USERS;
        //su dung cau lenh raw query
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                user.setFullname(cursor.getString(cursor.getColumnIndex(COLUMN_FULLNAME)));

                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return users;
    }

    public long insertUser(User user) {
        //xin quyen
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        //Ghep cap du lieu
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_FULLNAME, user.getFullname());
        //insert
        long result = database.insert(TABLE_USERS, null, values);
        //dong ket noi db
        database.close();
        return result;
    }

    public int updateUserAllDetails(User user) {
        //xin quyen
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        //Ghep cap du lieu
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_FULLNAME, user.getFullname());
        //update
        int result = database.update(
                TABLE_USERS,
                values,
                COLUMN_USERNAME+"=?",
                new String[] {user.getUsername()}
        );
        //dong ket noi db
        database.close();
        return result;
    }

    public int updateUserFullname(User user) {
        //xin quyen
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        //Ghep cap du lieu
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULLNAME, user.getFullname());
        //update
        int result = database.update(
                TABLE_USERS,
                values,
                COLUMN_USERNAME+"=?",
                new String[] {user.getUsername()}
        );
        //dong ket noi db
        database.close();
        return result;
    }

    public int deleteUser(User user) {
        //xin quyen
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        //xoa
        int result = database.delete(
                TABLE_USERS,
                COLUMN_USERNAME+"=?",
                new String[] {user.getUsername()}
        );
        //dong ket noi db
        database.close();
        return result;
    }

    public User getUserByUsername(String username) {
        //xin quyen
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        //cau lenh select
        String selectUserByUsername =
                "SELECT * FROM "+ TABLE_USERS +" "
                +"WHERE "+COLUMN_USERNAME+"=?";
        //rawQuery
        Cursor cursor = database.rawQuery(
                selectUserByUsername,
                new String[] {username}
        );
        if (cursor.moveToFirst()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
            user.setFullname(cursor.getString(cursor.getColumnIndex(COLUMN_FULLNAME)));

            cursor.close();
            database.close();
            return user;
        }

        cursor.close();
        database.close();
        return null;
    }

    /**
     * Truy v???n trong db xem username n??y ???? t???n t???i ch??a.
     * @param username username c???n ki???m tra
     * @return true n???u username ???? t???n t???i trong db,
     * false n???u username kh??ng t???n t???i trong db.
     */
    public boolean isUsernameExist(String username) {
        username = username.trim();
        //xin quyen
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        //cau lenh select
        String selectQuery =
                "SELECT COUNT("+COLUMN_USERNAME+") " +
                "FROM "+TABLE_USERS+" " +
                "WHERE "+COLUMN_USERNAME+"=?";
        //rawQuery
        Cursor cursor = database.rawQuery(
                selectQuery,
                new String[] {username}
        );
        if (cursor.moveToFirst()) {
            int numberOfRecordHasThisUsername = cursor.getInt(0);
            if (numberOfRecordHasThisUsername > 0) {
                return true;
            }
            return false;
        }
        cursor.close();
        database.close();
        return false;
    }

    /**
     * Ki???m tra xem pass truy???n v??o c?? ????ng v???i pass ???? l??u c???a username n??y kh??ng.
     * @param username username c???n ki???m tra password
     * @param passwordFromForm password c???n ki???m tra
     * @return true n???u hai pass gi???ng nhau,
     * ng?????c l???i tr??? v??? false n???u hai pass kh??c nhau
     */
    public boolean isPasswordCorrect(String username, String passwordFromForm) {
        String passwordFromDatabase = "";
        passwordFromForm = passwordFromForm.trim();
        //xin quyen
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        //cau lenh select
        String selectPasswordByUsername =
                "SELECT "+COLUMN_PASSWORD+" "+
                "FROM "+TABLE_USERS+" "+
                "WHERE "+COLUMN_USERNAME+"=?";
        //rawQuery
        Cursor cursor = database.rawQuery(selectPasswordByUsername, new String[] {username});
        if (cursor.moveToFirst()) {
            passwordFromDatabase = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
        }
        cursor.close();
        database.close();
        if (passwordFromForm.equals(passwordFromDatabase)) {
            return true;
        }
        return false;
    }

    public String getFullnameByUsername(String username) {
        String fullname = "";
        //xin quyen
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        //cau lenh select
        String selectPasswordByUsername =
                "SELECT "+COLUMN_FULLNAME+" "+
                        "FROM "+TABLE_USERS+" "+
                        "WHERE "+COLUMN_USERNAME+"=?";
        //rawQuery
        Cursor cursor = database.rawQuery(selectPasswordByUsername, new String[] {username});
        if (cursor.moveToFirst()) {
            fullname = cursor.getString(cursor.getColumnIndex(COLUMN_FULLNAME));
        }
        cursor.close();
        database.close();
        return fullname;
    }

}
