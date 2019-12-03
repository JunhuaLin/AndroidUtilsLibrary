package cn.junhua.android.utilslibrary.interpolator;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.animation.Interpolator;

/**
 * 基于贝塞尔曲线的动画插值器<br/>
 * 实现原理：<br/>
 * 借助{@link Path}和{@link PathMeasure}计算贝塞尔曲线各个点，通过{@link Interpolator}差值器采样获取对应应变点的值<br/>
 * 曲线变化点的起始点和结束点为(0,0)和(1,1),故差值器的取值的开始点和结束为0和1.<br/>
 * 此处不能称为插值器的取值区间为[0,1],因为根据参考点的不同取值可能为任意值，只是结果必定是从0开始到1结束。<br/>
 * Created by junhualin on 2019/11/27.
 */
public class BezierInterpolator implements Interpolator
{
	/**
	 * 参考点1
	 */
	private float x1, y1;
	/**
	 * 参考点2
	 */
	private float x2, y2;

	/**
	 * 计算贝塞尔曲线
	 */
	private PathMeasure mBezierPathMeasure;
	/**
	 * 贝塞尔曲线长度
	 */
	private float mBezierLength;

	/**
	 * 插值器结果temp
	 */
	private float[] mPosArrTemp = new float[]{0, 0};

	/**
	 * 贝塞尔曲线参考点
	 *
	 * @param x1 参考点x1
	 * @param y1 参考点y1
	 * @param x2 参考点x2
	 * @param y2 参考点y2
	 */
	public BezierInterpolator(float x1, float y1, float x2, float y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;

		initBezierPath();
	}

	/**
	 * 初始化贝塞尔曲线
	 */
	private void initBezierPath()
	{
		Path path = new Path();
		path.cubicTo(x1, y1, x2, y2, 1, 1);
		mBezierPathMeasure = new PathMeasure(path, false);
		mBezierLength = mBezierPathMeasure.getLength();
	}

	@Override
	public float getInterpolation(float input)
	{
		mBezierPathMeasure.getPosTan(mBezierLength * input, mPosArrTemp, null);
		return mPosArrTemp[1];
	}
}
