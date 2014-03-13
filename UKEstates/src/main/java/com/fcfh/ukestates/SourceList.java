package com.fcfh.ukestates;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

public class SourceList {
	private String controlFields = "Changed,Status";
	private String headerLine=null;
	private String changeDate=new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
	private HashMap<String, String> data=new HashMap<String, String>(11000);
	private boolean isAugmented=false;
	
	public void addLine(String line) {
		String ref = line.split(",")[0];
		data.put(ref, line);
	}
	
	public String augment(String line, boolean isNew, boolean isDeleted) {
		isAugmented=true;
		int i = line.indexOf(",");
		String ref = line.substring(0,i);
		String data = line.substring(i);
		String changeInd = isNew ? "N" : (isDeleted ? "D" : "C");
		String date = (isNew || isDeleted) ? changeDate : "";
		return ref+","+date+","+changeInd+data;
	}
	
	public String augmentHeader(String header) {
		int i = header.indexOf(",");
		String ref = header.substring(0,i);
		String data = header.substring(i);
		return ref+","+controlFields + data;
	}
	
	public void setHeader(String line) {
		this.headerLine=line;
	}

	public String getHeader() {
		return headerLine;
	}
	
	public Set<String> getReferences() {
		return data.keySet();
	}
	
	public void compareTo(SourceList previousList) {
		for(String ref: previousList.getReferences()){
			if (!data.containsKey(ref)) {
				data.put(ref, augment(previousList.data.get(ref), false, true));
			}
		}
		for(String ref: data.keySet()) {
			if (!previousList.getReferences().contains(ref)) {
				data.put(ref, augment(data.get(ref), true, false));
			} else {
				data.put(ref, augment(data.get(ref), false, false));
			}
		}
	}
	
	public void writeTo(BufferedWriter bw) throws IOException {
		bw.write(isAugmented ? augmentHeader(headerLine) : headerLine);
		bw.newLine();
		for(String line: data.values()) {
			bw.write(line);
			bw.newLine();
		}
	}
}
