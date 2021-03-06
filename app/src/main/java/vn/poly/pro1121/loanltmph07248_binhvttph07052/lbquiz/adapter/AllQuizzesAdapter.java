package vn.poly.pro1121.loanltmph07248_binhvttph07052.lbquiz.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import vn.poly.pro1121.loanltmph07248_binhvttph07052.lbquiz.FunctionLibrary;
import vn.poly.pro1121.loanltmph07248_binhvttph07052.lbquiz.R;
import vn.poly.pro1121.loanltmph07248_binhvttph07052.lbquiz.activity.MainActivity;
import vn.poly.pro1121.loanltmph07248_binhvttph07052.lbquiz.dao.QuizDao;
import vn.poly.pro1121.loanltmph07248_binhvttph07052.lbquiz.model.Quiz;
import vn.poly.pro1121.loanltmph07248_binhvttph07052.lbquiz.model.QuizSaveStatus;

import static vn.poly.pro1121.loanltmph07248_binhvttph07052.lbquiz.PublicVariableLibrary.STATUS_BOOKMARKED;
import static vn.poly.pro1121.loanltmph07248_binhvttph07052.lbquiz.PublicVariableLibrary.STATUS_UNBOOKMARKED;

public class AllQuizzesAdapter extends ArrayAdapter<Quiz> {
    FunctionLibrary functionLibrary;
    Activity context;
    int resource;

    //database
    QuizDao quizDao;

    //view
    ImageButton imageButtonBookmarkQuiz;

    //debug
    private static final String TAG = "AllQuizAdapterLog";

    public AllQuizzesAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        functionLibrary = new FunctionLibrary();
        quizDao = new QuizDao(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final int pos = position; //de su dung trong ham xu ly su kien
        int numberOfWordsWantRetrieve; //number of words of description displays
        LayoutInflater inflater = this.context.getLayoutInflater();
        View customView = inflater.inflate(resource, null);

        //lay view
        ImageView imgQuiz;
        TextView tvQuizTitle;
        TextView tvQuizDescription;

        imgQuiz = (ImageView) customView.findViewById(R.id.imgQuiz);
        tvQuizTitle = (TextView) customView.findViewById(R.id.tvQuizTitle);
        tvQuizDescription = (TextView) customView.findViewById(R.id.tvQuizDescription);
        imageButtonBookmarkQuiz = (ImageButton) customView.findViewById(R.id.imageButtonBookmarkQuiz);

        //set thong tin tu doi tuong len view
        Quiz quiz = getItem(position);
        imgQuiz.setImageBitmap(quiz.getImage());
        tvQuizTitle.setText(quiz.getName());

        String description = quiz.getDescription();
        numberOfWordsWantRetrieve = 20;
        //N???u m?? t??? > 25 t??? th?? ch??? hi???n th??? 25 t??? ?????u ti??n, ng?????c l???i hi???n th??? nh?? b??nh th?????ng
        if (functionLibrary.countWords(description) > numberOfWordsWantRetrieve) {
            //th?? c???t chu???i r???i th??m ...v??o
            String subStringOfDescription = functionLibrary.getFirstXWordsOfString(
                        description,
                        numberOfWordsWantRetrieve
                    );

            tvQuizDescription.setText(
                    String.format("%s...", subStringOfDescription)
            );
        } else {
            tvQuizDescription.setText(description);
        }

        changeImageDependOnBookmarkStatus(quiz);

        imageButtonBookmarkQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkOrUnbookmarkQuiz(getItem(pos), pos);
            }
        });


        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).napQuizDetailFragment(getItem(pos));
            }
        });

        return customView;
    }

    /**
     * N???u quiz ch??a ???????c ????nh d???u, chuy???n tr???ng th??i th??nh ???? ????nh d???u,
     * ng?????c l???i n???u quiz ???? ????nh d???u th?? chuy???n tr???ng th??i th??nh ch??a ????nh ?????u,
     * chuy???n h??nh ???nh minh h???a tr???ng th??i t????ng ???ng.
     */
    private void bookmarkOrUnbookmarkQuiz(Quiz quiz, int positionClicked) {
        QuizSaveStatus quizSaveStatus = quiz.getSaveStatus();
        if (quizSaveStatus.getId() == STATUS_UNBOOKMARKED) {
            //chuyen trang thai thanh bookmarked va chuyen hinh anh
            int result = quizDao.updateQuizBookmarkStatus(quiz, STATUS_BOOKMARKED);
            if (result > 0) {
                Toast.makeText(
                        context,
                        R.string.bookmarked,
                        Toast.LENGTH_SHORT
                ).show();
                refreshAdapter();
            }
        } else if (quizSaveStatus.getId() == STATUS_BOOKMARKED) {
            //chuyen trang thai thanh unbookmarked va chuyen hinh anh
            int result = quizDao.updateQuizBookmarkStatus(quiz, STATUS_UNBOOKMARKED);
            if (result > 0) {
                Toast.makeText(
                        context,
                        R.string.removed_bookmark,
                        Toast.LENGTH_SHORT
                ).show();
                refreshAdapter();
            }
        }
    }

    private void refreshAdapter() {
        clear();
        addAll(quizDao.getQuizOrderByNewestDateAddedToOldest());
        notifyDataSetChanged();
    }

    void changeImageDependOnBookmarkStatus(Quiz quiz) {
        if (quiz.getSaveStatus().getId() == STATUS_BOOKMARKED) {
            imageButtonBookmarkQuiz.setImageResource(R.drawable.ic_bookmark_black);
        } else if (quiz.getSaveStatus().getId() == STATUS_UNBOOKMARKED) {
            imageButtonBookmarkQuiz.setImageResource(R.drawable.ic_bookmark_white);
        }
    }
}
