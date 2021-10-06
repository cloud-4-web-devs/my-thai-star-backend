package cloud4webdevs.mythaistar.common.domain.exception;

public abstract class NotFoundException extends BusinessException {
    public NotFoundException(String msg) {
        super(msg);
    }
}
