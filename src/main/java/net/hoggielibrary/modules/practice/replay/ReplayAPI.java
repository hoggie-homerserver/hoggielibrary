package net.hoggielibrary.modules.practice.replay;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Ghost replay API for recording and playing back player actions.
 */
public final class ReplayAPI {

    private final Map<String, List<ReplayFrame>> recordings = new ConcurrentHashMap<>();
    private boolean recording;

    /**
     * Starts recording a replay.
     *
     * @param replayId the replay identifier
     */
    public void startRecording(String replayId) {
        recordings.put(replayId, new ArrayList<>());
        recording = true;
        HoggieLogger.info("Started replay recording: {}", replayId);
    }

    /**
     * Records a frame of player data.
     *
     * @param replayId the replay identifier
     * @param frame the frame data
     */
    public void recordFrame(String replayId, ReplayFrame frame) {
        List<ReplayFrame> frames = recordings.get(replayId);
        if (frames != null) {
            frames.add(frame);
        }
    }

    /**
     * Stops recording a replay.
     *
     * @param replayId the replay identifier
     * @return the recorded frames
     */
    public List<ReplayFrame> stopRecording(String replayId) {
        recording = false;
        List<ReplayFrame> frames = recordings.get(replayId);
        HoggieLogger.info("Stopped replay recording: {} ({} frames)", replayId,
                frames != null ? frames.size() : 0);
        return frames != null ? frames : List.of();
    }

    /**
     * Gets recorded frames for a replay.
     *
     * @param replayId the replay identifier
     * @return list of frames
     */
    public List<ReplayFrame> getReplay(String replayId) {
        return recordings.getOrDefault(replayId, List.of());
    }

    /**
     * Deletes a replay.
     *
     * @param replayId the replay identifier
     */
    public void deleteReplay(String replayId) {
        recordings.remove(replayId);
    }

    /**
     * Returns whether recording is active.
     *
     * @return true if recording
     */
    public boolean isRecording() {
        return recording;
    }

    /**
     * Returns all saved replay IDs.
     *
     * @return set of replay IDs
     */
    public Set<String> getReplayIds() {
        return recordings.keySet();
    }
}
