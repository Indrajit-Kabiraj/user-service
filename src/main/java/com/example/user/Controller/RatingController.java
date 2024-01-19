package com.example.user.Controller;

import com.example.user.DTOs.ReviewCreateDTO;
import com.example.user.Model.Ratings;
import com.example.user.Service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/v1/ratings/")
@RequiredArgsConstructor
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping("/getRatings")
    public CompletionStage<ResponseEntity<Map<String, List<Ratings>>>> getComments(@RequestParam(required = true) Long hotelId, @RequestParam(required = false) Long roomCategoryId) throws Exception {
        try{
            return ratingService.getRatings(hotelId, roomCategoryId).thenApplyAsync(res -> new ResponseEntity<Map<String, List<Ratings>>>(res, HttpStatus.OK));
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    
    @PostMapping("/_create")
    public CompletionStage<ResponseEntity<Map<String, Object>>> createReview(@RequestBody Ratings newRating){
        try{
            return ratingService.createReview(newRating).thenApplyAsync(res-> new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK));
        } catch (Exception e){
            Map<String, Object> res = new HashMap<>();
            res.put("error_message", e.getMessage());
            res.put("status", "FAILED");
            return CompletableFuture.completedFuture( new ResponseEntity<Map<String, Object>>(res, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PatchMapping("/edit/{id}")
    public CompletionStage<ResponseEntity<Map<String, Object>>> editReview(@RequestBody Ratings rating, @PathVariable long id){
        try{
            return ratingService.editReview(rating, id).thenApplyAsync(res-> new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK));
        } catch (Exception e){
            Map<String, Object> res = new HashMap<>();
            res.put("error_message", e.getMessage());
            res.put("status", "FAILED");
            return CompletableFuture.completedFuture( new ResponseEntity<Map<String, Object>>(res, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @DeleteMapping("/_delete/{id}")
    public CompletionStage<ResponseEntity<Map<String, Object>>> deleteReview(@PathVariable Long id){
        try{
            return ratingService.deleteReview(id).thenApplyAsync(res-> new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK));
        } catch (Exception e){
            Map<String, Object> res = new HashMap<>();
            res.put("error_message", e.getMessage());
            res.put("status", "FAILED");
            return CompletableFuture.completedFuture( new ResponseEntity<Map<String, Object>>(res, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
