package com.example.photoediter.ui.main.addtext;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.photoediter.R;
import com.example.photoediter.common.Constant;
import com.example.photoediter.common.models.MessageEvent;
import com.example.photoediter.databinding.FragmentAddTextBinding;
import com.example.photoediter.ui.base.BaseBindingFragment;
import com.example.photoediter.ui.main.MainViewModel;
import com.example.photoediter.utils.DensityUtils;
import com.xiaopo.flying.sticker.TextStickerState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class AddTextFragment extends BaseBindingFragment<FragmentAddTextBinding, AddTextViewModel> {
    private OptionTextFragment optionTextFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    private Dialog dialog;

    public AddTextFragment() {
        // Required empty public constructor
    }

    public static AddTextFragment newInstance() {
        AddTextFragment fragment = new AddTextFragment();
        return fragment;
    }

    @Override
    protected Class<AddTextViewModel> getViewModel() {
        return AddTextViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_text;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        initFunction();
        observeView();
        addFragment();
        confirmEditAddText();
        undo();
        redo();
    }

    @Override
    protected void onPermissionGranted() {

    }

    private void undo() {
        binding.layoutEditText.ivUndo.setOnClickListener(v -> {
            mainViewModel.setUndoTextSticker(true);
        });
    }

    private void redo() {
        binding.layoutEditText.ivRedo.setOnClickListener(v -> mainViewModel.setRedoTextSticker(true));
    }

    private void initFunction() {
        binding.tvAddText.setOnClickListener(v -> dialog.show());
        binding.tvEditFont.setOnClickListener(v -> {
            mainViewModel.typeText.setValue(Constant.TYPE_FONT_ADD_TEXT);
            mainViewModel.isClickFont.setValue(true);
        });
        binding.tvEditColor.setOnClickListener(v -> {
            mainViewModel.typeText.setValue(Constant.TYPE_COLOR_ADD_TEXT);
            mainViewModel.isClickColorText.setValue(true);

        });
        binding.tvEditBgColor.setOnClickListener(v -> {
            mainViewModel.typeText.setValue(Constant.TYPE_BG_COLOR_ADD_TEXT);
           mainViewModel.isClickColorBgText.setValue(true);
        });
    }

    private void observeView() {
        mainViewModel.isClickFont.observe(getViewLifecycleOwner(), data -> {
            if (data) {
                showFragment();
            }
            else {
                hideFragment();
            }
        });
        mainViewModel.isClickColorText.observe(getViewLifecycleOwner(), data -> {
            if (data) {
                showFragment();
            }
            else {
                hideFragment();
            }
        });
        mainViewModel.isClickColorBgText.observe(getViewLifecycleOwner(), data -> {
            if (data) {
                showFragment();
            }
            else{
                hideFragment();
            }
        });
    }

    private void confirmEditAddText() {
        binding.layoutEditText.ivCancel.setOnClickListener(v -> {
            mainViewModel.setIsClickItemText(false);
            mainViewModel.isCancelAddText.setValue(true);
        });
        binding.layoutEditText.ivDone.setOnClickListener(v -> mainViewModel.setIsClickItemText(false));
    }

    private void addFragment() {
        dialog = createDialogInputText();
        optionTextFragment = new OptionTextFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.csl_text, optionTextFragment).hide(optionTextFragment);
        ft.commit();
    }

    private void showFragment() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(optionTextFragment);
        ft.commit();
    }

    private void hideFragment() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(optionTextFragment);
        ft.commit();
    }
    private void enterText(EditText edt){
        if (edt.getText().toString().trim().length() == 0 || edt.getText() == null)
            Toast.makeText(requireContext(), requireContext().getString(R.string.alert_not_have_text), Toast.LENGTH_SHORT).show();
        else {
            mainViewModel.text.setValue(new MessageEvent(edt.getText().toString().trim()));
            edt.setText("");
            edt.setHint(requireContext().getString(R.string.content_add_text));
        }
        DensityUtils.showKeyboard(requireContext(), getView(), false);
        dialog.dismiss();
    }

    private Dialog createDialogInputText() {
        Dialog dialog = new Dialog(getContext(), R.style.blurDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_input_text);
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(p);
        AppCompatEditText edt = dialog.findViewById(R.id.edt_input_text);
        edt.requestFocus();
        dialog.findViewById(R.id.layout_edit_text).findViewById(R.id.iv_undo).setVisibility(View.GONE);
        dialog.findViewById(R.id.layout_edit_text).findViewById(R.id.iv_redo).setVisibility(View.GONE);

        //set view when show keyboard
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //event click button enter in keyboard
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    enterText(edt);
                }
                return false;
            }
        });

        //event cancel
        dialog.findViewById(R.id.layout_edit_text).findViewById(R.id.iv_cancel).setOnClickListener(v -> {
            DensityUtils.showKeyboard(getContext(), getView(), false);
            dialog.dismiss();
        });

        //event done
        dialog.findViewById(R.id.layout_edit_text).findViewById(R.id.iv_done).setOnClickListener(v -> {
            enterText(edt);
        });

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("TAG", "onStart Text: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("TAG", "onStop Text: ");

    }
}