/**
 * RandomSet
 */
package com.dbs.lib.collection;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @see https://stackoverflow.com/questions/124671/picking-a-random-element-from-a-set/5669034#5669034
 * @author lch at 10 May 2021 22:21:58
 * @since 1.1.2
 * @version 1.0
 */
public class RandomSet<E> extends AbstractSet<E> {

	List<E> dta = new ArrayList<E>();
	Map<E, Integer> idx = new HashMap<E, Integer>();

	public RandomSet() {
	}

	public RandomSet(Collection<E> items) {
		for (E item : items) {
			idx.put(item, dta.size());
			dta.add(item);
		}
	}

	@Override
	public boolean add(E item) {
		if (idx.containsKey(item)) {
			return false;
		}
		idx.put(item, dta.size());
		dta.add(item);
		return true;
	}

	/**
	 * Override element at position <code>id</code> with last element.
	 * 
	 * @param id
	 */
	public E removeAt(int id) {
		if (id >= dta.size()) {
			return null;
		}
		E res = dta.get(id);
		idx.remove(res);
		E last = dta.remove(dta.size() - 1);
		// skip filling the hole if last is removed
		if (id < dta.size()) {
			idx.put(last, id);
			dta.set(id, last);
		}
		return res;
	}

	@Override
	public boolean remove(Object item) {
		Integer id = idx.get(item);
		if (id == null) {
			return false;
		}
		removeAt(id);
		return true;
	}

	public E get(int i) {
		return dta.get(i);
	}

	public E pollRandom(Random rnd) {
		if (dta.isEmpty()) {
			return null;
		}
		int id = rnd.nextInt(dta.size());
		return removeAt(id);
	}

	@Override
	public int size() {
		return dta.size();
	}

	@Override
	public Iterator<E> iterator() {
		return dta.iterator();
	}
}