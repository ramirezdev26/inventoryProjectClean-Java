package co.diegofer.inventoryclean.usecase.query.getsuppliersbybranchid;

import co.diegofer.inventoryclean.model.supplier.Supplier;
import co.diegofer.inventoryclean.model.supplier.gateway.SupplierRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetSuppliersByBranchIdUseCase {

    private final SupplierRepository supplierRepository;

    public Flux<Supplier> apply(String branchId){ return supplierRepository.findSupplierByBranch(branchId); }

}
