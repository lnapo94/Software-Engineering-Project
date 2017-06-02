package it.polimi.ingsw.ps42.model;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polimi.ingsw.ps42.model.StaticList;

public class StaticListTest {

	@Test
	public void test() {
		StaticList<Integer> list = new StaticList<>(3);
		list.add(2);
		System.out.println(list.get(0));
		list.add(5);
		list.add(1);
		for(Integer i : list) {
			System.out.println("Number: " + i.toString());
		}
		list.remove(2);
		assertEquals("Now list has 2 elements", 2, list.size());
		for(Integer i : list) {
			System.out.println("Number: " + i.toString());
		}
		list.removeAll();
		System.out.println(list.size());
		for(Integer i : list) {
			System.out.println("Number: " + i.toString());
		}
		assertEquals("Now list is empty", 0, list.size());
	}
	

}
