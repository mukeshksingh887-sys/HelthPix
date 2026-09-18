package com.Patient_service.service;

import com.Patient_service.client.UserServiceClient;
import com.Patient_service.dto.PatientRequest;
import com.Patient_service.dto.PatientResponse;
import com.Patient_service.dto.UserResponse;
import com.Patient_service.entity.Patient;
import com.Patient_service.entity.UserType;
import com.Patient_service.exception.DeleteFaildException;
import com.Patient_service.exception.PatientNotFoundException;
import com.Patient_service.exception.UserNotFounException;
import com.Patient_service.repository.PatientRepository;
import com.Patient_service.entity.Gender;
import com.Patient_service.entity.PatientStatus;
import com.Patient_service.util.Util;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service

@Slf4j
public class PatientServiceImpl implements PatientService{

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private Util util;


    @Autowired
     private UserServiceClient userServiceClient;

//    @Autowired
//    private AiServiceClient aiServiceClient;

//    public MedicineSuggestionResponse
//    getMedicineSuggestion(
//            MedicineSuggestionRequest request) {
//
//        return aiServiceClient
//                .generateMedicineSuggestion(request);
//    }




    @Override
    @Transactional
    public PatientResponse createPatient(PatientRequest request) {

        log.info("user request data :  {}",request);
        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Patient already exists with email : " + request.getEmail()
            );
        }

        if (patientRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException(
                    "Patient already exists with phone : " + request.getPhone()
            );
        }

        UserResponse user ;

        try{
            user = userServiceClient.getUserById(request.getUserId());
            log.info(" user info : => {}",user);
        }catch (Exception ex){
            throw new UserNotFounException("user not found on the user service ");
        }


        if (UserType.valueOf(user.getRole()) != UserType.PATIENT) {
            throw new UserNotFounException(
                    "User is not registered as PATIENT");
        }


        Patient patient = new Patient();

        patient.setPatientCode(generatePatientCode());
        patient.setUserId(user.getId());
//          .userId(user.getId())
        patient.setFirstName(user.getFirstName());
        patient.setLastName(user.getLastName());
        patient.setEmail(user.getEmail());
        patient.setPhone(user.getPhone());
        patient.setDateOfBirth(LocalDate.parse(user.getDob()));
        patient.setGender(Gender.valueOf(user.getGender()));
        patient.setBloodGroup(user.getBloodGroup());
        patient.setAddress(user.getAddress());
        patient.setEmergencyContact("+91-0000000000");
        patient.setStatus(PatientStatus.valueOf(String.valueOf(PatientStatus.ACTIVE)));
        patient.setCreatedAt(LocalDateTime.now());
        patient.setUpdatedAt(LocalDateTime.now());


        Patient savedPatient = patientRepository.save(patient);




//
//        PatientEvent event = PatientEvent.builder()
//                .eventType("PATIENT_CREATED")
//                .patientId(savedPatient.getId())
//                .patientName(
//                        savedPatient.getFirstName()
//                                + " "
//                                + savedPatient.getLastName()
//                )
//                .email(savedPatient.getEmail())
//                .phone(savedPatient.getPhone())
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        kafkaProducer.publishPatientEvent(event);

        return util.mapToResponse(patient);
    }

    @Override
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found: " + id
                        ));

        return util.mapToResponse(patient);
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        List<PatientResponse> patientResponses = new ArrayList<>();

        List<Patient> patients = patientRepository.findAll();

        for (Patient patient : patients) {
            PatientResponse response = util.mapToResponse(patient);
            patientResponses.add(response);
        }

        return patientResponses;
    }


    @Override
    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found: " + id
                        ));
        if (patientRepository.existsByPhone(request.getPhone())
                && !patient.getPhone().equals(request.getPhone())) {

            throw new RuntimeException("Phone number :  " +request.getPhone()+ " already exists");
        }

        if (patientRepository.existsByEmail(request.getEmail())
                && !patient.getEmail().equals(request.getEmail())) {

            throw new RuntimeException("this Email already exists" + request.getPhone());
        }

        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(Gender.valueOf(request.getGender()));
        patient.setBloodGroup(request.getBloodGroup());
        patient.setAddress(request.getAddress());
        patient.setEmergencyContact(request.getEmergencyContact());


        Patient updated =
                patientRepository.save(patient);
        System.out.println(updated);

//        PatientEvent event = PatientEvent.builder()
//                .eventType("PATIENT_UPDATED")
//                .patientId(updated.getId())
//                .patientName(
//                        updated.getFirstName()
//                                + " "
//                                + updated.getLastName()
//                )
//                .email(updated.getEmail())
//                .phone(updated.getPhone())
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        kafkaProducer.publishPatientEvent(event);

        return util.mapToResponse(updated);

    }

    @Override
    @Transactional
    public PatientResponse PartialPatientUpdate(Long id, PatientRequest request) {


        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found" + id));


        if (patientRepository.existsByPhone(request.getPhone())
                && !patient.getPhone().equals(request.getPhone())) {

            throw new RuntimeException("Phone number already exists"+  request.getPhone());
        }

        if (patientRepository.existsByEmail(request.getEmail())
                && !patient.getEmail().equals(request.getEmail())) {

            throw new RuntimeException("this Email already exists" + request.getEmail());
        }


        if (request.getFirstName() != null) {
            patient.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            patient.setLastName(request.getLastName());
        }

        if (request.getEmail() != null) {
            patient.setEmail(request.getEmail());
        }

        if (request.getPhone() != null) {
            patient.setPhone(request.getPhone());
        }

        if (request.getDateOfBirth() != null) {
            patient.setDateOfBirth(request.getDateOfBirth());
        }

        if (request.getGender() != null) {
            patient.setGender(Gender.valueOf(request.getGender()));
        }
        if (request.getStatus() != null) {
            patient.setStatus(PatientStatus.valueOf(request.getStatus()));
        }
        if (request.getBloodGroup() != null) {
            patient.setBloodGroup(request.getBloodGroup());
        }

        if (request.getAddress() != null) {
            patient.setAddress(request.getAddress());
        }

        if (request.getEmergencyContact() != null) {
            patient.setEmergencyContact(request.getEmergencyContact());
        }

        Patient updatedPatient = patientRepository.save(patient);

        return util.mapToResponse(updatedPatient);
    }


    @Override
    public PatientResponse updateStatus(Long id, PatientStatus status) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found with id: " + id));

        patient.setStatus(status);

        Patient updatedPatient =
                patientRepository.save(patient);


        return util.mapToResponse(patientRepository.save(updatedPatient));
    }


    @Override
    @Transactional
    public PatientResponse deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found: " + id
                        ));



//          userServiceClient.deleteUserById(patient.getUserId());
//
//        patient.setStatus(PatientStatus.valueOf(String.valueOf(PatientStatus.INACTIVE)));
//
//         patientRepository.delete(patient);
        try {

            // Delete user from User Service
            userServiceClient.deleteUserById(
                    patient.getUserId());

            // Soft delete patient
            patient.setStatus(PatientStatus.INACTIVE);

            // Hard delete patient record
            patientRepository.delete(patient);

        } catch (Exception ex) {

            log.error(
                    "Failed to delete patient with userId: {}",
                    patient.getUserId(),
                    ex);

            throw new DeleteFaildException(
                    "Failed to delete patient and user"
                    );
        }

//        PatientEvent event = PatientEvent.builder()
//                .eventType("PATIENT_DELETED")
//                .patientId(id)
//                .patientName(
//                        patient.getFirstName()
//                                + " "
//                                + patient.getLastName()
//                )
//                .email(patient.getEmail())
//                .phone(patient.getPhone())
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        kafkaProducer.publishPatientEvent(event);
     return  util.mapToResponse(patient);
    }


    @Override
    @Transactional
    public List<PatientResponse> searchPatients(String keyword) {
        return patientRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        keyword,
                        keyword
                )
                .stream()
                .map(patient -> util.mapToResponse(patient) )
                .toList();
    }

    @Override
    public List<PatientResponse> getPatientsByStatus(String status) {

        List<Patient> patients =
                patientRepository.findByStatus(PatientStatus.valueOf(status));

        return patients.stream()
                .map(patient -> util.mapToResponse(patient))
                .toList();
    }

    @Override
    public PatientResponse getPatientByEmail(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found this Email : " + email
                        ));

        return util.mapToResponse(patient);
    }

    @Override
    public List<PatientResponse> getPatientsByBloodGroup(String bloodGroup) {

        return patientRepository.findByBloodGroup(bloodGroup)
                .stream()
                .map(util::mapToResponse)
                .toList();
    }

    @Override
    public long countPatients() {

        return patientRepository.count();
    }


    private String generatePatientCode() {

        return "PAT-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}
