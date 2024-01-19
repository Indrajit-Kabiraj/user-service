package com.example.user.Repo;

import com.example.user.Model.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepo extends JpaRepository<Ratings, Long> {

    @Query("SELECT r from Ratings r WHERE r.hotelId = ?1 AND r.roomCategoryId = ?2 ORDER BY r.rating DESC")
    List<Ratings> findRatingsByHotelAndRcId(Long hotelId, Long roomCategoryId);
    @Query("SELECT count(r.id) from Ratings r WHERE r.hotelId = ?1 AND r.ratingStatus <> 'ARCHIVED'")
    Long findRatingsCountByHotelId(Long hotelId);

    @Query("SELECT r from Ratings r WHERE r.hotelId = ?1 ORDER BY r.rating DESC")
    List<Ratings> findRatingsByHotelId(Long hotelId);
}
