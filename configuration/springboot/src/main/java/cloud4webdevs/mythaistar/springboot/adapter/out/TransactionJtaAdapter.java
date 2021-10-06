package cloud4webdevs.mythaistar.springboot.adapter.out;

import cloud4webdevs.mythaistar.common.port.out.TransactionPort;
import cloud4webdevs.mythaistar.common.port.out.TransactionalConsumer;
import cloud4webdevs.mythaistar.common.port.out.TransactionalMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * It will be difficult to implement it in separation of Springboot dependency because we need Spring IoC and component
 * scan to handle @Transactional properly if we don't want to get our hands dirty with JTA/manual tx handling (instead of * @Transactional).
 * So, for other container framework (such as Quarkus) we would need a separate implementation.
 */
@Service
public class TransactionJtaAdapter implements TransactionPort {
    @Override
    @Transactional
    public <T> T inTransaction(TransactionalMapper<T> handler) {
        return handler.accept();
    }

    @Override
    @Transactional
    public void withTransaction(TransactionalConsumer consumer) {
        TransactionPort.super.withTransaction(consumer);
    }
}
