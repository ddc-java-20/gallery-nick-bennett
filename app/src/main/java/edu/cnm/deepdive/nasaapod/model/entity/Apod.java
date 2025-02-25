package edu.cnm.deepdive.nasaapod.model.entity;

import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.net.URL;
import java.time.LocalDate;

public class Apod {

  @Expose
  @NonNull
  private String title;

  @Expose
  @SerializedName("explanation")
  private String description;

  @Expose
  @NonNull
  private LocalDate date;

  @Expose
  private String copyright;

  @Expose
  @SerializedName("media_type")
  @NonNull
  private MediaType mediaType;

  @Expose
  @SerializedName("url")
  @NonNull
  private URL lowDefUrl;

  @Expose
  @SerializedName("hdurl")
  private URL highDefUrl;

  @NonNull
  public String getTitle() {
    return title;
  }

  public void setTitle(@NonNull String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @NonNull
  public LocalDate getDate() {
    return date;
  }

  public void setDate(@NonNull LocalDate date) {
    this.date = date;
  }

  public String getCopyright() {
    return copyright;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  @NonNull
  public MediaType getMediaType() {
    return mediaType;
  }

  public void setMediaType(@NonNull MediaType mediaType) {
    this.mediaType = mediaType;
  }

  @NonNull
  public URL getLowDefUrl() {
    return lowDefUrl;
  }

  public void setLowDefUrl(@NonNull URL lowDefUrl) {
    this.lowDefUrl = lowDefUrl;
  }

  public URL getHighDefUrl() {
    return highDefUrl;
  }

  public void setHighDefUrl(URL highDefUrl) {
    this.highDefUrl = highDefUrl;
  }

  public enum MediaType {
    @SerializedName("image")
    IMAGE,
    @SerializedName("video")
    VIDEO
  }

}
