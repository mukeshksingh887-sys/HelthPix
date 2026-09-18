package com.ai_service.service;


import com.ai_service.dto.MedicineSuggestionRequest;
import com.ai_service.dto.MedicineSuggestionResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class MedicineSuggestionService {

    private final ChatClient chatClient;

    public MedicineSuggestionService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public MedicineSuggestionResponse generateSuggestion(
            MedicineSuggestionRequest request) {

        String patientInformation = buildPatientInformation(request);

        return chatClient
                .prompt()

                .system("""
                        You are an AI clinical decision-support assistant
                        integrated into a hospital management system.

                        Your job is NOT to independently diagnose or prescribe
                        medication.

                        You provide educational, AI-assisted suggestions
                        that MUST be reviewed by a qualified doctor.

                        IMPORTANT RULES:

                        1. Never claim certainty about a diagnosis.
                        2. Never replace a doctor's clinical judgment.
                        3. Consider allergies and existing medications.
                        4. Consider age, gender and weight when relevant.
                        5. Highlight missing information.
                        6. Highlight potentially dangerous symptoms.
                        7. Do not recommend a medication when insufficient
                           information is available.
                        8. Do not recommend controlled substances.
                        9. Do not invent patient information.
                        10. Do not invent laboratory results.
                        11. Always require doctor review.
                        12. If the symptoms could indicate an emergency,
                            clearly state that urgent medical evaluation
                            is required.
                        13. Return only information supported by the input
                            and general medical knowledge.
                        14. Never state that a medicine is definitely safe
                            for the patient.

                        The output must be structured according to the
                        requested Java response object.
                        """)

                .user("""
                        Review the following patient information.

                        Provide an AI-assisted clinical suggestion for
                        doctor review.

                        PATIENT INFORMATION:

                        %s

                        Focus on:

                        - Possible clinical considerations
                        - Potential medicine options only when appropriate
                        - Purpose of each medicine
                        - Important warnings
                        - Contraindications
                        - Missing information
                        - Whether urgent medical evaluation may be needed

                        Doctor review is mandatory.
                        """.formatted(patientInformation))

                .call()

                .entity(
                        MedicineSuggestionResponse.class,
                        spec -> spec
                                .useProviderStructuredOutput()
                                .validateSchema()
                );
    }

    private String buildPatientInformation(
            MedicineSuggestionRequest request) {

        return """
                Patient ID: %s
                Age: %s
                Gender: %s
                Weight: %s kg

                Symptoms:
                %s

                Allergies:
                %s

                Current Medications:
                %s

                Existing Conditions:
                %s

                Medical History:
                %s

                Lab Reports:
                %s
                """.formatted(

                request.patientId(),
                request.age(),
                request.gender(),
                request.weightKg(),
                request.symptoms(),
                request.allergies(),
                request.currentMedications(),
                request.existingConditions(),
                request.medicalHistory(),
                request.labReports()
        );
    }
}