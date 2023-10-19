package co.diegofer.inventoryclean.api;

import co.diegofer.inventoryclean.model.commands.*;
import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.RegisterFinalCustomerSaleCommand;
import co.diegofer.inventoryclean.usecase.command.addstocktoproduct.AddStockToProductUseCase;
import co.diegofer.inventoryclean.usecase.command.changeUserRole.ChangeUserRoleUseCase;
import co.diegofer.inventoryclean.usecase.command.registerSupplier.RegisterSupplierUseCase;
import co.diegofer.inventoryclean.usecase.command.registerbranch.RegisterBranchUseCase;
import co.diegofer.inventoryclean.usecase.command.registerfinalcustomersale.RegisterFinalCustomerSaleUseCase;
import co.diegofer.inventoryclean.usecase.command.registerproduct.RegisterProductUseCase;
import co.diegofer.inventoryclean.usecase.command.registerresellercustomersale.RegisterResellerCustomerSaleUseCase;
import co.diegofer.inventoryclean.usecase.command.registeruser.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final RegisterBranchUseCase registerBranchUseCase;

    private final RegisterProductUseCase registerProductUseCase;

    private final RegisterUserUseCase registerUserUseCase;
    private final RegisterSupplierUseCase registerSupplierUseCase;
    private final AddStockToProductUseCase addStockToProductUseCase;
    private final RegisterFinalCustomerSaleUseCase registerFinalCustomerSaleUseCase;
    private final RegisterResellerCustomerSaleUseCase registerResellerCustomerSaleUseCaseCommand;
    private final ChangeUserRoleUseCase changeUserRoleUseCase;

    public Mono<ServerResponse> listenPOSTRegisterBranchUseCase(ServerRequest serverRequest) {

        return registerBranchUseCase.apply(serverRequest.bodyToMono(RegisterBranchCommand.class))
                .flatMap(domainEvent -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(domainEvent))).next()
                .onErrorResume(Exception.class, e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> listenPOSTAddProduct(ServerRequest serverRequest) {

        return registerProductUseCase.apply(serverRequest.bodyToMono(AddProductCommand.class))
                .flatMap(domainEvent -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(domainEvent))).next()
                .onErrorResume(Exception.class, e -> ServerResponse.badRequest().bodyValue(e.getMessage()));

    }

    public Mono<ServerResponse> listenPOSTRegisterUser(ServerRequest serverRequest) {

        return registerUserUseCase.apply(serverRequest.bodyToMono(RegisterUserCommand.class))
                .flatMap(domainEvent -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(domainEvent))).next()
                .onErrorResume(Exception.class, e -> ServerResponse.badRequest().bodyValue(e.getMessage()));

    }

    public Mono<ServerResponse> listenPOSTRegisterSupplier(ServerRequest serverRequest) {

        return registerSupplierUseCase.apply(serverRequest.bodyToMono(RegisterSupplierCommand.class))
                .flatMap(domainEvent -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(domainEvent))).next()
                .onErrorResume(Exception.class, e -> ServerResponse.badRequest().bodyValue(e.getMessage()));

    }

    public Mono<ServerResponse> listenPATCHAddStock(ServerRequest serverRequest) {

        return addStockToProductUseCase.apply(serverRequest.bodyToMono(AddStockToProductCommand.class))
                .flatMap(domainEvent -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(domainEvent))).next()
                .onErrorResume(Exception.class, e -> ServerResponse.badRequest().bodyValue(e.getMessage()));

    }

    public Mono<ServerResponse> listenPATCHChangeRole(ServerRequest serverRequest) {

        return changeUserRoleUseCase.apply(serverRequest.bodyToMono(ChangeUserRoleCommand.class))
                .flatMap(domainEvent -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(domainEvent))).next()
                .onErrorResume(Exception.class, e -> ServerResponse.badRequest().bodyValue(e.getMessage()));

    }



    public Mono<ServerResponse> listenPATCHRegisterFinalCustomerSale(ServerRequest serverRequest) {

        return registerFinalCustomerSaleUseCase.apply(serverRequest.bodyToMono(RegisterFinalCustomerSaleCommand.class))
                .flatMap(domainEvent -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(domainEvent))).next()
                .onErrorResume(Exception.class, e -> ServerResponse.badRequest().bodyValue(e.getMessage()));

    }

    public Mono<ServerResponse> listenPATCHResellerFinalCustomerSale(ServerRequest serverRequest) {

        return registerResellerCustomerSaleUseCaseCommand.apply(serverRequest.bodyToMono(RegisterResellerCustomerSaleCommand.class))
                .flatMap(domainEvent -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(domainEvent))).next()
                .onErrorResume(Exception.class, e -> ServerResponse.badRequest().bodyValue(e.getMessage()));

    }


}
