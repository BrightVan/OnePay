/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2017, King, china
**                          All Rights Reserved
**                                
**                      By(King)
**                         
**------------------------------------------------------------------------------
*/
package com.pay.one.union;

import android.os.Parcel;
import android.os.Parcelable;

import com.pay.one.core.IPayEntity;

public class UnionPayEntity implements IPayEntity, Parcelable {

    public Mode mode;
    public String tn;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mode == null ? -1 : this.mode.ordinal());
        dest.writeString(this.tn);
    }

    public UnionPayEntity() {
    }

    protected UnionPayEntity(Parcel in) {
        int tmpMode = in.readInt();
        this.mode = tmpMode == -1 ? null : Mode.values()[tmpMode];
        this.tn = in.readString();
    }

    public static final Creator<UnionPayEntity> CREATOR = new Creator<UnionPayEntity>() {
        @Override
        public UnionPayEntity createFromParcel(Parcel source) {
            return new UnionPayEntity(source);
        }

        @Override
        public UnionPayEntity[] newArray(int size) {
            return new UnionPayEntity[size];
        }
    };
}
