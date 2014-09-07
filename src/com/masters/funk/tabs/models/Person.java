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
  private int id;

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
   * Bitmap of the user's photo.
   */
  private byte[] photo;

  public int getId() {
    return id;
  }

  public void setId(int id) {
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

  /**
   * Create an empty person (mainly for database)
   */
  public Person() {
    this.id = -1;
    this.name = "";
    this.catchup = 0;
    this.photo = null;
  }

  /**
   * Create a person with the following properties
   * @param id unique id for the database
   * @param name name of the person
   * @param catchup catch up setting for the person
   * @param photo photo of the person
   */
  public Person(int id, String name, int catchup, byte[] photo) {
    this.id = id;
    this.name = name;
    this.catchup = catchup;
    this.photo = photo;
  }

  public Person(Parcel in) {
    this.id = in.readInt();
    this.name = in.readString();
    this.catchup = in.readInt();
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
    dest.writeInt(id);
    dest.writeString(name);
    dest.writeInt(catchup);
    // cannot read a null byte array so store the byte array length
    // which we can check when we read the parcel
    if (photo == null) {
      dest.writeInt(0);
    } else {
      dest.writeInt(photo.length);
      dest.writeByteArray(photo);
    }
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
