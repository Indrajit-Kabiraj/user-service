package com.example.user.Service;

import com.example.user.DTOs.ReviewCreateDTO;
import com.example.user.DTOs.UserHotelRatingDTO;
import com.example.user.Model.Ratings;
import com.example.user.Repo.RatingRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
@RequiredArgsConstructor
public class RatingService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RatingRepo ratingRepo;
    public CompletionStage<Map<String, List<Ratings>>> getRatings(Long hotelId, Long roomCategoryId) {
        List<Ratings> ratings = new ArrayList<>();
        if(roomCategoryId!=null){
            ratings = ratingRepo.findRatingsByHotelAndRcId(hotelId, roomCategoryId);
        }
        else{
            ratings = ratingRepo.findRatingsByHotelId(hotelId);
        }
        HashMap<String, List<Ratings>> res = new HashMap<>();
        res.put("ratings", ratings);
        return CompletableFuture.completedFuture(res);
    }

    public CompletionStage<Map<String, Object>> createReview(Ratings newRating) throws Exception {
        try{
            newRating.setRatingStatus("ACTIVE");
            ratingRepo.save(newRating);
            Map<String, Object> resp = new HashMap<>();
            Long countOfRatings = ratingRepo.findRatingsCountByHotelId(newRating.getHotelId());
            UserHotelRatingDTO hotelRatingDTO = new UserHotelRatingDTO(newRating.getRating(), newRating.getHotelId(), countOfRatings);
            ObjectMapper objectMapper = new ObjectMapper();
            String queuePayloadString = objectMapper.writeValueAsString(hotelRatingDTO);
            rabbitTemplate.convertAndSend("","q.hotel-review",queuePayloadString);
            resp.put("data", newRating);
            return CompletableFuture.completedFuture(resp);
        } catch (Exception e){
            throw new Exception();
        }
    }

    public CompletionStage<Map<String, Object>> editReview(Ratings rating, Long id) throws Exception {
        Optional<Ratings> existingRating = ratingRepo.findById(id);
        if(existingRating.isPresent()){
            existingRating = updateRating(existingRating, rating);
            ratingRepo.save(existingRating.get());
            Map<String, Object> resp = new HashMap<>();
            resp.put("updated_review", existingRating.get());
            return CompletableFuture.completedFuture(resp);
        }
        else{
            throw new Exception("Review can't be edited!");
        }
    }

    private Optional<Ratings> updateRating(Optional<Ratings> existingRating, Ratings rating) {
        if(rating.getRating() > 0.0) existingRating.get().setRating(rating.getRating());
        if(rating.getHotelId()!=null) existingRating.get().setHotelId(rating.getHotelId());
        if(rating.getRoomCategoryId()!=null) existingRating.get().setRoomCategoryId(rating.getRoomCategoryId());
        if(rating.getReview()!=null) existingRating.get().setReview(rating.getReview());
        LocalDateTime currTime = LocalDateTime.now();
        existingRating.get().setReviewDate(currTime);
        return existingRating;
    }

    public CompletionStage<Map<String, Object>> deleteReview(Long id) throws Exception {
        Optional<Ratings> existingRating = ratingRepo.findById(id);
        if(existingRating.isPresent()){
            existingRating.get().setRatingStatus("ARCHIVED");
            ratingRepo.save(existingRating.get());
            Map<String, Object> res = new HashMap<>();
            res.put("status","ARCHIVED");
            return CompletableFuture.completedFuture(res);
        }
        else{
            throw new Exception("Rating not found!");
        }
    }
}
