package co.diegofer.inventoryclean.usecase.auth.registersuper;

import co.diegofer.inventoryclean.model.commands.RegisterUserCommand;
import co.diegofer.inventoryclean.model.user.RoleEnum;
import co.diegofer.inventoryclean.model.user.User;
import co.diegofer.inventoryclean.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RegisterSuperUseCaseTest {
    @Mock
    private UserRepository userRepository;

    private RegisterSuperUseCase registerSuperUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        registerSuperUseCase = new RegisterSuperUseCase(userRepository);
    }

    @Test
    void registerSuperUser() {
        RegisterUserCommand registerUserCommand = new RegisterUserCommand(null, "userid", "John", "Doe", "john@example.com", "securepassword", "SUPER");

        User expectedUser = new User("John", "Doe", "john@example.com", "securepassword", RoleEnum.SUPER.name(), null);

        when(userRepository.saveASuper(Mockito.any(User.class))).thenReturn(Mono.just(expectedUser));

        StepVerifier.create(registerSuperUseCase.apply(Mono.just(registerUserCommand)))
                .expectNextMatches(user -> user.equals(expectedUser))
                .verifyComplete();
    }
}