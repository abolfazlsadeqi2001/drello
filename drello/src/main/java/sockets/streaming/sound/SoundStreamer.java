package sockets.streaming.sound;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import configurations.sockets.streaming.BoardWriter;
import configurations.sockets.streaming.SoundAppender;
import configurations.sockets.streaming.SoundStreamerValues;
import configurations.sockets.streaming.SoundWriter;
import configurations.streamer.login.StreamerLogin;
import sockets.streaming.board.BoardStreaming;

@ServerEndpoint("/sound_streamer")
public class SoundStreamer extends SoundStreamingParent {

	@OnOpen
	public void onOpen(Session session) throws IOException {
		if (SoundStreamerValues.isStreamSessionInUsed()) {
			// close the current session because we cannot handle two sessions at a same time
			CloseReason closeReason = new CloseReason(CloseCodes.CANNOT_ACCEPT, "another streamer is streaming");
			session.close(closeReason);
		} else {
			SoundWriter.setStreamIndex();
			SoundWriter.createFolderForCurrentStreamIndex();
			SoundStreamerValues.setStreamerSession(session);
			SoundStreamerValues.setStreamSessionInUsed();
			// set the limits for time and size
			session.setMaxBinaryMessageBufferSize(MAX_BINARRY_MESSAGE);
			session.setMaxIdleTimeout(MAX_TIME_OUT);
			session.setMaxTextMessageBufferSize(MAX_TEXT_MESSAGE);
		}
	}

	@OnMessage
	public synchronized void onMessage(Session session, byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		if (!SoundStreamerValues.isHeaderBlobDefined()) {
			SoundStreamerValues.setHeaderBlob(buffer);
		}
		// broad cast the received message
		SoundClientStream.broadCast(buffer);
		SoundWriter.writeMessage(bytes);
	}
	
	@OnMessage
	public void onTextMessage(Session session, String str) throws IOException {
		if (str.equals("start")) {
			long currentMiliSeconds = System.currentTimeMillis();
			SoundStreamerValues.setMiliSecondsOnStartStreaming(currentMiliSeconds);
			BoardStreaming.sendStart();
			SoundStreamerValues.setStreamStarted();
		} else if (str.equals("finish")) {
			StreamerLogin.setStreamingAllowed(false);
			session.close();
		}
	}

	@OnError
	public void onError(Throwable th) {
		// TODO handle error
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) throws IOException, UnsupportedAudioFileException {
		if (reason.getCloseCode() != CloseCodes.CANNOT_ACCEPT) {
			SoundStreamerValues.resetVariables();
			SoundClientStream.closeAllClients();
			BoardStreaming.closeServer();
			
			SoundWriter.updatePreviousStreamsDurationByThisStreamTitle();
			SoundWriter.appendPreviousSoundToCurrent();
			BoardWriter.mergetPreviousJSONFileToCurrentFile();
			
			if(!StreamerLogin.isStreamingAllowed())
				SoundAppender.finishStream();
		}
	}
}
