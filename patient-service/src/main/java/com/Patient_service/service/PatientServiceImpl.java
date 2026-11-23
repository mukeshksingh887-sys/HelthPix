package com.Patient_service.service;

import com.Patient_service.client.AppointmentServiceClient;
import com.Patient_service.client.UserServiceClient;
import com.Patient_service.dto.*;
import com.Patient_service.entity.*;
import com.Patient_service.exception.AlreadyExist;
import com.Patient_service.exception.DeleteFaildException;
import com.Patient_service.exception.PatientNotFoundException;
import com.Patient_service.exception.UserNotFounException;
import com.Patient_service.repository.MedicalHistoryRepository;
import com.Patient_service.repository.PatientRepository;
import com.Patient_service.util.Util;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PatientServiceImpl implements PatientService{

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @Autowired
    private AppointmentServiceClient appointmentServiceClient;

    @Autowired
    private Util util;

    @Autowired
     private UserServiceClient userServiceClient;






    @Override
    @Transactional
    public PatientResponse createPatient(PatientRequest request) {

        // 1. Validate request
        validatePatientRequest(request);

        // 2. Fetch user from User Service
        UserResponse user;

        try {
            user = userServiceClient.getUserById(request.getUserId());
        } catch (Exception ex) {
            log.error("Failed to fetch user: {}", request.getUserId(), ex);
            throw new UserNotFounException(
                    "Unable to retrieve user from User Service"
            );
        }

        if (user == null || user.getId() == null) {
            throw new UserNotFounException("User not found");
        }

        // 3. Validate role
        if (!UserType.PATIENT.name()
                .equalsIgnoreCase(user.getRole())) {
            throw new IllegalArgumentException(
                    "User is not registered as PATIENT"
            );
        }

        // 4. Prevent duplicate patient
        if (patientRepository.existsByUserId(user.getId())) {
            throw new AlreadyExist(
                    "Patient already exists for userId: " + user.getId()
            );
        }

        // 5. Create patient entity
        Patient patient = Patient.builder()
                .userId(user.getId())
                .patientCode(generatePatientCode())
                .build();

        // 6. Map medical histories
        if (request.getMedicalHistories() != null) {
            for (MedicalHistoryRequest dto
                    : request.getMedicalHistories()) {

                MedicalHistory history = mapMedicalHistoryRequest(dto);

                patient.addMedicalHistory(history);
            }
        }

        // 7. Map appointment histories, if supplied
        if (request.getAppointmentHistories() != null) {
            for ( AppointmentHistory dto
                    : request.getAppointmentHistories()) {

                AppointmentHistory appointment =
                        mapAppointmentHistoryRequest(dto);

                patient.addAppointmentHistory(appointment);
            }
        }

        // 8. Save patient and child entities
        Patient savedPatient = patientRepository.save(patient);

        // 9. Return combined patient + user + history response
        return mapPatientResponse(savedPatient, user);
    }

    // =========================================================
    // VALIDATION HELPERS
    // =========================================================

    private void validatePatientRequest(PatientRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Patient request must not be null"
            );
        }

        if (request.getUserId() == null) {
            throw new IllegalArgumentException(
                    "User ID is required"
            );
        }


        log.info( " medical history :::: {}",request.getMedicalHistories());

        if (request.getMedicalHistories() != null) {
            request.getMedicalHistories().forEach(dto -> {
                log.info(" Disease is ::::: {}",dto.getDisease());

                if (dto == null
                        || dto.getDisease() == null
                        || dto.getDisease().isBlank()) {
                    throw new IllegalArgumentException(
                            "Disease is required for medical history"
                    );

                }
            });
        }

        if (request.getAppointmentHistories() != null) {
            request.getAppointmentHistories().forEach(dto -> {
                if (dto == null) {
                    throw new IllegalArgumentException(
                            "Appointment history must not be null"
                    );
                }
            });
        }
    }

    // =========================================================
    // PATIENT CODE GENERATOR
    // =========================================================

    private String generatePatientCode() {
        return "PAT-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    // =========================================================
    // REQUEST DTO -> ENTITY MAPPERS
    // =========================================================

    private MedicalHistory mapMedicalHistoryRequest(
            MedicalHistoryRequest dto) {

        return MedicalHistory.builder()
                .doctorName(dto.getDoctorName())
                .disease(dto.getDisease())
                .medications(dto.getMedications())
                .surgeryDetails(dto.getSurgeryDetails())
                .notes(dto.getNotes())
                .doctorNotes(dto.getDoctorNotes())
                .diagnosisDate(dto.getDiagnosisDate())
                .build();
    }

    private AppointmentHistory mapAppointmentHistoryRequest(
            AppointmentHistory dto) {

        return AppointmentHistory.builder()
                .doctorId(dto.getDoctorId())
                .appointmentDate(dto.getAppointmentDate())
                .doctorName(dto.getDoctorName())
                .reason(dto.getReason())
                .status(dto.getStatus())
                .build();
    }

    // =========================================================
    // ENTITY -> RESPONSE DTO MAPPERS
    // =========================================================

    private PatientResponse mapPatientResponse(
            Patient patient,
            UserResponse user) {

        return PatientResponse.builder()
                .patientId(patient.getPatientId())
                .patientCode(patient.getPatientCode())
                .userId(user.getId())

                // User Service fields
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())

                // Patient child collections
                .medicalHistories(
                        mapMedicalHistoryResponses(
                                patient.getMedicalHistory()
                        )
                )
                .appointmentHistory(
                        mapAppointmentHistoryResponses(
                                patient.getAppointmentHistory()
                        )
                )
                .build();
    }

    private List<MedicalHistoryResponse> mapMedicalHistoryResponses(
            List<MedicalHistory> histories) {

        if (histories == null) {
            return List.of();
        }

        return histories.stream()
                .filter(Objects::nonNull)
                .map(this::mapMedicalHistoryResponse)
                .collect(Collectors.toList());
    }

    private MedicalHistoryResponse mapMedicalHistoryResponse(
            MedicalHistory history) {

        return MedicalHistoryResponse.builder()
                .diagnosis(history.getDiagnosis())
                .medications(history.getMedications())
                .notes(history.getNotes())
                .diagnosisDate(history.getDiagnosisDate())
                .build();
    }

    private List<AppointmentHistoryResponse>
    mapAppointmentHistoryResponses(
            List<AppointmentHistory> histories) {

        if (histories == null) {
            return List.of();
        }

        return histories.stream()
                .filter(Objects::nonNull)
                .map(this::mapAppointmentHistoryResponse)
                .collect(Collectors.toList());
    }

    private AppointmentHistoryResponse mapAppointmentHistoryResponse(
            AppointmentHistory appointment) {

        return AppointmentHistoryResponse.builder()
                .appointmentDate(appointment.getAppointmentDate().toLocalDate())
                .patientId(
                        appointment.getPatient() != null
                                ? appointment.getPatient().getPatientId()
                                : null
                )
                .status(appointment.getStatus() != null
                        ? AppointmentStatus.valueOf(appointment.getStatus())
                        : null)
                .reason(appointment.getReason())
                .build();
    }





    @Transactional
    @Override
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found: " + id
                        ));

        UserResponse user;

        try {
            user = userServiceClient.getUserById(patient.getUserId());
        } catch (Exception ex) {
            log.error("Failed to fetch user: {}", patient.getUserId(), ex);
            throw new UserNotFounException(
                    "Unable to retrieve user from User Service"
            );
        }

//         Return combined patient + user + history response
        return mapPatientResponse(patient, user);
    }



    @Override
    public PatientDetailsResponse getPatientProfile(
            Long patientId) {

        Patient patient = patientRepository
                .findById(patientId)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found"));

        List<AppointmentHistoryResponse> appointments =
                appointmentServiceClient
                        .getAppointmentsByPatientId(
                                patientId);


        List<MedicalHistory> histories =
                medicalHistoryRepository.findByPatient_PatientId(patientId);

        UserResponse user;

        try {
            user = userServiceClient.getUserById(patient.getUserId());
        } catch (Exception ex) {
            log.error("Failed to fetch user: {}", patient.getUserId(), ex);
            throw new UserNotFounException(
                    "Unable to retrieve user from User Service"
            );
        }

//         Return combined patient + user + history response
//        return mapPatientResponse(patient, user);


        return PatientDetailsResponse.builder()
                .id(patient.getPatientId())
                .patientCode(patient.getPatientCode())
                .userId(patient.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .bloodGroup(user.getBloodGroup())
//                .medicalHistories()
                .appointmentHistory(appointments)
                .build();
    }




    @Transactional
    @Override
    public  PatientResponse deletePatient(Long id) {
        // 1. Find patient
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found: " + id
                        ));

        // 2. Get User ID
        Long userId = patient.getUserId();

        // 3. Fetch user details if required for response
//        UserResponse user = userServiceClient.getUserById(userId);

        UserResponse user;

        try {
            user = userServiceClient.getUserById(patient.getUserId());
        } catch (Exception ex) {
            throw new UserNotFounException(
                    "Unable to retrieve user from User Service"
            );
        }



        // 4. Create response before deletion
        PatientResponse response =
                mapPatientResponse(patient, user);

        // 5. Delete user from User Service
        try{
            userServiceClient.deleteUserById(userId);
        }catch (Exception ex){
            throw  new DeleteFaildException("Unable to delete user in user service");
        };


        // 6. Delete patient from Patient Service
        // Medical history and appointment history
        // will also be deleted through CascadeType.ALL
        patientRepository.delete(patient);

        // 7. Return response
        return response;
    }




    @Override
    @Transactional
    public List<PatientResponse> getAllPatients() {

        List<Patient> patients = patientRepository.findAll();

        List<PatientResponse> patientResponses = new ArrayList<>();

        for (Patient patient : patients) {

            UserResponse user;

            try {
                user = userServiceClient.getUserById(patient.getUserId());

            } catch (Exception ex) {

                log.error("Failed to fetch user: {}",
                        patient.getUserId(), ex);

                throw new UserNotFounException(
                        "Unable to retrieve user: " + patient.getUserId()
                );
            }

            PatientResponse response =
                    mapPatientResponse(patient, user);

            patientResponses.add(response);
        }

        return patientResponses;
    }

//    @Override
//    public List<PatientResponse> getAllPatients() {
//        List<PatientResponse> patientResponses = new ArrayList<>();
//
//        List<Patient> patients = patientRepository.findAll();
//
//
//
//        for (Patient patient : patients) {
//            UserResponse user;
//
//            try {
//                user = userServiceClient.getUserById(patient.getUserId());
//            } catch (Exception ex) {
//                log.error("Failed to fetch user: {}", patient.getUserId(), ex);
//                throw new UserNotFounException(
//                        "Unable to retrieve user from User Service"
//                );
//            }
//
//            PatientResponse response = mapPatientResponse(patient, user);;
//            patientResponses.add(response);
//        }
//
//        return patientResponses;
//    }
//



//    @Override
//    @Transactional
//    public PatientResponse updatePatient(Long id, PatientRequest request) {
//        Patient patient = patientRepository.findById(id)
//                .orElseThrow(() ->
//                        new PatientNotFoundException(
//                                "Patient not found: " + id
//                        ));
//        if (patientRepository.existsByPhone(request.getPhone())
//                && !patient.getPhone().equals(request.getPhone())) {
//
//            throw new RuntimeException("Phone number :  " +request.getPhone()+ " already exists");
//        }
//
//        if (patientRepository.existsByEmail(request.getEmail())
//                && !patient.getEmail().equals(request.getEmail())) {
//
//            throw new RuntimeException("this Email already exists" + request.getPhone());
//        }
//
//        patient.setFirstName(request.getFirstName());
//        patient.setLastName(request.getLastName());
//        patient.setEmail(request.getEmail());
//        patient.setPhone(request.getPhone());
//        patient.setDateOfBirth(request.getDateOfBirth());
//        patient.setGender(Gender.valueOf(request.getGender()));
//        patient.setBloodGroup(request.getBloodGroup());
//        patient.setAddress(request.getAddress());
//        patient.setEmergencyContact(request.getEmergencyContact());
//
//
//        Patient updated =
//                patientRepository.save(patient);
//        System.out.println(updated);
//
////        PatientEvent event = PatientEvent.builder()
////                .eventType("PATIENT_UPDATED")
////                .patientId(updated.getId())
////                .patientName(
////                        updated.getFirstName()
////                                + " "
////                                + updated.getLastName()
////                )
////                .email(updated.getEmail())
////                .phone(updated.getPhone())
////                .timestamp(LocalDateTime.now())
////                .build();
////
////        kafkaProducer.publishPatientEvent(event);
//
//        return util.mapToResponse(updated);
//
//    }
//

//
//    @Override
//    @Transactional
//    public PatientResponse1 PartialPatientUpdate(Long id, PatientRequest request) {
//
//
//        Patient patient = patientRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Patient not found" + id));
//        UserResponse user;
//
//        try {
//            user = userServiceClient.getUserById(patient.getUserId());
//        } catch (Exception ex) {
//            log.error("Failed to fetch user: {}", patient.getUserId(), ex);
//            throw new UserNotFounException(
//                    "Unable to retrieve user from User Service"
//            );
//        }
//
//
//        if (patientRepository.existsByPhone(request.getPhone())
//                && !patient.getPhone().equals(request.getPhone())) {
//
//            throw new RuntimeException("Phone number already exists"+  request.getPhone());
//        }
//
//        if (patientRepository.existsByEmail(request.getEmail())
//                && !patient.getEmail().equals(request.getEmail())) {
//
//            throw new RuntimeException("this Email already exists" + request.getEmail());
//        }
//
//
//        if (request.getFirstName() != null) {
//            patient.setFirstName(request.getFirstName());
//        }
//
//        if (request.getLastName() != null) {
//            patient.setLastName(request.getLastName());
//        }
//
//        if (request.getEmail() != null) {
//            patient.setEmail(request.getEmail());
//        }
//
//        if (request.getPhone() != null) {
//            patient.setPhone(request.getPhone());
//        }
//
//        if (request.getDateOfBirth() != null) {
//            patient.setDateOfBirth(request.getDateOfBirth());
//        }
//
//        if (request.getGender() != null) {
//            patient.setGender(Gender.valueOf(request.getGender()));
//        }
//        if (request.getStatus() != null) {
//            patient.setStatus(PatientStatus.valueOf(request.getStatus()));
//        }
//        if (request.getBloodGroup() != null) {
//            patient.setBloodGroup(request.getBloodGroup());
//        }
//
//        if (request.getAddress() != null) {
//            patient.setAddress(request.getAddress());
//        }
//
//        if (request.getEmergencyContact() != null) {
//            patient.setEmergencyContact(request.getEmergencyContact());
//        }
//
//        Patient updatedPatient = patientRepository.save(patient);
//
//        return util.mapToResponse(updatedPatient);
//    }
//

//    @Override
//    public PatientResponse1 updateStatus(Long id, PatientStatus status) {
//
//        Patient patient = patientRepository.findById(id)
//                .orElseThrow(() ->
//                        new PatientNotFoundException(
//                                "Patient not found with id: " + id));
//
//        patient.setStatus(status);
//
//        Patient updatedPatient =
//                patientRepository.save(patient);
//
//
//        return util.mapToResponse(patientRepository.save(updatedPatient));
//    }
//
//

//
//
//
////          userServiceClient.deleteUserById(patient.getUserId());
////
////        patient.setStatus(PatientStatus.valueOf(String.valueOf(PatientStatus.INACTIVE)));
////
////         patientRepository.delete(patient);
//        try {
//
//            // Delete user from User Service
//            userServiceClient.deleteUserById(
//                    patient.getUserId());
//
//            // Soft delete patient
//            patient.setStatus(PatientStatus.INACTIVE);
//
//            // Hard delete patient record
//            patientRepository.delete(patient);
//
//        } catch (Exception ex) {
//
//            log.error(
//                    "Failed to delete patient with userId: {}",
//                    patient.getUserId(),
//                    ex);
//
//            throw new DeleteFaildException(
//                    "Failed to delete patient and user"
//                    );
//        }
//
//     return  util.mapToResponse(patient);
//    }
//
//
//    @Override
//    @Transactional
//    public List<PatientResponse1> searchPatients(String keyword) {
//        return patientRepository
//                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
//                        keyword,
//                        keyword
//                )
//                .stream()
//                .map(patient -> util.mapToResponse(patient) )
//                .toList();
//    }
//
//    @Override
//    public List<PatientResponse1> getPatientsByStatus(String status) {
//
//        List<Patient> patients =
//                patientRepository.findByStatus(PatientStatus.valueOf(status));
//
//        return patients.stream()
//                .map(patient -> util.mapToResponse(patient))
//                .toList();
//    }
//
//    @Override
//    public PatientResponse1 getPatientByEmail(String email) {
//        Patient patient = patientRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new PatientNotFoundException(
//                                "Patient not found this Email : " + email
//                        ));
//
//        return util.mapToResponse(patient);
//    }
//
//    @Override
//    public List<PatientResponse1> getPatientsByBloodGroup(String bloodGroup) {
//
//        return patientRepository.findByBloodGroup(bloodGroup)
//                .stream()
//                .map(util::mapToResponse)
//                .toList();
//    }
//
//    @Override
//    public long countPatients() {
//
//        return patientRepository.count();
//    }
//


}
