package cloud4webdevs.mythaistar.common.port.out;

@FunctionalInterface
public interface TransactionalMapper<T> {
    T accept();
}
