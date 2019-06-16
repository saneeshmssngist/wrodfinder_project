package saneesh.moviefun.worldfinder.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WordData
{

    String id;

    String data;

    String answer;

    String question;

    String flag;

    @SerializedName("answer_count")
    ArrayList<String> answerCount;

    @SerializedName("letter_count")
    String letterCount;

    @SerializedName("content_datas")
    ArrayList<WordData> contentDatas;

    public String getLetterCount() {
        return letterCount;
    }

    public void setLetterCount(String letterCount) {
        this.letterCount = letterCount;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(ArrayList<String> answerCount) {
        this.answerCount = answerCount;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ArrayList<WordData> getContentDatas() {
        return contentDatas;
    }

    public void setContentDatas(ArrayList<WordData> contentDatas) {
        this.contentDatas = contentDatas;
    }
}
