package cn.junhua.android.utilslibrary.ninepatch;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.NinePatch;
import android.graphics.drawable.NinePatchDrawable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 * 动态构建bitmap图片的拉伸区域，生成NinePatchDrawable，即动态生成点9图<p>
 * https://mp.weixin.qq.com/s?__biz=MzI1NjEwMTM4OA==&mid=2651232105&idx=1&sn=fcc4fa956f329f839f2a04793e7dd3b9&mpshare=1&scene=21&srcid=0719Nyt7J8hsr4iYwOjVPXQE#wechat_redirect
 * Created by junhualin on 2019/12/4.
 * <p>
 * This chunk specifies how to split an image into segments for
 * scaling.
 * <p>
 * There are J horizontal and K vertical segments.  These segments divide
 * the image into J*K regions as follows (where J=4 and K=3):
 * <p>
 *      F0   S0    F1     S1
 *   +-----+----+------+-------+
 * S2|  0  |  1 |  2   |   3   |
 *   +-----+----+------+-------+
 *   |     |    |      |       |
 *   |     |    |      |       |
 * F2|  4  |  5 |  6   |   7   |
 *   |     |    |      |       |
 *   |     |    |      |       |
 *   +-----+----+------+-------+
 * S3|  8  |  9 |  10  |   11  |
 *   +-----+----+------+-------+
 * <p>
 * Each horizontal and vertical segment is considered to by either
 * stretchable (marked by the Sx labels) or fixed (marked by the Fy
 * labels), in the horizontal or vertical axis, respectively. In the
 * above example, the first is horizontal segment (F0) is fixed, the
 * next is stretchable and then they continue to alternate. Note that
 * the segment list for each axis can begin or end with a stretchable
 * or fixed segment.
 */
public class NinePatchBuilder
{
	private int width, height;
	private Bitmap bitmap;
	private Resources resources;
	private ArrayList<Integer> xRegions = new ArrayList<>();
	private ArrayList<Integer> yRegions = new ArrayList<>();

	public NinePatchBuilder(Resources resources, Bitmap bitmap)
	{
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		this.bitmap = bitmap;
		this.resources = resources;
	}

	/**
	 * 构造函数
	 *
	 * @param width  图片宽
	 * @param height 图片高
	 */
	public NinePatchBuilder(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	/**
	 * x拉伸区域
	 *
	 * @param x     起点
	 * @param width 宽度
	 */
	public NinePatchBuilder addXRegion(int x, int width)
	{
		xRegions.add(x);
		xRegions.add(x + width);
		return this;
	}

	/**
	 * x拉伸区间
	 *
	 * @param x1 起点
	 * @param x2 终点
	 */
	public NinePatchBuilder addXRegionPoints(int x1, int x2)
	{
		xRegions.add(x1);
		xRegions.add(x2);
		return this;
	}

	/**
	 * x拉伸区间
	 *
	 * @param xPercent     起点
	 * @param widthPercent 宽度
	 */
	public NinePatchBuilder addXRegion(float xPercent, float widthPercent)
	{
		int xtmp = (int) (xPercent * this.width);
		xRegions.add(xtmp);
		xRegions.add(xtmp + (int) (widthPercent * this.width));
		return this;
	}

	/**
	 * x拉伸区间
	 *
	 * @param x1Percent 起点
	 * @param x2Percent 终点
	 */
	public NinePatchBuilder addXRegionPoints(float x1Percent, float x2Percent)
	{
		xRegions.add((int) (x1Percent * this.width));
		xRegions.add((int) (x2Percent * this.width));
		return this;
	}

	/**
	 * x拉伸中心区间
	 *
	 * @param width 宽度
	 */
	public NinePatchBuilder addXCenteredRegion(int width)
	{
		int x = (int) ((this.width - width) / 2);
		xRegions.add(x);
		xRegions.add(x + width);
		return this;
	}

	/**
	 * x拉伸中心区间
	 *
	 * @param widthPercent 宽度百分比
	 */
	public NinePatchBuilder addXCenteredRegion(float widthPercent)
	{
		int width = (int) (widthPercent * this.width);
		int x = (int) ((this.width - width) / 2);
		xRegions.add(x);
		xRegions.add(x + width);
		return this;
	}

	/**
	 * y轴拉伸
	 *
	 * @param y      起点
	 * @param height 高度
	 */
	public NinePatchBuilder addYRegion(int y, int height)
	{
		yRegions.add(y);
		yRegions.add(y + height);
		return this;
	}

	/**
	 * y轴拉伸
	 *
	 * @param y1 起点
	 * @param y2 终点
	 */
	public NinePatchBuilder addYRegionPoints(int y1, int y2)
	{
		yRegions.add(y1);
		yRegions.add(y2);
		return this;
	}

	/**
	 * y轴拉伸
	 *
	 * @param yPercent      起点百分比
	 * @param heightPercent 高度
	 */
	public NinePatchBuilder addYRegion(float yPercent, float heightPercent)
	{
		int ytmp = (int) (yPercent * this.height);
		yRegions.add(ytmp);
		yRegions.add(ytmp + (int) (heightPercent * this.height));
		return this;
	}

	/***
	 * y轴拉伸，百分比定位
	 * @param y1Percent 起点百分比
	 * @param y2Percent 终点百分比
	 */
	public NinePatchBuilder addYRegionPoints(float y1Percent, float y2Percent)
	{
		yRegions.add((int) (y1Percent * this.height));
		yRegions.add((int) (y2Percent * this.height));
		return this;
	}

	/**
	 * y轴中心拉伸宽度
	 *
	 * @param height y轴中心拉伸高度
	 */
	public NinePatchBuilder addYCenteredRegion(int height)
	{
		int y = (int) ((this.height - height) / 2);
		yRegions.add(y);
		yRegions.add(y + height);
		return this;
	}

	/**
	 * y轴中心拉伸宽度
	 *
	 * @param heightPercent y轴中心拉伸高度百分比
	 */
	public NinePatchBuilder addYCenteredRegion(float heightPercent)
	{
		int height = (int) (heightPercent * this.height);
		int y = (int) ((this.height - height) / 2);
		yRegions.add(y);
		yRegions.add(y + height);
		return this;
	}

	/***
	 * 拼接.9图的拉伸区域
	 * @return byte[] 拉伸信息
	 */
	public byte[] buildChunk()
	{
		if (xRegions.size() == 0)
		{
			xRegions.add(0);
			xRegions.add(width);
		}
		if (yRegions.size() == 0)
		{
			yRegions.add(0);
			yRegions.add(height);
		}

		int NO_COLOR = 1;//0x00000001;
		int COLOR_SIZE = 9;//could change, may be 2 or 6 or 15 - but has no effect on output
		int arraySize = 1 + 2 + 4 + 1 + xRegions.size() + yRegions.size() + COLOR_SIZE;
		ByteBuffer byteBuffer = ByteBuffer.allocate(arraySize * 4).order(ByteOrder.nativeOrder());
		byteBuffer.put((byte) 1);//was translated
		byteBuffer.put((byte) xRegions.size());//divisions x
		byteBuffer.put((byte) yRegions.size());//divisions y
		byteBuffer.put((byte) COLOR_SIZE);//color size

		//skip
		byteBuffer.putInt(0);
		byteBuffer.putInt(0);

		//padding -- always 0 -- left right top bottom
		byteBuffer.putInt(0);
		byteBuffer.putInt(0);
		byteBuffer.putInt(0);
		byteBuffer.putInt(0);

		//skip
		byteBuffer.putInt(0);

		for (int rx : xRegions)
			byteBuffer.putInt(rx); // regions left right left right ...
		for (int ry : yRegions)
			byteBuffer.putInt(ry);// regions top bottom top bottom ...

		for (int i = 0; i < COLOR_SIZE; i++)
			byteBuffer.putInt(NO_COLOR);

		return byteBuffer.array();
	}

	/**
	 * 构建NinePatch拉伸信息
	 *
	 * @return NinePatch
	 */
	public NinePatch buildNinePatch()
	{
		byte[] chunk = buildChunk();
		if (bitmap != null)
		{
			return new NinePatch(bitmap, chunk, null);
		}
		return null;
	}

	/**
	 * 构建NinePatchDrawable
	 *
	 * @return NinePatchDrawable
	 */
	public NinePatchDrawable build()
	{
		NinePatch ninePatch = buildNinePatch();
		if (ninePatch != null)
		{
			return new NinePatchDrawable(resources, ninePatch);
		}
		return null;
	}

}
