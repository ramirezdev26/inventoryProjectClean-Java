package co.diegofer.inventoryclean.usecase.query.getallbranches;

import co.diegofer.inventoryclean.model.branch.Branch;
import co.diegofer.inventoryclean.model.branch.gateways.BranchRepository;
import co.diegofer.inventoryclean.model.product.Product;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetAllBranchesUseCase {

    private final BranchRepository branchRepository;

    public Flux<Branch> apply() {
        return branchRepository.getAllBranches();
    }


}
