package au.org.weedon.redblacktree;

import java.util.Optional;

public class RBNilNode<T> implements RBNode<T> {

    @Override
    public Optional<T> getValue() {
        return Optional.empty();
    }

    @Override
    public void setValue(T value) {
        throw new NullPointerException("Attempted to setValue() on a RBNilNode");
    }

    @Override
    public RBNode<T> getParent() {
        throw new NullPointerException("Attempted to getParent() on a RBNilNode");
    }

    @Override
    public void setParent(RBNode parent) {
        throw new NullPointerException("Attempted to setParent() on a RBNilNode");
    }

    @Override
    public RBNode<T> getLeft() {
        throw new NullPointerException("Attempted to getLeft() on a RBNilNode");
    }

    @Override
    public void setLeft(RBNode left) {
        throw new NullPointerException("Attempted to setLeft() on a RBNilNode");
    }

    @Override
    public RBNode<T> getRight() {
        throw new NullPointerException("Attempted to getRight() on a RBNilNode");
    }

    @Override
    public void setRight(RBNode right) {
        throw new NullPointerException("Attempted to setRight() on a RBNilNode");
    }

    @Override
    public RBColor getColor() {
        return RBColor.BLACK;
    }

    @Override
    public void setColor(RBColor color) {
        throw new NullPointerException("Attempted to setColor() on a RBNilNode");
    }

    @Override
    public boolean isNil() {
        return true;
    }
}
