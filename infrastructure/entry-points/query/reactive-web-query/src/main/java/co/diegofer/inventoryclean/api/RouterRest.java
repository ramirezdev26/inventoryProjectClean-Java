package co.diegofer.inventoryclean.api;

import co.diegofer.inventoryclean.model.InvoiceEntity;
import co.diegofer.inventoryclean.model.branch.Branch;
import co.diegofer.inventoryclean.model.product.Product;
import co.diegofer.inventoryclean.model.supplier.Supplier;
import co.diegofer.inventoryclean.model.user.User;
import co.diegofer.inventoryclean.usecase.generics.InvoiceData;
import co.diegofer.inventoryclean.usecase.query.getallbranches.GetAllBranchesUseCase;
import co.diegofer.inventoryclean.usecase.query.getproductsbybranchid.GetProductsByBranchIdUseCase;
import co.diegofer.inventoryclean.usecase.query.getinvoicesbybranchid.GetInvoicesByBranchIdUseCase;
import co.diegofer.inventoryclean.usecase.query.getsuppliersbybranchid.GetSuppliersByBranchIdUseCase;
import co.diegofer.inventoryclean.usecase.query.getusersbybranchid.GetUsersByBranchIdUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperation(path = "/branches", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = GetAllBranchesUseCase.class, method = RequestMethod.GET,
            beanMethod = "apply",
            operation = @Operation(operationId = "viewAllBranches", tags = "Query usecases",
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success",
                                    content = @Content (array = @ArraySchema(schema = @Schema(implementation = Branch.class))))
                    }))
    public RouterFunction<ServerResponse> viewAllBranches(GetAllBranchesUseCase viewBranches) {
        return route(GET("/branches"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(viewBranches.apply(), Branch.class)));
    }


    @Bean
    @RouterOperation(path = "/products/{branch_id}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = GetProductsByBranchIdUseCase.class,
            method = RequestMethod.GET,
            beanMethod = "apply",
            operation = @Operation(operationId = "getProductsByBranch", tags = "Query usecases",
                    parameters = {
                            @Parameter(name = "branch_id", description = "Branch ID", required = true, in = ParameterIn.PATH)
                    },
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success",
                                    content = @Content (array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
                            @ApiResponse(responseCode = "400", description = "Branch not found")
                    }))
    public RouterFunction<ServerResponse> getProductsByBranch(GetProductsByBranchIdUseCase getProductsByBranchIdUseCase) {
        return route(GET("/products/{branch_id}"),
                request -> getProductsByBranchIdUseCase.apply(request.pathVariable("branch_id"))
                        .collectList()
                        .flatMap(products -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromPublisher(getProductsByBranchIdUseCase.apply(request.pathVariable("branch_id")),
                                        Product.class))
                        ).onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(throwable.getMessage())));
    }

    @Bean
    @RouterOperation(path = "/users/{branch_id}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = GetUsersByBranchIdUseCase.class,
            method = RequestMethod.GET,
            beanMethod = "apply",
            operation = @Operation(operationId = "getUsersByBranch", tags = "Query usecases",
                    parameters = {
                            @Parameter(name = "branch_id", description = "Branch ID", required = true, in = ParameterIn.PATH)
                    },
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success",
                                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))),
                            @ApiResponse(responseCode = "400", description = "Branch not found")
                    }))
    public RouterFunction<ServerResponse> getUsersByBranch(GetUsersByBranchIdUseCase getUsersByBranchIdUseCase) {
        return route(GET("/users/{branch_id}"),
                request -> getUsersByBranchIdUseCase.apply(request.pathVariable("branch_id"))
                        .collectList()
                        .flatMap(products -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromPublisher(getUsersByBranchIdUseCase.apply(request.pathVariable("branch_id")),
                                        User.class))
                        ).onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(throwable.getMessage())));
    }

    @Bean
    @RouterOperation(path = "/suppliers/{branch_id}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = GetSuppliersByBranchIdUseCase.class,
            method = RequestMethod.GET,
            beanMethod = "apply",
            operation = @Operation(operationId = "getSuppliersByBranch", tags = "Query usecases",
                    parameters = {
                            @Parameter(name = "branch_id", description = "Branch ID", required = true, in = ParameterIn.PATH)
                    },
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success",
                                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Supplier.class)))),
                            @ApiResponse(responseCode = "400", description = "Branch not found")
                    }))
    public RouterFunction<ServerResponse> getSuppliersByBranch(GetSuppliersByBranchIdUseCase getSuppliersByBranchIdUseCase) {
        return route(GET("/suppliers/{branch_id}"),
                request -> getSuppliersByBranchIdUseCase.apply(request.pathVariable("branch_id"))
                        .collectList()
                        .flatMap(products -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromPublisher(getSuppliersByBranchIdUseCase.apply(request.pathVariable("branch_id")),
                                        Supplier.class))
                        ).onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(throwable.getMessage())));
    }


    @Bean
    @RouterOperation(path = "/invoices/{branch_id}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = GetInvoicesByBranchIdUseCase.class,
            method = RequestMethod.GET,
            beanMethod = "apply",
            operation = @Operation(operationId = "getInvoicesByBranch", tags = "Query usecases",
                    parameters = {
                            @Parameter(name = "branch_id", description = "Branch ID", required = true, in = ParameterIn.PATH)
                    },
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success",
                                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvoiceData.class)))),
                            @ApiResponse(responseCode = "400", description = "Branch not found")
                    }))
    public RouterFunction<ServerResponse> getInvoicesByBranch (GetInvoicesByBranchIdUseCase getInvoicesByBranchIdUseCase) {
        return route(GET("/invoices/{branch_id}"),
                request -> getInvoicesByBranchIdUseCase.apply(request.pathVariable("branch_id"))
                        .collectList()
                        .flatMap(products -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromPublisher(getInvoicesByBranchIdUseCase.apply(request.pathVariable("branch_id")),
                                        InvoiceData.class))
                        ).onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(throwable.getMessage())));
    }


}