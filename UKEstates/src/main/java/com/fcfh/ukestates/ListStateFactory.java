package com.fcfh.ukestates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ListStateFactory {
	public static String FILE_IMPL="FILE";
	private static String FILE_NAME = "/home/dan/UnclaimedEstateListState.dat";
	public static ListState createListState(String type) {
		if (FILE_IMPL.equals(type)) {
			if (new File(FILE_NAME).exists()) {
				return readFromFile(FILE_NAME);				
			} else {
				return new ListStateFileImpl();
			}
		}
		throw new IllegalStateException("No implementation for type " + type);
	}
	
	
	private static ListState readFromFile(String file) {
		ObjectInputStream ois;
		Object o = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(new File(file)));
			o = ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (o instanceof ListState) {
			return (ListState)o;
		}
		throw new IllegalStateException("Object not instance of ListState " + o.getClass());
	}
	
	protected static void commitState(ListState listState) {
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(new File(FILE_NAME)));
			oos.writeObject(listState);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
