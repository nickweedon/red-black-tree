package au.org.weedon.redblacktree;

import java.util.Optional;

public class RBNodeImpl<T> implements RBNode<T> {

    private RBNode<T> parent;
    private RBNode<T> left;
    private RBNode<T> right;
    private RBColor color;
    private T value;

    public RBNodeImpl(RBNode<T> parent, RBNode<T> left, RBNode<T> right, RBColor color, T value) {
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.color = color;
        //noinspection unchecked
        this.value = value;
    }

    @Override
    public Optional<T> getValue() {
        //noinspection unchecked
        return value == null ? Optional.empty() : Optional.of(value);
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public RBNode<T> getParent() {
        return parent;
    }

    @Override
    public void setParent(RBNode<T> parent) {
        this.parent = parent;
    }

    @Override
    public RBNode<T> getLeft() {
        return left;
    }

    @Override
    public void setLeft(RBNode<T> left) {
        this.left = left;
    }

    @Override
    public RBNode<T> getRight() {
        return right;
    }

    @Override
    public void setRight(RBNode<T> right) {
        this.right = right;
    }

    @Override
    public RBColor getColor() {
        return color;
    }

    @Override
    public void setColor(RBColor color) {
        this.color = color;
    }

    @Override
    public boolean isNil() {
        return false;
    }
}
