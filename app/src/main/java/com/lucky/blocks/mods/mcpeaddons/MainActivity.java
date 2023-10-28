package com.lucky.blocks.mods.mcpeaddons;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.bumptech.glide.load.Key;
import com.developer.kalert.KAlertDialog;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.android.material.navigation.NavigationView;
import com.marcoscg.ratedialog.RateDialog;
import com.smarteist.autoimageslider.SliderView;
import es.dmoral.toasty.Toasty;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements MapsAdapter.OnCardClickListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String[] PERMISSIONS = {"android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final String TAG = "HttpDownloadManager";
    public int alreadyExecuted = 0;
    public int alreadyShown = 0;
    public int alreadyShownS = 0;
    @BindView(R.id.buttonGroup)
    SegmentedButtonGroup buttonGroup;
    RatingBar indicatorRatingBar;
    String lang;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    NavigationView mNavigationView;
    RateDialog rateDialog;
    SearchView searchView;
    SliderView sliderView;
    private MapsAdapter fullAdapter;
    private List<Map> fullList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Map map;
    private List<Map> mapList;
    private MapsAdapter mapsAdapter;
    private Menu menuList;
    private List<Map> modList;
    private MapsAdapter modsAdapter;
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private SliderItem slide;
    private List<SliderItem> sliderItemList;
    CardView updatemodal;
    RelativeLayout gotoupdate;

    public InterstitialAd mInterstitialAd;

    public void onPopupButtonClick(View view) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.lang = Locale.getDefault().getLanguage();
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        setModLists();
        this.sliderItemList = new ArrayList();
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        this.recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(0), true));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setButtonGroup();
        setDrawer();
        setMaps();
        initRateDialog();
        this.mInterstitialAd = ApplicationManager.getInstance().getInterAd();
        ApplicationManager.getInstance().LoadIntersttialAd();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.mapList.clear();
        this.fullList.clear();
        this.modList.clear();
        setMaps();
        this.mNavigationView.setCheckedItem(R.id.nav_full);
        this.buttonGroup.setPosition(2, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        menuItem.getItemId();
        if (menuItem.getItemId() == R.id.menu_sort) {
            if (this.buttonGroup.getPosition() == 2) {
                sortModsByDate(this.fullList, this.fullAdapter);
            } else if (this.buttonGroup.getPosition() == 1) {
                sortModsByDate(this.mapList, this.mapsAdapter);
            } else if (this.buttonGroup.getPosition() == 0) {
                sortModsByDate(this.modList, this.modsAdapter);
            }
            return true;
        } else if (this.mToggle.onOptionsItemSelected(menuItem)) {
            return true;
        } else {
            return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onCardClick(View view, int i) {
        if (this.recyclerView.getAdapter().equals(this.mapsAdapter)) {
            this.map = this.mapList.get(i);
        } else if (this.recyclerView.getAdapter().equals(this.modsAdapter)) {
            this.map = this.modList.get(i);
        } else if (this.recyclerView.getAdapter().equals(this.fullAdapter)) {
            this.map = this.fullList.get(i);
        }
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("item_id", this.map.getId());
        startActivity(intent);
        finish();
    }

    private int dpToPx(int i) {
        return Math.round(TypedValue.applyDimension(1, i, getResources().getDisplayMetrics()));
    }

    public void setMaps() {
        String str;
        String str2;
        String string;
        String string2;
        String string3;
        String loadJSONFromAsset = loadJSONFromAsset();
        try {
            JSONArray jSONArray = new JSONObject(loadJSONFromAsset).getJSONArray("items");
            int i = 0;
            while (true) {
                str = "id";
                str2 = loadJSONFromAsset;
                if (i >= jSONArray.length()) {
                    break;
                }
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                int i2 = jSONObject.getInt("id");
                if (this.lang != "ru") {
                    string3 = jSONObject.getString("name_en");
                } else {
                    string3 = jSONObject.getString(AppMeasurementSdk.ConditionalUserProperty.NAME);
                }
                this.fullList.add(new Map(i2, string3, jSONObject.getString("description"), jSONObject.getString("archive"), jSONObject.getString("image"), null, jSONObject.getString("views"), Float.parseFloat(jSONObject.getString("rating")), jSONObject.getString("version"), jSONObject.getString("downloads"), jSONObject.getString("rating"), jSONObject.getString("type"), jSONObject.getInt("sort"), jSONObject.getInt("unsort")));
                i++;
                loadJSONFromAsset = str2;
            }
            JSONArray jSONArray2 = new JSONObject(str2).getJSONArray("guns");
            int i3 = 0;
            while (i3 < jSONArray2.length()) {
                JSONObject jSONObject2 = jSONArray2.getJSONObject(i3);
                int i4 = jSONObject2.getInt("id");
                JSONArray jSONArray3 = jSONArray2;
                if (this.lang != "ru") {
                    string2 = jSONObject2.getString("name_en");
                } else {
                    string2 = jSONObject2.getString(AppMeasurementSdk.ConditionalUserProperty.NAME);
                }
                this.mapList.add(new Map(i4, string2, jSONObject2.getString("description"), jSONObject2.getString("archive"), jSONObject2.getString("image"), null, jSONObject2.getString("views"), Float.parseFloat(jSONObject2.getString("rating")), jSONObject2.getString("version"), jSONObject2.getString("downloads"), jSONObject2.getString("rating"), jSONObject2.getString("type"), 0, 0));
                i3++;
                jSONArray2 = jSONArray3;
            }
            JSONArray jSONArray4 = new JSONObject(str2).getJSONArray("weapons");
            int i5 = 0;
            while (i5 < jSONArray4.length()) {
                JSONObject jSONObject3 = jSONArray4.getJSONObject(i5);
                int i6 = jSONObject3.getInt(str);
                String str3 = str;
                if (this.lang != "ru") {
                    string = jSONObject3.getString("name_en");
                } else {
                    string = jSONObject3.getString(AppMeasurementSdk.ConditionalUserProperty.NAME);
                }
                this.modList.add(new Map(i6, string, jSONObject3.getString("description"), jSONObject3.getString("archive"), jSONObject3.getString("image"), null, jSONObject3.getString("views"), Float.parseFloat(jSONObject3.getString("rating")), jSONObject3.getString("version"), jSONObject3.getString("downloads"), jSONObject3.getString("rating"), jSONObject3.getString("type"), 0, 0));
                i5++;
                str = str3;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.recyclerView.setAdapter(this.fullAdapter);
        this.buttonGroup.setVisibility(0);
    }

    @Override
    public void onStop() {
        super.onStop();
        ApplicationManager.getInstance().changeAd(0);
    }


    public void setDrawer() {
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        this.mNavigationView = navigationView;
        navigationView.setNavigationItemSelectedListener(this);
        this.mToggle = new ActionBarDrawerToggle(this, this.mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        Menu menu = this.mNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SubMenu subMenu = item.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int i2 = 0; i2 < subMenu.size(); i2++) {
                    applyFontToMenuItem(subMenu.getItem(i2));
                }
            }
            applyFontToMenuItem(item);
        }
        this.mNavigationView.setCheckedItem(R.id.nav_full);
        this.mDrawerLayout.addDrawerListener(this.mToggle);
        this.mToggle.syncState();
        this.mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public final boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_share) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.SEND");
                    intent.putExtra("android.intent.extra.TEXT", ((Object) getText(R.string.share_text)) + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                    intent.setType("text/plain");
                    startActivity(Intent.createChooser(intent, getResources().getText(R.string.share)));
                } else if (itemId == R.id.nav_rate) {
                    rateDialog.showDialog();
                } else if (itemId == R.id.nav_write) {
                    Intent intent2 = new Intent("android.intent.action.SEND");
                    intent2.setType("message/rfc822");
                    intent2.putExtra("android.intent.extra.EMAIL", new String[]{"chernov.vadim1283@gmail.com"});
                    intent2.putExtra("android.intent.extra.SUBJECT", getString(R.string.problem));
                    try {
                        startActivity(Intent.createChooser(intent2, getString(R.string.sendmail)));
                    } catch (ActivityNotFoundException unused) {
                        Toast.makeText(MainActivity.this, getText(R.string.nomail), 0).show();
                    }
                }  else if (itemId == R.id.nav_mods) {
                    buttonGroup.setPosition(0, true);
                } else if (itemId == R.id.nav_maps) {
                    buttonGroup.setPosition(1, true);
                } else if (itemId == R.id.nav_full) {
                    buttonGroup.setPosition(2, true);
                } else if (itemId == R.id.nav_sub) {
                    startActivity(new Intent(MainActivity.this, SubscriptionActivity.class));
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    private void applyFontToMenuItem(MenuItem menuItem) {
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "fonts/exom.otf");
        SpannableString spannableString = new SpannableString(menuItem.getTitle());
        spannableString.setSpan(new CustomTypefaceSpan("", createFromAsset), 0, spannableString.length(), 18);
        menuItem.setTitle(spannableString);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initRateDialog() {
        this.rateDialog = new RateDialog(this, 2, 0);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.setFlags(268435456);
        startActivity(intent);
    }


    public void setButtonGroup() {
        SegmentedButtonGroup segmentedButtonGroup = (SegmentedButtonGroup) findViewById(R.id.buttonGroup);
        this.buttonGroup = segmentedButtonGroup;
        segmentedButtonGroup.setVisibility(4);
        this.buttonGroup.setPosition(2, false);
        this.linearLayout.removeAllViews();
        this.linearLayout.addView(this.buttonGroup);
        this.buttonGroup.setOnPositionChangedListener(new SegmentedButtonGroup.OnPositionChangedListener() {
            @Override
            public final void onPositionChanged(int i) {
                if (i == 0) {
                    filterModType(modsAdapter);
                    mNavigationView.setCheckedItem(R.id.nav_mods);
                    if (alreadyShownS == 0) {
                        showCookieBar(getString(R.string.mod_selector_title_weapons), getString(R.string.mod_selector_msg_weapons));
                        alreadyShownS = 1;
                    }
                } else if (i != 1) {
                    if (i != 2) {
                        return;
                    }
                    filterModType(fullAdapter);
                    mNavigationView.setCheckedItem(R.id.nav_full);
                } else {
                    filterModType(mapsAdapter);
                    mNavigationView.setCheckedItem(R.id.nav_maps);
                    if (alreadyShown == 0) {
                        showCookieBar(getString(R.string.mod_selector_title_guns), getString(R.string.mod_selector_msg_guns));
                        alreadyShown = 1;
                    }
                }
            }
        });
    }


    public void filterModType(MapsAdapter mapsAdapter) {
        this.recyclerView.setAdapter(mapsAdapter);
    }

    public void setModLists() {
        this.mapList = new ArrayList();
        MapsAdapter mapsAdapter = new MapsAdapter(this, this.mapList);
        this.mapsAdapter = mapsAdapter;
        mapsAdapter.setOnCardClickListener(this);
        this.modList = new ArrayList();
        MapsAdapter mapsAdapter2 = new MapsAdapter(this, this.modList);
        this.modsAdapter = mapsAdapter2;
        mapsAdapter2.setOnCardClickListener(this);
        this.fullList = new ArrayList();
        MapsAdapter mapsAdapter3 = new MapsAdapter(this, this.fullList);
        this.fullAdapter = mapsAdapter3;
        mapsAdapter3.setOnCardClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menuList = menu;
        getMenuInflater().inflate(R.menu.my_menu, menu);
        setDescending();
        return true;
    }

    private void hideMenu() {
        MenuItem findItem = this.menuList.findItem(R.id.menu_sort);
        findItem.getIcon().setColorFilter(getResources().getColor(R.color.colorPrimarySplash), PorterDuff.Mode.SRC_IN);
        findItem.setEnabled(false);
    }

    private void showMenu() {
        MenuItem findItem = this.menuList.findItem(R.id.menu_sort);
        findItem.getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        findItem.setVisible(false);
        findItem.setEnabled(false);
    }

    private void setAscending() {
        MenuItem findItem = this.menuList.findItem(R.id.menu_sort);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_sort_ascending_svgrepo_com);
        drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        findItem.setIcon(drawable);
        findItem.setVisible(false);
        findItem.setEnabled(false);
    }

    private void setDescending() {
        MenuItem findItem = this.menuList.findItem(R.id.menu_sort);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_sort_descending_svgrepo_com);
        drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        findItem.setIcon(drawable);
        findItem.setVisible(false);
        findItem.setEnabled(false);
    }

    public void sortModsByDate(List<Map> list, MapsAdapter mapsAdapter) {
        if (list.get(0).getId() == 311) {
            Collections.sort(list, new Comparator() {
                @Override
                public final int compare(Object obj, Object obj2) {
                    int compare;
                    compare = Integer.compare(((Map) obj2).getSort(), ((Map) obj).getSort());
                    return compare;
                }
            });
            setAscending();
        } else if (list.get(0).getId() != 311) {
            Collections.sort(list, new Comparator() {
                @Override
                public final int compare(Object obj, Object obj2) {
                    int compare;
                    compare = Integer.compare(((Map) obj2).getUnSort(), ((Map) obj).getUnSort());
                    return compare;
                }
            });
            setDescending();
        }
        mapsAdapter.notifyDataSetChanged();
        showToasty();
    }

    public void showToasty() {
        Toasty.Config.getInstance().setToastTypeface(Typeface.createFromAsset(getAssets(), "fonts/exom.otf")).allowQueue(false).setTextSize(20).apply();
        Toasty.custom((Context) this, (int) R.string.sorted_by_date, getResources().getDrawable(R.drawable.ic_information_svgrepo_com), (int) R.color.toggle_bty, (int) R.color.white, 0, true, true).show();
    }

    public void showCookieBar(String str, String str2) {
        CookieBar.build(this).setDuration(3000L).setIcon(R.drawable.ic_modsa).setTitle(str).setMessage(str2).setBackgroundColor(R.color.toggle_bty).setTitleColor(R.color.white).setCookiePosition(80).show();
    }

    public String loadJSONFromAsset() {
        try {
            InputStream open = getAssets().open("maps.json");
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr, Key.STRING_CHARSET_NAME);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}