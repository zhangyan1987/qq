package com.yan.plugins.qq.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.yan.plugins.qq.Activator;
import com.yan.plugins.qq.log.Logger;


public class ImageManager {
	
	public static final String MINI_QQ = "/icons/miniqq.jpg";
	public static final String QQ = "/icons/qq.jpg";
	
	
	private static Map<String ,Image> imageCache;  
	
	//use Map to cache images if they have been loaded
	
	
	
	private static Image loadImage(String imageName) {
		
		Logger.log("load image : "+imageName);
		Image	image = new Image(Display.getCurrent(),
				Activator.class.getResourceAsStream(imageName));
		return image;
	}
	
	public static Image getImage(String imageName) {
		if(imageName == null)
			throw new RuntimeException("image name is null");
		if(imageCache == null) {
			imageCache = new HashMap<String, Image>();
			Image image = loadImage(imageName);
			imageCache.put(imageName, image);
			return image;
		} else {
			if(imageCache.get(imageName) == null) {
				Image image = loadImage(imageName);
				imageCache.put(imageName, image);
				return image;
			} else {
				
				return imageCache.get(imageName);
			}
				
		}
		
	}

	
}
