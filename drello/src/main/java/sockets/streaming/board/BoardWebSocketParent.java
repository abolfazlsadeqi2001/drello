package sockets.streaming.board;

public class BoardWebSocketParent {
	protected static final int MAX_TEXT_MESSAGE_SIZE = 100 * 1024;// max gained text message is 35 kB
	protected static final int TIME_OUT_PER_MILI_SECONDS = 10 * 1000;// sync time is 5
}
