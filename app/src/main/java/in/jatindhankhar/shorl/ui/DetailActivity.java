package in.jatindhankhar.shorl.ui;


import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import net.glxn.qrgen.android.QRCode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.jatindhankhar.shorl.R;
import in.jatindhankhar.shorl.model.Analytics;
import in.jatindhankhar.shorl.ui.custom.TimeagoLayout;
import in.jatindhankhar.shorl.utils.Constants;
import in.jatindhankhar.shorl.utils.Utils;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.qrcode)
    ImageView qrcode;
    @BindView(R.id.long_url)
    TextView longUrl;
    @BindView(R.id.short_url)
    TextView shortUrl;
    @BindView(R.id.click_count)
    TextView clickCount;
    @BindView(R.id.timeagoLayout)
    TimeagoLayout timeagoLayout;
    @BindView(R.id.pie_chart)
    PieChart pieChart;
    @BindView(R.id.error_layout)
    View errorLayout;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if(toolbar != null)
        {getSupportActionBar().setDisplayHomeAsUpEnabled(true);}
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detail");
        gson = new Gson();
        //Toast.makeText(this, "Yo " + getIntent().getStringExtra("test"), Toast.LENGTH_SHORT).show();

        shortUrl.setText(Utils.getGooglShortUrl(getIntent().getStringExtra(Constants.ARG_SHORT_URL)));
        longUrl.setText(getIntent().getStringExtra(Constants.ARG_LONG_URL));
        timeagoLayout.setTargetDate(getIntent().getStringExtra(Constants.ARG_CREATED_DATE));
        Analytics analytics = gson.fromJson(getIntent().getStringExtra(Constants.ARG_ANALYTICS_DATA),Analytics.class);
        String shortClicks = analytics.getAllTime().getShortUrlClicks();
        String longClicks = analytics.getAllTime().getLongUrlClicks();
        clickCount.setText(shortClicks + " Clicks");
        if(shortClicks == null) shortClicks = "0";
        if (longClicks == null) longClicks = "0";
        qrcode.setImageBitmap(QRCode.from(shortUrl.getText().toString()).withSize(250,220).bitmap());

        //qrcode.setBackground(QRCode.from("www.example.org").withSize(250,250).bitmap());
        //pieChart =
        /*List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(Float.parseFloat(shortClicks),"Short  Url Clicks"));
        entries.add(new PieEntry(Float.parseFloat(shortClicks),"Long Url Clicks"));
        PieDataSet pieDataSet =  new PieDataSet(entries,"Click Stats");
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setHoleRadius(30); */

        List<PieEntry> entries = new ArrayList<>();

        Float short_actual = Float.parseFloat(shortClicks);

        Float long_actual = Float.parseFloat(longClicks);

        Float short_final_percent = (short_actual / (long_actual + short_actual)) * 100;
        Float long_final_percent = (long_actual / (long_actual + short_actual)) * 100;

        if (short_actual != Float.parseFloat("0"))
        entries.add(new PieEntry(short_final_percent,"Short Url Clicks"));
        if (long_actual != Float.parseFloat("0"))
        entries.add(new PieEntry(long_final_percent,"Long Url Clicks"));
        PieDataSet set = new PieDataSet(entries, "Url Click Count");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.invalidate(); // refresh

        if(pieChart.isEmpty())
        {
            pieChart.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);

        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
