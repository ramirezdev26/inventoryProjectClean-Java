package co.diegofer.inventoryclean.usecase.mysqlupdaterv2;

import co.diegofer.inventoryclean.model.events.BranchCreated;
import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.usecase.MySqlUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MySQLUpdaterV2UseCaseTest {
    @Mock
    private MySqlUpdater updater;

    private MySQLUpdaterV2UseCase mySQLUpdaterV2UseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mySQLUpdaterV2UseCase = new MySQLUpdaterV2UseCase(updater);
    }

    @Test
    void accept() {
        DomainEvent domainEvent = new BranchCreated("name","country","city");

        // Act
        mySQLUpdaterV2UseCase.accept(domainEvent);

        verify(updater, times(1)).applyEvent(domainEvent);
    }
}