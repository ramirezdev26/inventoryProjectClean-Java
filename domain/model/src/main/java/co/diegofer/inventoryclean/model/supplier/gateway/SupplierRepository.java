package co.diegofer.inventoryclean.model.supplier.gateway;

import co.diegofer.inventoryclean.model.supplier.Supplier;
import co.diegofer.inventoryclean.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SupplierRepository {

    public Mono<Supplier> saveASupplier(Supplier supplier);
    public Flux<Supplier> findSupplierByBranch(String branchId);

}
