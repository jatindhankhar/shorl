package in.jatindhankhar.shorl.ui;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;


import net.glxn.qrgen.android.QRCode;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.jatindhankhar.shorl.R;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.qrcode)
    ImageView qrcode;
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
        Toast.makeText(this, "Yo " + getIntent().getStringExtra("test"), Toast.LENGTH_SHORT).show();
        qrcode.setImageBitmap(QRCode.from("www.example.org").withSize(300,300).bitmap());


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
