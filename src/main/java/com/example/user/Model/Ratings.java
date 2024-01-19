package com.example.user.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
@Entity
public class Ratings {
    @Id
    @SequenceGenerator(name="idSequenceUsers", sequenceName="ID_SEQUENCE_RATINGS", allocationSize=1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idSequenceRatings")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "hotel_id")
    private Long hotelId;
    @Column(name = "room_category_id")
    private Long roomCategoryId;
    @Column(name = "name")
    private String name;
    @Column(name = "rating")
    private float rating;
    @Column(name = "review")
    private String review;
    @Column(name = "image_location")
    private String imageLocation;
    @Column(name = "review_date")
    LocalDateTime reviewDate;
    @Column(name = "rating_status")
    String ratingStatus;

}
