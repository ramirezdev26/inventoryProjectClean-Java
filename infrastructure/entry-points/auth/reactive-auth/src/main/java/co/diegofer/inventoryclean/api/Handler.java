package co.diegofer.inventoryclean.api;

import co.diegofer.inventoryclean.model.commands.*;

import co.diegofer.inventoryclean.usecase.auth.loginuser.LoginUserUseCase;
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


    private final LoginUserUseCase loginUserUseCase;



    public Mono<ServerResponse> listenPOSTLoginUser(ServerRequest serverRequest) {

        return loginUserUseCase.apply(serverRequest.bodyToMono(LoginCommand.class))
                .flatMap(domainEvent -> {
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(domainEvent));
                })
                .onErrorResume(Exception.class, e -> {
                    return ServerResponse.badRequest().bodyValue(e.getMessage());
                });
    }

}
