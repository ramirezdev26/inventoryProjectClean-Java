package co.diegofer.inventoryclean.usecase.query.getusersbybranchid;

import co.diegofer.inventoryclean.model.product.Product;
import co.diegofer.inventoryclean.model.product.gateways.ProductRepository;
import co.diegofer.inventoryclean.model.user.User;
import co.diegofer.inventoryclean.model.user.gateways.UserRepository;
import co.diegofer.inventoryclean.usecase.query.getproductsbybranchid.GetProductsByBranchIdUseCase;
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

class GetUsersByBranchIdUseCaseTest {
    @Mock
    private UserRepository userRepository;
    private GetUsersByBranchIdUseCase getUsersByBranchIdUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        getUsersByBranchIdUseCase = new GetUsersByBranchIdUseCase(userRepository); }

    @Test
    void apply() {

        List<User> sampleUsers = new ArrayList<>();
        sampleUsers.add(new User("name1", "lastname", "email", "password", "ADMIN","root"));
        sampleUsers.add(new User("name2", "lastname", "email", "password", "ADMIN","root"));
        sampleUsers.add(new User("name3", "lastname", "email", "password", "ADMIN","root"));

        when(userRepository.findUsersByBranch("root")).thenReturn(Flux.fromIterable(sampleUsers));

        // Act
        Flux<User> result = getUsersByBranchIdUseCase.apply("root");

        StepVerifier.create(result)
                .expectNextCount(3)
                .verifyComplete();

    }
}