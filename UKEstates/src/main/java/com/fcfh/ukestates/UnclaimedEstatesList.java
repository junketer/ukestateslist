package com.fcfh.ukestates;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnclaimedEstatesList {

	static String URL ="https://www.gov.uk/government/uploads/system/uploads/attachment_data/file/{0}/UnclaimedEstatesList.csv";
	static long id = 292366l;
	
	private static final String LINE_DELIMITER = System
			.getProperty("line.separator");
	private static String LAST_MODIFIED = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String currentFile = "./UnclaimedEstates.csv";
		if (args.length==1) {
			currentFile = args[0];
		}
		try {
			SourceList newList = null; 
			do {
				try {
					newList= downloadList();
				} catch (IOException ioe) {
					System.err.print(ioe.getMessage());
				}
				id++;
			} while (newList==null && (id < (id + 1000)));
			
			SourceList currentList = readCurrentFile(currentFile);
			if (!currentList.getReferences().isEmpty()) {
				newList.compareTo(currentList);				
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(currentFile));
			newList.writeTo(bw);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static SourceList readCurrentFile(String currentFile) throws IOException {
		File f = new File(currentFile);
		if (!f.exists()) {
			return new SourceList();
		}
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		SourceList list = new SourceList();
		boolean isHeaderLine= true;
		while ((line=br.readLine())!=null) {
			if (isHeaderLine) {
				list.setHeader(line);
			} else {
				list.addLine(line);				
			}
			
			isHeaderLine=false;
		}
		br.close();
		return list;
	}

	private static SourceList downloadList()
			throws IOException {
		// https://www.gov.uk/government/uploads/system/uploads/attachment_data/file/289134/UnclaimedEstatesList.csv
		URL url = new URL(MessageFormat.format(URL, Long.toString(id)));
		URLConnection con = url.openConnection();
		con.connect();
		/*
		 * System.out.println(" Headers: "); for (String key:
		 * con.getHeaderFields().keySet()) { System.out.println(key + " : " +
		 * con.getHeaderField(key)); }
		 */
		LAST_MODIFIED = con.getHeaderField("Last-Modified");
		InputStream is = con.getInputStream();
		BufferedReader br = new BufferedReader(
				new java.io.InputStreamReader(is));
		String line = null;
		SourceList list = new SourceList();
		boolean isHeaderLine = true;
		while ((line = br.readLine()) != null) {
			if (isHeaderLine) {
				list.setHeader(line);
			} else {
				list.addLine(line);
			}
			isHeaderLine=false;
		}
		return list;
	}

	private static Date parseModifiedDate(String lastModified)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
		return sdf.parse(lastModified);
	}
}
