package au.org.weedon.redblacktree;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.Renderer;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

public class RBTreeGifRenderer<T extends Comparable> {

    private static final Logger logger = LoggerFactory.getLogger(RBTreeGifRenderer.class);
    private RBTree<T> rbTree;
    int width;
    int height;

    public RBTreeGifRenderer(RBTree<T> rbTree, int width, int height) {
        this.rbTree = rbTree;
        this.width = width;
        this.height = height;
    }

    private Renderer renderGraph(Graph graph) {
        logger.debug("Generating graph image...");
        return Graphviz.fromGraph(graph)
                //.width(width)
                //.height(height)
                .render(Format.PNG);
    }

    private static <T extends Comparable> Color getGraphColorFromRBNodeColor(RBNode<T> rbNode) {
        return rbNode.getColor() == RBNode.RBColor.BLACK ? Color.BLACK : Color.RED;
    }

    private static <T extends Comparable> Node createGraphNodeFromRBNode(RBNode<T> rbNode) {
        return node(rbNode.getValue().get().toString())
                .with(getGraphColorFromRBNodeColor(rbNode));
    }

    public BufferedImage createFrame() {
        //return renderGraph(renderTree()).toImage();
        BufferedImage frame = renderGraph(renderTree()).toImage();

        //BufferedImage convertedFrame = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage convertedFrame = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        convertedFrame.getGraphics().setColor(java.awt.Color.WHITE);
        convertedFrame.getGraphics().fillRect(0, 0, width, height);
        convertedFrame.getGraphics().drawImage(frame, width / 2 - frame.getWidth() / 2 / 2, 0, null);
        return convertedFrame;
    }


    private Graph renderTree() {

        Map<RBNode<T>, Node> nodeMap = new HashMap<>();

        Graph visualGraph = graph("redblacktree")
                .directed()
                .graphAttr().with(Color.WHITE);


        for(RBNode<T> rbNode : rbTree.iterateNodesDFS(RBTree.DFSNodeIterator.TraversalOrder.Postorder)) {

            System.out.println(rbNode.getValue());

            if(rbNode.getParent().isNil()) {
                visualGraph = visualGraph
                        .with(nodeMap.computeIfAbsent(rbNode, RBTreeGifRenderer::createGraphNodeFromRBNode));
                continue;
            }

            Node parentNode = nodeMap.computeIfAbsent(rbNode.getParent(), RBTreeGifRenderer::createGraphNodeFromRBNode);
            Node childNode = nodeMap.computeIfAbsent(rbNode, RBTreeGifRenderer::createGraphNodeFromRBNode);

            Node newNode = parentNode.link(childNode);
            nodeMap.put(rbNode.getParent(), newNode);
        }
        return visualGraph;
    }

}
