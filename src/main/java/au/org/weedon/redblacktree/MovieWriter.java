package au.org.weedon.redblacktree;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import java.awt.image.BufferedImage;
import static com.xuggle.xuggler.Global.DEFAULT_TIME_UNIT;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MovieWriter {


    public static IMediaWriter getMediaWriter(String fileName) {
        // total duration of the media

        final long duration = DEFAULT_TIME_UNIT.convert(5, SECONDS);

        // video parameters

        final int videoStreamIndex = 0;
        final int videoStreamId = 0;
        final long frameRate = DEFAULT_TIME_UNIT.convert(15, MILLISECONDS);
        final int width = 320;
        final int height = 200;

        // audio parameters

        final int audioStreamIndex = 1;
        final int audioStreamId = 0;
        final int channelCount = 1;
        final int sampleRate = 44100; // Hz
        final int sampleCount = 1000;

        // create a media writer and specify the output file

        final IMediaWriter writer = ToolFactory.makeWriter(fileName);

        // add the video stream

        writer.addVideoStream(videoStreamIndex, videoStreamId,
                width, height);

        // add the audio stream

/*
        writer.addAudioStream(audioStreamIndex, audioStreamId,
                channelCount, sampleRate);
*/

        // create some balls to show on the screen

        //au.org.weedon.redblacktree.MovieWriter balls = new MovingBalls(ballCount, width, height, sampleCount);

        // the clock time of the next frame

        long nextFrameTime = 0;

        // the total number of audio samples

        long totalSampleCount = 0;

        // loop through clock time, which starts at zero and increases based
        // on the total number of samples created thus far

/*
        for (long clock = 0;
             clock < duration;
             clock = IAudioSamples.samplesToDefaultPts(totalSampleCount, sampleRate)) {

            // while the clock time exceeds the time of the next video frame,
            // get and encode the next video frame

            while (clock >= nextFrameTime)
            {
                BufferedImage frame = balls.getVideoFrame(frameRate);
                writer.encodeVideo(videoStreamIndex, frame, nextFrameTime,
                        DEFAULT_TIME_UNIT);
                nextFrameTime += frameRate;
            }

            // compute and encode the audio for the balls

*/
/*
            short[] samples = balls.getAudioFrame(sampleRate);
            writer.encodeAudio(audioStreamIndex, samples, clock,
                    DEFAULT_TIME_UNIT);
            totalSampleCount += sampleCount;
*//*

        }

        // manually close the writer

        writer.close();
*/
        return writer;
    }
}

