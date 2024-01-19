package com.example.user.Controller;

import com.example.user.DTOs.UserBookingNotiDTO;
import com.example.user.Model.Users;
import com.example.user.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController
{
    @Autowired
    private UserService userService;

    @GetMapping("/_search/{userId}")
    public CompletionStage<ResponseEntity<Map<String,Users>>> getUserById(@PathVariable Long userId){
        return userService.getUserById(userId).thenApplyAsync(res -> new ResponseEntity<Map<String,Users>>(res, HttpStatus.OK));
    }

    @PostMapping("/_search")
    public CompletionStage<ResponseEntity<Map<String,Users>>> getUserByEmail(@RequestBody Map<String,String> email){
        return userService.getUserByEmail(email.get("email")).thenApplyAsync(res -> new ResponseEntity<Map<String,Users>>(res, HttpStatus.OK));
    }

    @PostMapping("/sendOtp")
    public CompletionStage<ResponseEntity<Map<String,String>>> sendOtpToUserEmail(@RequestBody Users user) throws MessagingException, UnsupportedEncodingException {
        return userService.sendOtpToUserEmail(user).thenApplyAsync(res->new ResponseEntity<Map<String,String>>(res, HttpStatus.OK));
    }

    @PostMapping("/validateOtp")
    public CompletionStage<ResponseEntity<Map<String,String>>> validateOtpForAnEmail(@RequestBody Map<String,String> user){
        return userService.validateOtpForUser(user.get("email"), user.get("otp")).thenApplyAsync(res->new ResponseEntity<Map<String,String>>(res, HttpStatus.OK));
    }

    @PostMapping("/bookings/confirmation")
    @RabbitListener(queues = {"q.user-notification_q"})
    public void sendConfirmedStatusEmail(@RequestBody String updateMessage) throws JsonProcessingException, MessagingException, UnsupportedEncodingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        UserBookingNotiDTO statusUpdate = mapper.readValue(updateMessage, UserBookingNotiDTO.class);
        System.out.println(statusUpdate);
        userService.sendConfirmationEmail(statusUpdate);
        Map<String, String> res = new HashMap<>();
        res.put("data", updateMessage);
    }
}
