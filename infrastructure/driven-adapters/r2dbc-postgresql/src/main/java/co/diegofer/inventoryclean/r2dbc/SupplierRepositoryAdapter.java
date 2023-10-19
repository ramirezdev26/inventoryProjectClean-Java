package co.diegofer.inventoryclean.r2dbc;

import co.diegofer.inventoryclean.model.supplier.Supplier;
import co.diegofer.inventoryclean.model.supplier.gateway.SupplierRepository;
import co.diegofer.inventoryclean.model.user.User;
import co.diegofer.inventoryclean.r2dbc.data.SupplierData;
import co.diegofer.inventoryclean.r2dbc.data.UserData;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class SupplierRepositoryAdapter implements SupplierRepository {

    private final ObjectMapper mapper;

    private final SupplierRepositoryR2dbc supplierRepositoryR2dbc;

    @Override
    public Mono<Supplier> saveASupplier(Supplier supplier) {
        SupplierData supplierData = mapper.map(supplier, SupplierData.class);
        return supplierRepositoryR2dbc.saveASupplier(supplierData.getId(),supplierData.getName(),
                supplierData.getNumber(),supplierData.getEmail(),
                supplierData.getPayment_term(),supplierData.getBranchId()).thenReturn(
                mapper.map(supplierData, Supplier.class)
        ).onErrorMap(DataIntegrityViolationException.class, e -> new DataIntegrityViolationException("Error creating supplier: "+e.getMessage()));    }

    @Override
    public Flux<Supplier> findSupplierByBranch(String branchId) {
        return supplierRepositoryR2dbc.findByBranchId(branchId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Branch not found")))
                .map(supplier -> mapper.map(supplier, Supplier.class));
    }
}
