package com.syml.proposalRefinance.vaadin;

import java.io.File;
import java.io.FileNotFoundException;

import com.vaadin.server.ClassResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Image;

public class ImageService {
	
	
	public static Image getImage(Resource source){
		
		Image image = new Image();
		image.setSource(source);
		return image;
		
	}
	
	public static FileResource  getFile(){
		String basepath = VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath();
		File file = new File(basepath+"/war/images/image.png");
		FileResource fileresource = new FileResource(file);
		return fileresource;
		
	}
	/*public static ClassResource  getImageFile(){
		ClassResource cl = new ClassResource("/war/images/image.png");
		
		//FileResource fileresource = new FileResource(file);
		return cl;
		
	}*/
	public static ThemeResource  getImageFile(){
		ThemeResource cl = new ThemeResource("images/image.png");
		//VaadinService.getCurrent().getConfiguredTheme(VaadinService.getCurrentRequest());

		//FileResource fileresource = new FileResource(file);
		return cl;
		
	}

}
