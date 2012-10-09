import java.io.*;
import java.net.*;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;

public class DccConnection implements Runnable
{
    private String nick;
    private String recipient;
    private JFrame frame;
    private TabGroup tabGroup;
    private DccChatTab dccTab;

    private byte[] ipBytes;
    private int port;

    private ServerSocketChannel ssc;
    private ServerSocket ss;
    private SelectionKey key;
    private Selector selector;
    private ByteBuffer resp;

    private boolean writable;
    
    public DccConnection(String nick, String recipient, JFrame frame, TabGroup tabGroup)
    {
	this.nick = nick;
	this.recipient = recipient;
	this.frame = frame;
	this.tabGroup = tabGroup;
	writable = false;
	//	initConnection();
    }

    public void run() 
    {
	try
	    {
		ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ss = ssc.socket();
		ss.setReuseAddress(true);
		InetSocketAddress address = new InetSocketAddress("172.16.0.9", 9000);
		ss.bind(address);
		selector = Selector.open();
	
		key = ssc.register( selector, SelectionKey.OP_ACCEPT );

		InetAddress inetAddress = ss.getInetAddress();
		ipBytes = inetAddress.getAddress();
		port = ss.getLocalPort();
		
		for( int i = 0; i < ipBytes.length; i++ )
		    System.out.println(ipBytes[i]);
		System.out.println();

		boolean connected = true;
		//	int num = selector.select();
		//	Set selectedKeys = selector.selectedKeys();
		//	Iterator it = selectedKeys.iterator();
		
		while( connected )
		    {
			//			ByteBuffer resp = ByteBuffer.wrap(new String("got it\n").getBytes());
			while( selector.select() > 0 && connected )
			    {
				Set selectedKeys = selector.selectedKeys();
				Iterator it = selectedKeys.iterator();
				
				while( it.hasNext() )
				    {
					key = (SelectionKey)it.next();
					
					if (key.isAcceptable()) 
					    {
						// this means that a new client has hit the port our main
						// socket is listening on, so we need to accept the  connection
						// and add the new client socket to our select pool for reading
						// a command later
						System.out.println("Accepting connection!");
						// this will be the ServerSocketChannel we initially registered
						// with the selector in main()
						ServerSocketChannel sch = (ServerSocketChannel)key.channel();
						SocketChannel ch = sch.accept();
						ch.configureBlocking(false);
						ch.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
						dccTab = new DccChatTab(recipient, tabGroup, this);
					    } 
					else if (key.isReadable() ) 
					    {
						
						// one of our client sockets has received a command and
						// we're now ready to read it in
						System.out.println("Accepting command!");                            
						SocketChannel ch = (SocketChannel)key.channel();
						//						if( ch.isConnected() )
						//						    {
							ByteBuffer buf = ByteBuffer.allocate(200);
							int readBytes = ch.read(buf);
							if( readBytes < 0 )
							    {
								connected = false;
								dccTab.setSockClosed(nick, "Remote host closed socket");
								break;
							    }
							buf.flip();
							Charset charset = Charset.forName("UTF-8");
							CharsetDecoder decoder = charset.newDecoder();
							CharBuffer cbuf = decoder.decode(buf);
							String message = cbuf.toString();
							dccTab.setMessage(recipient, message);
							System.out.print(cbuf.toString());
							// re-register this socket with the selector, this time
							// for writing since we'll want to write something to it
							// on the next go-around
							ch.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
							/*
    }
						else
						    {
							connected = false;
							break;
						    }
							*/
					    }
					else if (writable && key.isWritable()) 
					    {
						System.out.println("Sending response!");
						//						SocketChannel ch = (SocketChannel)key.channel();
						//						ch.register(selector, SelectionKey.OP_WRITE);
						// we are ready to send a response to one of the client sockets
						// we had read a command from previously
						System.out.println("Sending response!");
						SocketChannel ch = (SocketChannel)key.channel();
						resp.rewind();
						ch.write(resp);
						resp.rewind();
						// we may get another command from this guy, so prepare
						// to read again. We could also close the channel, but
						// that sort of defeats the whole purpose of doing async
						ch.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
						writable = false;
					    }
					it.remove();
				    }
				//			num = selector.select();
			    }
		    }
	    }
	catch(ClosedChannelException cce)
	    {
		cce.printStackTrace();
	    }
	catch(IOException ioe)
	    {
		ioe.printStackTrace();
	    }
    }

    public int getIpBytes()
    {
	int i = (ipBytes[3] | ( ipBytes[2] << 8 ) | ( ipBytes[1] << 16 ) | ( ipBytes[0] << 24 ));
	return i;
    }

    public int getPort()
    {
	return port;
    }

    public void sendDccMessage(String message) 
    {
	//	try
	//  {
		writable = true;
		//		ssc.register(selector, SelectionKey.OP_WRITE);
		resp = ByteBuffer.wrap(new String(message + "\r\n").getBytes());
		resp.rewind();
		//	    }
		/*	catch(IOException ioe)
	    {
		ioe.printStackTrace();
	    }
		*/
    }
}