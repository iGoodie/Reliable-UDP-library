package fr.slaynash.communication.handlers;

import fr.slaynash.communication.rudp.Packet;
import fr.slaynash.communication.utils.PacketQueue;
import igoodie.utils.io.NetUtils;

/**
 * Packet handling class base to handle ordered packets. <br/>
 * For UDP channel, <b><i>handleUDP()</b></i> method handles up-to-date packets, but drops the depricated packets. <br/>
 * For RUDP channel, <b><i>handleRUDP()</b></i> method orders the packets, and then handles them.
 * @author iGoodie
 */
public class OrderedPacketHandler extends PacketHandler {
	
	protected PacketQueue reliableQueue = new PacketQueue();
	protected short lastRUDPSeq = -1;
	protected short lastUDPSeq = -1;

	/* Connection Handlers */
	@Override
	public void onConnection() {}

	@Override
	public void onDisconnectedByRemote(String reason) {
		reliableQueue = new PacketQueue();
		lastRUDPSeq = -1;
	}
	
	@Override
	public void onDisconnectedByLocal(String reason) {
		reliableQueue = new PacketQueue();
		lastRUDPSeq = -1;
	}

	/* Packet-receive Handlers */
	@Override
	public void onPacketReceived(byte[] data) {
		Packet packet = new Packet(data); //Parse received packet
		
		if(NetUtils.sequence_greater_than(packet.getHeader().getSequenceNo(), lastUDPSeq)) { //If newSeq < lastSeq
			handleUDP(packet);
			lastUDPSeq = packet.getHeader().getSequenceNo(); // Update newest packet seq
		}
	}

	public void handleUDP(Packet packet) {
		System.out.println("Handling (UDP): " + packet); //Print packet just to test
	}
	
	@Override
	public void onReliablePacketReceived(byte[] data) {
		Packet packet = new Packet(data); //Parse received packet
		short expectedSeq = NetUtils.shortIncrement(lastRUDPSeq); //last + 1
		
		if(NetUtils.sequence_greater_than(lastRUDPSeq, packet.getHeader().getSequenceNo())) { // (last > received) == (received < last)
			return; // Drop the packet, because we already handled it
		}
		
		//Received an unexpected packet? Enqueue and pass
		if(packet.getHeader().getSequenceNo() != expectedSeq) { 
			reliableQueue.enqueue(packet);
			return;
		}
		
		// Handle expected packet
		handleRUDP(packet); 
		lastRUDPSeq = packet.getHeader().getSequenceNo();
		expectedSeq = NetUtils.shortIncrement(lastRUDPSeq); 

		// Handle every waiting packet
		while(!reliableQueue.isEmpty() && reliableQueue.peek().getHeader().getSequenceNo() == expectedSeq) {
			packet = reliableQueue.dequeue();
			handleRUDP(packet);
			lastRUDPSeq = expectedSeq;
			expectedSeq = NetUtils.shortIncrement(lastRUDPSeq);			
		}
	}
	
	public void handleRUDP(Packet packet) {
		System.out.println("Handling (RUDP): " + packet); //Print packet just to test
	}

	@Override
	public void onRemoteStatsReturned(int sentRemote, int sentRemoteR, int receivedRemote, int receivedRemoteR) {}

	/*
	//Reliability ordering test
	public static void main(String[] args) {
		OrderedPacketHandler handler = new OrderedPacketHandler();
		byte[][] list = new byte[][] {
			{0x1, 0b0000_0000, 0b0000_0000}, //0 *
			{0x1, 0b0000_0000, 0b0000_0001}, //1 *
			{0x1, 0b0000_0000, 0b0000_0011}, //3
			{0x1, 0b0000_0000, 0b0000_0101}, //5
			{0x1, 0b0000_0000, 0b0000_0100}, //4 *
			{0x1, 0b0000_0000, 0b0000_0111}, //7
			{0x1, 0b0000_0000, 0b0000_0010}, //2 *
			{0x1, 0b0000_0000, 0b0000_0110}, //6
			{0x1, 0b0000_0000, 0b0000_1000}, //8 *
		};
		
		for(byte[] p_data : list) {
			handler.onReliablePacketReceived(p_data);
		}
	}*/
}
