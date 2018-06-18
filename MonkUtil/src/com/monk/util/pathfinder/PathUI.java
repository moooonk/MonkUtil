/**
 * 
 */
package com.monk.util.pathfinder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * @author huangguanlin
 *
 * 2018年6月18日
 */
public class PathUI {

	protected Shell shell;
	private Display display;
	private Canvas canvas;
	private Button btnDfsur_1;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			PathUI window = new PathUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		System.exit(0);
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(MapData.SCENE_WIDTH + 36, MapData.SCENE_HEIGHT + 88);
		shell.setText("SWT Application");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 10, MapData.SCENE_WIDTH, MapData.SCENE_HEIGHT);
		
		canvas = new GLCanvas(composite, SWT.NONE, new GLData());
		canvas.setBounds(0, 0, MapData.SCENE_WIDTH, MapData.SCENE_HEIGHT);
		
		Button btnDfsur = new Button(shell, SWT.NONE);
		btnDfsur.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				MarkedDFSUR dfs = new MarkedDFSUR(new MapData(), new Point(0, 0), new Point(80, 400));
				List<Point> res = dfs.searchPath();
				print(res, dfs.mapData);				
			}
		});
		btnDfsur.setBounds(10, 576, 80, 27);
		btnDfsur.setText("DFSUR");
		
		btnDfsur_1 = new Button(shell, SWT.NONE);
		btnDfsur_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				MarkedDfsPlus dfs = new MarkedDfsPlus(new MapData(), new Point(0, 0), new Point(80, 400));
				List<Point> res = dfs.searchPath();
				print(res, dfs.mapData);	
			}
		});
		btnDfsur_1.setBounds(96, 576, 80, 27);
		btnDfsur_1.setText("DFSUR+");
	}
	
	public void print(List<Point> points, MapData mapData){
		display.syncExec(() -> {
			ImageData imageData = new ImageData( MapData.SCENE_WIDTH, MapData.SCENE_HEIGHT, 32, new PaletteData(0xFF000000, 0xFF0000, 0xFF00));
			for(int i = 0; i < MapData.SCENE_WIDTH; i++){
				for(int j = 0; j < MapData.SCENE_HEIGHT; j++){
					if(mapData.getMap()[i][j] != 0){
						imageData.setPixel(i, j, 0xFF0000FF);
					}
				}
			}
			Image image = new Image(display, imageData);
			GC gc = new GC(canvas);
			gc.drawImage(image, 0, 0);
			
			ImageData whiteData = new ImageData(1, 1, 32, new PaletteData(0xFF000000, 0xFF0000, 0xFF00));
			whiteData.setPixel(0, 0, 0xFFFFFFFF);
			Image whiteImage = new Image(display, whiteData);
			for(int i = points.size() - 1; i >=0 ; i--){
				gc.drawImage(whiteImage, points.get(i).x, points.get(i).y);
				try {
					Thread.currentThread().sleep(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			image.dispose();
			whiteImage.dispose();
			gc.dispose();
		});
	}
}
