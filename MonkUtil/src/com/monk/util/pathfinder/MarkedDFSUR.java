/**
 * 
 */
package com.monk.util.pathfinder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author huangguanlin
 *
 * 2018年6月18日
 */
public class MarkedDFSUR extends AlgorithmBase{

	/** 已扩展地图到某点的距离,1表示不可走的点 */
	protected int[][] map;
	/** 已扩展地图到某点的距离 */
	private int[][] h;
	private List<Integer>[][] dirSq;
	private List<Point> res;
	private int height;
	private int width;
	private int maxLength;
	
	public MarkedDFSUR(MapData mapData, Point startPoint, Point endPoint) {
		super(mapData, startPoint, endPoint);
		this.height = mapData.getHeight();
		this.width = mapData.getWidth();
		this.maxLength = this.height * this.width;
		map = new int[width][height];
		h = new int[width][height];
		dirSq = new List[width][height];
		for(int i = 0; i < width; i ++){
			for(int j = 0; j < height; j++){
				h[i][j] = (int) (Math.sqrt((endPoint.x - i) * (endPoint.x - i) + (endPoint.y - j) * (endPoint.y - j)) * 10);
				map[i][j] = mapData.getMap()[i][j];
				List<Integer> dirSqList = (List<Integer>) Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
				dirSq[i][j] = dirSqList;
			}			
		}
		int max = 9999999;
		for(int i = 0; i < width; i ++){
			for(int j = 0; j < height; j++){
				ArrayList<Integer> temp = new ArrayList<>();
				int k;
				for(k = 0; k < 8; k++){
					int adjX = i + DIRX[k];
					int adjY = j + DIRY[k];
					if (adjX < 0 || adjX >= width || adjY < 0 || adjY >= height){
						temp.add(max);
					}else{
						temp.add(h[adjX][adjY]);
					}
				}
				//冒泡排序？
				for (k = 0; k < 7; k++){
					for (int l = k + 1; l < 8; l++){
						if (temp.get(k) > temp.get(l)){
							int tempInt = temp.get(k);
							temp.set(k, temp.get(l));
							temp.set(l, tempInt);
							
							tempInt = dirSq[i][j].get(k);
							dirSq[i][j].set(k, dirSq[i][j].get(l));
							dirSq[i][j].set(l, tempInt);
						}
					}
				}
			}
		}
	}
	
	private void dfsUR(){
		long startTime = System.currentTimeMillis();
		int f = 0;
		int b[] = new int[maxLength];
		openX = new ArrayList<>();
		openY = new ArrayList<>();
		openX.add(0);
		openY.add(0);
		boolean bb = true;
		boolean bd = true;
		while(bb){
			int ox = openX.get(f);
			int oy = openY.get(f);
			b[f] = -1;
			bd = true;
			while(bd){
				b[f]++;
				if(b[f] < 8){
					count++;
					int adjX = ox + DIRX[dirSq[ox][oy].get(b[f])];
					int adjY = oy + DIRY[dirSq[ox][oy].get(b[f])];
					
					// 如果超出范围
					if (adjX < 0 || adjX >= width || adjY < 0 || adjY >= height){
						continue;
					}
					// 如果是不可走区域，或是已走过的区域
					if (map[adjX][adjY] != 0){
						continue;
					}
					map[adjX][adjY] = map[ox][oy] + DIRV[dirSq[ox][oy].get(b[f])];
					f++;
					if(f >= openX.size()){
						openX.add(adjX);
						openY.add(adjY);
					}else{
						openX.set(f, adjX);
						openY.set(f, adjY);
					}
					if (adjX == endPoint.x && adjY == endPoint.y){
						useTime = System.currentTimeMillis() - startTime;
						for (int i = f; i >= 0; i--){
							res.add(new Point(openX.get(i), openY.get(i)));
						}
						bb = false;
						System.out.println("count = " + count);
						System.out.println("useTime = " + useTime);
						return;
					}
					else{
						bd = false;
					}
				}else{
					f--;
					bd = false;
					if (f < 0)
					{
						bb = false;
					}
				}
			}
		}
		useTime = System.currentTimeMillis() - startTime;
		System.out.println("count = " + count);
		System.out.println("useTime = " + useTime);
	}
	
	@Override
	public List<Point> searchPath(){
		res = new ArrayList<>();
		map[startPoint.x][startPoint.y] = 1;
		dfsUR();
		return res;
	}
	
	@Override
	public List<Point> getOpenPoint(){
		List<Point> res = new ArrayList<>();
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				if(map[i][j] != 0 && map[i][j] != 1){
					res.add(new Point(i, j));
				}
			}
		}
		return res;
	}
}
