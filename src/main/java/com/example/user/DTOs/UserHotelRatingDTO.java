package com.example.user.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserHotelRatingDTO {
    float rating;
    Long hotelId;
    Long countOfRatings;
}
