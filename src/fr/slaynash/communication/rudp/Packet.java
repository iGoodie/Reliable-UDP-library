package fr.slaynash.communication.rudp;

import igoodie.utils.io.NetUtils;

/**
 * Simple packet definition to be extended by other packet types
 * @author iGoodie
 */
public abstract class Packet {

	public static final int HEADER_SIZE = 3; //bytes
	
	public static class PHeader {

		public static byte createTypeHeader(byte id, boolean reliable) {
			if((id & 0b1000_0000) == 0b1000_0000) //If leftmost bit is set
				throw new IllegalArgumentException("Packet id too big for adding the reliability bit");
			if(reliable)
				return (byte) (id | (1 << 7));
			return id;
		}
		
		public static boolean isTypeReliable(int packetType) {
			return (packetType & 0b1000_0000) == 0b1000_0000;
		}
		
		
		private boolean isReliable = false;
		private short sequenceNum;

		public boolean isReliable() {
			return isReliable;
		}

		public short getSequenceNo() {
			return sequenceNum;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Reliable:" + isReliable + ", ");
			sb.append("SequenceNo:" + sequenceNum);
			return sb.toString();
		}
	}

	/* Fields*/
	private PHeader header = new PHeader();
	private byte[] rawPayload;

	/* Constructor */
	public Packet(byte[] data) {
		//Parse header
		header.isReliable = PHeader.isTypeReliable(data[0]);
		header.sequenceNum = NetUtils.asShort(data, 1);

		//Parse payload
		rawPayload = new byte[data.length - HEADER_SIZE];
		System.arraycopy(data, HEADER_SIZE, rawPayload, 0, rawPayload.length);
	}

	/* Getter and Setters */
	public PHeader getHeader() {
		return header;
	}

	public byte[] getRawPayload() {
		return rawPayload;
	}
	
	/* Overrides */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("h{");
		sb.append(header);
		sb.append("} p{");
		sb.append(NetUtils.asHexString(rawPayload));
		sb.append("}");
		return sb.toString();
	}
}
