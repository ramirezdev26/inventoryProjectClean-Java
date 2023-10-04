package co.diegofer.inventoryclean.usecase.mysqlupdaterv2;

import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.usecase.MySqlUpdater;

import java.util.function.Consumer;

public class MySQLUpdaterV2UseCase implements Consumer<DomainEvent> {

    private final MySqlUpdater updater;


    public MySQLUpdaterV2UseCase(MySqlUpdater updater) {
        this.updater = updater;
    }


    @Override
    public void accept(DomainEvent event) {
        updater.applyEvent(event);
    }
}

