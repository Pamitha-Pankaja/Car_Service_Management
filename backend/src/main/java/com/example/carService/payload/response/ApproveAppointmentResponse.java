package com.example.carService.payload.response;


public class ApproveAppointmentResponse {
    private Long appointmentId;
    private String status ;

    public ApproveAppointmentResponse() {
    }

    public ApproveAppointmentResponse(Long appointmentId, String status) {
        this.appointmentId = appointmentId;
        this.status = status;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
