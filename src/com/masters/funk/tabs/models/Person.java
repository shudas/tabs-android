package com.masters.funk.tabs.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Contains information about a single person.
 */
public class Person implements Parcelable {
  /**
   * Unique id of the person
   */
  private long id;

  /**
   * Person's name
   */
  private String name;

  /**
   * Catchup setting is an integer value indicating how frequently to push a notification
   * to remind the user to catch up with the person.
   */
  private int catchup;

  /**
   * The last time this person was updated. This can be used to sort the People page.
   */
  private long updateTime;

  /**
   * Bitmap of the user's photo.
   */
  private byte[] photo;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCatchup() {
    return catchup;
  }

  public void setCatchup(int catchup) {
    this.catchup = catchup;
  }

  public byte[] getPhoto() {
    return photo;
  }

  public void setPhoto(byte[] photo) {
    this.photo = photo;
  }

  public long getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(long updateTime) {
    this.updateTime = updateTime;
  }


  /**
   * Create an empty person (mainly for database)
   */
  public Person() {
    this.id = 0L;
    this.name = "";
    this.catchup = 0;
    this.photo = null;
    this.updateTime = System.currentTimeMillis();
  }

  /**
   * Create a person with the following properties
   * @param id unique id for the database
   * @param name name of the person
   * @param catchup catch up setting for the person
   * @param photo photo of the person
   */
  public Person(long id, String name, int catchup, byte[] photo, long updateTime) {
    this.id = id;
    this.name = name;
    this.catchup = catchup;
    this.photo = photo;
    this.updateTime = updateTime;
  }

  public Person(Parcel in) {
    this.id = in.readLong();
    this.name = in.readString();
    this.catchup = in.readInt();
    this.updateTime = in.readLong();
    if (in.readInt() != 0) {
      in.readByteArray(photo);
    } else {
      photo = null;
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(id);
    dest.writeString(name);
    dest.writeInt(catchup);
    dest.writeLong(updateTime);
    // cannot read a null byte array so store the byte array length
    // which we can check when we read the parcel
    if (photo == null) {
      dest.writeInt(0);
    } else {
      dest.writeInt(photo.length);
      dest.writeByteArray(photo);
    }
  }

  @Override
  public String toString() {
    return "Name: " + name + " ID: " + id + " Catchup: " + catchup + " Update Time: " + updateTime;
  }

  @SuppressWarnings("unchecked")
  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    public Person createFromParcel(Parcel in)
    {
      return new Person(in);
    }

    public Person[] newArray(int size)
    {
      return new Person[size];
    }
  };
}
