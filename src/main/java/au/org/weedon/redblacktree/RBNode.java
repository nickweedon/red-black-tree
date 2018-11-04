package au.org.weedon.redblacktree;

import java.util.Optional;

public interface RBNode<T> {
    enum RBColor { RED, BLACK };

    Optional<T> getValue();
    void setValue(T value);
    RBNode<T> getParent();
    void setParent(RBNode<T> parent);
    RBNode<T> getLeft();
    void setLeft(RBNode<T> left);
    RBNode<T> getRight();
    void setRight(RBNode<T> right);
    RBColor getColor();
    void setColor(RBColor color);
    boolean isNil();
}
