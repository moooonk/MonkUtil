/**
 * 
 */
package com.monk.util.pathfinder;

/**
 * @author huangguanlin
 *
 * 2018年6月18日
 */
public class MapData {
	public static final int SCENE_WIDTH = 960;
	public static final int SCENE_HEIGHT= 560;
	private int[][] map = new int[SCENE_WIDTH][SCENE_HEIGHT];
	
	public MapData(){
		creatMap();
	}
	
	public int[][] getMap(){
		return map;
	}
	
	public int getHeight(){
		return SCENE_HEIGHT;
	}
	
	public int getWidth(){
		return SCENE_WIDTH;
	}
	
	private void creatObstacle(int x, int y, int xlen, int ylen){
		for(int i = x; i < x + xlen; i++){
			for(int j = y; j < y + ylen; j++){
				map[i][j] = 1;
			}
		}
	}
	
	private void creatMap(){
		creatObstacle(20, 250, 480, 20);
		creatObstacle(20, 200, 20, 50);
		creatObstacle(100, 200, 20, 50);
	}
	
	
}
