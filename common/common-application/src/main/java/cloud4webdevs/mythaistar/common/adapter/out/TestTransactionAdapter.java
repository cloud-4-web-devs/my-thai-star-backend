package cloud4webdevs.mythaistar.common.adapter.out;

import cloud4webdevs.mythaistar.common.port.out.TransactionPort;
import cloud4webdevs.mythaistar.common.port.out.TransactionalMapper;

public class TestTransactionAdapter implements TransactionPort {

    private int callCount = 0;

    @Override
    public <T> T inTransaction(TransactionalMapper<T> handler) {
        ++callCount;
        return handler.accept();
    }

    public boolean hasBeenCalled() {
        return callCount > 0;
    }

    public boolean hasBeenCalledTimes(int times) {
        return callCount == times;
    }

    public void reset() {
        callCount = 0;
    }
}
