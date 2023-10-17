package co.diegofer.inventoryclean.r2dbc;

import co.diegofer.inventoryclean.model.product.Product;
import co.diegofer.inventoryclean.model.user.User;
import co.diegofer.inventoryclean.r2dbc.data.UserData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryR2dbc extends ReactiveCrudRepository<UserData, String> {

    @Query("INSERT INTO User(id, name, last_name, password, email, role, branch_id) VALUES(:id, :name, :last_name, :password, :email, :role, :branchId)")
    Mono<UserData> saveUser(@Param("id") String id,
                            @Param("name") String name,
                            @Param("last_name") String last_name,
                            @Param("password") String password,
                            @Param("email") String email,
                            @Param("role") String role,
                            @Param("branchId") String branchId);

    @Query("SELECT * FROM User WHERE email = :email")
    Mono<UserData> findByEmail(@Param("email") String email);

    Flux<UserData> findByBranchId(String branchId);

    @Query("SELECT * FROM User WHERE id = :id")
    Mono<UserData> findUserById(@Param("id") String id);

}
