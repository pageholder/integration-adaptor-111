package uk.nhs.adaptors.oneoneone.cda.report.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;

import uk.nhs.connect.iucds.cda.ucr.TEL;

import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.Period;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ContactPointMapperTest {

    @Mock
    PeriodMapper periodMapper;

    @InjectMocks
    ContactPointMapper contactPointMapper;

    @Mock
    Period period;

    public static final String TEL_NUMBER = "1357962783";
    public static final String USE_ITK = "H";

    @Test
    public void mapContactPoint() {
        TEL tel = TEL.Factory.newInstance();
        tel.setValue(TEL_NUMBER);
        tel.setUse(Collections.singletonList(USE_ITK));
        tel.addNewUseablePeriod();

        when(periodMapper.mapPeriod(ArgumentMatchers.any())).thenReturn(period);

        ContactPoint contactPoint = contactPointMapper.mapContactPoint(tel);

        assertThat(contactPoint.getValue()).isEqualTo(TEL_NUMBER);
        assertThat(contactPoint.getUse()).isEqualTo(ContactPoint.ContactPointUse.HOME);
        assertThat(contactPoint.getPeriod()).isEqualTo(period);
    }

}
