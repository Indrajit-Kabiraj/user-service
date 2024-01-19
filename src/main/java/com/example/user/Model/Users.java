package com.example.user.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@AllArgsConstructor
public class Users {
    @Id
    @SequenceGenerator(name="idSequenceUsers", sequenceName="ID_SEQUENCE_USERS", allocationSize=1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idSequenceUsers")
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_created_at")
    private LocalDate userCreatedAt;

    private String Address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_validated_user")
    private boolean isValidatedUser;

    @Column(name = "last_otp")
    private Long lastOtp;

    @Column(name = "last_requested_otp_time")
    private Long lastRequestedOtpTimestamp;

    @Column(name = "last_login")
    private LocalDate lastLogin;

    public Users(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getUserCreatedAt() {
        return userCreatedAt;
    }

    public void setUserCreatedAt(LocalDate userCreatedAt) {
        this.userCreatedAt = userCreatedAt;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isValidatedUser() {
        return isValidatedUser;
    }

    public void setValidatedUser(boolean validatedUser) {
        isValidatedUser = validatedUser;
    }

    public Long getLastRequestedOtpTimestamp() {
        return lastRequestedOtpTimestamp;
    }

    public void setLastRequestedOtpTimestamp(Long lastRequestedOtpTimestamp) {
        this.lastRequestedOtpTimestamp = lastRequestedOtpTimestamp;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Long getLastOtp() {
        return lastOtp;
    }

    public void setLastOtp(Long lastOtp) {
        this.lastOtp = lastOtp;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userCreatedAt=" + userCreatedAt +
                ", Address='" + Address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isValidatedUser=" + isValidatedUser +
                ", lastRequestedOtpTimestamp=" + lastRequestedOtpTimestamp +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
