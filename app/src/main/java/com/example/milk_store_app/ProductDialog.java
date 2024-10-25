package com.example.milk_store_app;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.milk_store_app.models.request.ProductUpdateRequest;
import com.example.milk_store_app.models.response.ProductResponse;
import com.example.milk_store_app.repository.ProductRepository;
import com.example.milk_store_app.services.ProductServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;

import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDialog extends DialogFragment {

    private static final String ARG_PRODUCT = "product";
    private ProductResponse product;
    private ProductServices productServices;
    private ImageView imgProduct;
    private EditText etProductName, etProductPrice, etProductStock, etProductDescription;
    private Button btnUpdateProduct;
    private Uri selectedImageUri;
    @Setter
    private ProductUpdateListener productUpdateListener;

    public ProductDialog() {
        // Required empty public constructor
    }

    @NonNull
    public static ProductDialog newInstance(ProductResponse product) {
        ProductDialog fragment = new ProductDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
            getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (ProductResponse) getArguments().getSerializable(ARG_PRODUCT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_dialog, container, false);

        // Initialize views
        imgProduct = view.findViewById(R.id.imgProduct);
        etProductName = view.findViewById(R.id.etProductName);
        etProductPrice = view.findViewById(R.id.etProductPrice);
        etProductStock = view.findViewById(R.id.etProductStock);
        etProductDescription = view.findViewById(R.id.etProductDescription);
        btnUpdateProduct = view.findViewById(R.id.btnUpdateProduct);

        productServices = ProductRepository.getProductServices(requireContext());

        // Populate views with product data
        if (product != null) {
            setProductDetails();
        }

        // Handle the image picker button click event
        imgProduct.setOnClickListener(v -> openImagePicker());

        // Handle the update button click event
        btnUpdateProduct.setOnClickListener(v -> {
            // Retrieve updated values from the input fields
            String updatedName = etProductName.getText().toString();
            BigDecimal updatedPrice = new BigDecimal(etProductPrice.getText().toString());
            int updatedStock = Integer.parseInt(etProductStock.getText().toString());
            String updatedDescription = etProductDescription.getText().toString();

            // Update the product object
            product.setName(updatedName);
            product.setPrice(updatedPrice);
            product.setStock(updatedStock);
            product.setDescription(updatedDescription);

            // If an image is selected, upload it first and then update the product
            if (selectedImageUri != null) {
                uploadImage(selectedImageUri, updatedName, updatedPrice, updatedStock, updatedDescription);
            } else {
                updateProduct(null, updatedName, updatedPrice, updatedStock, updatedDescription);
            }
        });

        return view;
    }

    private void setProductDetails() {
        // Load product image with Glide (if available)
        Glide.with(this)
                .load(product.getImageUrl())
                .into(imgProduct);

        etProductName.setText(product.getName());
        etProductPrice.setText(String.valueOf(product.getPrice()));
        etProductStock.setText(String.valueOf(product.getStock()));
        etProductDescription.setText(product.getDescription());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null && imgProduct != null) {
                        imgProduct.setImageURI(selectedImageUri);
                    }
                }
            }
    );

    private void uploadImage(Uri uri, String updatedName, BigDecimal updatedPrice, int updatedStock, String updatedDescription) {
        try {
            File imageFile = copyUriToLocalFile(uri);
            RequestBody requestFile = RequestBody.create(imageFile, MediaType.parse("image/*"));
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

            productServices.uploadImage(body).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Image uploaded successfully, now update the product with the image URL
                        String imageUrl = response.body();
                        updateProduct(imageUrl, updatedName, updatedPrice, updatedStock, updatedDescription);
                    } else {
                        showErrorDialog("Failed to upload image");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    showErrorDialog("Failed to upload image: " + t.getMessage());
                }
            });
        } catch (IOException e) {
            showErrorDialog("Failed to copy image: " + e.getMessage());
        }
    }

    private void updateProduct(String imageUrl, String updatedName, BigDecimal updatedPrice, int updatedStock, String updatedDescription) {
        ProductUpdateRequest productUpdateRequest = ProductUpdateRequest.builder()
                .name(updatedName)
                .price(updatedPrice)
                .stock(updatedStock)
                .description(updatedDescription)
                .imageUrl(imageUrl != null ? imageUrl : product.getImageUrl())
                .build();

        productServices.updateProduct(product.getId(), productUpdateRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                if (response.isSuccessful()) {
                    builder.setMessage("Product updated successfully");
                    if (productUpdateListener != null) {
                        productUpdateListener.onProductUpdated();
                    }
                    dismiss();
                } else {
                    builder.setMessage("Failed to update product");
                }
                builder.setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    dismiss();
                }).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showErrorDialog("Failed to update product: " + t.getMessage());
            }
        });
    }

    private File copyUriToLocalFile(Uri uri) throws IOException {
        File localFile = new File(requireContext().getCacheDir(), "temp_image.jpg");

        try (InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
             OutputStream outputStream = Files.newOutputStream(localFile.toPath())) {

            byte[] buffer = new byte[1024];
            int length;
            while (true) {
                assert inputStream != null;
                if (!((length = inputStream.read(buffer)) > 0)) break;
                outputStream.write(buffer, 0, length);
            }
        }

        return localFile;
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public interface ProductUpdateListener {
        void onProductUpdated();
    }

}
