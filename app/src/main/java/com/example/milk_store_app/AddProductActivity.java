package com.example.milk_store_app;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.milk_store_app.models.request.ProductCreateRequest;
import com.example.milk_store_app.models.response.BrandResponse;
import com.example.milk_store_app.models.response.ProductResponse;
import com.example.milk_store_app.repository.BrandRepository;
import com.example.milk_store_app.repository.ProductRepository;
import com.example.milk_store_app.services.BrandServices;
import com.example.milk_store_app.services.ProductServices;
import com.example.milk_store_app.session.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.NonNull;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    EditText txtProductName, txtProductDescription, txtProductPrice, txtProductStock;
    Spinner spinner;
    Button btnSelectImg, btnAdd, btnGoBack;
    ImageView imgProduct;

    ProductServices productServices;
    BrandServices brandServices;
    SessionManager sessionManager;
    ArrayAdapter<BrandResponse> brandAdapter;
    BrandResponse selectedBrand;
    Uri selectedImageUri;
    ActivityResultLauncher<Intent> resultLauncher;
    String imageUrl; //This is the image url that we will receive after upload picture
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ProjectData();
        GetAllBrands();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                BrandResponse brand = (BrandResponse) adapterView.getItemAtPosition(position);
                selectedBrand = brand;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        RegisterResult();

        btnSelectImg.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            resultLauncher.launch(intent);
        });

        btnAdd.setOnClickListener(v -> {
            String name = txtProductName.getText().toString();
            String description = txtProductDescription.getText().toString();
            String price = txtProductPrice.getText().toString();
            String stock = txtProductStock.getText().toString();
            String brandId = selectedBrand.getId();
            if (name.isEmpty() || description.isEmpty()
                    || price.isEmpty() || stock.isEmpty() || brandId.isEmpty()) {
                Toast.makeText(this, "Please fill in all the fields!", Toast.LENGTH_SHORT).show();
                return;
            }
            MultipartBody.Part imagePart = null;
            if (selectedImageUri == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                imagePart = PrepareFilePart(selectedImageUri);
                //Upload image
                imageUrl = productServices.uploadImage(imagePart).execute().body();
                if (!imageUrl.isEmpty()){
                    productServices.createProduct(new ProductCreateRequest(
                                    name,
                                    description,
                                    BigDecimal.valueOf(Long.parseLong(price)),
                                    Integer.parseInt(stock),
                                    imageUrl,
                                    UUID.fromString(brandId)
                            )
                    ).enqueue(
                            new Callback<ProductResponse>() {
                                @Override
                                public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(AddProductActivity.this, "Add product successfully!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(AddProductActivity.this, "Add product failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<ProductResponse> call, Throwable t) {
                                }
                            }
                    );
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    // Helper method to read InputStream into a byte array
    private byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private MultipartBody.Part PrepareFilePart(Uri fileUri) throws IOException {
        ContentResolver contentResolver = getContentResolver();
        String mimeType = contentResolver.getType(fileUri); // Get MIME type of the file

        // Open InputStream from Uri
        InputStream inputStream = contentResolver.openInputStream(fileUri);
        byte[] fileBytes = getBytesFromInputStream(inputStream);

        // Create RequestBody using byte array and MIME type
        RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), fileBytes);

        // Get file name from Uri
        String fileName = txtProductName.getText().toString();

        // Create MultipartBody.Part
        return MultipartBody.Part.createFormData("image", fileName, requestBody);
    }


    private void RegisterResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == RESULT_OK) {
                            Intent intent = o.getData();
                            selectedImageUri = intent.getData();
                            Glide.with(AddProductActivity.this).load(selectedImageUri).into(imgProduct);
                        }
                    }
                }
        );
    }

    private void GetAllBrands() {
        brandServices.getBrands("")
                .enqueue(new Callback<List<BrandResponse>>() {
                    @Override
                    public void onResponse(Call<List<BrandResponse>> call, Response<List<BrandResponse>> response) {
                        brandAdapter.addAll(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<BrandResponse>> call, Throwable t) {
                        Toast.makeText(AddProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ProjectData() {
        txtProductName = (EditText) findViewById(R.id.txt_product_name);
        txtProductDescription = (EditText) findViewById(R.id.txt_product_description);
        txtProductPrice = (EditText) findViewById(R.id.txt_product_price);
        txtProductStock = (EditText) findViewById(R.id.txt_product_stock);
        spinner = (Spinner) findViewById(R.id.select_brand);
        btnSelectImg = (Button) findViewById(R.id.btn_select_img);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnGoBack = (Button) findViewById(R.id.btn_go_back);
        imgProduct = (ImageView) findViewById(R.id.img_product);

        productServices = ProductRepository.getProductServices(this);
        brandServices = BrandRepository.getBrandServices(this);
        sessionManager = new SessionManager(this);
        //Init spinner adapter
        brandAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        //Set layout for the adapter and assign to spinner
        brandAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(brandAdapter);
        selectedBrand = new BrandResponse();

        btnGoBack.setOnClickListener(v -> {
            finish();
        });
    }
}