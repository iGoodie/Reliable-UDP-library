# jRUDP-with-Goodieutils
A wrapper for [**Slaynash/jRUDP**](https://github.com/Slaynash/jRUDP) repository included Goodieutils.
jRUDP is a Reliable Java UDP Library for multiplayer games and more.

Compile and run
---
#### Requirements
- Java 8+
- [iGoodie/Goodieutils 1.0.0+](https://github.com/iGoodie/Goodieutils)

Examples:
---
```java
public class Server
{
	public static RUDPServer serverInstance;
	public static final int SERVER_PORT = 56448;
	public static void main(String[] args)
	{
		try {
			serverInstance = new RUDPServer(SERVER_PORT);
			serverInstance.setPacketHandler(OrderedPacketHandler.class);
			serverInstance.start();
		}
		catch(SocketException e) {
			ConsolePrinter.error("Port %d is occupied. Server couldn't be initialized.", SERVER_PORT);
			System.exit(-1);
		}

		//send data to every client
		for(RUDPClient c : serverInstance.getConnectedClients()) {
			c.sendPacket(new byte[]{0x00});
			c.sendReliablePacket(new byte[]{0x00});
		}

		serverInstance.kick("localhost", 1234); //kick localhost:1234
		serverInstance.stop();
	}
}
```

```java
public class Client
{
	public static final InetAddress SERVER_HOST = NetUtils.getInternetAdress("localhost");
	public static final int SERVER_PORT = 56448;

	public static RUDPClient client;

	public static void main(String[] args)
	{
		try {
			client = new RUDPClient(SERVER_HOST, SERVER_PORT);
			client.setPacketHandler(OrderedPacketHandler.class);
			client.connect();
		}
		catch(SocketException e) {
			ConsolePrinter.error("Cannot allow port for the client. Client can't be launched.");
			System.exit(-1);
		}
		catch(UnknownHostException e) {
			ConsolePrinter.error("Unknown host: %o", SERVER_HOST);
			System.exit(-1);
		}
		catch(SocketTimeoutException e) {
			ConsolePrinter.error("Connection to %o:%d timed out.", SERVER_HOST, SERVER_PORT);
		}
		catch (InstantiationException e) {} //Given handler class can't be instantiated.
		catch (IllegalAccessException e) {} //Given handler class can't be accessed.
		catch(IOException e) {}

		client.sendPacket(new byte[]{0x00}); //Send packet to the server
		client.sendReliablePacket(new byte[]{0x00}); //Send packet to the server

		client.disconnect(); //Disconnect from server
	}
}
```

## Getting support
If you have any question or you found a problem, you can;
- [Open an issue](https://github.com/Slaynash/jRUDP/issues) on the official jRUDP repository **Slaynash/jRUDP**
- Send **Slaynash** an email at [slaynash@survival-machines.fr](mailto:slaynash@survival-machines.fr)
- Contact **Slaynash** on Discord by messaging Slaynash#2879
- [Open an issue](https://github.com/iGoodie/jRUDP-with-Goodieutils/issues) on the wrapper repository **iGoodie/jRUDP-with-Goodieutils**
- Send **iGoodie** an email at [igoodie@programmer.net](mailto:igoodie@programmer.net)
- Contact **iGoodie** on Discord by messaging iGoodie#1945
