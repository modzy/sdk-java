package com.modzy.sdk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ModelSearchParams extends Pagination{

    protected String modelId;

    protected String author;

    protected String createdByEmail;

    protected String name;

    protected String description;

    protected Boolean isActive;

    protected Boolean isExpired;

    protected Boolean isRecommended;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    protected Date lastActiveDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    protected Date expirationDateTime;

    public ModelSearchParams(){
        super();
        this.perPage = 1000;
    }

    public ModelSearchParams(
            String modelId, String author, String createdByEmail, String name,
            String description, Boolean isActive, Boolean isExpired, Boolean isRecommended,
            Date lastActiveDateTime, Date expirationDateTime,
            Integer page, Integer perPage, String sortBy, String direction
        ) {
        this();
        this.modelId = modelId;
        this.author = author;
        this.createdByEmail = createdByEmail;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.isExpired = isExpired;
        this.isRecommended = isRecommended;
        this.lastActiveDateTime = lastActiveDateTime;
        this.expirationDateTime = expirationDateTime;
        this.page = page;
        this.perPage = perPage;
        this.sortBy = sortBy;
        this.direction = direction;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreatedByEmail() {
        return createdByEmail;
    }

    public void setCreatedByEmail(String createdByEmail) {
        this.createdByEmail = createdByEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }

    public Boolean getRecommended() {
        return isRecommended;
    }

    public void setRecommended(Boolean recommended) {
        isRecommended = recommended;
    }

    public Date getLastActiveDateTime() {
        return lastActiveDateTime;
    }

    public void setLastActiveDateTime(Date lastActiveDateTime) {
        this.lastActiveDateTime = lastActiveDateTime;
    }

    public Date getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(Date expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }
}
