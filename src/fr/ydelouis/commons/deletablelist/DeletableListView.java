package fr.ydelouis.commons.deletablelist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class DeletableListView extends ListView
{
	private int tagId = -1;
	private int draggedItemPosition = -1;
	private float draggedViewOffset = 0;
	private float lastMotionEventX;
	private OnItemDeleteListener onItemDeleteListener;
	
	public DeletableListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public DeletableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		if(tagId == -1)
			throw new RuntimeException("The tag id must be set before the adapter");
		super.setAdapter(new SwipeToDeleteAdapter(adapter));
	}
	
	@Override
	public ListAdapter getAdapter() {
		if(super.getAdapter() == null)
			return null;
		return ((SwipeToDeleteAdapter) super.getAdapter()).getAdapter();
	}
	
	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		if(draggedItemPosition != (Integer) child.getTag(tagId))
			return super.drawChild(canvas, child, drawingTime);
		
		float alpha = 1 - Math.abs(draggedViewOffset / getWidth());
		RectF rect = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
		canvas.saveLayerAlpha(rect, (int) (alpha*255), Canvas.ALL_SAVE_FLAG);
		canvas.translate(draggedViewOffset, 0);
		boolean hasInvalidated = super.drawChild(canvas, child, drawingTime);
		canvas.translate(-draggedViewOffset, 0);
		canvas.restore();
		
		return hasInvalidated;
	}
	
	private boolean isToBeDeleted() {
		return Math.abs(draggedViewOffset) > getWidth()*1/2;
	}
	
	private void startBackAnimation() {
		new CountDownTimer(300, 50) {
			public void onTick(long millisUntilFinished) {
				if(draggedViewOffset > 0) {
					draggedViewOffset -= getWidth()/(300/50);
					draggedViewOffset = Math.max(0, draggedViewOffset);
				}
				if(draggedViewOffset < 0) {
					draggedViewOffset += getWidth()/(300/50);
					draggedViewOffset = Math.min(0, draggedViewOffset);
				}
				notifyDataSetChanged();
			}
			
			@Override
			public void onFinish() {
				draggedViewOffset = 0;
				draggedItemPosition = -1;
				notifyDataSetChanged();
			}
		}.start();
	}
	
	private void startDeletionAnimation() {
		new CountDownTimer(300, 50) {
			public void onTick(long millisUntilFinished) {
				if(draggedViewOffset > 0) {
					draggedViewOffset += getWidth()/3/(300/50);
				}
				if(draggedViewOffset < 0) {
					draggedViewOffset -= getWidth()/3/(300/50);
				}
				notifyDataSetChanged();
			}
			
			@Override
			public void onFinish() {
				if(onItemDeleteListener != null) 
					onItemDeleteListener.onItemDeleted(DeletableListView.this, draggedItemPosition);
				draggedViewOffset = 0;
				draggedItemPosition = -1;
				notifyDataSetChanged();
			}
		}.start();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				draggedItemPosition = pointToPosition((int) event.getX(), (int) event.getY());
				draggedViewOffset = 0;
				lastMotionEventX = event.getX();
				notifyDataSetChanged();
				break;
			case MotionEvent.ACTION_MOVE:
				draggedViewOffset += event.getX() - lastMotionEventX;
				lastMotionEventX = event.getX();
				notifyDataSetChanged();
				break;
			case MotionEvent.ACTION_UP:
				draggedViewOffset += event.getX() - lastMotionEventX;
				lastMotionEventX = event.getX();
				if(isToBeDeleted())
					startDeletionAnimation();
				else
					startBackAnimation();
				return draggedViewOffset > 20;
		}
		
		return super.onTouchEvent(event);
	}
	
	public void notifyDataSetChanged() {
		((SwipeToDeleteAdapter) super.getAdapter()).notifyDataSetChanged();
	}
	
	public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {
		this.onItemDeleteListener = onItemDeleteListener;
	}
	
	public interface OnItemDeleteListener {
		public void onItemDeleted(DeletableListView listView, int position);
	}
	
	private class SwipeToDeleteAdapter extends BaseAdapter
	{
		private ListAdapter adapter;
		
		public SwipeToDeleteAdapter(ListAdapter adapter) {
			this.adapter = adapter;
		}
		
		public ListAdapter getAdapter() {
			return adapter;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			view = adapter.getView(position, view, parent);
			view.setTag(tagId, position);
			return view;
		}
		
		public int getCount() {
			return adapter.getCount();
		}
		public Object getItem(int position) {
			return adapter.getItem(position);
		}
		public long getItemId(int position) {
			return adapter.getItemId(position);
		}
	}
}
