package com.example.user.Service;

import com.example.user.DTOs.UserBookingNotiDTO;
import com.example.user.Model.Users;
import com.example.user.Repo.UserRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    JavaMailSender mailSender;

    public CompletionStage<Map<String,Users>> getUserById(Long userId) {
        Optional<Users> user = userRepo.findById(userId);
        Map<String,Users> res = new HashMap<>();
        if(user.isPresent() == true){
            res.put("user",user.get());
            return CompletableFuture.completedFuture(res);
        }
        else{
            res.put("user",null);
            return CompletableFuture.completedFuture(res);
        }
    }

    public CompletionStage<Map<String,Users>> getUserByEmail(String email) {
        Map<String,Users> res = new HashMap<>();
        if(validEmail(email)){
            Optional<Users> user = userRepo.findUserByEmail(email);
            res.put("user",user.get());
            return CompletableFuture.completedFuture(res);
        }
        else{
            res.put("user",null);
            return CompletableFuture.completedFuture(res);
        }
    }

    public boolean validEmail(String email) {
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return patternMatches(email, regexPattern);
    }

    public boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public void sendOTPEmail(Users customer, Long OTP) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("indrajitizian@gmail.com", "Hotel Support");
        helper.setTo(customer.getEmail());

        String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";

        String content = "<p>Hello " + customer.getFirstName() + "</p>"
                + "<p>For security reason, you're required to use the following "
                + "One Time Password to login:</p>"
                + "<p><b>" + OTP + "</b></p>"
                + "<br>"
                + "<p>Note: this OTP is set to expire in 5 minutes.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public void sendConfirmationEmail(UserBookingNotiDTO bookingData) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        Users user = userRepo.findById(bookingData.getUserId()).get();
        String email = user.getEmail();
        helper.setFrom("indrajitizian@gmail.com", "Hotel Support");
        helper.setTo(email);

        String subject = "Order Confirmed! Here we go....!";

        String content = "<p>Hello " + user.getFirstName() + " " + user.getLastName() + "</p>"
                + "<p>Your order is now confirmed! "
                + "Enjoy your holidays, Please find the details below:</p>"
                + "<p><b>Booking ID:" + bookingData.getOrderRefId() + "</b></p>"
                + "<p><b>Booking Order Date:" + bookingData.getOrderDate() + "</b></p>"
                + "<p><b>Hotel Name:" + bookingData.getHotelName() + "</b></p>"
                + "<p><b>Hotel Address:" + bookingData.getHotelAddress() + "</b></p>"
                + "<p><b>Room Name:" + bookingData.getRoomName() + "</b></p>"
                + "<p><b>Order Amount:" + bookingData.getOrderAmount() + "</b></p>"
                + "<br>"
                + "<p>Note: this OTP is set to expire in 5 minutes.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public CompletionStage<Map<String,String>> sendOtpToUserEmail(Users user) throws MessagingException, UnsupportedEncodingException {
        Optional<Users> existingUser = userRepo.findUserByEmail(user.getEmail());
        Map<String,String> res = new HashMap<String,String>();
        if(existingUser.isPresent() == false || !existingUser.get().isValidatedUser()){
            System.out.println(new Random().nextLong());
            Long OTP = 1000000L + (new Random().nextLong(100000L))%100000;
            System.out.println(OTP);
            user.setLastOtp(OTP);
            user.setLastRequestedOtpTimestamp(System.currentTimeMillis());
            sendOTPEmail(user, OTP);
            if(existingUser.isPresent()){
                existingUser.get().setLastOtp(OTP);
                existingUser.get().setLastRequestedOtpTimestamp(System.currentTimeMillis());
                userRepo.save(existingUser.get());
            }
            else
                userRepo.save(user);
            res.put("otpStatus","OTP Sent! to user: " + user.getEmail());
            res.put("otpValidationFlag","false");
            return CompletableFuture.completedFuture(res);
        }
        else{
            res.put("user_id", user.getId().toString());
            res.put("otpStatus","User Already validated!");
            res.put("otpValidationFlag","true");
        }
        return CompletableFuture.completedFuture(res);
    }

    public CompletionStage<Map<String,String>> validateOtpForUser(String email, String otp) {
        Optional<Users> existingUser = userRepo.findUserByEmail(email);
        Map<String,String> res = new HashMap<>();
        if(existingUser.isPresent()){
            Long validatingOtp = Long.parseLong(otp);
            Long timeWindow = 60 * 5L * 1000;
            Long currTimestamp = System.currentTimeMillis() - timeWindow;
            if((existingUser.get().getLastOtp().equals(validatingOtp)) && existingUser.get().getLastRequestedOtpTimestamp() >= currTimestamp ){

                existingUser.get().setValidatedUser(true);
                userRepo.save(existingUser.get());
                res.put("user_id",existingUser.get().getId().toString());
                res.put("validated", "true");
                res.put("status", "successful!");
            }
            else{
                res.put("user_id","-1");
                res.put("validated", "false");
                res.put("error_message", "otp_expired or the provided otp is incorrect!");
            }
        }
        else{
            res.put("user_id","-1");
            res.put("valid", "false");
            res.put("error_message", "User doesn't exist!");
        }
        return CompletableFuture.completedFuture(res);
    }

}
