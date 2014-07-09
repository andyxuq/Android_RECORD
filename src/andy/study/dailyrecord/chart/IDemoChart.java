package andy.study.dailyrecord.chart;

import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Defines the demo charts.
 */
public interface IDemoChart {
  /** A constant for the name field in a list activity. */
  String NAME = "name";
  /** A constant for the description field in a list activity. */
  String DESC = "desc";

  /**
   * Returns the chart name.
   * 
   * @return the chart name
   */
  String getName();

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  String getDesc();

  /**
   * Executes the chart demo.
   * 
   * @param context the context
   * @return the built view
   */  
  View executeWithView(Context context);

}
