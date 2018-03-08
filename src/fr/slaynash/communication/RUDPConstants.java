package fr.slaynash.communication;

import fr.slaynash.communication.rudp.Packet;

public final class RUDPConstants {
	public static final int RECEIVE_MAX_SIZE = 4096;
	public static final int CLIENT_TIMEOUT_TIME = 5000;
	
	/**
	 * maximum time between ping packets before disconnecting client
	 */
	public static final long CLIENT_TIMEOUT_TIME_MILLISECONDS = 5000L;
	
	/**
	 * Packet's time before dropping it
	 */
	public static final long PACKET_TIMEOUT_TIME_MILLISECONDS = 5000L;
	
	/**
	 * Packet's seq store time after being received (used to avoid duplicate packets)
	 */
	public static final long PACKET_STORE_TIME_MILLISECONDS = 2000L;
	
	public static final int VERSION_MAJOR = 1;
	public static final int VERSION_MINOR = 0;
	
	public static final long PING_INTERVAL = 1000;
	
	public static class PacketType {
		public static final byte UNRELIABLE				= Packet.PHeader.createTypeHeader((byte)0 , false);
		public static final byte RELIABLE				= Packet.PHeader.createTypeHeader((byte)1 , true );
		public static final byte HANDSHAKE_START		= Packet.PHeader.createTypeHeader((byte)2 , false);
		public static final byte HANDSHAKE_OK			= Packet.PHeader.createTypeHeader((byte)3 , false);
		public static final byte HANDSHAKE_ERROR		= Packet.PHeader.createTypeHeader((byte)4 , false);
		public static final byte PING_REQUEST			= Packet.PHeader.createTypeHeader((byte)5 , false);
		public static final byte PING_RESPONSE			= Packet.PHeader.createTypeHeader((byte)6 , false);
		public static final byte DISCONNECT_FROMCLIENT	= Packet.PHeader.createTypeHeader((byte)7 , false);
		public static final byte DISCONNECT_FROMSERVER	= Packet.PHeader.createTypeHeader((byte)8 , true );
		public static final byte RELY					= Packet.PHeader.createTypeHeader((byte)9 , false);
		public static final byte PACKETSSTATS_REQUEST	= Packet.PHeader.createTypeHeader((byte)10, false);
		public static final byte PACKETSSTATS_RESPONSE	= Packet.PHeader.createTypeHeader((byte)11, false);
	}
	
	
}
