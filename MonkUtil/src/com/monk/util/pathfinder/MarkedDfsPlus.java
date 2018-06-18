/**
 * 
 */
package com.monk.util.pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author huangguanlin
 *
 * 2018年6月18日
 */
public class MarkedDfsPlus extends MarkedDFSUR{

	private static final int MARK = 2;
	/**
	 * @param mapData
	 * @param startPoint
	 * @param endPoint
	 */
	public MarkedDfsPlus(MapData mapData, Point startPoint, Point endPoint) {
		super(mapData, startPoint, endPoint);
	}

	private void init(List<Point> road){
		for(Point point : road){
			map[point.x][point.y] = MARK;
		}
	}
	
	@Override
	public List<Point> searchPath(){
		long startTime = System.currentTimeMillis();
		List<Point> road = super.searchPath();
		if(road.size() == 0){
			return road;
		}
		this.initData();
		this.init(road);
		map[startPoint.x][startPoint.y] = 1;
		openX.add(startPoint.x);
		openY.add(startPoint.y);
		parents.add(-1);
		openTail++;
		while (openHead < openTail){
			int ox = openX.get(openHead);
			int oy = openY.get(openHead);
			openHead++;
			
			for(int n = 0; n < 8; n++){
				count++;
				int adjX = ox + DIRX[n];
				int adjY = oy + DIRY[n];
				// 如果超出范围
				if (adjX < 0 || adjX >= mapData.getWidth() || adjY < 0 || adjY >= mapData.getHeight()){
					continue;
				}

				// 已搜索路径的区域
				if (map[adjX][adjY] != MARK){
					continue;
				}
				map[adjX][adjY] = map[ox][oy] + DIRV[n];
				openX.add(adjX);
				openY.add(adjY);
				parents.add(openHead - 1);
				System.out.println(openHead - 1);
				openTail++;

				if (adjX == endPoint.x && adjY == endPoint.y){
					hasFind = true;
					break;
				}
			}
			if (hasFind)
			{
				break;
			}
		}
		useTime = System.currentTimeMillis() - startTime;
		if (!hasFind){
			return Collections.emptyList();
		}
		System.out.println("count = " + count);
		System.out.println("useTime = " + useTime);
		return linkRoad();
	}
	
	@Override
	public List<Point> getOpenPoint(){
		List<Point> res = new ArrayList<>();
		for(int i = 0; i < openX.size(); i++){
			res.add(new Point(openX.get(i), openY.get(i)));
		}
		return res;
	}
	
}
