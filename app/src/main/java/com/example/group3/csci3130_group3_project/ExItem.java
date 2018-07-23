package com.example.group3.csci3130_group3_project;

public class ExItem {
    private int mImaSource;
    private String mText;

    public ExItem()
    {

    }
    public ExItem(int mImaSource,String mText)
    {
        this.mImaSource=mImaSource;
        this.mText=mText;
    }
    public int getmImaSource() {
        return mImaSource;
    }

    public String getmText() {
        return mText;
    }
}