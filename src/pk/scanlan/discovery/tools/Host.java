package pk.scanlan.discovery.tools;


import java.math.BigInteger;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.pm.FeatureInfo;
import android.os.StrictMode;
import android.util.Log;



public class Host {
	
	public enum  DEVICE_TYP
	{
		ROUTER,
		HOST,
		THIS,
		UNKNOW
		
	}
	public String TAG ="Host";
	
	 public  DEVICE_TYP mType =DEVICE_TYP.UNKNOW; 
	    public int isAlive = 1;
	    public int position = 0;
	    public int responseTime = 0; // ms
	  
	    private IP4Address mIpAddres = null;
	    public String hostname = "Unknow";
	    public String hardwareAddress ;
	    public String nicVendor = "Unknown";
	    public String os = "Unknown";
	    public HashMap<Integer, String> services = null;
	    public Host(String ip,String mac)
	    {
	    	
	    	try {
				mIpAddres = new IP4Address(ip);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	hardwareAddress = mac;
	    	hostname = hostname +" hostname "+ mIpAddres.toInetAddress().getHostName();
	    	deviaceTyp();
	    }
	    public Host(String ip)
	    {
	    	
	    	try {
				mIpAddres = new IP4Address(ip);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	hardwareAddress = Network.NOMAC;
	    	hostname = "Unknow ";
	    	deviaceTyp();
	    }
	    private void deviaceTyp()
	    {
	    	if(mIpAddres.toInetAddress().equals(System.getNetwork().getLocalAddress()))
	    		mType = DEVICE_TYP.THIS;
	    	else if(mIpAddres.toInetAddress().equals(System.getNetwork().getGatewayAddress()))
	    		mType = DEVICE_TYP.ROUTER;
	    	else
	    		mType = DEVICE_TYP.HOST;
	    	
	    }
	    public InetAddress getAddress()
	    {
	    	return mIpAddres.toInetAddress();
	    	
	    }
	    public String getAddressAsString()
	    {
	    	return mIpAddres.toString();
	    	
	    }
	    public void setAlias(String s)
	    {
	    	hostname = s;
	    	
	    }
	    public String getAlias()
	    {
	    	return hostname;
	    	
	    }
	    public boolean hasAlias()
	    {
	    	return (hostname!=null);
	    	
	    }
	    public String getHardwareAsString()
	    {
	    	return hardwareAddress;
	    	
	    	  }
	
	    public void setHardwerAddres( String macAddress ) {
			hardwareAddress = macAddress;
			
	    }
	    
	    public static byte[] parseMacAddress( String macAddress ) {
			if( macAddress != null && macAddress.equals("null") == false && macAddress.isEmpty() == false )
			{

		        String[] bytes = macAddress.split(":");
		        byte[] parsed  = new byte[bytes.length];
		
		        for (int x = 0; x < bytes.length; x++)
		        {
		            BigInteger temp = new BigInteger(bytes[x], 16);
		            byte[] raw 		= temp.toByteArray();
		            parsed[x] 		= raw[raw.length - 1];
		        }
							        
		        return parsed;
			}
			
			return null;
			
	    }
	    public String getDescription(){
			
			
			 if( mType == DEVICE_TYP.HOST)
			{
				String vendor = System.getMacVendor( parseMacAddress(hardwareAddress) ),
					   desc   ="Nazwa " +hostname+mIpAddres.toString()+ hardwareAddress;
				
				if( vendor != null )
					desc += " - " + vendor;
				
				if(mIpAddres.toString().equals( System.getNetwork().getGatewayAddress() ) )
					desc += " ( Your network gateway / router )";

				else if( mIpAddres.toString().equals( System.getNetwork().getLocalAddressAsString() ) )
					desc += " ( This device )";
				
				return desc.trim();
			}		
			else if( mType == DEVICE_TYP.UNKNOW )
				return mIpAddres.toString();
			
			return "";
		}
	    public boolean comesAfter( Host target ){

			if( mType == DEVICE_TYP.UNKNOW )
				return false;
			
			else if( mType == DEVICE_TYP.HOST )
				return mIpAddres.toInteger() > target.mIpAddres.toInteger();					
				else
					return false;
			
		
		}
	    public boolean isRouter() {
			
			
				return (  mIpAddres.equals( System.getNetwork().getGatewayAddress() ) );
			
		}
		public boolean equals( Host target ) {
			
			
			
			return mIpAddres.toString().equals(target.mIpAddres.toString());
		}
		
		public boolean equals( Object o ) {
			if( o instanceof Host )
				return equals( (Host)o );
			
			else
				return false;
		}
	    
	    
}
