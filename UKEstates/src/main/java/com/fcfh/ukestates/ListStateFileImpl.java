package com.fcfh.ukestates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ListStateFileImpl implements ListState, Serializable {

	private Date lastModifiedDate=null;
	private List<String> deletedEstates=new ArrayList<String>(0);
	private List<String> estates=new ArrayList<String>(0);
	private List<String> newEstates=new ArrayList<String>(0);

	/**
	 * 
	 */
	private static final long serialVersionUID = -478652987921816878L;

	public Date getLastModified() {
		return lastModifiedDate;
	}

	public void setLastModified(Date modified) {
		this.lastModifiedDate=modified;
	}

	public List<String> getDeletedEstates() {
		if (deletedEstates==null) {
			deletedEstates = new ArrayList<String>(0);
		}
		return deletedEstates;
	}

	public void addDeletedEstates(List<String> estates) {
		getDeletedEstates().addAll(estates);
	}

	public void commitState() {
		ListStateFactory.commitState(this);
	}

	public List<String> getEstates() {
		return new ArrayList<String>(estates);
	}

	public void addEstates(List<String> estatesIn) {
		if (estatesIn==null || estatesIn.isEmpty()) {
			return;
		}
		if (this.estates==null) {
			estates = new ArrayList<String>(estatesIn.size());
		}
		estates.addAll(estatesIn);
		
	}

	public List<String> getNewEstates() {
		return new ArrayList<String>(newEstates);
	}

	public void addNewEstates(List<String> estates) {
		if (estates==null || estates.isEmpty()) {
			return;
		}
		if (newEstates==null) {
			newEstates = new ArrayList<String>(estates.size());
		}
		newEstates.addAll(estates);
	}

	public void updateState(Set<String> currentEstates) {
		// tmp list to manage computations, initialised with our view of the estates
		List<String> tmpList = new ArrayList<String>(estates);
		// remove the currentEstates from the downloaded file
		tmpList.removeAll(currentEstates);
		deletedEstates.clear();
		// update the deleted estates
		deletedEstates.addAll(tmpList);
		// clear tmp list and init with the current estates
		tmpList.clear();
		tmpList.addAll(currentEstates);
		// remove all the estates we have from last time
		tmpList.removeAll(estates);
		newEstates.clear();
		// clear the new estates and update with the new ones 
		newEstates.addAll(tmpList);
		// clear our previous view and update with the current estates
		estates.clear();
		estates.addAll(currentEstates);
	}

	public boolean isOutOfDate(Date modified) {
		return getLastModified().before(modified);
	}
	
	
}
