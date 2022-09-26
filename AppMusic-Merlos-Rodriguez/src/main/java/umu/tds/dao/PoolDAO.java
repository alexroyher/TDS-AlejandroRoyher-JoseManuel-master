package umu.tds.dao;

import java.util.HashMap;

public class PoolDAO {
	private static PoolDAO instance = null;
	private HashMap<Integer,Object> pool;
	
	private PoolDAO(){
		pool = new HashMap<Integer,Object>();
	}
	
	public static PoolDAO getInstance(){
		if(instance == null) instance = new PoolDAO();
		return instance;
	}
	
	public void addObject(int id, Object object) {
		pool.put(id, object);
	}
	
	public void removeObject(int id) {
		pool.remove(id);
	}
	
	public Object getObject(int id) {
		return pool.get(id);
	}
	
	public boolean contains(int id){
		return pool.containsKey(id);
	}
	
	public void empty() {
		this.pool = new HashMap<Integer, Object>();
	}
}