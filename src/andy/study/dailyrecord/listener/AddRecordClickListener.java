package andy.study.dailyrecord.listener;


import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import andy.study.dailyrecord.AddRecord;
import andy.study.dailyrecord.MainActivity;
import andy.study.dailyrecord.R;
import andy.study.dailyrecord.RecordHelper;
import andy.study.dailyrecord.model.Record;
import andy.study.dailyrecord.util.ConfigLoader;


public class AddRecordClickListener implements OnClickListener {

	private AddRecord addRecord;
	
	public AddRecordClickListener(AddRecord addRecord) {
		this.addRecord = addRecord;
	}
	
	@Override
	public void onClick(View view) {			
		if (view == addRecord.getBackButton() || view == addRecord.getBackButton_bottom()) {
			Intent intent = new Intent(addRecord, MainActivity.class);
			addRecord.startActivity(intent);				
		} else if (view == addRecord.getSaveButton() || view == addRecord.getSaveButton_bottom()) {
			if (addRecord.getCostText().getText().toString().trim().equals("")) {					
//				suggestView.setText(Html.fromHtml("<font color='red'>未填写具体花费</font>"));
				Toast.makeText(addRecord.getApplicationContext(), "未填写具体花费", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Builder dialog = new AlertDialog.Builder(addRecord);
			dialog.setTitle("SaveOrContinue");
			dialog.setMessage("Save\nOr Continue to add a new Record?");
			dialog.setIcon(R.drawable.ic_launcher);
			dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.i(ConfigLoader.TAG, "You click cancel button...");
				}
			});
			dialog.setNegativeButton("Save", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.i(ConfigLoader.TAG, "You click save button...");
					
					saveAllRecord();
				}
			});
			dialog.setNeutralButton("Save&Continue", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.i(ConfigLoader.TAG, "You click save and continue button...");						
					
					Record record = addRecord.getCurrentUiRecord();
					RecordHelper.addNewRecordToList(record);
					addRecord.clearFormTabel();
				}
			});
			dialog.create();
			dialog.show();
		} else if (view == addRecord.getShowRecord_button()) {
			Builder listDialog = new AlertDialog.Builder(addRecord);
			listDialog.setTitle("已填写的记录列表");
			listDialog.setIcon(R.drawable.ic_launcher);
			
			final String[] recordArray = RecordHelper.getListArray();
			if (recordArray != null) {
				final boolean[] checkArray = RecordHelper.getListArrayChecked();
				final List<String> checkedItems = new ArrayList<String>();
				listDialog.setMultiChoiceItems(recordArray, checkArray, new DialogInterface.OnMultiChoiceClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						Log.i(ConfigLoader.TAG, which + " is " + isChecked);							
						checkArray[which] = isChecked;
					}
				});
				listDialog.setPositiveButton("删除选中项", new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int alreayChecked = 0;
						for (int i=0;i<checkArray.length;i++) {
							boolean checked = checkArray[i];
							if (checked) {
								checkedItems.add(recordArray[i]);
								alreayChecked ++;
							}
						}							
						if (alreayChecked == 0) {
							Toast.makeText(addRecord.getApplicationContext(), "没有选中项", Toast.LENGTH_SHORT).show();
						} else {								
							List<Record> recordList = new ArrayList<Record>(RecordHelper.getRecordList()) ;
							List<String> checkedItemsLoopArray = new ArrayList<String>(checkedItems);
							for (int j = 0;j<recordList.size();j++) {
								Record record = recordList.get(j);
								for (String checkedItem : checkedItemsLoopArray) {
									String[] items = checkedItem.split(ConfigLoader.RECORD_SEPARATE);
									if (record.getType_name().equals(items[0]) && String.valueOf(record.getRecord_value()).equals(items[1])) {
										int position = RecordHelper.getRecordList().indexOf(record);
										if (RecordHelper.getRecordList().get(position) != null) {
											RecordHelper.getRecordList().remove(position);
										}								
										break;
									}
								}
							}
							Toast.makeText(addRecord.getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
						}
					}					
				});
				listDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {						
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
					}
				});
				listDialog.setNeutralButton("Save", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						saveAllRecord();
					}
				});
			} else {
				listDialog.setMessage("当前无记录...");
				listDialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
			}
			listDialog.create();
			listDialog.show();
		} else {
			Log.i(ConfigLoader.TAG, "Oh Shit, What are u clicking...");
		}
	}
	
	public void saveAllRecord() {
		Record record = addRecord.getCurrentUiRecord(); 
		if (null != record) {
			RecordHelper.addNewRecordToList(record);
		}
		boolean saveResult = RecordHelper.saveAllRecord(addRecord.getDm());
		if (!saveResult) {
			Toast.makeText(addRecord.getApplicationContext(), "未能成功保存记录", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(addRecord.getApplicationContext(), "save success...", Toast.LENGTH_LONG).show();
		}
	}
}
