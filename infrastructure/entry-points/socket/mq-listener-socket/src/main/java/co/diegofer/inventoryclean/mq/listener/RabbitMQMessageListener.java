package co.diegofer.inventoryclean.mq.listener;

import co.diegofer.inventoryclean.controller.InvoiceSocketController;
import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.mq.listener.data.Notification;
import co.diegofer.inventoryclean.serializer.JSONMapper;
import co.diegofer.inventoryclean.controller.ProductSocketController;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RabbitMQMessageListener {

    private final ProductSocketController productController;
    private final InvoiceSocketController invoiceController;
    private final JSONMapper eventSerializer;
    public static final String BRANCH_CREATED_SOCKET_QUEUE = "inventory.events.branch.created.socket.queue";
    public static final String PRODUCT_ADDED_SOCKET_QUEUE = "inventory.events.product.added.socket.queue";
    public static final String CUSTOMER_SALE_REGISTERED_SOCKET_QUEUE = "inventory.events.sale.customer.registered.socket.queue";
    public static final String RESELLER_SALE_REGISTERED_SOCKET_QUEUE = "inventory.events.sale.reseller.registered.socket.queue";
    public static final String STOCK_ADDED_SOCKET_QUEUE = "inventory.events.stock.added.socket.queue";
    public static final String SALE_REGISTERED_SOCKET_QUEUE = "inventory.events.sale.registered.socket.queue";



    @RabbitListener(queues = BRANCH_CREATED_SOCKET_QUEUE)
    public void receiveBranchCreatedEvent (String message) throws ClassNotFoundException {
        Notification notification = Notification.from(message);
        DomainEvent event = (DomainEvent) eventSerializer
                .readFromJson(notification.getBody(), Class.forName(notification.getType()));
        productController.sendBranchCreated("mainSpace", event);
    }

    @RabbitListener(queues = {PRODUCT_ADDED_SOCKET_QUEUE, CUSTOMER_SALE_REGISTERED_SOCKET_QUEUE, RESELLER_SALE_REGISTERED_SOCKET_QUEUE, STOCK_ADDED_SOCKET_QUEUE })
    public void receiveProductEvents(String message) throws ClassNotFoundException {
        Notification notification = Notification.from(message);
        DomainEvent event = (DomainEvent) eventSerializer
                .readFromJson(notification.getBody(), Class.forName(notification.getType()));
        productController.sendProductEventToSocket(event.aggregateRootId(), event);
    }

    @RabbitListener(queues = {SALE_REGISTERED_SOCKET_QUEUE})
    public void receiveInvoiceEvents(String message) throws ClassNotFoundException {
        Notification notification = Notification.from(message);
        DomainEvent event = (DomainEvent) eventSerializer
                .readFromJson(notification.getBody(), Class.forName(notification.getType()));
        invoiceController.sendInvoiceEventToSocket(event.aggregateRootId(), event);
    }

}
