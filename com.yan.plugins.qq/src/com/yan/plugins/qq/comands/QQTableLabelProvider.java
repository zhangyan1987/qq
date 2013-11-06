package com.yan.plugins.qq.comands;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.yan.plugins.qq.Activator;
import com.yan.plugins.qq.model.User;

public class QQTableLabelProvider extends LabelProvider {

	private Image image ;
	@Override
	public Image getImage(Object element) {
		Display display = Display.getCurrent();
		if(null == image)
			image = new Image(display,Activator.class.getResourceAsStream("/icons/minqq.jpg"));
		return image;
		//return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
	}

	@Override
	public String getText(Object element) {
		// TODO Auto-generated method stub
		User user = (User)element;
		return user.getNickName()+"("+user.getQQnumber()+")";
	}

}
