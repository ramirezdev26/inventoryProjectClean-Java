package co.diegofer.inventoryclean.mongo;

import co.diegofer.inventoryclean.model.InvoiceEntity;
import co.diegofer.inventoryclean.serializer.JSONMapper;
import co.diegofer.inventoryclean.usecase.generics.InvoiceRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Date;

@Repository
public class MongoInvoiceRepositoryAdapter implements InvoiceRepository
{

    private final ReactiveMongoTemplate template;
    private final JSONMapper eventSerializer;

    public MongoInvoiceRepositoryAdapter(ReactiveMongoTemplate template, JSONMapper eventSerializer) {
        this.template = template;
        this.eventSerializer = eventSerializer;
    }


    @Override
    public Mono<InvoiceEntity> saveInvoice(InvoiceEntity invoice) {
        return template.save(invoice);
    }
}
