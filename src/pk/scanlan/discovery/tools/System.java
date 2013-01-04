package pk.scanlan.discovery.tools;




import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


import android.content.Context;
import android.util.Log;

public class System {
	private static Vector<Host> mHosts = null;
	private static int  			     mCurrentHost = 0;
	private static Map< String, String > mVendors       = null;
	private static Context mContext = null;
	public static String TAG ="System";
	private static Network mNetwork = null;

	
	public static void init(Context c) 
	{
	  mContext = c;
	try {
		mNetwork = new Network(c);
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.e(TAG,"Wyj e" + e);
	}
	mHosts = new Vector<Host>();
	(new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Host gateway = new Host( mNetwork.getGatewayAddress().getHostAddress(), mNetwork.getGatewayHardwareAsString()),
					   device  = new Host( mNetwork.getLocalAddressAsString(), mNetwork.getLocalHardwereAsString() );
				
				gateway.setAlias( mNetwork.getSSID() );
				device.setAlias( android.os.Build.MODEL);
				
				
				synchronized(mHosts) {
				mHosts.add( gateway );
				}
				synchronized(mHosts) {
				mHosts.add( device );
				}
		}
	})).start();
		
	
		
	}
	public static Network getNetwork()
	{
		
		return mNetwork;
	}
	public static boolean hasHost(Host h)
	{
		return mHosts.contains(h);
		
	}
	
	public static Host getHostByAddress(String addr)
	{
   int i, size = mHosts.size();
		
		for( i = 0; i < size; i++ )
		{
			synchronized( mHosts )
			{
				Host h = mHosts.get( i );
			
				if( h!= null && h.getAddress() != null && h.getAddressAsString().equals(addr) )
					return h;
			}
		}
		
		return null;
		
	}
	public static void addHost( int index, Host Host ){
		mHosts.add( index, Host );
		// update current Host index
		if( mCurrentHost >= index )
			mCurrentHost++;
	}
	public static Vector<Host> getHosts()
	{
		return mHosts;
	}
	public static Host getHost(int i)
	{
		return mHosts.get(i);
	}
	
	public static void addHost( Host Host ){
		mHosts.add( Host );
	}
	
	public static boolean addOrderedHost( Host host ){
		if( host != null && hasHost( host ) == false )
		{
			for( int i = 0; i < getHosts().size(); i++ )
			{
				if( getHost( i ).comesAfter( host ) )
				{
					addHost( i, host );
					return true;
				}
			}
			
			addHost( host );
			
			return true;
		}
		
		return false;
	}
	
	private static void preloadVendors( ) {
		if( mVendors == null )
		{
			try
			{
				mVendors				 = new HashMap<String,String>();			
				FileInputStream fstream  = new FileInputStream( mContext.getFilesDir().getAbsolutePath() + "/tools/nmap/nmap-mac-prefixes" );
				DataInputStream in 	     = new DataInputStream(fstream);
				BufferedReader  reader   = new BufferedReader(new InputStreamReader(in));
				String 		    line;

				while( ( line = reader.readLine() ) != null )   
				{
					line = line.trim();
					if( line.startsWith("#") == false && line.isEmpty() == false )  
					{
						String[] tokens = line.split( " ", 2 );
						
						if( tokens.length == 2 )
							mVendors.put( tokens[0], tokens[1] );						
					}
				}
				
				in.close();
			}
			catch( Exception e )
			{
				Log.e( TAG, "Wyj"+e );
			}
		}
	}
	public static String getMacVendor( byte[] mac ){	
		//preloadVendors();
		
		if( mac != null && mac.length >= 3 )
			//return mVendors.get( String.format( "%02X%02X%02X", mac[0], mac[1], mac[2] ) );
	return "Unknow";
			else
			return null;
	}
public static void clean()
{
	
}
public static void setCurrentHost(int id)
{
	mCurrentHost = id;
	}

public static Host getCurrentHost()
{
return mHosts.get(mCurrentHost);	
}

}

