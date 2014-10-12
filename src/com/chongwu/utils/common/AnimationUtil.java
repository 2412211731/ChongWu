package com.chongwu.utils.common;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {
    
	/**
	 * 如果view在A(x,y)点 那么动画就是从B点(x+fromXDelta, y+fromYDelta)点移动到C 点(x+toXDelta,y+toYDelta)点.
	 * @param fromXDelta	这个参数表示动画开始的点离当前View X坐标上的差值；
	 * @param toXDelta	这个参数表示动画结束的点离当前View X坐标上的差值；
	 * @param fromYDelta	这个参数表示动画开始的点离当前View Y坐标上的差值；
	 * @param toYDelta	这个参数表示动画开始的点离当前View Y坐标上的差值；
	 * @return
	 */
	public static TranslateAnimation createTranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta){
		TranslateAnimation tsAnim = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
		return tsAnim;
	}
	
	/**
	 * 如果选择参照为Animation.ABSOLUTE，那么对应的值应该是具体的坐标值，比如100到300，指绝对的屏幕像素单位
	 * 如果选择参照为Animation.RELATIVE_TO_SELF或者 Animation.RELATIVE_TO_PARENT指的是相对于自身或父控件，对应值应该理解为相对于自身或者父控件的几倍或百分之多少。多试参数就明白了。
	 * @param fromXType	第一个参数是x轴方向的值的参照(Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF,or Animation.RELATIVE_TO_PARENT);
	 * @param fromXValue	第二个参数是第一个参数类型的起始值；
	 * @param toXType	x轴方向的终点参照；
	 * @param toXValue	x轴方向的终点对应值；
	 * @param fromYType
	 * @param fromYValue
	 * @param toYType
	 * @param toYValue
	 * @return
	 */
	public static TranslateAnimation createTranslateAnimation(int fromXType,float fromXValue,int toXType,float  toXValue,int fromYType,float  fromYValue,int toYType,float  toYValue){
		TranslateAnimation tsAnim = new TranslateAnimation(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue, toYType, toYValue);
		return tsAnim;
	}
	
	/**
	 * 透明动画
	 * @param fromAlpha // 0.0表示完全透明// 1.0表示完全不透明
	 * @param toAlpha
	 * @return
	 */
	public static AlphaAnimation createAlphaAnimation(float fromAlpha,float  toAlpha){
		AlphaAnimation tsAnim = new AlphaAnimation(fromAlpha, toAlpha);
		return tsAnim;
	}
	
	/**
	 * 旋转动画
	 * @param fromDegrees 开始角度
	 * @param toDegrees 结束角度
	 * @return
	 */
	public static RotateAnimation createRotateAnimation(float fromDegrees,float  toDegrees){
		RotateAnimation tsAnim = new RotateAnimation(fromDegrees, toDegrees);
		return tsAnim;
	}
	
	/**
	 * 旋转动画
	 * @param fromDegrees 开始角度
	 * @param toDegrees 结束角度
	 * @param pivotX 选转中心点
	 * @param pivotY 选转中心点
	 * @return
	 */
	public static RotateAnimation createRotateAnimation(float fromDegrees,float  toDegrees,float  pivotX, float  pivotY){
		RotateAnimation tsAnim = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
		return tsAnim;
	}
	
	/**
	 * 缩放动画
	 * @param fromX 动画起始时 X坐标上的伸缩尺寸 
	 * @param toX 动画结束时 X坐标上的伸缩尺寸 
	 * @param fromY 动画起始时 Y坐标上的伸缩尺寸 
	 * @param toY 动画结束时Y坐标上的伸缩尺寸 
	 * @return
	 */
	public static ScaleAnimation createScaleAnimation(float fromX,float  toX,float  fromY,float  toY){
		ScaleAnimation tsAnim = new ScaleAnimation(fromX, toX, fromY, toY);
		return tsAnim;
	}
 
	/**
	 * 设置动画的常用参数
	 * @param anim 动画
	 * @param durationMillis 动画持续时间
	 * @param repeatCount 设置重复次数 
	 * @param isFillAfter 动画执行完后是否停留在执行完的状态 
	 * @param startOffset 执行前的等待时间 
	 * @return
	 */
	public static Animation setAnimationAttribute(Animation anim,long durationMillis,int repeatCount,boolean isFillAfter,long startOffset){
		/** 常用方法 */ 
		anim.setDuration(durationMillis);//设置动画持续时间 
		anim.setRepeatCount(repeatCount);//设置重复次数 
		anim.setFillAfter(isFillAfter);//动画执行完后是否停留在执行完的状态 
		anim.setStartOffset(startOffset);//执行前的等待时间 
		return anim;
	}
	
	
	/**
	 * 多种动画组合
	 * @param transAnim 移动动画
	 * @param scaleAnim 缩放动画
	 * @param alphaAnim 透明动画
	 * @param rotateAnim 选中动画
	 * @return AnimationSet
	 */
	public static AnimationSet setAnimationSet(TranslateAnimation transAnim,ScaleAnimation scaleAnim,AlphaAnimation alphaAnim,RotateAnimation rotateAnim){
		 AnimationSet animationSet = new AnimationSet(true);    
		 if(transAnim != null){
			 animationSet.addAnimation(transAnim);
		 }
		 if(scaleAnim != null){
			 animationSet.addAnimation(scaleAnim);
		 }
		 if(alphaAnim != null){
			 animationSet.addAnimation(alphaAnim);
		 }
		 if(rotateAnim != null){
			 animationSet.addAnimation(rotateAnim);
		 }
         return animationSet;
	}
	 
	/**
	 * 帧动画
	 * @param oneShot 是否重复
	 * @param drawableList 
	 * @param duration 动画持续时间
	 * @return AnimationDrawable
	 */
	public AnimationDrawable setAnimationDrawable(boolean oneShot,ArrayList<Drawable> drawableList,int duration){
		AnimationDrawable animDrawable = new AnimationDrawable();
		animDrawable.setOneShot(oneShot);
		int size = drawableList.size();
		for(int i=0;i<size;i++){
			animDrawable.addFrame(drawableList.get(i), duration);
		}
		return animDrawable;
	}
	
	/**
	 * 帧动画
	 * @param oneShot 是否重复 
	 * @param drawable 帧图片
	 * @param duration 动画持续时间
	 * @return AnimationDrawable
	 */
	public AnimationDrawable setAnimationDrawable(boolean oneShot,Drawable drawable,int duration){
		AnimationDrawable animDrawable = new AnimationDrawable();
		animDrawable.setOneShot(oneShot);
		animDrawable.addFrame(drawable, duration);
		return animDrawable;
	}
	
	/**
	 * 帧动画
	 * @param animDrawable 帧动画
	 * @param drawable 帧图片
	 * @param duration 动画持续时间
	 * @return
	 */
	public AnimationDrawable setAnimationDrawable(AnimationDrawable animDrawable,Drawable drawable,int duration){
		animDrawable.addFrame(drawable, duration);
		return animDrawable;
	}
	
	/**
	 * 从资源文件创建动画
	 * @param context 
	 * @param souceId 动画资源id
	 * @return
	 */
	public Animation creatAnimationFromXml(Context context,int souceId){
		  return  AnimationUtils.loadAnimation(context, souceId);
	}
}
