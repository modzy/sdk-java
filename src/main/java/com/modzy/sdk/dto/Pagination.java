package com.modzy.sdk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Pagination {

    protected Integer page;

    @JsonProperty("per-page")
    protected Integer perPage = 1000;

    @JsonProperty("sort-by")
    protected String sortBy;

    protected String direction;

}
