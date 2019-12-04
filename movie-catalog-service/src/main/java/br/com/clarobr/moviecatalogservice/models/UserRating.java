package br.com.clarobr.moviecatalogservice.models;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class UserRating {

    private String userId;
    
    private List<Rating> ratings;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Rating> getRatings() {
    	return new ArrayList<Rating>(ratings);
    }

    public void setRatings(List<Rating> ratings) {
    	List<Rating> clone = new ArrayList<>(ratings);
        this.ratings = clone;
    }
    
    public void initData(String userId) {
        this.setUserId(userId);
        this.setRatings(Arrays.asList(
                new Rating("1234", 3),
                new Rating("5678", 4)
        ));
    }

}
