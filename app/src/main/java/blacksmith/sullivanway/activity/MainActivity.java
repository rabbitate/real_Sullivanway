package blacksmith.sullivanway.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import blacksmith.sullivanway.R;
import blacksmith.sullivanway.database.FavoriteRoute;
import blacksmith.sullivanway.database.MyDBOpenHelper;
import blacksmith.sullivanway.database.Station;
import blacksmith.sullivanway.dialog.StationMenuDialog;
import blacksmith.sullivanway.routeguidance.Route;
import blacksmith.sullivanway.routeguidance.RouteFinder;
import blacksmith.sullivanway.routeguidance.RouteWrapper;
import blacksmith.sullivanway.routeguidance.StationMatrix;
import blacksmith.sullivanway.utils.SubwayLine;
import blacksmith.sullivanway.utils.SubwayMapTouchPoint;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.GONE;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    public static final Locale DEFAULT_LOCALE = Locale.KOREAN;
    private static final int FAV_ACTIVITY_CODE = 0;
    private static final int PATH_INFO_ACTIVITY_CODE = 1;
    private static final int TRANS_SETTING_ACTIVITY_CODE = 2;

    private StationMatrix stationMatrix;
    private ArrayList<Station> stnIdx;
    private SubwayMapTouchPoint subwayMapTouchPoint;
    private SearchListAdapter searchListAdapter;

    private boolean isExit; //Back??? ?????? ????????? ??????
    private boolean isFabOpen; //FloatingAction Open/Close ??????
    private Animation fab_open, fab_close, rotate_forward, rotate_backward; //FloatingAction ???????????????

    // View
    private FloatingActionButton fab, fab_favorite, fab_trans_map, fab_settings; //FloatingActionButton
    private PhotoView lineMapView;
    private SearchView searchView;
    private ListView searchList;
    private TextView startStnTextView, endStnTextView;
    private StationMenuDialog dialog;

    // Observables
    private Disposable loadingDbTaskObs = null;
    private Disposable findingRouteTaskObs = null;
    private Disposable testingSettingTaskObs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        setContentView(R.layout.activity_main);

        loadDatabaseAsync(); // ????????? ???????????? ????????????. (?????????)

        isExit = false;
        isFabOpen = false;

        // ????????? ??????
        lineMapView = findViewById(R.id.line_map_view);
        lineMapView.setImageResource(R.drawable.naver_subway);
        lineMapView.setMaximumScale(2.8f); //?????????????????? ??????
        lineMapView.setMediumScale(2.0f); //?????????????????? ?????? (2.0f???????????? ????????? ???????????? ????????? ?????? ???????????? 2.0f ????????????????????? ????????????
        lineMapView.setMinimumScale(1.0f); //?????????????????? ??????
        lineMapView.setScale(2.0f, true); //??? ????????? ??? ?????? ?????? ??????
        lineMapView.setOnViewTapListener(new OnLineMapViewTab());

        // ????????? ????????? ????????? ??????
        searchList = findViewById(R.id.search_list);
        searchListAdapter = new SearchListAdapter(this);
        searchList.setAdapter(searchListAdapter);
        searchList.setOnItemClickListener(new OnSearchListItemClick());
        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new OnSearchViewQueryText());

        // ?????? ????????? ?????????, ???????????? ???????????? ????????????
        startStnTextView = findViewById(R.id.startStnTextView);
        endStnTextView = findViewById(R.id.endStnTextView);
        startStnTextView.setOnClickListener(v -> {
            v.setVisibility(GONE);
            subwayMapTouchPoint.startStn = null;
        });
        endStnTextView.setOnClickListener(v -> {
            v.setVisibility(GONE);
            subwayMapTouchPoint.endStn = null;
        });

        // activity_main ??? ????????? ?????? ?????? ????????????
        fab = findViewById(R.id.fab);
        fab_favorite = findViewById(R.id.fab_favorite);
        fab_trans_map = findViewById(R.id.fab_trans_map);
        fab_settings = findViewById(R.id.fab_settings);
        fab.setOnClickListener(v -> {
            animateFAB(); // ?????? ????????? FloatingAction ??????????????? ??????
        });
        fab_favorite.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoriteRouteActivity.class);
            startActivityForResult(intent, FAV_ACTIVITY_CODE);
        });
        fab_trans_map.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TransferMapListActivity.class);
            startActivity(intent);
        });
        fab_settings.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, TRANS_SETTING_ACTIVITY_CODE);
        });

        // FloatingAction ???????????????
        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

    }

    @Override
    public void onBackPressed() {
        if (searchListAdapter.getCount() != 0) {
            searchView.setQuery("", false);
            searchListAdapter.clear();
            searchList.setAdapter(searchListAdapter); //???????????? ??????
            return;
        }

        if (isExit) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "??????????????? ?????? ??? ???????????????.", Toast.LENGTH_SHORT).show();
            isExit = true;
            new Thread(() -> {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isExit = false;
            }).start();
        }
    }

    @Override
    protected void onDestroy() {
        if (loadingDbTaskObs != null)
            loadingDbTaskObs.dispose();
        if (findingRouteTaskObs != null)
            findingRouteTaskObs.dispose();
        if (testingSettingTaskObs != null)
            testingSettingTaskObs.dispose();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FAV_ACTIVITY_CODE:
                case PATH_INFO_ACTIVITY_CODE:
                    // intent??? ?????? ?????????, ?????????, ???????????? ?????? SubwayMapTouchPoint.StnPoint ????????? ??????
                    subwayMapTouchPoint.startStn = subwayMapTouchPoint.getStation(stnIdx, data.getStringExtra("startLineNm"), data.getStringExtra("startStnNm"));
                    subwayMapTouchPoint.endStn = subwayMapTouchPoint.getStation(stnIdx, data.getStringExtra("endLineNm"), data.getStringExtra("endStnNm"));

                    // ?????? ?????? ????????? ??????
                    findRouteAsync();
                    break;

                case TRANS_SETTING_ACTIVITY_CODE:
                    // ??????????????? ?????? ????????? ????????? ??????
                    testSettingAsync();
                    break;
                default:
            }
        }
    }

    // ??? ?????? dialog
    private void displayStationTouchDialog(final Station stn) {
        // Dialog ??????, ?????? ????????? ??????
        dialog = new StationMenuDialog(MainActivity.this, subwayMapTouchPoint.getLineNms(stnIdx, stn), stn.getStnNm());
        dialog.setOnClickListener(v -> {
            switch (v.getId()) {
                /* ??? switch???????????? ????????? ?????? ??? ??????(stn)??? ?????????????????? ????????? ???????????? ??????
                 * lineMapView??? startStn(?????????), endStn(?????????), viaStn(?????????)??? '??????'?????????
                 * '??? ?????? ????????????'(StnInfoPagerActivity)??? ????????? ??? ??????
                 *
                 * switch??? ????????? ?????? '?????? ?????? ????????????'(RouteGuidancePagerActivity)???
                 * ?????????(SubwayMapTouchPoint.startStn), ?????????(SubwayMapTouchPoint.endStn)??? ??? ??? ????????? ???????????? ??????????????? ??????,
                 * ?????????, ????????? ??? ??? ???????????? ?????? ?????????(SubwayMapTouchPoint.viaStn)??? ???????????? ?????????-?????????-????????? ????????? ????????? **/
                case R.id.start: //?????????
                    subwayMapTouchPoint.startStn = stn; //SubwayMapTouchPoint map??? startStn??? ????????? ????????? ????????????
                    startStnTextView.setText(String.format("  %s: %s   ", getString(R.string.start_station), stn.getStnNm()));
                    startStnTextView.setVisibility(View.VISIBLE);
                    if (subwayMapTouchPoint.endStn == null) { //SubwayMapTouchPoint map??? endStn??? ?????????????????? ????????????
                        dialog.cancel();
                        return; //null?????? ?????????????????? ????????????,
                    }
                    break; //null??? ????????? switch??? ???????????? '?????? ?????? ????????????'(RouteGuidancePagerActivity)??? ????????????

                case R.id.end: //????????? (R.id.start??? ?????????)
                    subwayMapTouchPoint.endStn = stn;
                    endStnTextView.setText(String.format("  %s: %s   ", getString(R.string.end_station), stn.getStnNm()));
                    endStnTextView.setVisibility(View.VISIBLE);
                    if (subwayMapTouchPoint.startStn == null) {
                        dialog.cancel();
                        return;
                    }
                    break;

                case R.id.info: //??????
                    // ??? ?????? Activity ??????
                    Intent intent = new Intent(MainActivity.this, StnInfoPagerActivity.class);
                    intent.putExtra("lines", subwayMapTouchPoint.getLineNms(stnIdx, stn));
                    intent.putExtra("stnNm", stn.getStnNm());
                    MainActivity.this.startActivity(intent);
                    dialog.cancel();
                    return;

                default:
                    dialog.cancel();
                    return;
            }

            /* ?????????(startStn), ?????????(endStn) ?????? ??????????????????,
               ???????????? ????????? ?????? **/
            findRouteAsync();
            dialog.cancel();
        });
        dialog.show(); //dialog??? ????????????
    }

    // FloatingAction ??????????????? Open/Close
    private void animateFAB() {
        if (isFabOpen) {
            fab.startAnimation(rotate_backward);
            fab_favorite.startAnimation(fab_close);
            fab_trans_map.startAnimation(fab_close);
            fab_settings.startAnimation(fab_close);
            fab_favorite.setClickable(false);
            fab_trans_map.setClickable(false);
            fab_settings.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(rotate_forward);
            fab_favorite.startAnimation(fab_open);
            fab_trans_map.startAnimation(fab_open);
            fab_settings.startAnimation(fab_open);
            fab_favorite.setClickable(true);
            fab_trans_map.setClickable(true);
            fab_settings.setClickable(true);
            isFabOpen = true;
        }
    }


    // ????????? ???????????? ???????????? Task
    private void loadDatabaseAsync() {
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("???????????? ????????? ???????????? ???..");
        dialog.setCancelable(false); //??????????????? ?????? Task??? ???????????? ????????? ??????
        dialog.show();

        Completable loadingDbTask$ = Completable.create(emitter -> {
            // Preference??? ?????? DB ????????? ????????????. ??????: true, ?????????: false
            // state: Preference ???????????? ex. state.xml
            // isDbValid: state.xml??? ??????. isDbValid ?????? ?????????
            SharedPreferences state = getSharedPreferences("state", MODE_PRIVATE);
            boolean isDbValid = state.getBoolean("isDbValid", false);

            // ?????? DB ????????? ???????????????
            SharedPreferences.Editor editor = state.edit();
            editor.putBoolean("isDbValid", isDbValid);
            editor.apply();

            MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(MainActivity.this);
            SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
            myDBOpenHelper.setDatabase(db);
            if (!isDbValid) //????????? ???????????? ????????? DB??? ?????? ????????????
                myDBOpenHelper.initDatabase(db, true);

            subwayMapTouchPoint = new SubwayMapTouchPoint(MainActivity.this); //??? ?????? ?????? ?????????
            stationMatrix = new StationMatrix(myDBOpenHelper.getReadableDatabase());
            stnIdx = stationMatrix.getStnIdx();

            // DB ?????? ??? ?????? ???, DB ????????? true??? ????????????
            editor = state.edit();
            editor.putBoolean("isDbValid", true);
            editor.apply();

            emitter.onComplete();
        });

        loadingDbTaskObs = loadingDbTask$
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> {
                            loadingDbTaskObs = null;
                            dialog.dismiss();
                            // StationMatirx ?????? ??? ???????????? ????????????
                            testSettingAsync();
                        }
                );
    }

    // ?????? ???????????? Task
    private void findRouteAsync() {
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("?????? ?????????..");
        dialog.setCancelable(false); //??????????????? ?????? Task??? ???????????? ????????? ??????
        dialog.show();

        Completable findingRouteTask$ = Completable.create(emitter -> {
            /* SubwayMapTouchPoint map??? startStn, endStn???
             * ?????????????????????????????? ????????? ??? ????????? intent??? ????????????  **/
            if (Station.compare(subwayMapTouchPoint.startStn, subwayMapTouchPoint.endStn) == 2) { //???????????? ???????????? ?????? ???
                String startLineNm = subwayMapTouchPoint.startStn.getLineNm();
                String startStnNm = subwayMapTouchPoint.startStn.getStnNm();
                String endLineNm = subwayMapTouchPoint.endStn.getLineNm();
                String endStnNm = subwayMapTouchPoint.endStn.getStnNm();

                // ??????????????? ??????????????? ??? ??? ?????? ?????? ??????
                Calendar calendar = Calendar.getInstance(); //?????? ?????? ??????
                ArrayList<Integer> startIdxs = subwayMapTouchPoint.getIndexes(stnIdx, subwayMapTouchPoint.startStn);
                ArrayList<Integer> endIdxs = subwayMapTouchPoint.getIndexes(stnIdx, subwayMapTouchPoint.endStn);
                stationMatrix.setVirtualNodes(startIdxs, endIdxs);

                Route lessTransInfo = new RouteFinder(0, stationMatrix).getRoute(); //???????????? ??????
                Route minDistInfo = new RouteFinder(1, stationMatrix).getRoute(); //???????????? ??????
                ArrayList<RouteWrapper> lessTransItems = RouteWrapper.createArrayListInstance(MainActivity.this, stnIdx, calendar, lessTransInfo);
                ArrayList<RouteWrapper> minDistItems = RouteWrapper.createArrayListInstance(MainActivity.this, stnIdx, calendar, minDistInfo);

                // history ??????
                boolean favorite = FavoriteRoute.insert(false, startLineNm, endLineNm, startStnNm, endStnNm);
                //?????? ????????? ??????????????? ???????????? ????????? true, ????????? false??? ????????????

                // PathInfoActivity??? ?????? intent ?????? (??????, ??????, ????????????)
                Intent intent = new Intent(MainActivity.this, RouteGuidancePagerActivity.class);
                intent.putParcelableArrayListExtra("lessTransItems", lessTransItems);
                intent.putExtra("lessTransInfo", lessTransInfo);
                intent.putParcelableArrayListExtra("minDistItems", minDistItems);
                intent.putExtra("minDistInfo", minDistInfo);

                // Favorite ????????? ?????? ???????????????, ???????????????, ?????????????????? intent??? ?????????
                intent.putExtra("startLineNm", startLineNm);
                intent.putExtra("endLineNm", endLineNm);
                intent.putExtra("startStnNm", startStnNm);
                intent.putExtra("endStnNm", endStnNm);
                intent.putExtra("favorite", favorite);

                // RouteGuidancePagerActivity ??????
                MainActivity.this.startActivityForResult(intent, PATH_INFO_ACTIVITY_CODE);
            } else {
                Toast.makeText(MainActivity.this, "???????????? ???????????? ????????? ??????????????????", Toast.LENGTH_SHORT).show();
            }

            /* '?????? ?????? ????????????'??? ???????????? ?????? MainActivity??? ???????????? ???,
             * ?????????, ?????????, ???????????? ????????? ????????? ??? ????????? null??? ???????????????   */
            subwayMapTouchPoint.startStn = null;
            subwayMapTouchPoint.endStn = null;

            emitter.onComplete();
        });

        findingRouteTaskObs = findingRouteTask$
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> {
                            findingRouteTaskObs = null;
                            dialog.dismiss();
                            startStnTextView.setVisibility(GONE);
                            endStnTextView.setVisibility(GONE);
                        }
                );
    }

    // ??????????????? ?????? ???????????? Task
    private void testSettingAsync() {
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("?????? ?????? ???..");
        dialog.setCancelable(false); //??????????????? ?????? Task??? ???????????? ????????? ??????
        dialog.show();

        Single<Boolean> testingSettingTask$ = Single.create(emitter -> {
            SharedPreferences temp_transStnPref = getSharedPreferences(SettingsActivity.TEMP_SETTING, MODE_PRIVATE);
            Set<String> values = temp_transStnPref.getStringSet(SettingsActivity.TRANS_STN, null);
            boolean dijkstraError = false;
            int n = stationMatrix.getN();
            int[][] mMatrix = new int[n][n];
            int[][] rawMatrix = stationMatrix.getRawMatrix();
            for (int i = 0; i < n; i++)
                mMatrix[i] = rawMatrix[i].clone();
            if (values != null) {
                for (String value : values) {
                    StringTokenizer cutter = new StringTokenizer(value, "|", false);
                    String stnNm = cutter.nextToken();
                    String startLineNm = cutter.nextToken();
                    String endLineNm = cutter.nextToken();
                    int start = stationMatrix.getIndex(startLineNm, stnNm);
                    int end = stationMatrix.getIndex(endLineNm, stnNm);
                    mMatrix[start][end] = mMatrix[end][start] = StationMatrix.INF;
                }

                // ???????????????, ?????????????????? ?????? ????????? (????????? found[]?????? OutOfIndexException ??????)
                ArrayList<Integer> startIdxs = subwayMapTouchPoint.getIndexes(stnIdx, stnIdx.get(0)); //?????????
                ArrayList<Integer> endIdxs = subwayMapTouchPoint.getIndexes(stnIdx, stnIdx.get(1)); //?????????
                setVirtualNodes(mMatrix, n, startIdxs, endIdxs);

                // ??????????????? ?????????
                try {
                    testDijkstra(mMatrix, n - 2);

                    // stationMatrix??? matrix, transMatrix ??????
                    stationMatrix.initMatrix(mMatrix);

                    // ????????? SharedPreference ??????
                    SharedPreferences transStnPref = getSharedPreferences(SettingsActivity.SETTING, MODE_PRIVATE);
                    SharedPreferences.Editor editor = transStnPref.edit();
                    editor.putStringSet(SettingsActivity.TRANS_STN, values);
                    editor.apply();
                } catch (ArrayIndexOutOfBoundsException e) { //??????????????? ????????? ??????!!
                    dijkstraError = true;
                }
            }

            emitter.onSuccess(dijkstraError);
        });

        testingSettingTaskObs = testingSettingTask$
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(dijkstraError -> {
                    testingSettingTaskObs = null;
                    dialog.dismiss();

                    if (dijkstraError) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        AlertDialog alertDialog;
                        builder.setMessage(R.string.trans_stn_setting_err_msg);
                        builder.setPositiveButton(R.string.trans_stn_setting_dialog_confirm, (d, w) -> {
                            d.dismiss();
                            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                            startActivityForResult(intent, TRANS_SETTING_ACTIVITY_CODE);
                        });
                        alertDialog = builder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                });
    }

    private void setVirtualNodes(int[][] matrix, int n, ArrayList<Integer> startLineIdxs, ArrayList<Integer> endLineIdxs) {
        // ?????????
        int start = n - 2, end = n - 1;
        for (int i = 0; i < n; i++) {
            matrix[i][start] = StationMatrix.INF;
            matrix[start][i] = StationMatrix.INF;
        }
        matrix[start][start] = 0;
        for (int i = 0; i < n; i++) {
            matrix[i][end] = StationMatrix.INF;
            matrix[end][i] = StationMatrix.INF;
        }
        matrix[end][end] = 0;
        // ???????????????, ??????????????? ??????
        for (int i : startLineIdxs) {
            matrix[i][start] = 0;
            matrix[start][i] = 0;
        }
        for (int i : endLineIdxs) {
            matrix[i][end] = 0;
            matrix[end][i] = 0;
        }
    }

    private int choose(int[] distance, boolean[] found) {
        int min = StationMatrix.INF;
        int choose = -1;

        for (int i = 0; i < distance.length; i++) {
            if (!found[i] && distance[i] < min) {
                min = distance[i];
                choose = i;
            }
        }
        return choose;
    }

    private void testDijkstra(int[][] matrix, int start) {
        // ?????????
        int[] distance = matrix[start].clone();
        int n = distance.length;
        boolean[] found = new boolean[n];
        for (int i = 0; i < n; i++)
            found[i] = false;
        found[start] = true;

        int loop = n - 1;
        while (loop-- != 0) {
            int choose = choose(distance, found);
            found[choose] = true;
            for (int i = 0; i < n; i++)
                if (!found[i] && distance[choose] + matrix[choose][i] < distance[i])
                    distance[i] = distance[choose] + matrix[choose][i];
        }
    }


    // SearchListView??? Adapter
    private class SearchListAdapter extends BaseAdapter {
        private ViewGroup.LayoutParams params =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        private Context context;
        private int id = R.layout.item_station;
        private ArrayList<MyItem> myItems = new ArrayList<>();

        SearchListAdapter(Context context) {
            this.context = context;
        }

        void add(String lineNm, String stnNm, int pointx, int pointy) {
            for (MyItem item : myItems) {
                if (pointx == item.x && pointy == item.y) {
                    item.lienNms.add(lineNm);
                    return;
                }
            }
            myItems.add(new MyItem(lineNm, stnNm, pointx, pointy));
        }

        void clear() {
            myItems.clear();
        }

        @Override
        public int getCount() {
            return myItems.size();
        }

        @Override
        public MyItem getItem(int position) {
            return myItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(id, parent, false);

                holder = new Holder();
                holder.lineSymLayout = convertView.findViewById(R.id.lineSymLayout);
                holder.stnNm = convertView.findViewById(R.id.stnNm);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
                holder.lineSymLayout.removeAllViews();
            }

            MyItem stn = myItems.get(position);
            for (String lineNm : stn.lienNms) {
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(SubwayLine.getResId(lineNm));
                holder.lineSymLayout.addView(imageView, params);
            }
            holder.stnNm.setText(stn.stnNm);

            return convertView;
        }

        private class Holder {
            LinearLayout lineSymLayout;
            TextView stnNm;
        }

        private class MyItem {
            private ArrayList<String> lienNms = new ArrayList<>();
            private String stnNm;
            private int x, y;

            private MyItem(String lienNm, String stnNm, int x, int y) {
                lienNms.add(lienNm);
                this.stnNm = stnNm;
                this.x = x;
                this.y = y;
            }

            public String getStnNm() {
                return stnNm;
            }

        }

    }


    /* Listener */
    private class OnLineMapViewTab implements OnViewTapListener {

        @Override
        public void onViewTap(View view, float x, float y) {
            // ????????? ??????(x,y)??? ???????????? 'stn ??????'??? ????????? ?????? ??????(StnPoint: ????????????, ?????????, ?????????)??? ????????????
            Station stn = subwayMapTouchPoint.getStation(
                    stnIdx,
                    lineMapView.getDisplayRect().left, lineMapView.getDisplayRect().top,
                    lineMapView.getScale(), x, y);

            if (stn != null) //????????? ????????? ?????? ?????? ?????? Dialog??? ?????????
                displayStationTouchDialog(stn);
        }
    }

    private class OnSearchViewQueryText implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            if (searchListAdapter.getCount() == 0)
                return false;

            AdapterView.OnItemClickListener listener = searchList.getOnItemClickListener();
            if (listener != null)
                listener.onItemClick(null, null, 0, 0);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String mText) {
            searchListAdapter.clear();
            if (mText.equals(""))
                return false;
            for (Station stn : stnIdx)
                if (stn.getStnNm().startsWith(mText))
                    searchListAdapter.add(stn.getLineNm(), stn.getStnNm(), stn.getPointx(), stn.getPointy());
            searchList.setAdapter(searchListAdapter); //???????????? ??????
            return true;
        }

    }

    private class OnSearchListItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String stnNm = searchListAdapter.getItem(position).getStnNm();
            Station mStn = null;
            for (Station stn : stnIdx)
                if (stnNm.equals(stn.getStnNm()))
                    mStn = stn;

            if (mStn != null) {
                searchView.setQuery("", false);
                searchListAdapter.clear();
                searchList.setAdapter(searchListAdapter); //???????????? ??????
                displayStationTouchDialog(mStn);
            }
        }

    }

}
