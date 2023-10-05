package co.diegofer.inventoryclean.usecase.generics;

import co.diegofer.inventoryclean.model.InvoiceEntity;
import co.diegofer.inventoryclean.model.generic.DomainEvent;
import reactor.core.publisher.Mono;

public interface InvoiceRepository {
    Mono<InvoiceEntity> saveInvoice(InvoiceEntity invoice);
}
