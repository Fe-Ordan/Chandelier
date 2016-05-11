package com.prolificinteractive.swipeactionlayout.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.prolificinteractive.swipeactionlayout.widget.ActionItem;
import com.prolificinteractive.swipeactionlayout.widget.SwipeActionLayout;
import java.util.Arrays;

public class RecyclerViewActivity extends AppCompatActivity {

  private SwipeActionLayout swipeActionLayout;
  private RecyclerView list;
  private DummyAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_recycler_view);

    final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    swipeActionLayout = (SwipeActionLayout) findViewById(R.id.swipe_action_layout);
    swipeActionLayout.setOnActionSelectedListener(new SwipeActionLayout.OnActionListener() {
      @Override public void onActionSelected(int index, ActionItem action) {
        Toast.makeText(RecyclerViewActivity.this, String.format("%d", index), Toast.LENGTH_SHORT)
            .show();
      }
    });

    adapter = new DummyAdapter();
    list = (RecyclerView) findViewById(android.R.id.list);
    list.setLayoutManager(new LinearLayoutManager(this));
    list.setAdapter(adapter);

    swipeActionLayout.populateActionItems(Arrays.asList(
        new ActionItem(R.drawable.ic_close),
        new ActionItem(R.drawable.ic_add),
        new ActionItem(R.drawable.ic_check)
    ));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  class DummyAdapter extends RecyclerView.Adapter<DummyViewHolder>
      implements View.OnLongClickListener {

    @Override
    public DummyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      final View view = LayoutInflater
          .from(parent.getContext())
          .inflate(R.layout.item_dummy, list, false);
      view.setOnLongClickListener(this);
      return new DummyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DummyViewHolder holder, int position) {
      holder.title.setText(String.format("Title %d", position));
      holder.subtitle.setText(String.format("Subtitle %d", position));
    }

    @Override
    public int getItemCount() {
      return 10;
    }

    @Override public boolean onLongClick(View v) {
      swipeActionLayout.showActions();
      return true;
    }
  }

  class DummyViewHolder extends RecyclerView.ViewHolder {

    private final TextView title;
    private final TextView subtitle;

    public DummyViewHolder(View view) {
      super(view);
      title = (TextView) view.findViewById(android.R.id.text1);
      subtitle = (TextView) view.findViewById(android.R.id.text2);
    }
  }
}
