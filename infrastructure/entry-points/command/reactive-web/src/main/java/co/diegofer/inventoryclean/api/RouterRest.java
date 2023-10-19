package co.diegofer.inventoryclean.api;

import co.diegofer.inventoryclean.model.branch.Branch;
import co.diegofer.inventoryclean.model.commands.*;
import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.RegisterFinalCustomerSaleCommand;
import co.diegofer.inventoryclean.model.events.*;
import co.diegofer.inventoryclean.model.product.Product;
import co.diegofer.inventoryclean.usecase.command.addstocktoproduct.AddStockToProductUseCase;
import co.diegofer.inventoryclean.usecase.command.changeUserRole.ChangeUserRoleUseCase;
import co.diegofer.inventoryclean.usecase.command.registerSupplier.RegisterSupplierUseCase;
import co.diegofer.inventoryclean.usecase.command.registerbranch.RegisterBranchUseCase;
import co.diegofer.inventoryclean.usecase.command.registerfinalcustomersale.RegisterFinalCustomerSaleUseCase;
import co.diegofer.inventoryclean.usecase.command.registerproduct.RegisterProductUseCase;
import co.diegofer.inventoryclean.usecase.command.registerresellercustomersale.RegisterResellerCustomerSaleUseCase;
import co.diegofer.inventoryclean.usecase.command.registeruser.RegisterUserUseCase;
import co.diegofer.inventoryclean.usecase.query.getallbranches.GetAllBranchesUseCase;
import co.diegofer.inventoryclean.usecase.query.getproductsbybranchid.GetProductsByBranchIdUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {


    @Bean
    @RouterOperation(path = "/branches", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = RegisterBranchUseCase.class, method = RequestMethod.POST,
            beanMethod = "apply",
            operation = @Operation(operationId = "saveBranch", tags = "Command usecases",
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = BranchCreated.class))),
                            @ApiResponse(responseCode = "400")},
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RegisterBranchCommand.class)))))
    public RouterFunction<ServerResponse> saveBranch(Handler handler){
        return route(POST("/branches").and(accept(MediaType.APPLICATION_JSON)),
                handler::listenPOSTRegisterBranchUseCase);
    }


    @Bean
    @RouterOperation(path = "/product", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = RegisterProductUseCase.class, method = RequestMethod.POST,
            beanMethod = "apply",
            operation = @Operation(operationId = "saveProduct", tags = "Command usecases",
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ProductAdded.class))),
                            @ApiResponse(responseCode = "400")},
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = AddProductCommand.class)))))
    public RouterFunction<ServerResponse> saveProduct(Handler handler){
        return route(POST("/product").and(accept(MediaType.APPLICATION_JSON)),
                handler::listenPOSTAddProduct);
    }

    @Bean
    @RouterOperation(path = "/user", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = RegisterUserUseCase.class, method = RequestMethod.POST,
            beanMethod = "apply",
            operation = @Operation(operationId = "saveUser", tags = "Command usecases",
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = UserAdded.class))),
                            @ApiResponse(responseCode = "400")},
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RegisterUserCommand.class)))))
    public RouterFunction<ServerResponse> saveUser(Handler handler){
        return route(POST("/user").and(accept(MediaType.APPLICATION_JSON)),
                handler::listenPOSTRegisterUser);
    }

    @Bean
    @RouterOperation(path = "/supplier", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = RegisterSupplierUseCase.class, method = RequestMethod.POST,
            beanMethod = "apply",
            operation = @Operation(operationId = "saveSupplier", tags = "Command usecases",
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = SupplierAdded.class))),
                            @ApiResponse(responseCode = "400")},
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RegisterSupplierCommand.class)))))
    public RouterFunction<ServerResponse> saveSupplier(Handler handler){
        return route(POST("/supplier").and(accept(MediaType.APPLICATION_JSON)),
                handler::listenPOSTRegisterSupplier);
    }


    @Bean
    @RouterOperation(path = "product/purchase", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = AddStockToProductUseCase.class, method = RequestMethod.PATCH, beanMethod = "apply",
            operation = @Operation(operationId = "patchAddProductStock", tags = "Command usecases",
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Accepted", content = @Content(schema = @Schema(implementation = StockToProductAdded.class))),
                            @ApiResponse(responseCode = "400")},
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = AddStockToProductCommand.class)))))
    public RouterFunction<ServerResponse> patchAddProductStock(Handler handler) {
        return route(PATCH("product/purchase").and(accept(MediaType.APPLICATION_JSON)),
                handler::listenPATCHAddStock);
    }

    @Bean
    @RouterOperation(path = "user/role", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = ChangeUserRoleUseCase.class, method = RequestMethod.PATCH, beanMethod = "apply",
            operation = @Operation(operationId = "patchChangeRoleUser", tags = "Command usecases",
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Accepted", content = @Content(schema = @Schema(implementation = RoleToUserChanged.class))),
                            @ApiResponse(responseCode = "400")},
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ChangeUserRoleCommand.class)))))
    public RouterFunction<ServerResponse> patchChangeRoleUser(Handler handler) {
        return route(PATCH("user/role").and(accept(MediaType.APPLICATION_JSON)),
                handler::listenPATCHChangeRole);
    }

    @Bean
    @RouterOperation(path = "product/customer-sale", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = RegisterFinalCustomerSaleUseCase.class, method = RequestMethod.PATCH, beanMethod = "apply",
            operation = @Operation(operationId = "patchRegisterFinalCustomerSale", tags = "Command usecases",
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Accepted", content = @Content(schema = @Schema(implementation = FinalCustomerSaleRegistered.class))),
                            @ApiResponse(responseCode = "400")},
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RegisterFinalCustomerSaleCommand.class)))))
    public RouterFunction<ServerResponse> patchRegisterFinalCustomerSale(Handler handler){
        return route(PATCH("product/customer-sale").and(accept(MediaType.APPLICATION_JSON)),
                handler::listenPATCHRegisterFinalCustomerSale);
    }

    @Bean
    @RouterOperation(path = "product/reseller-sale", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = RegisterResellerCustomerSaleUseCase.class, method = RequestMethod.PATCH, beanMethod = "apply",
            operation = @Operation(operationId = "patchRegisterResellerCustomerSale", tags = "Command usecases",
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Accepted", content = @Content(schema = @Schema(implementation = ResellerCustomerSaleRegistered.class))),
                            @ApiResponse(responseCode = "400")},
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RegisterResellerCustomerSaleCommand.class)))))
    public RouterFunction<ServerResponse> patchRegisterResellerCustomerSale(Handler handler){
        return route(PATCH("product/reseller-sale").and(accept(MediaType.APPLICATION_JSON)),
                handler::listenPATCHResellerFinalCustomerSale);
    }

}
