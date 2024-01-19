package com.example.user.DTOs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBookingNotiDTO {
    String hotelName;
    String roomName;
    String hotelAddress;
    Long userId;
    LocalDate orderDate;
    LocalDate orderStartDate;
    LocalDate orderEndDate;
    String orderRefId;
    float orderAmount;
    List<String> allUsers;
    String status;

    public UserBookingNotiDTO(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        UserBookingNotiDTO userBookingNotiDTO = mapper.readValue(message, UserBookingNotiDTO.class);
        this.hotelName = userBookingNotiDTO.getHotelName();
        this.roomName = userBookingNotiDTO.getRoomName();
        this.hotelAddress = userBookingNotiDTO.getHotelAddress();
        this.userId = userBookingNotiDTO.getUserId();
        this.orderDate = userBookingNotiDTO.getOrderDate();
        this.orderStartDate = userBookingNotiDTO.getOrderStartDate();
        this.orderEndDate = userBookingNotiDTO.getOrderEndDate();
        this.orderRefId = userBookingNotiDTO.getOrderRefId();
        this.orderAmount = userBookingNotiDTO.getOrderAmount();
        this.allUsers = userBookingNotiDTO.getAllUsers();
        this.status = userBookingNotiDTO.getStatus();
    }

    @Override
    public String toString() {
        return "UserBookingNotiDTO{" +
                "hotelName='" + hotelName + '\'' +
                ", roomName='" + roomName + '\'' +
                ", hotelAddress='" + hotelAddress + '\'' +
                ", userId=" + userId +
                ", orderDate=" + orderDate +
                ", orderStartDate=" + orderStartDate +
                ", orderEndDate=" + orderEndDate +
                ", orderRefId='" + orderRefId + '\'' +
                ", orderAmount=" + orderAmount +
                ", allUsers=" + allUsers +
                ", status='" + status + '\'' +
                '}';
    }
}
