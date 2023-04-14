package com.anf.core.models;

import java.util.List;
import java.util.Map;
import java.util.Date;

public interface NewsInfoModel {
    public String getTitle();
    public String getAuthor();
    public Date getCurrentDate();
    public String getDescription();
    public String getThumbnail();
}