package kr.susemi99.gpxtracker;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import kr.susemi99.gpxtracker.constants.AppConstant;
import kr.susemi99.gpxtracker.utils.FileUtil;
import kr.susemi99.gpxtracker.widgets.RecyclerViewDivider;

public class FileListActivity extends AppCompatActivity {
  private static final String[] EXTENSIONS = {".gpx", ".kml"};
//  private static final String[] EXTENSIONS = {".gpx", ".tcx", ".kml", ".kmz"};

  private SwipeRefreshLayout refreshLayout;
  private Disposable permissionDisposable;
  private Adapter adapter = new Adapter();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.file_list_activity);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    refreshLayout = findViewById(R.id.refreshLayout);
    refreshLayout.setOnRefreshListener(this::requestPermission);

    RecyclerView listView = findViewById(R.id.listView);
    listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    listView.addItemDecoration(new RecyclerViewDivider());
    listView.setAdapter(adapter);

    requestPermission();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_file_list, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }
    else if (item.getItemId() == R.id.menu_refresh) {
      requestPermission();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    try {
      permissionDisposable.dispose();
    } catch (Exception ignore) { }
  }

  private void requestPermission() {
    permissionDisposable = new RxPermissions(this)
      .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
      .subscribe(granted -> reload());
  }

  private void reload() {
    refreshLayout.setRefreshing(true);
    adapter.addAll(FileUtil.getFileList(EXTENSIONS));
    refreshLayout.setRefreshing(false);
  }

  /***********************************
   * adapter
   ***********************************/
  private class Adapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<File> items = new ArrayList<>();

    void addAll(ArrayList<File> items) {
      this.items.clear();
      this.items.addAll(items);
      notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      return new ViewHolder(inflater.inflate(android.R.layout.simple_list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      File item = items.get(position);

      holder.title.setText(item.getName());

      holder.itemView.setOnClickListener(__ -> {
        setResult(RESULT_OK, new Intent().putExtra(AppConstant.SELECTED_FILE_PATH, item.getAbsolutePath()));
        finish();
      });
    }

    @Override
    public int getItemCount() {
      return items.size();
    }
  }

  /***********************************
   * view holder
   ***********************************/
  public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView title;

    public ViewHolder(View itemView) {
      super(itemView);
      title = itemView.findViewById(android.R.id.text1);
    }
  }
}
