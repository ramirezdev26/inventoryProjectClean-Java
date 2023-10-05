package co.diegofer.inventoryclean.mq.listener;

import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.mq.listener.data.Notification;
import co.diegofer.inventoryclean.serializer.JSONMapper;
import co.diegofer.inventoryclean.controller.SocketController;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RabbitMQMessageListener {

    private final SocketController controller;
    private final JSONMapper eventSerializer;
    public static final String BRANCH_CREATED_SOCKET_QUEUE = "inventory.events.branch.created.socket.queue";

    @RabbitListener(queues = BRANCH_CREATED_SOCKET_QUEUE)
    public void process(String message) throws ClassNotFoundException {
        Notification notification = Notification.from(message);
        DomainEvent event = (DomainEvent) eventSerializer
                .readFromJson(notification.getBody(), Class.forName(notification.getType()));
        controller.sendBranchCreated("mainSpace", event);
    }
}
