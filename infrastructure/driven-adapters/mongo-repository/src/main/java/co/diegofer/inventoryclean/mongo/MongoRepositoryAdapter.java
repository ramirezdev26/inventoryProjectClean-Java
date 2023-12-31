package co.diegofer.inventoryclean.mongo;

import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.mongo.data.StoredEvent;
import co.diegofer.inventoryclean.serializer.JSONMapper;
import co.diegofer.inventoryclean.usecase.generics.DomainEventRepository;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Date;

@Repository
public class MongoRepositoryAdapter implements DomainEventRepository
// implements ModelRepository from domain
{

    private final ReactiveMongoTemplate template;
    private final JSONMapper eventSerializer;

    public MongoRepositoryAdapter(ReactiveMongoTemplate template, JSONMapper eventSerializer) {
        this.template = template;
        this.eventSerializer = eventSerializer;
    }

    @Override
    public Flux<DomainEvent> findById(String aggregateId) {
        return template.find(new Query(Criteria.where("aggregateRootId").is(aggregateId)), StoredEvent.class)
                .sort(Comparator.comparing(StoredEvent::getOccurredOn))
                .map(storedEvent -> storedEvent.deserializeEvent(eventSerializer));
    }

    @Override
    public Mono<DomainEvent> saveEvent(DomainEvent event) {
        StoredEvent storedEvent = new StoredEvent();
        storedEvent.setAggregateRootId(event.aggregateRootId());
        storedEvent.setEventBody(StoredEvent.wrapEvent(event, eventSerializer));
        storedEvent.setOccurredOn(new Date());
        storedEvent.setTypeName(event.getClass().getTypeName());
        return template.save(storedEvent)
                .map(eventStored -> eventStored.deserializeEvent(eventSerializer));
    }
}
