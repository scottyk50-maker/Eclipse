import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;


public class Buildit 
{

	public static void main(String[] args) 
	{
			// TODO Auto-generated method stub
	//       copyFile("source","target");
//	    File sourceFile = new File("D:\\test\\file_example_JPG_100KB.JPG");
	    File sourceFile = new File("D:\\test\\file_example_TIFF_1MB.tiff");
	
	//    do while(dWhile < 100)
	//	{	
	    
	    int subcount = 0;
	    
	    while (subcount < 5) 
	    {
		    System.out.println("sub Count is: " + subcount);
		    subcount++;
		    fileLoop(subcount, sourceFile);
	  
	    }	
	}

	private static void fileLoop(int subcount, File sourceFile)
	{
	    int filecount = 0;

		while (filecount < 5) 
	    {
	        System.out.println("file Count is: " + filecount);
	        filecount++;
	    
	        
//	        Calendar cal = Calendar.getInstance();
//	        Date dateInstance = null;
//			cal.setTime(dateInstance);
//	        cal.add(Calendar.DATE, subcount);
//	        Date TheDate = cal.getTime();

	        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Calendar cal = Calendar.getInstance();

	        //Add one day to current date.
	        cal.add(Calendar.DATE, -subcount);
	        System.out.println(dateFormat.format(cal.getTime()));	        
	        
//	        LocalDate TheDate = LocalDate.now().minusDays(subcount);
//	        String TargetDir = "D:\\testschema\\"+TheDate+"\\";
	        String TargetDir = "D:\\testschema\\"+dateFormat.format(cal.getTime())+"\\";
	        System.out.println(TargetDir);
	        String str5 = String.format("%012d", filecount); // Filling with zeroes  
	        System.out.println(TargetDir  + str5);
	        
	        File targetLocation = new File(TargetDir);    
	    	File destinationFile = new File(TargetDir  + str5 );      
		    
	        if (!targetLocation.exists()) {
	            targetLocation.mkdir();
	        }
	    	
		    try 
		    {
		    	   copyFile(sourceFile, destinationFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
	    } 
	}	
	
	private static void copyFile(File sourceFile, File destFile)
	        throws IOException 
	{
	    if (!sourceFile.exists()) {
	        return;
	    }
	    if (!destFile.exists()) {
	        destFile.createNewFile();
	    }
	    FileChannel source = null;
	    FileChannel destination = null;
	    source = new FileInputStream(sourceFile).getChannel();
	    destination = new FileOutputStream(destFile).getChannel();
	    if (destination != null && source != null) {
	        destination.transferFrom(source, 0, source.size());
	    }
	    if (source != null) {
	        source.close();
	    }
	    if (destination != null) {
	        destination.close();
	    }

	}
	
}

