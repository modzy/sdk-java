package com.modzy.sdk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ModelSearchParams extends Pagination{

    protected String modelId;

    protected String author;

    protected String createdByEmail;

    protected String name;

    protected String description;

    @JsonProperty("isActive")
    protected Boolean active;

    @JsonProperty("isExpired")
    protected Boolean expired;

    @JsonProperty("isRecommended")
    protected Boolean recommended;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    protected Date lastActiveDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    protected Date expirationDateTime;

}
