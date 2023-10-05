package co.diegofer.inventoryclean.mongo;

import co.diegofer.inventoryclean.model.InvoiceEntity;
import co.diegofer.inventoryclean.serializer.JSONMapper;
import co.diegofer.inventoryclean.usecase.generics.InvoiceData;
import co.diegofer.inventoryclean.usecase.generics.InvoiceRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class MongoInvoiceRepositoryAdapter implements InvoiceRepository
{

    private final ReactiveMongoTemplate template;
    private final JSONMapper eventSerializer;
    private final ObjectMapper mapper;


    public MongoInvoiceRepositoryAdapter(ReactiveMongoTemplate template, JSONMapper eventSerializer, ObjectMapper mapper) {
        this.template = template;
        this.eventSerializer = eventSerializer;
        this.mapper = mapper;
    }


    @Override
    public Mono<InvoiceEntity> saveInvoice(InvoiceEntity invoice) {
        return template.save(invoice);
    }

    @Override
    public Flux<InvoiceData> getInvoicesByBranchId(String branchId) {
        Query query = new Query(Criteria.where("branchId").is(branchId));
        return template.find(query, InvoiceData.class, "invoiceEntity");}
}
