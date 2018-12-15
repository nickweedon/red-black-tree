package au.org.weedon.redblacktree;

import java.util.*;

public class RBTree<V extends Comparable> {

	private RBNodeBuilder<V> nodeBuilder = new RBNodeBuilder<>();

	private RBNode<V> head;

	public RBTree() {
		nodeBuilder.setColor(RBNode.RBColor.RED);
		head = RBNodeBuilder.buildNullNode();
	}

	public Iterable<RBNode<V>> iterateNodesDFS(DFSNodeIterator.TraversalOrder traversalOrder) {
		return () -> new DFSNodeIterator<>(head, traversalOrder);
	}

	public static class DFSValueIterator<V> implements Iterator<V> {

		private DFSNodeIterator<V> dfsNodeIterator;

		public DFSValueIterator(DFSNodeIterator<V> dfsNodeIterator) {
			this.dfsNodeIterator = dfsNodeIterator;
		}

		@Override
		public boolean hasNext() {
			return dfsNodeIterator.hasNext();
		}

		@Override
		public V next() {
			return dfsNodeIterator.next().getValue().orElse(null);
		}
	}


	public static class DFSNodeIterator<V> implements Iterator<RBNode<V>> {

		public enum TraversalOrder { Preorder, Inorder, Postorder };
		private TraversalOrder traversalOrder;
		private RBNode<V> queuedNode;

		public static class BinaryTreeStackFrame<V> {
			private TraversalOrder traversalState;
			private RBNode<V> rbNode;

			public BinaryTreeStackFrame(TraversalOrder traversalState, RBNode<V> rbNode) {
				this.traversalState = traversalState;
				this.rbNode = rbNode;
			}

			public BinaryTreeStackFrame(RBNode<V> rbNode) {
				this.traversalState = TraversalOrder.Preorder;
				this.rbNode = rbNode;
			}

			public TraversalOrder getTraversalState() {
				return traversalState;
			}

			public void setTraversalState(TraversalOrder traversalState) {
				this.traversalState = traversalState;
			}

			public RBNode<V> getRbNode() {
				return rbNode;
			}

			public void setRbNode(RBNode<V> rbNode) {
				this.rbNode = rbNode;
			}
		}

		Stack<BinaryTreeStackFrame<V>> dfsStack = new Stack<>();

		public DFSNodeIterator(RBNode<V> head, TraversalOrder traversalOrder) {
			this.traversalOrder = traversalOrder;
			if(!head.isNil()) {
				dfsStack.add(new BinaryTreeStackFrame<>(head));
			}
		}

		private RBNode<V> getNextNode() {
			while(
					!dfsStack.empty()
            ) {
				BinaryTreeStackFrame<V> stackFrame = dfsStack.pop();
				RBNode<V> node = stackFrame.getRbNode();

				switch(stackFrame.getTraversalState()) {
					case Preorder: {
						stackFrame.setTraversalState(TraversalOrder.Inorder);
						dfsStack.add(stackFrame);
						if(
						        !node.getLeft().isNil()
                        ) {
							dfsStack.add(new BinaryTreeStackFrame<V>(node.getLeft()));
						}
						if(
						        traversalOrder == TraversalOrder.Preorder
                        ) {
							return node;
						}
						break;
					}
					case Inorder: {
						stackFrame.setTraversalState(TraversalOrder.Postorder);
						dfsStack.add(stackFrame);
						if(
						        !node.getRight().isNil()
                        ) {
							dfsStack.add(new BinaryTreeStackFrame<V>(node.getRight()));
						}
						if(
						        traversalOrder == TraversalOrder.Inorder
                        ) {
							return node;
						}
						break;
					}
					case Postorder: {
						if(
						        traversalOrder == TraversalOrder.Postorder
                        ) {
							return node;
						}
						break;
					}
				}
			}
			return null;
		}

		@Override
		public boolean hasNext() {
			// Crap
			if(
			        queuedNode != null
            ) {
				return true;
			}
			queuedNode = getNextNode();
			return queuedNode != null;
		}

		@Override
		public RBNode<V> next() {
			RBNode<V> returnedNode = queuedNode != null
					? queuedNode
					: getNextNode();

			queuedNode = null;

			if(
			        returnedNode == null
            ) {
				throw new RuntimeException("DFSNodeIterator next() called before calling hasNext()");
			}

			return returnedNode;
		}
	}

	public void add(V value) {

		if(
		        head.isNil()
        ) {
			head = nodeBuilder.setValue(value).build();
			return;
		}

		addNode(head, nodeBuilder.setValue(value).build());
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private int compareNodes(Optional<V> valueA, Optional<V> valueB) {
		if(
		        !valueA.isPresent() && !valueB.isPresent()
        ) {
			return 0;
		}
		//noinspection ConstantConditions,unchecked
		return valueA.map(value -> value.compareTo(valueB.orElse(null)))
				.orElseGet(() -> -valueB.get().compareTo(valueA.orElse(null)));
	}

	private void addNode(RBNode<V> currentNode, RBNode<V> valueNode) {

		if(compareNodes(valueNode.getValue(), currentNode.getValue()) > 0) {
			if(
			        currentNode.getLeft().isNil()
            ) {
				currentNode.setLeft(valueNode);
				valueNode.setParent(currentNode);
			} else {
				addNode(currentNode.getLeft(), valueNode);
			}
		} else {
			if(
			        currentNode.getRight().isNil()
            ) {
				currentNode.setRight(valueNode);
				valueNode.setParent(currentNode);
			} else {
				addNode(currentNode.getRight(), valueNode);
			}
		}
	}
	
	public class Silly {
		public void stuff() {
			
		}
	}

}
