package saneesh.moviefun.wordfinder.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WordData implements Parcelable
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

    //new changes
    @SerializedName("contents")
    ArrayList<String> contentsArray;


    public ArrayList<String> getContentsArray() {
        return contentsArray;
    }

    public void setContentsArray(ArrayList<String> contentsArray) {
        this.contentsArray = contentsArray;
    }

    public static Creator<WordData> getCREATOR() {
        return CREATOR;
    }

    protected WordData(Parcel in) {
        id = in.readString();
        data = in.readString();
        answer = in.readString();
        question = in.readString();
        flag = in.readString();
        answerCount = in.createStringArrayList();
        letterCount = in.readString();
        contentDatas = in.createTypedArrayList(WordData.CREATOR);
    }

    public static final Creator<WordData> CREATOR = new Creator<WordData>() {
        @Override
        public WordData createFromParcel(Parcel in) {
            return new WordData(in);
        }

        @Override
        public WordData[] newArray(int size) {
            return new WordData[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(data);
        parcel.writeString(answer);
        parcel.writeString(question);
        parcel.writeString(flag);
        parcel.writeStringList(answerCount);
        parcel.writeString(letterCount);
        parcel.writeTypedList(contentDatas);
    }
}
