package andy.study.dailyrecord.chart;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer.FillOutsideLine;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;
import andy.study.dailyrecord.R;

public class MainCostChart extends AbstractDemoChart {

	@Override
	public String getName() {
		return "MainCostChart";
	}

	@Override
	public String getDesc() {
		return "MainCostChart";
	}

	@Override
	public View executeWithView(Context context) {
		String[] titles = new String[] {"收益图标"};
		List<double[]> x = new ArrayList<double[]>();
		x.add(new double[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });

		List<double[]> values = new ArrayList<double[]>();
		values.add(new double[] { 23, 12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4,
				26.1, 23.6, 20.3, 17.2, 13.9 });

		int[] colors = new int[] { context.getResources().getColor(R.color.cost_chart_line) };
		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE };
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setFillPoints(true);
		}
		setChartSettings(renderer, "近7天消费记录", "Month", "Temperature", 0, 14,
				10, 30, context.getResources().getColor(R.color.cost_today_tcolor)
				, Color.GRAY); // 设置X,Y坐标最大值
		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(context.getResources().getColor(
				R.color.cost_today_tcolor));
		renderer.setMarginsColor(context.getResources().getColor(R.color.cost_today_tcolor));
		renderer.setShowGrid(true);
		renderer.setPanEnabled(false, false);//设置不能左右上下拖动
		renderer.setShowLegend(false);       //显示图标下方说明
		//隐藏X轴坐标坐标显示，并设置每一个位置的标签（实现自定义X轴坐标的功能）
		renderer.setXLabels(0);
		renderer.addXTextLabel(1, "测试");
		renderer.addXTextLabel(1, "测试1");
		renderer.addXTextLabel(1, "测试2");
		renderer.addXTextLabel(1, "测试3");		
		// renderer.setXTitle("goods test"); //设置X轴标题
		
		// renderer.setDisplayValues(true);
		XYSeriesRenderer r = (XYSeriesRenderer) renderer.getSeriesRendererAt(0);
		FillOutsideLine line = new FillOutsideLine(FillOutsideLine.Type.BELOW);
		line.setColor(context.getResources().getColor(R.color.cost_chart_bg));
		r.addFillOutsideLine(line);
		r.setDisplayChartValues(true); // 设置在上方显示值
		r.setChartValuesTextSize(20);
		r.setChartValuesTextAlign(Align.LEFT);
		r.setLineWidth(3);		//设置线条宽度

		XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
		return ChartFactory.getLineChartView(context, dataset, renderer);
	}
}
