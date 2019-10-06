package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountAverageRatingResponse {
    @SerializedName("total_reviews")
    @Expose
    private Integer totalReviews;

    @SerializedName("average_rating")
    @Expose
    private Double averageRating;

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public Double getAverageRating() {
        return averageRating;
    }
}
