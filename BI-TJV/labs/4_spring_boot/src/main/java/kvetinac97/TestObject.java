package kvetinac97;

public class TestObject {

    private final long id;
    private final String content;

    public TestObject ( long id, String content ) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

}
