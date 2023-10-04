package co.diegofer.inventoryclean.mq.listener;

import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.mq.listener.data.Notification;
import co.diegofer.inventoryclean.serializer.JSONMapper;
import co.diegofer.inventoryclean.usecase.mysqlupdaterv2.MySQLUpdaterV2UseCase;
import co.diegofer.inventoryclean.usecase.query.mysqlupdaterusecase.MySQLUpdaterUseCase;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RabbitMQMessageListener {

    private final MySQLUpdaterV2UseCase useCase;
    private final JSONMapper eventSerializer;
    public static final String BRANCH_QUEUE = "inventory.events.branch.created.queue";

    @RabbitListener(queues = BRANCH_QUEUE)
    public void process(String message) throws ClassNotFoundException {
        Notification notification = Notification.from(message);
        DomainEvent event = (DomainEvent) eventSerializer
                .readFromJson(notification.getBody(), Class.forName(notification.getType()));
        useCase.accept(event);
    }
}
