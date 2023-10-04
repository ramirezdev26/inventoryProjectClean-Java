package co.diegofer.inventoryclean.usecase.registerbranch;

import co.diegofer.inventoryclean.model.branch.gateways.BranchRepository;
import co.diegofer.inventoryclean.usecase.command.registerbranch.RegisterBranchUseCase;
import co.diegofer.inventoryclean.usecase.generics.DomainEventRepository;
import co.diegofer.inventoryclean.usecase.generics.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterBranchUseCaseTest {


    @Mock
    private DomainEventRepository repository;

    @Mock
    private EventBus eventBus;

    private RegisterBranchUseCase registerBranchUseCase;

    @BeforeEach
    void setUp() {
        registerBranchUseCase = new RegisterBranchUseCase(repository, eventBus);
    }


    @Test
    void successfulScenario() {
       /* RegisterBranchCommand registerBranchCommand = new RegisterBranchCommand(
                "Branch name",
                new LocationCommand("Location","country")
        );

        Branch savedBranch = new Branch("Branch name", "Location, country");
        savedBranch.setId("root");

        when(branchRepository.saveABranch(Mockito.any())).thenReturn(
                Mono.just(savedBranch));

        when(repository.saveEvent(Mockito.any())).thenReturn(Mono.just(new BranchCreated()));

        // Act
        StepVerifier.create(registerBranchUseCase.apply(Mono.just(registerBranchCommand)))
                .expectNextCount(1)
                .verifyComplete();*/



    }
}