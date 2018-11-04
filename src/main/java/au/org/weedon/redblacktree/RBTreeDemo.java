package au.org.weedon.redblacktree;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.Renderer;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.madgag.gif.fmsware.AnimatedGifEncoder;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;


import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import java.awt.image.BufferedImage;
import static com.xuggle.xuggler.Global.DEFAULT_TIME_UNIT;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;


public class RBTreeDemo {

    private static final Logger logger = LoggerFactory.getLogger(RBTreeDemo.class);

    private static final String graphFile = "media/image%d.png";
    //private static final String movieFile = "example/movie.gif";
    private static final String movieFile = "media/movie.mov";



    public static void createGraph() {
        int imageNumber = 1;

        RBTree<Integer> rbTree = new RBTree<>();

        final int width = 2048;
        final int height = 1536;
        final int fps = 3;
        final int framesPerChange = 5;
        final int treeNodeValueSeed = 555;


/*
        for(RBNode<Integer> node : rbTree.iterateNodesDFS(RBTree.DFSNodeIterator.TraversalOrder.Postorder)) {
            System.out.println("Value: " + node.getValue().get());
        }
*/

/*
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();

        encoder.start(movieFile);
        encoder.setFrameRate(2.5f);
        encoder.setRepeat(999);
        encoder.setSize(300, 600);
*/

        final IMediaWriter mediaWriter = ToolFactory.makeWriter(movieFile);
        mediaWriter.addVideoStream(0, 0, width, height);

        final long frameRate = DEFAULT_TIME_UNIT.convert(1000 / fps, MILLISECONDS);
        long nextFrameTime = 0;


        List<Integer> treeNums = IntStream.rangeClosed(1, 30)
                .boxed().collect(Collectors.toList());

        RBTreeGifRenderer<?> treeGifRenderer = new RBTreeGifRenderer<>(rbTree, width, height);

        Random random = new Random();
        random.setSeed(treeNodeValueSeed);

/*
        BufferedImage frame = treeGifRenderer.createFrame();
        for(int i = 0; i < framesPerChange; i++) {
            mediaWriter.encodeVideo(0, frame, nextFrameTime, DEFAULT_TIME_UNIT);
            nextFrameTime += frameRate;
        }
*/

/*
        encoder.addFrame(frame);
        encoder.addFrame(frame);
*/

        while(treeNums.size() > 0) {
            int numIndex = Math.abs(random.nextInt()) % treeNums.size();
            int num = treeNums.get(numIndex);
            treeNums.remove(numIndex);
            rbTree.add(num);
            BufferedImage frame = treeGifRenderer.createFrame();
/*
            encoder.addFrame(frame);
            encoder.addFrame(frame);
*/
            for(int i = 0; i < framesPerChange; i++) {
                mediaWriter.encodeVideo(0, frame, nextFrameTime, DEFAULT_TIME_UNIT);
                nextFrameTime += frameRate;
            }
        }


        //encoder.finish();

        mediaWriter.close();
    }

    private static String getRelativeFileName(StackTraceElement stackTraceElement) {
        String className = stackTraceElement.getClassName();
        String fileName = stackTraceElement.getFileName();
        return getRelativeFileName("src\\main\\java", className, fileName);
    }

    private static String getRelativeFileName(String base, String className, String fileName) {

        System.out.println("Clazz: " + className);
        List<String> tokens = new LinkedList<>(Arrays.asList(className.split(Pattern.quote("."))));
        if(tokens.size() == 0) {
            throw new RuntimeException("Could not parse path from class name");
        }
        tokens.remove(tokens.size() - 1);
        return base + "\\" + String.join("\\", tokens) + "\\" + fileName;
    }

    private static String getAboveLine() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
        int line = stackTraceElement.getLineNumber();
        String relativeFileName = getRelativeFileName(stackTraceElement);

        String currentLine = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(relativeFileName))) {
            currentLine = bufferedReader.readLine();
            for(int currentNum = 1;
                currentNum < line - 1 && currentLine != null;
                currentLine = bufferedReader.readLine(), ++currentNum) {
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentLine;
    }

    public static void main(String[] args) throws IOException {

        // Example comment!!!
        System.out.println(getAboveLine());

        //createGraph();



    }
}
