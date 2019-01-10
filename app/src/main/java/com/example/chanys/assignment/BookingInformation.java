package com.example.chanys.assignment;

public class BookingInformation {
    String BookingID;
    String Username;
    String CourtName;
    String BookingDay;
    String BookingMonth;
    String BookingYear;
    String StartTime;
    String EndTime;

    public BookingInformation(){
    }

    public BookingInformation(String bookingID, String username, String courtName, String bookingDay, String bookingMonth, String bookingYear, String startTime, String endTime) {
        BookingID = bookingID;
        Username = username;
        CourtName = courtName;
        BookingDay = bookingDay;
        BookingMonth = bookingMonth;
        BookingYear = bookingYear;
        StartTime = startTime;
        EndTime = endTime;
    }

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getCourtName() {
        return CourtName;
    }

    public void setCourtName(String courtName) {
        CourtName = courtName;
    }

    public String getBookingDay() {
        return BookingDay;
    }

    public void setBookingDay(String bookingDay) {
        BookingDay = bookingDay;
    }

    public String getBookingMonth() {
        return BookingMonth;
    }

    public void setBookingMonth(String bookingMonth) {
        BookingMonth = bookingMonth;
    }

    public String getBookingYear() {
        return BookingYear;
    }

    public void setBookingYear(String bookingYear) {
        BookingYear = bookingYear;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }
}
