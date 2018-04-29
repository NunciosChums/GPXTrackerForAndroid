package kr.susemi99.gpxtracker.widgets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewDivider extends RecyclerView.ItemDecoration {
  private int color;
  private int height = 1; // in px format
  private Paint paint;

  public RecyclerViewDivider() {
    this.color = Color.parseColor("#FFCCCCCC");
//    this.height = height;
    this.paint = new Paint();
    paint.setAntiAlias(true);
  }

  @Override
  public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
    super.onDrawOver(c, parent, state);

    int left = parent.getPaddingLeft();
    int right = parent.getWidth() - parent.getPaddingRight();
    int childCount = parent.getChildCount();

    for (int i = 0; i < childCount; i++) {
      View child = parent.getChildAt(i);
      RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
      int top = child.getBottom() + params.bottomMargin;
      int bottom = top + height;
      paint.setColor(color);
      c.drawRect(left, top, right, bottom, paint);
    }
  }
}
