package org.eclipse.libra.framework.jonas.internal.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil {
	public static void unzip(InputStream in, File dest) throws IOException
	   {
	      if (!dest.exists())
	      {
	         if (! dest.mkdirs()){
		         throw new IOException("Could not create destination directory.");
	         }
	      }
	      if (!dest.isDirectory())
	      {
	         throw new IOException("Destination must be a directory.");
	      }
	      ZipInputStream jin = new ZipInputStream(in);
	      byte[] buffer = new byte[1024];
	      
	      ZipEntry entry = jin.getNextEntry();
	      while (entry != null)
	      {
	         String fileName = entry.getName();
	         if (fileName.charAt(fileName.length() - 1) == '/')
	         {
	            fileName = fileName.substring(0, fileName.length() - 1);
	         }
	         if (fileName.charAt(0) == '/')
	         {
	            fileName = fileName.substring(1);
	         }
	         if (File.separatorChar != '/')
	         {
	            fileName = fileName.replace('/', File.separatorChar);
	         }
	         File file = new File(dest, fileName);
	         if (entry.isDirectory())
	         {
	            // make sure the directory exists
	            file.mkdirs();
	            jin.closeEntry();
	         } 
	         else
	         {
	            // make sure the directory exists
	            File parent = file.getParentFile();
	            if (parent != null && !parent.exists())
	            {
	               parent.mkdirs();
	            }
	            
	            // dump the file
	            OutputStream out = new FileOutputStream(file);
	            int len = 0;
	            while ((len = jin.read(buffer, 0, buffer.length)) != -1)
	            {
	               out.write(buffer, 0, len);
	            }
	            out.flush();
	            out.close();
	            jin.closeEntry();
	            
	            if (! file.setLastModified(entry.getTime())){
	            	// not a real problem, but findbugs requires checking the return code
	            }
	         }
	         entry = jin.getNextEntry();
	      }
	      
	      jin.close();
	   }
}
