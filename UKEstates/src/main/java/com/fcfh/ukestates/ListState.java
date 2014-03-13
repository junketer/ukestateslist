package com.fcfh.ukestates;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ListState {
	
	public boolean isOutOfDate(Date modified);
	
	public Date getLastModified();
	public void setLastModified(Date modified);
	
	public List<String> getDeletedEstates();
	public void addDeletedEstates(List<String> estates);
	
	public List<String> getEstates();
	public void addEstates(List<String> estates);
	
	public List<String> getNewEstates();
	public void addNewEstates(List<String> estates);
	
	public void updateState(Set<String> currentEstates);
	public void commitState();
}
