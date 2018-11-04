package au.org.weedon.redblacktree;

public class RBNodeBuilder<T> {

    private RBNode left;
    private RBNode right;
    private RBNode parent;
    private RBNode.RBColor color;
    private Object value;

    private static RBNilNode nullNode = new RBNilNode();

    public RBNodeBuilder<T> setLeft(RBNode<T> left) {
        this.left = left;
        return this;
    }

    public RBNodeBuilder<T> setRight(RBNode<T> right) {
        this.right = right;
        return this;
    }

    public RBNodeBuilder<T> setParent(RBNode<T> parent) {
        this.parent = parent;
        return this;
    }

    public RBNodeBuilder<T> setColor(RBNode.RBColor color) {
        this.color = color;
        return this;
    }

    public RBNodeBuilder<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public RBNode<T> build() {
        //noinspection unchecked
        return new RBNodeImpl(
                parent == null ? buildNullNode() : parent,
                left == null ? buildNullNode() : left,
                right == null ? buildNullNode() : right,
                color, value);
    }

    static <T> RBNode<T> buildNullNode() {
        //noinspection unchecked
        return (RBNode<T>) nullNode;
    }
}
