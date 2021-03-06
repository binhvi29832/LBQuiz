package vn.poly.pro1121.loanltmph07248_binhvttph07052.lbquiz.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.poly.pro1121.loanltmph07248_binhvttph07052.lbquiz.database.DatabaseHelper;
import vn.poly.pro1121.loanltmph07248_binhvttph07052.lbquiz.model.QuizResult;

public class QuizResultDao {
    public static final String TABLE_QUIZ_RESULTS = "QuizResults";

    public static final String COLUMN_QUIZ_RESULTS_QUIZ_ID = "quizId";
    public static final String COLUMN_QUIZ_RESULTS_UPPER_LIMIT = "upperLimit";
    public static final String COLUMN_QUIZ_RESULTS_LOWER_LIMIT = "lowerLimit";
    public static final String COLUMN_QUIZ_RESULTS_TITLE = "title";
    public static final String COLUMN_QUIZ_RESULTS_DESCRIPTION = "description";

    DatabaseHelper databaseHelper;

    public QuizResultDao(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public List<QuizResult> getQuizResultsByQuizId(int quizId) {
        List<QuizResult> listResultsOfThisQuiz = new ArrayList<>();
        //xin quyen
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        //cau lenh select
        String selectQuery = "SELECT * FROM "+TABLE_QUIZ_RESULTS+" WHERE "+COLUMN_QUIZ_RESULTS_QUIZ_ID+"=?";
        //rawQuery
        Cursor cursor = database.rawQuery(
                selectQuery,
                new String[] {String.valueOf(quizId)}
                );
        if (cursor.moveToFirst()) {
            do {
                QuizResult quizResult = new QuizResult();
                quizResult.setQuizId(cursor.getInt(cursor.getColumnIndex(COLUMN_QUIZ_RESULTS_QUIZ_ID)));
                quizResult.setUpperLimit(cursor.getDouble(cursor.getColumnIndex(COLUMN_QUIZ_RESULTS_UPPER_LIMIT)));
                quizResult.setLowerLimit(cursor.getDouble(cursor.getColumnIndex(COLUMN_QUIZ_RESULTS_LOWER_LIMIT)));
                quizResult.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_RESULTS_TITLE)));
                quizResult.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_RESULTS_DESCRIPTION)));

                listResultsOfThisQuiz.add(quizResult);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return listResultsOfThisQuiz;
    }

    /**
     * L???y danh s??ch k???t qu???, so s??nh xem ??i???m ng?????i ch??i ?????t ???????c n???m trong
     * kho???ng c???a ????p ??n n??o th?? tr??? v??? ?????i t?????ng ????p ??n ????.
     * @param quizId B??i tr???c nghi???m c???n l???y ????p ??n
     * @param score ??i???m ng?????i ch??i ?????t ???????c
     * @return QuizResult ?????i t?????ng k???t qu??? c???a tr???c nghi???m
     */
    public QuizResult getResultByQuizIdAndScore(int quizId, double score) {
        double lowerLimit, upperLimit;
        List<QuizResult> resultList = getQuizResultsByQuizId(quizId);

        for (int i = 0; i<resultList.size(); i++) {
            QuizResult quizResult = resultList.get(i);
            lowerLimit = quizResult.getLowerLimit();
            upperLimit = quizResult.getUpperLimit();
            if (score >= lowerLimit && score <= upperLimit) {
                return quizResult;
            }
        }
        //Ch???y h???t v??ng for m?? kh??ng th???y th?? tr??? v??? null
        return null;
    }
}
