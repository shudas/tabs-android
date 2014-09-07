package com.masters.funk.tabs.models;

/**
 * Contains information about a single tab
 */
public class Tab {
  /**
   * Unique id of the tag
   */
  private long id;

  /**
   * Main content of the tag
   */
  private String text;

  /**
   * The type of tabIcon used for this tab.
   */
  private String tabIcon;

  /**
   * The last time the tab was updated
   */
  private long updateTimeMillis;

  /**
   * the id of the person whose information is tied to this tab.
   */
  private long personId;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getTabIcon() {
    return tabIcon;
  }

  public void setTabIcon(String tabIcon) {
    this.tabIcon = tabIcon;
  }

  public long getUpdateTimeMillis() {
    return updateTimeMillis;
  }

  public void setUpdateTimeMillis(long updateTimeMillis) {
    this.updateTimeMillis = updateTimeMillis;
  }

  public long getPersonId() {
    return personId;
  }

  public void setPersonId(long personId) {
    this.personId = personId;
  }

  public Tab() {
    setValues(-1, "", "", 0, 0);
  }

  /**
   * Create a tab with the following properties
   * @param id unique id for the database
   * @param text main text content of the tab
   * @param tabIcon the name of the tabIcon used
   * @param updateTimeMillis the last time this tab was updated
   * @param personId the unique id of the person who is tied to this tab
   */
  public Tab(long id, String text, String tabIcon, long updateTimeMillis, long personId) {
    setValues(id, text, tabIcon, updateTimeMillis, personId);
  }

  public void setValues(long id, String text, String tabIcon, long updateTimeMillis, long personId) {
    this.id = id;
    this.text = text;
    this.tabIcon = tabIcon;
    this.updateTimeMillis = updateTimeMillis;
    this.personId = personId;
  }
}
