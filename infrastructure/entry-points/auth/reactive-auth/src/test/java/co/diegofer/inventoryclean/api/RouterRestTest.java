package co.diegofer.inventoryclean.api;

import co.diegofer.inventoryclean.model.commands.*;
import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.ProductSale;
import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.RegisterFinalCustomerSaleCommand;
import co.diegofer.inventoryclean.model.commands.custom.ProductToAdd;
import co.diegofer.inventoryclean.model.events.*;
import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.model.user.AuthResponse;
import co.diegofer.inventoryclean.usecase.auth.loginuser.LoginUserUseCase;
import co.diegofer.inventoryclean.usecase.command.addstocktoproduct.AddStockToProductUseCase;
import co.diegofer.inventoryclean.usecase.command.changeUserRole.ChangeUserRoleUseCase;
import co.diegofer.inventoryclean.usecase.command.registerSupplier.RegisterSupplierUseCase;
import co.diegofer.inventoryclean.usecase.command.registerbranch.RegisterBranchUseCase;
import co.diegofer.inventoryclean.usecase.command.registerfinalcustomersale.RegisterFinalCustomerSaleUseCase;
import co.diegofer.inventoryclean.usecase.command.registerproduct.RegisterProductUseCase;
import co.diegofer.inventoryclean.usecase.command.registerresellercustomersale.RegisterResellerCustomerSaleUseCase;
import co.diegofer.inventoryclean.usecase.command.registeruser.RegisterUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RouterRestTest {

    private WebTestClient webTestClient;

    @Mock
    private LoginUserUseCase loginUserUseCase;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient
                .bindToRouterFunction(new RouterRest().loginUser(new Handler(loginUserUseCase)))
                .configureClient()
                .baseUrl("http://localhost:8083")
                .build();
    }

    @Test
    void loginUser() {

        String token = "accessToken";

        LoginCommand loginCommand = new LoginCommand("username", "password");

        AuthResponse authResponse = new AuthResponse(token);

        when(loginUserUseCase.apply(any(Mono.class))).thenReturn(Mono.just(authResponse));

        webTestClient.post()
                .uri("api/v1/auth/login")
                .body(Mono.just(loginCommand), LoginCommand.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponse.class)
                .value(response -> {
                    assertEquals(token, response.getToken());
                });
    }
}