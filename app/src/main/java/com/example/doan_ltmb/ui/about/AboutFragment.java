package com.example.doan_ltmb.ui.about;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.doan_ltmb.databinding.FragmentAboutBinding;
import com.example.doan_ltmb.ui.auth.LoginActivity;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupUserInfo();
        setupOrderActions();
        setupMenuActions();

        binding.btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
    }

    private void setupUserInfo() {
        // Load profile picture with Glide
        Glide.with(this)
                .load("https://raw.githubusercontent.com/tienthinh-workspace/image-storage/main/snapback_logo.jpg")
                .circleCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(binding.ivUserAvatar);

        binding.tvUserName.setText("Snapback Store Elite");
        binding.tvUserEmail.setText("premium.headwear@snapback.vn");
    }

    private void setupOrderActions() {
        View.OnClickListener orderListener = v -> {
            String message = "";
            int id = v.getId();
            if (id == binding.tvOrderHistory.getId()) message = "Xem tất cả đơn hàng";
            else if (id == binding.btnPendingPayment.getId()) message = "Đơn chờ thanh toán";
            else if (id == binding.btnPendingShip.getId()) message = "Đơn chờ giao hàng";
            else if (id == binding.btnShipping.getId()) message = "Đơn đang giao";
            else if (id == binding.btnRating.getId()) message = "Đơn chờ đánh giá";

            if (!message.isEmpty()) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        };

        binding.tvOrderHistory.setOnClickListener(orderListener);
        binding.btnPendingPayment.setOnClickListener(orderListener);
        binding.btnPendingShip.setOnClickListener(orderListener);
        binding.btnShipping.setOnClickListener(orderListener);
        binding.btnRating.setOnClickListener(orderListener);
    }

    private void setupMenuActions() {
        binding.tvVoucher.setOnClickListener(v -> 
            Toast.makeText(getContext(), "Ví Voucher của bạn", Toast.LENGTH_SHORT).show());
        
        binding.tvSettings.setOnClickListener(v -> 
            Toast.makeText(getContext(), "Thiết lập tài khoản", Toast.LENGTH_SHORT).show());
        
        binding.tvHelpCenter.setOnClickListener(v -> 
            Toast.makeText(getContext(), "Trung tâm hỗ trợ", Toast.LENGTH_SHORT).show());

        binding.tvPrivacy.setOnClickListener(v ->
            Toast.makeText(getContext(), "Chính sách & Bảo mật", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
