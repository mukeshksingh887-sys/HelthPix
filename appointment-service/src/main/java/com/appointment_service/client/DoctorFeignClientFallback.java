package com.appointment_service.client;



import com.appointment_service.dto.DoctorResponse;
import com.appointment_service.exception.ServiceUnavailableException;
import org.springframework.stereotype.Component;

@Component
public class DoctorFeignClientFallback implements DoctorServiceClient {
//    @Override
//    public DoctorResponse  getDoctorByEmail(String email) {
//        throw new ServiceUnavailableException("DOCTOR - SERVICE is unavailable");
//    }


    @Override
    public DoctorResponse getDoctorById(Long id) {
        throw  new ServiceUnavailableException("DOCTOR - SERVICE is unavailable");
    }



}
