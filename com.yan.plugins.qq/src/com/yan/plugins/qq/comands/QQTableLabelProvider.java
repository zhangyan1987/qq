package com.yan.plugins.qq.comands;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.yan.plugins.qq.util.ImageManager;
import com.yan.qq.common.User;

public class QQTableLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		
		return ImageManager.getImage(ImageManager.MINI_QQ);
		//return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
	}

	@Override
	public String getText(Object element) {
		// TODO Auto-generated method stub
		User user = (User)element;
		return user.getNickName()+"("+user.getQQnumber()+")";
	}

}
