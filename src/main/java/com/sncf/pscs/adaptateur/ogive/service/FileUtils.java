package com.sncf.pscs.adaptateur.ogive.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtils {

	/**
	 * Post a file. Create it and write its content.
	 * 
	 * @param localServerFolder
	 *            the url where to post the file
	 * @param fileName
	 *            the name of the file
	 * @param content
	 *            the content of the file
	 * @throws IOException 
	 */
	public static void postFile(final String localServerFolder, final String fileName, final byte[] content) throws IOException
	{
		FileOutputStream fos = null;
		try
		{
			final File localFile = createFile(localServerFolder, fileName);
			fos = new FileOutputStream(localFile);
			fos.write(content);
		}
		finally
		{
			if (fos != null)
			{
				try
				{
					fos.close();
				}
				catch (IOException e)
				{

				}
			}
		}
	}

	/**
	 * Copy a file to an other with a given name
	 * 
	 * @param source
	 * @param dest
	 * @throws FileNotFoundException
	 *             , IOException
	 * @throws IOException
	 */
	public static void copyFile(final String source, final String dest) throws FileNotFoundException, IOException
	{
		FileChannel channelIn = null;
		FileChannel channelOut = null;

		try
		{
			channelIn = new FileInputStream(source).getChannel();
			channelOut = new FileOutputStream(dest).getChannel();

			channelIn.transferTo(0, channelIn.size(), channelOut);
		}
		finally
		{
			if (channelIn != null)
			{
				channelIn.close();
			}
			if (channelOut != null)
			{
				channelOut.close();
			}
		}
	}
	/**
	 * Create a file.
	 * 
	 * @param path
	 *            the path of the file
	 * @param name
	 *            the name of the file
	 * @return an object of type {@link File} representing the file to create
	 * @throws IOException
	 */
	public static File createFile(final String path, final String name) throws IOException
	{
		final File file = new File(path + "/" + name);
		file.createNewFile();
		return file;
	}
	
	/**
	 * Check if a path is correct, if not, correct it.
	 * 
	 * @param path
	 *            the path to check.
	 * @return the path
	 */
	public static String checkPath(final String path)
	{

		String newPath = path;
		// Check the path is ended by '/' or '\'
		if (newPath != null && !newPath.isEmpty())
		{
			final char lastChar = newPath.charAt(newPath.length() - 1);
			if (lastChar != '/' && lastChar != '\\')
			{
				newPath = newPath.concat("/");
			}
		}
		else
		{
			newPath = "/";
		}
		return newPath;
	}
	
	/**
	 * Return true if file exist.
	 * 
	 * @param filePath
	 * @return boolean
	 */
	public static boolean isFileExist(final String filePath)
	{
		return new File(filePath).exists();
	}
}
