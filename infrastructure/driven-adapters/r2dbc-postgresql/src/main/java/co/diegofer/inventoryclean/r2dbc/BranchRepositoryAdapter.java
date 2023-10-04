package co.diegofer.inventoryclean.r2dbc;

import co.diegofer.inventoryclean.model.branch.Branch;
import co.diegofer.inventoryclean.model.branch.gateways.BranchRepository;
import co.diegofer.inventoryclean.r2dbc.data.BranchData;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BranchRepositoryAdapter implements BranchRepository {

    private final ObjectMapper mapper;

    private final BranchRepositoryR2dbc branchRepository;

    private final DatabaseClient dbClient;

    @Override
    public Mono<Branch> saveABranch(Branch branch) {
        BranchData branchData = mapper.map(branch, BranchData.class);
        dbClient.sql("insert into Branch(id, name, country, city) values(:id, :name, :country, :city)")
                .bind("id", branch.getId())
                .bind("name", branchData.getName())
                .bind("country", branchData.getCountry())
                .bind("city", branchData.getCity())
                .fetch()
                .one()
                .subscribe();
        return Mono.just(branch);
    }
}
