package co.diegofer.inventoryclean.r2dbc;

import co.diegofer.inventoryclean.model.supplier.Supplier;
import co.diegofer.inventoryclean.r2dbc.data.SupplierData;
import co.diegofer.inventoryclean.r2dbc.data.UserData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SupplierRepositoryR2dbc extends ReactiveCrudRepository<SupplierData, String> {


    @Query("INSERT INTO Supplier(id, name, number, email, payment_term, branch_id) VALUES(:id, :name, :number, :email, :payment_term, :branchId)")
    Mono<SupplierData> saveASupplier(@Param("id") String id,
                            @Param("name") String name,
                            @Param("number") Integer number,
                            @Param("email") String email,
                            @Param("payment_term") String payment_term,
                            @Param("branchId") String branchId);

    Flux<SupplierData> findByBranchId(String branchId);

}
