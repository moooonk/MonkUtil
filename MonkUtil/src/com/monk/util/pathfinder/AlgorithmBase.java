/**
 * 
 */
package com.monk.util.pathfinder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangguanlin
 *
 * 2018年6月18日
 */
public class AlgorithmBase {
	protected static final int[] DIRX = {1, 0, -1, 0, 1, -1, -1, 1};
	protected static final int[] DIRY = {0, 1, 0, -1, 1, 1, -1, -1};
	protected static final int[] DIRV = {10, 10, 10, 10, 14, 14, 14, 14};
	
	protected ArrayList<Integer> openX;
	protected ArrayList<Integer> openY;
	protected ArrayList<Integer> parents;
	
	protected int openHead;
	protected int openTail;
	
	protected boolean hasFind;
	protected int count;
	
	protected long useTime;
	protected MapData mapData;
	
	protected Point startPoint;
	protected Point endPoint;
	
	public AlgorithmBase(MapData mapData, Point startPoint, Point endPoint){
		openX = new ArrayList<>();
		openY = new ArrayList<>();
		parents = new ArrayList<>();
		init(mapData, startPoint, endPoint);
	}
	
	protected List<Point> linkRoad(){
		int p = openTail - 1;
		List<Point> res = new ArrayList<Point>();
		while(p != -1){
			res.add(new Point(openX.get(p), openY.get(p)));
			p = parents.get(p);
		}
		return res;
	}
	
	protected List<Point> getOpenPoint(){
		List<Point> res = new ArrayList<Point>();
		for(int i = 0; i < openX.size(); i++){
			res.add(new Point(openX.get(i), openY.get(i)));
		}
		return res;
	}

	public int getCount() {
		return count;
	}

	public long getUseTime() {
		return useTime;
	}
	
	public void init(MapData mapData, Point startPoint, Point endPoint){
		this.mapData = mapData;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}
	
	public List<Point> searchPath(){
		return null;
	}
	
	protected void initData(){
		hasFind = false;
		openHead = 0;
		openTail = 0;
		parents = new ArrayList<>();
		openX = new ArrayList<>();
		openY = new ArrayList<>();
	}
	
}
