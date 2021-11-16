package com.example.hrshouserentalsystem.Model;


import androidx.annotation.NonNull;

import org.parceler.Parcel;

//************************************************************
@Parcel
public class Selling
//************************************************************
{
    public String user_id,img_url,mainTitle,subTitle,sellingDescription,sellingType;
    public int roomCount,bathroomCount,washingCount;
    public double selling_price,rating;

    //************************************************************
    public Selling()
    //************************************************************
    {
    }

    //************************************************************
    public Selling(String user_id, String img_url, String mainTitle, String subTitle, String sellingDescription, String sellingType, int roomCount, int bathroomCount, int washingCount, double selling_price, double rating)
    //************************************************************
    {
        this.user_id = user_id;
        this.img_url = img_url;
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
        this.sellingDescription = sellingDescription;
        this.sellingType = sellingType;
        this.roomCount = roomCount;
        this.bathroomCount = bathroomCount;
        this.washingCount = washingCount;
        this.selling_price = selling_price;
        this.rating = rating;
    }

    //************************************************************
    public String getUser_id()
    //************************************************************
    {
        return user_id;
    }


    //************************************************************
    public String getImg_url()
    //************************************************************
    {
        return img_url;
    }


    //************************************************************
    public String getMainTitle()
    //************************************************************
    {
        return mainTitle;
    }


    //************************************************************
    public String getSubTitle()
    //************************************************************
    {
        return subTitle;
    }


    //************************************************************
    public String getSellingDescription()
    //************************************************************
    {
        return sellingDescription;
    }


    //************************************************************
    public String getSellingType()
    //************************************************************
    {
        return sellingType;
    }


    //************************************************************
    public int getRoomCount()
    //************************************************************
    {
        return roomCount;
    }


    //************************************************************
    public int getBathroomCount()
    //************************************************************
    {
        return bathroomCount;
    }


    //************************************************************
    public int getWashingCount()
    //************************************************************
    {
        return washingCount;
    }

    //************************************************************
    public double getSelling_price()
    //************************************************************
    {
        return selling_price;
    }

    //************************************************************
    public double getRating()
    //************************************************************
    {
        return rating;
    }

    @NonNull
    @Override
    public String toString() {
        return "Selling{" +
                "user_id='" + user_id + '\'' +
                ", img_url='" + img_url + '\'' +
                ", mainTitle='" + mainTitle + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", sellingDescription='" + sellingDescription + '\'' +
                ", sellingType='" + sellingType + '\'' +
                ", roomCount=" + roomCount +
                ", bathroomCount=" + bathroomCount +
                ", washingCount=" + washingCount +
                ", selling_price=" + selling_price +
                ", rating=" + rating +
                '}';
    }
}
