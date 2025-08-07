package ps.exalt.events;

public abstract class MyData<T> {
    private final T id;

    public MyData(T id) {
        this.id = id;
    }

    public T getId() {
        return id;
    }
}
