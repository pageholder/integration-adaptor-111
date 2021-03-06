package uk.nhs.adaptors.oneoneone.cda.report.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.nhs.connect.iucds.cda.ucr.IVLTS;
import uk.nhs.connect.iucds.cda.ucr.POCDMT000002UK01AssociatedEntity;
import uk.nhs.connect.iucds.cda.ucr.POCDMT000002UK01Participant1;

import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParticipantMapperTest {

    @Mock
    private PractitionerMapper practitionerMapper;

    @Mock
    private PeriodMapper periodMapper;

    @InjectMocks
    private ParticipantMapper participantMapper;

    @Mock
    private Practitioner practitioner;

    @Mock
    private Period period;

    @Test
    public void mapParticipant() {
        POCDMT000002UK01Participant1 encounterParticipant = mock(POCDMT000002UK01Participant1.class);
        POCDMT000002UK01AssociatedEntity associatedEntity = mock(POCDMT000002UK01AssociatedEntity.class);
        IVLTS time = mock(IVLTS.class);

        when(encounterParticipant.getTypeCode()).thenReturn("CON");
        when(encounterParticipant.getAssociatedEntity()).thenReturn(associatedEntity);
        when(encounterParticipant.getTime()).thenReturn(time);
        when(periodMapper.mapPeriod(ArgumentMatchers.isA(IVLTS.class)))
            .thenReturn(period);
        when(practitionerMapper.mapPractitioner(ArgumentMatchers.isA(POCDMT000002UK01AssociatedEntity.class)))
            .thenReturn(practitioner);

        Encounter.EncounterParticipantComponent participantComponent = participantMapper.mapEncounterParticipant(encounterParticipant);

        assertThat(participantComponent.getIndividualTarget()).isEqualTo(practitioner);
        assertThat(participantComponent.getPeriod()).isEqualTo(period);
        assertThat(participantComponent.getType().get(0).getText()).isEqualTo("CON");
    }

}
