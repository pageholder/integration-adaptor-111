package uk.nhs.adaptors.oneoneone.cda.report.mapper;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import uk.nhs.connect.iucds.cda.ucr.POCDMT000002UK01Participant1;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ParticipantMapper {

    private PeriodMapper periodMapper;

    private PractitionerMapper practitionerMapper;

    public Encounter.EncounterParticipantComponent mapEncounterParticipant(POCDMT000002UK01Participant1 encounterParticipant) {
        Practitioner practitioner = practitionerMapper
            .mapPractitioner(encounterParticipant.getAssociatedEntity());

        return new Encounter.EncounterParticipantComponent()
            .setType(retrieveTypeFromITK(encounterParticipant))
            .setPeriod(periodMapper.mapPeriod(encounterParticipant.getTime()))
            .setIndividual(new Reference(practitioner))
            .setIndividualTarget(practitioner);
    }

    private List<CodeableConcept> retrieveTypeFromITK(POCDMT000002UK01Participant1 encounterParticipant) {
        return Collections.singletonList(new CodeableConcept()
            .setText(encounterParticipant.getTypeCode()));
    }
}
