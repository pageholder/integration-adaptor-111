package uk.nhs.adaptors.oneoneone.cda.report.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import uk.nhs.connect.iucds.cda.ucr.AD;
import uk.nhs.connect.iucds.cda.ucr.PN;
import uk.nhs.connect.iucds.cda.ucr.POCDMT000002UK01AssignedAuthor;
import uk.nhs.connect.iucds.cda.ucr.POCDMT000002UK01AssignedEntity;
import uk.nhs.connect.iucds.cda.ucr.POCDMT000002UK01AssociatedEntity;
import uk.nhs.connect.iucds.cda.ucr.POCDMT000002UK01Person;
import uk.nhs.connect.iucds.cda.ucr.TEL;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PractitionerMapperTest {

    @Mock
    private HumanNameMapper humanNameMapper;

    @Mock
    private ContactPointMapper contactPointMapper;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private PractitionerMapper practitionerMapper;

    @Mock
    private HumanName humanName;

    @Mock
    private ContactPoint contactPoint;

    @Mock
    private Address address;

    @Test
    public void mapPractitionerFromAssignedEntity() {
        POCDMT000002UK01AssignedEntity assignedEntity = POCDMT000002UK01AssignedEntity.Factory.newInstance();
        assignedEntity.setAssignedPerson(createPerson());
        assignedEntity.setTelecomArray(createTelecomArray());
        assignedEntity.setAddrArray(createAddrArray());

        when(humanNameMapper.mapHumanName(ArgumentMatchers.any())).thenReturn(humanName);
        when(contactPointMapper.mapContactPoint(ArgumentMatchers.any())).thenReturn(contactPoint);
        when(addressMapper.mapAddress(ArgumentMatchers.any())).thenReturn(address);

        Practitioner practitioner = practitionerMapper.mapPractitioner(assignedEntity);

        assertThat(practitioner.getIdElement().getValue()).startsWith("urn:uuid:");
        assertThat(practitioner.getActive()).isEqualTo(true);
        assertThat(practitioner.getNameFirstRep()).isEqualTo(humanName);
        assertThat(practitioner.getTelecomFirstRep()).isEqualTo(contactPoint);
        assertThat(practitioner.getAddressFirstRep()).isEqualTo(address);
    }

    @Test
    public void mapPractitionerFromAssociatedEntity() {
        POCDMT000002UK01AssociatedEntity associatedEntity = POCDMT000002UK01AssociatedEntity.Factory.newInstance();
        associatedEntity.setAssociatedPerson(createPerson());
        associatedEntity.setTelecomArray(createTelecomArray());
        associatedEntity.setAddrArray(createAddrArray());

        when(humanNameMapper.mapHumanName(ArgumentMatchers.any())).thenReturn(humanName);
        when(contactPointMapper.mapContactPoint(ArgumentMatchers.any())).thenReturn(contactPoint);
        when(addressMapper.mapAddress(ArgumentMatchers.any())).thenReturn(address);

        Practitioner practitioner = practitionerMapper.mapPractitioner(associatedEntity);

        assertThat(practitioner.getIdElement().getValue()).startsWith("urn:uuid:");
        assertThat(practitioner.getActive()).isEqualTo(true);
        assertThat(practitioner.getNameFirstRep()).isEqualTo(humanName);
        assertThat(practitioner.getTelecomFirstRep()).isEqualTo(contactPoint);
        assertThat(practitioner.getAddressFirstRep()).isEqualTo(address);
    }

    @Test
    public void mapPractitionerForAssignedAuthor() {
        POCDMT000002UK01AssignedAuthor associatedEntity = POCDMT000002UK01AssignedAuthor.Factory.newInstance();
        associatedEntity.setAssignedPerson(createPerson());
        associatedEntity.setTelecomArray(createTelecomArray());
        associatedEntity.setAddrArray(createAddrArray());

        when(humanNameMapper.mapHumanName(ArgumentMatchers.any())).thenReturn(humanName);
        when(contactPointMapper.mapContactPoint(ArgumentMatchers.any())).thenReturn(contactPoint);
        when(addressMapper.mapAddress(ArgumentMatchers.any())).thenReturn(address);

        Practitioner practitioner = practitionerMapper.mapPractitioner(associatedEntity);

        assertThat(practitioner.getIdElement().getValue()).startsWith("urn:uuid:");
        assertThat(practitioner.getActive()).isEqualTo(true);
        assertThat(practitioner.getNameFirstRep()).isEqualTo(humanName);
        assertThat(practitioner.getTelecomFirstRep()).isEqualTo(contactPoint);
        assertThat(practitioner.getAddressFirstRep()).isEqualTo(address);
    }

    private AD[] createAddrArray() {
        AD ad = AD.Factory.newInstance();
        return new AD[] { ad };
    }

    private TEL[] createTelecomArray() {
        TEL tel = TEL.Factory.newInstance();
        return new TEL[] { tel };
    }

    private POCDMT000002UK01Person createPerson() {
        POCDMT000002UK01Person person = POCDMT000002UK01Person.Factory.newInstance();
        PN pn = PN.Factory.newInstance();
        PN[] pnArray = { pn };
        person.setNameArray(pnArray);
        return person;
    }
}
