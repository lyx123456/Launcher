package com.example.ly.launcher.ui;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ly.launcher.R;


public class UpdateDialog extends Dialog {

	public UpdateDialog(Context context) {
		super(context);
	}

	public UpdateDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title = "更新内容";
		private String message;
		private String positiveButtonText;
		private String negativeButtonText = "稍后再说";
		private View contentView;
		private OnClickListener positiveButtonClickListener;
		private OnClickListener negativeButtonClickListener;
		private ListView listView;
		private  MyAdapter adapter ;
		private String[] mDatas = new String[]{};

		public Builder(Context context,String[] datas) {
			this.context = context;
			this.adapter = new MyAdapter();
			this.mDatas = datas;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}
		

		/**
		 * Set the Dialog message from resource
		 * @param message
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}


		/**
		 * Set the Dialog title from resource
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}



		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public UpdateDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final UpdateDialog dialog = new UpdateDialog(context, R.style.Dialog);
			View layout = inflater.inflate(R.layout.dialog_update, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			// set the dialog title
			((TextView) layout.findViewById(R.id.update_title)).setText(title);
			listView = (ListView) layout.findViewById(R.id.update_lv);
			listView.setAdapter(adapter);
			// set the confirm button
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.update_btn_ok))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {

						((Button) layout.findViewById(R.id.update_btn_ok))
								.setOnClickListener(new View.OnClickListener() {
									public void onClick(View v) {
											positiveButtonClickListener.onClick(dialog,
													DialogInterface.BUTTON_POSITIVE);
									}
								});


				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.update_btn_ok).setVisibility(
						View.GONE);
			}
			// set the cancel button
				((Button) layout.findViewById(R.id.update_btn_cancel))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.update_btn_cancel))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}else{
					((Button) layout.findViewById(R.id.update_btn_cancel))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
				}
			// set the content message
			if (message != null) {
				((TextView) layout.findViewById(R.id.update_message)).setText(message);
			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.update_content))
						.removeAllViews();
//				((LinearLayout) layout.findViewById(R.id.content))
//						.addView(contentView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}
		
		public class MyAdapter extends BaseAdapter {

			private ViewHolder holder;


			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mDatas.length;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView=LayoutInflater.from(context).inflate(R.layout.dialog_update_item, null);
					holder = new ViewHolder();
					holder.cityName = (TextView) convertView.findViewById(R.id.update_item_tv);
//					holder.rl = (RelativeLayout) convertView.findViewById(R.id.update_rlitem);
					convertView.setTag(holder);
				}else{
					holder = (ViewHolder) convertView.getTag();
				}

				holder.cityName.setText(mDatas[position]);

				return convertView;
			}
			
		}
		
		class ViewHolder {
			TextView cityName;
//			RelativeLayout rl;
		}
	}

}

