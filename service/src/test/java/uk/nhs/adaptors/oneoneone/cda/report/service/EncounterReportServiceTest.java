package uk.nhs.adaptors.oneoneone.cda.report.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;
import javax.jms.Session;

import org.hl7.fhir.dstu3.model.Bundle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import uk.nhs.adaptors.oneoneone.config.AmqpProperties;
import uk.nhs.connect.iucds.cda.ucr.POCDMT000002UK01ClinicalDocument1;

@RunWith(MockitoJUnitRunner.class)
public class EncounterReportServiceTest {

    private static final String ENCOUNTER_REPORT_MAPPING = "<encounter-report-mapping>";
    private static final String QUEUE_NAME = "Encounter-Report";

    @InjectMocks
    private EncounterReportService encounterReportService;

    @Mock
    private AmqpProperties amqpProperties;

    @Mock
    private JmsTemplate jmsTemplate;

    @Mock
    private EncounterReportBundleService encounterReportBundleService;

    @Mock
    private FhirContext fhirContext;

    @Before
    public void setUp() {
        when(amqpProperties.getQueueName()).thenReturn(QUEUE_NAME);
    }

    @Test
    public void transformAndPopulateToGP() throws JMSException {
        POCDMT000002UK01ClinicalDocument1 clinicalDoc = mock(POCDMT000002UK01ClinicalDocument1.class);
        Bundle encounterBundle = mock(Bundle.class);
        when(encounterReportBundleService.createEncounterBundle(clinicalDoc)).thenReturn(encounterBundle);
        IParser parser = mock(IParser.class);
        when(fhirContext.newJsonParser()).thenReturn(parser);
        when(parser.encodeResourceToString(encounterBundle)).thenReturn(ENCOUNTER_REPORT_MAPPING);
        Session session = mock(Session.class);

        encounterReportService.transformAndPopulateToGP(clinicalDoc);

        ArgumentCaptor<MessageCreator> argumentCaptor = ArgumentCaptor.forClass(MessageCreator.class);
        verify(jmsTemplate).send(eq(QUEUE_NAME), argumentCaptor.capture());
        argumentCaptor.getValue().createMessage(session);
        verify(session).createTextMessage(ENCOUNTER_REPORT_MAPPING);
    }
}
