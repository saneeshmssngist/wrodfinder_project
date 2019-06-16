package saneesh.moviefun.worldfinder.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Coordinate implements Parcelable
{

    int XCord;

    int YCord;

    public Coordinate() {
    }

    public int getXCord() {
        return XCord;
    }

    public void setXCord(int XCord) {
        this.XCord = XCord;
    }

    public int getYCord() {
        return YCord;
    }

    public void setYCord(int YCord) {
        this.YCord = YCord;
    }

    public static Creator<Coordinate> getCREATOR() {
        return CREATOR;
    }

    protected Coordinate(Parcel in) {
    }

    public static final Creator<Coordinate> CREATOR = new Creator<Coordinate>() {
        @Override
        public Coordinate createFromParcel(Parcel in) {
            return new Coordinate(in);
        }

        @Override
        public Coordinate[] newArray(int size) {
            return new Coordinate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
