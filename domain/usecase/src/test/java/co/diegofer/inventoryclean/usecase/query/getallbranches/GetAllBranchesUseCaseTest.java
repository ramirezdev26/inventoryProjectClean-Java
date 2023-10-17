package co.diegofer.inventoryclean.usecase.query.getallbranches;

import co.diegofer.inventoryclean.model.branch.Branch;
import co.diegofer.inventoryclean.model.branch.gateways.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetAllBranchesUseCaseTest {

    @Mock
    private BranchRepository branchRepository;
    private GetAllBranchesUseCase getAllBranchesUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        getAllBranchesUseCase = new GetAllBranchesUseCase(branchRepository); }

    @Test
    void apply() {

        List<Branch> sampleBranches = new ArrayList<>();
        sampleBranches.add(new Branch("Branch1", "country", "city"));
        sampleBranches.add(new Branch("Branch2", "country", "city"));
        sampleBranches.add(new Branch("Branch3", "country", "city"));

        when(branchRepository.getAllBranches()).thenReturn(Flux.fromIterable(sampleBranches));

        // Act
        Flux<Branch> result = getAllBranchesUseCase.apply();

        StepVerifier.create(result)
                .expectNextCount(3)
                .verifyComplete();

    }
}