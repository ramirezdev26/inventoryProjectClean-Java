package co.diegofer.inventoryclean.usecase.query.getusersbybranchid;

import co.diegofer.inventoryclean.model.user.User;
import co.diegofer.inventoryclean.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetUsersByBranchIdUseCase {

    private final UserRepository productRepository;

    public Flux<User> apply(String branchId) {
        return productRepository.findUsersByBranch(branchId);
    }

}
