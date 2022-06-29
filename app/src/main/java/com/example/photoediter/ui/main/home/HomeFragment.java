package com.example.photoediter.ui.main.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.photoediter.R;
import com.example.photoediter.common.Constant;
import com.example.photoediter.common.models.DataColor;
import com.example.photoediter.common.models.MessageEvent;
import com.example.photoediter.databinding.FragmentHomeBinding;
import com.example.photoediter.feature.DrawingView;
import com.example.photoediter.ui.base.BaseBindingFragment;
import com.example.photoediter.ui.main.MainActivity;
import com.example.photoediter.ui.main.MainViewModel;
import com.example.photoediter.ui.main.addtext.AddTextFragment;
import com.example.photoediter.ui.main.draw.DrawFragment;
import com.example.photoediter.ui.main.filter.FilterFragment;
import com.example.photoediter.ui.main.sticker.StickerFragment;
import com.example.photoediter.utils.BitmapUtils;
import com.example.photoediter.utils.DensityUtils;
import com.example.photoediter.utils.DialogUtils;
import com.example.photoediter.utils.FilterOption;
import com.example.photoediter.utils.HeightProvider;
import com.example.photoediter.utils.SaveUtils;
import com.filter.base.GPUImageFilter;
import com.xiaopo.flying.sticker.DialogDrawable;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.Sticker;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class HomeFragment extends BaseBindingFragment<FragmentHomeBinding, MainViewModel> {
    private Dialog dialog, dialogExit;
    private HeightProvider heightProvider;
    private String uri;
    private DrawingView drawingView;
    private GPUImageFilter gpuImageFilter;
    private Sticker currentSticker;
    private DialogDrawable dialogText;
    private Typeface typeface;
    private Typeface currentTypeface;
    private FilterFragment filterFragment;
    private DrawFragment drawFragment;
    private AddTextFragment addTextFragment;
    private StickerFragment stickerFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    private Boolean checkShowFilter;
    private Boolean checkShowDraw;
    private Boolean checkShowText;
    private Boolean checkShowSticker;
    private Boolean checkShowDrawColor;
    private Boolean checkShowDrawSize;
    private Boolean checkShowTextFont;
    private Boolean checkShowTextColor;
    private Boolean checkShowTextBg;
    private Boolean checkShowKeyBoard;
    private long mLastClickTime = 0;
    private int colorText = 0;
    private int colorBgText = 0;
    private final int filterFragmentIndex = 0;
    private final int drawFragmentIndex = 1;
    private final int textFragmentIndex = 2;
    private final int stickerFragmentIndex = 3;

    private int wR, hR;
    private int wRl = 0;
    private int hRl = 0;
    private int wTemp = 0;
    private int hTemp = 0;
    private static final String TAG = "HaiPd";


    @Override
    protected Class<MainViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onCreatedView Home: ");

        setupView();
        initKeyBoard();
        getImageFilter();
        addBubbleText();
        setFontText();
        setColorText();
        setBackgroundColorText();
        eventClick();
        insertSticker();
        setBrushColor();
        setBrushSize();
        undo();
        redo();
        delete();
        eventBack();
        save();
        observeEventView();
        if (savedInstanceState != null) {
            checkShowFilter = savedInstanceState.getBoolean(Constant.SHOW_FILTER);
            checkShowText = savedInstanceState.getBoolean(Constant.SHOW_TEXT);
            checkShowDraw = savedInstanceState.getBoolean(Constant.SHOW_DRAW);
            checkShowSticker = savedInstanceState.getBoolean(Constant.SHOW_STICKER);
            Log.d(TAG, "check savedInstanceState : " + checkShowFilter);

            mainViewModel.setIsClickItemFilter(checkShowFilter);
            mainViewModel.setIsClickItemText(checkShowText);
            mainViewModel.setIsClickItemDraw(checkShowDraw);
            mainViewModel.setIsClickItemSticker(checkShowSticker);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState Home: ");
        outState.putBoolean(Constant.SHOW_FILTER, checkShowFilter);
        outState.putBoolean(Constant.SHOW_TEXT, checkShowText);
        outState.putBoolean(Constant.SHOW_DRAW, checkShowDraw);
        outState.putBoolean(Constant.SHOW_STICKER, checkShowSticker);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart Home: ");

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView Home: ");
        mainViewModel.colorText.setValue(0);
        mainViewModel.dataColor.postValue(null);
        mainViewModel.typeFont.setValue(null);
        mainViewModel.dataBgColor.postValue(null);
        mainViewModel.brushSize.setValue(0);
        mainViewModel.colorBrush.setValue(new DataColor(null, -1));
        mainViewModel.customColorBrush.setValue(0);
        mainViewModel.setIsBack(false);
        if (gpuImageFilter != null) {
            binding.filter.setFilter(null);
        }
        heightProvider.dismiss();
        binding.draw.clearAll();
        uri = null;
        fragmentList.clear();
    }

    private void setUpCheckShow() {
        checkShowFilter = false;
        checkShowDraw = false;
        checkShowSticker = false;
        checkShowText = false;
        checkShowDrawColor = false;
        checkShowKeyBoard = false;
        checkShowTextFont = false;
        checkShowTextColor = false;
        checkShowTextBg = false;
    }

    @SuppressLint("LongLogTag")
    private void setupView() {
        setUpCheckShow();
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Window window = requireActivity().getWindow();
        window.getDecorView().setSystemUiVisibility(View.VISIBLE);
        filterFragment = FilterFragment.newInstance();
        drawFragment = DrawFragment.newInstance();
        addTextFragment = AddTextFragment.newInstance();
        stickerFragment = StickerFragment.newInstance();
        fragmentList.add(filterFragment);
        fragmentList.add(drawFragment);
        fragmentList.add(addTextFragment);
        fragmentList.add(stickerFragment);
        Log.d(TAG, "setupView Home: ");
        addFragment();
        assert getArguments() != null;
        uri = getArguments().getString(Constant.REQUEST_URI_FROM_MAINACTIVITY);
        if (uri != null) {
            Glide.with(requireContext()).asBitmap().load(uri).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    setSizeImage(resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                }
            });
        }
        dialogExit = createDialogExit();

    }

    private void insertSticker() {
        mainViewModel.sticker2.observe(this, new Observer() {
                    @Override
                    public void onChanged(Object o) {
                        if (o instanceof MessageEvent) {
                            if (((MessageEvent) o).getStringValue() != null) {
                                if (dialogText != null) {
                                    if (((DrawableSticker) currentSticker).getDrawable() instanceof DialogDrawable) {
                                        ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setInEdit(false);
                                    }
                                }
                                Glide.with(requireActivity()).asBitmap().load(((MessageEvent) o).getStringValue()).into(new CustomTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        binding.sticker.addSticker(resource);
                                        currentSticker = binding.sticker.getSticker();
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                    }
                                });
                            }
                        } else {
                            if (currentSticker != null) {
                                binding.sticker.deleteSticker();
                            }
                        }
                    }
                }
        );
    }

    private void getImageFilter() {
        mainViewModel.filter.observe(this, data -> {
            if (data instanceof GPUImageFilter) {
                binding.filter.setFilter((GPUImageFilter) data);
                this.gpuImageFilter = (GPUImageFilter) data;
            }
        });

    }

    private void showLayout() {
        binding.ctlConfirmEditTop.setVisibility(View.GONE);
        binding.fragmentPlaceEditPhoto.setVisibility(View.VISIBLE);
        binding.ctlSelectEditBottom.setVisibility(View.GONE);
    }

    private void hideLayout() {
        binding.fragmentPlaceEditPhoto.setVisibility(View.GONE);
        binding.ctlConfirmEditTop.setVisibility(View.VISIBLE);
        binding.ctlSelectEditBottom.setVisibility(View.VISIBLE);
    }

    private void observeEventView() {
        mainViewModel.isClickItemFilter.observe(getViewLifecycleOwner(), data -> {
            if (data) {
                Log.d(TAG, "observeEventView click filter true: ");
                showFragment(filterFragmentIndex);
                checkShowFilter = true;
                showLayout();
            } else {
                hideFragment(filterFragmentIndex);
                Log.d(TAG, "observeEventView click filter false: ");
                checkShowFilter = false;
                hideLayout();
            }
        });
        mainViewModel.isClickItemSticker.observe(getViewLifecycleOwner(), data -> {
            if (data) {
                showFragment(stickerFragmentIndex);
                checkShowSticker = true;
                binding.sticker.setLooked(false);
                showLayout();
            } else {
                hideFragment(stickerFragmentIndex);
                checkShowSticker = false;
                hideLayout();
            }
        });
        mainViewModel.isClickItemText.observe(getViewLifecycleOwner(), data -> {
            if (data) {
                showFragment(textFragmentIndex);
                checkShowText = true;
                binding.sticker.setLooked(false);
                mainViewModel.isClickFont.setValue(false);
                mainViewModel.isClickColorText.setValue(false);
                mainViewModel.isClickColorBgText.setValue(false);
                showLayout();
            } else {
                hideFragment(textFragmentIndex);
                checkShowText = false;
                hideLayout();
            }
        });
        mainViewModel.isClickItemDraw.observe(getViewLifecycleOwner(), data -> {
            if (data) {
                showFragment(drawFragmentIndex);
                checkShowDraw = true;
                binding.sticker.setLooked(true);
                binding.draw.setDraw(true);
                mainViewModel.isClickSizeDraw.setValue(false);
                mainViewModel.isClickColorDraw.setValue(false);
                showLayout();
            } else {
                hideFragment(drawFragmentIndex);
                checkShowDraw = false;
                binding.draw.setDraw(false);
                hideLayout();
            }
        });
        mainViewModel.isClickColorDraw.observe(this, data -> {
            checkShowDrawColor = data;
        });
        mainViewModel.isClickSizeDraw.observe(this, data -> {
            checkShowDrawSize = data;
        });
        mainViewModel.isClickColorText.observe(this, data -> {
            checkShowTextColor = data;
        });
        mainViewModel.isClickColorBgText.observe(this, data -> {
            checkShowTextBg = data;
        });
        mainViewModel.isClickFont.observe(this, data -> {
            checkShowTextFont = data;
        });

        mainViewModel.isBack.observe(getViewLifecycleOwner(), data -> {
            if (data) {
                Navigation.findNavController(binding.getRoot()).popBackStack(R.id.ucropFragment, false);
            }
        });
    }

    private void showEditText() {
        binding.cslEnterText.setVisibility(View.VISIBLE);
        binding.edtInputText.requestFocus();
        checkShowKeyBoard = true;
        binding.edtInputText.setText(((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).getTextDraw());
        binding.edtInputText.setSelection(binding.edtInputText.getText().length());
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        DensityUtils.showKeyboard(requireContext(), binding.edtInputText, true);
        if (checkCurrent()) {
            binding.ivInputDone.setOnClickListener(v -> {
                if (binding.edtInputText.getText() != null && binding.edtInputText.getText().toString().trim().length() != 0) {
                    ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setText(binding.edtInputText.getText().toString().trim());
                    updateText();
                    DensityUtils.showKeyboard(requireContext(), binding.getRoot(), false);
                    checkShowKeyBoard = false;
                    binding.cslEnterText.setVisibility(View.GONE);
                    mainViewModel.setIsClickItemText(true);
                } else {
                    Toast.makeText(requireContext(), requireContext().getString(R.string.not_to_blank_text), Toast.LENGTH_SHORT).show();
                    binding.edtInputText.setText(((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).getTextDraw());
                    binding.edtInputText.setSelection(binding.edtInputText.getText().length());
                }

            });
            binding.edtInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (binding.edtInputText.getText() != null && binding.edtInputText.getText().toString().trim().length() != 0) {
                        ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setText(binding.edtInputText.getText().toString().trim());
                        updateText();
                        DensityUtils.showKeyboard(requireContext(), binding.getRoot(), false);
                        checkShowKeyBoard = false;
                        binding.cslEnterText.setVisibility(View.GONE);
                        mainViewModel.setIsClickItemText(true);
                    } else {
                        Toast.makeText(requireContext(), requireContext().getString(R.string.not_to_blank_text), Toast.LENGTH_SHORT).show();
                        binding.edtInputText.setText(((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).getTextDraw());
                        binding.edtInputText.setSelection(binding.edtInputText.getText().length());
                    }
                    return false;
                }
            });
        }
    }

    private void eventClick() {
//edit text sticker on long click
//        binding.sticker.setOnStickerLongClickListener(sticker -> {
//            if (sticker != null) {
//                if (((DrawableSticker) currentSticker).getDrawable() instanceof DialogDrawable)
//                    ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setInEdit(false);
//                currentSticker = sticker;
//                if (((DrawableSticker) sticker).getDrawable() instanceof DialogDrawable) {
//                    ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setInEdit(true);
//                    showEditText();
//                }
//
//            }
//
//        });
        binding.sticker.setOnStickerClickListener(sticker -> {
            if (sticker != null) {
                if (((DrawableSticker) currentSticker).getDrawable() instanceof DialogDrawable)
                    ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setInEdit(false);
                currentSticker = sticker;
                if (((DrawableSticker) sticker).getDrawable() instanceof DialogDrawable) {
                    // tap in text sticker
                    ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setInEdit(true);
                    Log.d("TAG", "eventClick sticker text: ");
                    binding.sticker.bringToFront();
                    binding.sticker.requestLayout();
                    binding.sticker.invalidate();
                    binding.sticker.getParent().requestLayout();

                    /* edit text sticker on double tap*/
                    if (SystemClock.elapsedRealtime() - mLastClickTime < Constant.TIME_DELAY_500) {
                        showEditText();
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                } else {
                    //tap in sticker
                    Log.d("TAG", "eventClick sticker: ");

                }
            } else if (dialogText != null) {
                if (((DrawableSticker) currentSticker).getDrawable() instanceof DialogDrawable) {
                    ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setInEdit(false);
                    Log.d("TAG", "eventClick space sticker: ");
                    binding.sticker.invalidate();
//                    binding.rlDetail1.invalidate();

                }
            }
        });
        binding.tvFilter.setOnClickListener(v -> mainViewModel.setIsClickItemFilter(true));
        binding.tvSticker.setOnClickListener(v -> mainViewModel.setIsClickItemSticker(true));
        binding.tvText.setOnClickListener(v -> mainViewModel.setIsClickItemText(true));
        binding.tvDraw.setOnClickListener(v -> {
            drawingView = new DrawingView(getContext());
            mainViewModel.setIsClickItemDraw(true);
        });
    }

    private void setBrushSize() {
        mainViewModel.brushSize.observe(getViewLifecycleOwner(), data -> {
            if (data != 0) binding.draw.setSize(data);
            else binding.draw.setSize(10);
        });
    }

    private void setBrushColor() {
        mainViewModel.colorBrush.observe(getViewLifecycleOwner(), dataColor -> {
            if (dataColor.getColor() != null && dataColor.getPosition() != -1) {
                binding.draw.setColor(android.graphics.Color.parseColor(Constant.Color_promoted + dataColor.getColor()));
            } else binding.draw.setColor(Color.BLACK);
        });
        mainViewModel.customColorBrush.observe(getViewLifecycleOwner(), data -> {
            if (data != 0) {
                binding.draw.setColor(data);
            } else binding.draw.setColor(Color.BLACK);
        });
    }

    private void setBackgroundColorText() {
        mainViewModel.dataBgColor.observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                if (checkCurrent()) {
                    if (data.getColor() != null && data.getPosition() != -1) {
                        ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorBackground(android.graphics.Color.parseColor(Constant.Color_promoted + data.getColor()));
                    } else {
                        ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorBackground(Color.TRANSPARENT);
                        colorBgText = ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).getBackground();
                    }
                } else {
                    colorBgText = android.graphics.Color.parseColor(Constant.Color_promoted + data.getColor());
                }

            } else {
                if (checkCurrent()) {
                    ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorBackground(Color.TRANSPARENT);
                    colorBgText = Color.TRANSPARENT;
                }

            }
            updateText();
        });
        mainViewModel.bgColorText.observe(getViewLifecycleOwner(), data -> {
            if (checkCurrent()) {
                ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorBackground(data);
                colorBgText = ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).getBackground();
            } else colorBgText = Color.TRANSPARENT;
            updateText();

        });
    }

    private void setColorText() {
        mainViewModel.dataColor.observe(getViewLifecycleOwner(), dataColor -> {
            if (dataColor != null) {
                if (checkCurrent()) {
                    if (dataColor.getColor() != null && dataColor.getPosition() != -1) {
                        ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorText(android.graphics.Color.parseColor(Constant.Color_promoted + dataColor.getColor()));
                    } else {
                        ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorText(ContextCompat.getColor(requireContext(), R.color.black));
                    }
                    colorText = ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).getColor();
                } else {
                    colorText = android.graphics.Color.parseColor(Constant.Color_promoted + dataColor.getColor());
                }

            } else {
                if (checkCurrent()) {
                    ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorText(ContextCompat.getColor(requireContext(), R.color.black));
                    colorText = Color.BLACK;
                }

            }
            updateText();
        });
        mainViewModel.colorText.observe(getViewLifecycleOwner(), data -> {
            if (data != 0) {
                if (checkCurrent()) {
                    ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorText(data);
                    colorText = ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).getColor();
                } else {
                    colorText = data;
                }

            } else {
                colorText = Color.BLACK;
            }
            updateText();
        });
    }

    private void setFontText() {
        mainViewModel.typeFont.observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                typeface = Typeface.createFromAsset(requireActivity().getAssets(), data);
                if (checkCurrent()) {
                    ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setFontText(typeface);
                }
            } else {
                if (checkCurrent()) {
                    ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setFontText(currentTypeface);
                } else typeface = null;
            }
            updateText();
        });
        mainViewModel.positionFont.observe(getViewLifecycleOwner(), position -> {
            if (position != -1) {
                if (checkCurrent()) {
                }
                updateText();
            }
        });
    }

    private void addBubbleText() {
        mainViewModel.text.observe(this, data -> {
            if (data instanceof MessageEvent) {
                if (((MessageEvent) data).getStringValue() != null) {
                    if (dialogText != null) dialogText.setInEdit(false);
                    addBubble();
                    if (checkCurrent()) {
                        ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setText(((MessageEvent) data).getStringValue());
                        currentTypeface = ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).getFontText();
                        if (colorBgText != 0)
                            ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorBackground(colorBgText);
                        if (colorText != 0)
                            ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorText(colorText);
                        if (typeface != null)
                            ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setFontText(typeface);

                        updateText();
                    }
                }
            }
        });
    }

    private void updateText() {
        binding.sticker.invalidate();
    }

    private void undo() {
        undoDraw();
        undoSticker();
        undoText();
    }

    private void redo() {
        redoDraw();
        redoSticker();
        redoText();
    }

    private void undoText() {
        mainViewModel.undoTextSticker.observe(getViewLifecycleOwner(), data -> {
            if (data) {
                if (dialogText != null && dialogText.list.size() != 0) {
                    dialogText.undo();
                    updateText();
                    if (dialogText.list.size() == 0) {
                        binding.sticker.setInEdit(false);
                    }
                }
            }
        });

    }

    private void redoText() {
        mainViewModel.redoTextSticker.observe(getViewLifecycleOwner(), data -> {
            if (data) {
                if (dialogText != null && dialogText.redoList.size() != 0) {
                    dialogText.redo();
                    updateText();
                }
            }
        });
    }

    private void undoDraw() {
        mainViewModel.isUndoDraw.observe(getViewLifecycleOwner(), data -> {
            if (data) binding.draw.onClickUndo();
        });
    }

    private void redoDraw() {
        mainViewModel.isRedoDraw.observe(getViewLifecycleOwner(), data -> {
            if (data) binding.draw.onClickRedo();
        });
    }

    private void delete() {
        deleteDraw();
        deleteText();
    }

    private void deleteDraw() {
        mainViewModel.isDeleteDraw.observe(getViewLifecycleOwner(), data -> {
            if (data) binding.draw.clearAll();
        });
    }

    private void deleteText() {
        mainViewModel.isCancelAddText.observe(getViewLifecycleOwner(), data -> {
            if (data) {
                if (checkCurrent() && dialogText != null) {
                    dialogText.delete();
                    dialogText = null;
                    binding.sticker.deleteSticker();
                }
                updateText();
            }
        });
    }

    private void undoSticker() {
        mainViewModel.undoSticker.observe(getViewLifecycleOwner(), data -> {
            if (data) {
                binding.sticker.setInEdit(false);
                binding.sticker.undoSticker();
            }
        });
    }

    private void redoSticker() {
        mainViewModel.redoSticker.observe(getViewLifecycleOwner(), data -> {
            if (data) {
                binding.sticker.redoSticker();
            }
        });
    }

    private void addBubble() {
        dialogText = new DialogDrawable(requireContext());
        binding.sticker.addSticker(dialogText);
        currentSticker = new DrawableSticker(dialogText);
    }

    private boolean checkCurrent() {
        if (currentSticker != null) {
            if ((((DrawableSticker) currentSticker).getDrawable()) != null) {
                return ((DrawableSticker) currentSticker).getDrawable() instanceof DialogDrawable;
            }
        }
        return false;
    }

    private void eventBack() {
        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                if (!checkShowDraw & !checkShowFilter & !checkShowText & !checkShowSticker
                        & !checkShowDrawSize & !checkShowDrawColor
                        & !checkShowTextBg & !checkShowTextFont & !checkShowTextColor
                        & !checkShowKeyBoard) {
                    dialogExit.show();
                    return true;
                }
                if (checkShowFilter) {
                    mainViewModel.setIsClickItemFilter(false);
                    mainViewModel.filter.setValue(new GPUImageFilter());
                    mainViewModel.backFilter.setValue(true);
                    return true;
                }
                if (checkShowSticker) {
                    mainViewModel.setIsClickItemSticker(false);
                    mainViewModel.sticker2.postValue(null);
                    mainViewModel.backSticker.setValue(true);
                    return true;
                }
                if (checkShowText & !checkShowTextBg & !checkShowTextFont & !checkShowTextColor) {
                    mainViewModel.setIsClickItemText(false);
                    mainViewModel.isCancelAddText.setValue(true);
                    return true;
                }
                if (checkShowDraw & !checkShowDrawColor & !checkShowDrawSize) {
                    mainViewModel.setIsClickItemDraw(false);
                    mainViewModel.setIsDeleteDraw(true);
                    return true;
                }
                if (checkShowDrawColor) {
                    mainViewModel.setIsClickColorDraw(false);
                    mainViewModel.setColorBrush(new DataColor(null, -1));
                    mainViewModel.setCustomColorBrush(0);
                    mainViewModel.backColorBrush.setValue(true);
                    return true;
                }
                if (checkShowDrawSize) {
                    mainViewModel.setIsClickSizeDraw(false);
                    mainViewModel.setBrushSize(10);
                    mainViewModel.backSizeBrush.setValue(true);
                    return true;
                }
                if (checkShowTextFont) {
                    mainViewModel.isClickFont.setValue(false);
                    mainViewModel.typeFont.setValue(null);
                    mainViewModel.positionFont.setValue(-1);
                    mainViewModel.backFontText.setValue(true);
                    return true;
                }
                if (checkShowTextColor) {
                    mainViewModel.isClickColorText.setValue(false);
                    mainViewModel.setDataColor(null);
                    mainViewModel.backColorText.setValue(true);
                    return true;
                }
                if (checkShowTextBg) {
                    mainViewModel.isClickColorBgText.setValue(false);
                    mainViewModel.setDataBgColor(null);
                    mainViewModel.backColorBgText.setValue(true);
                    return true;
                }
                if (checkShowKeyBoard) {
                    DensityUtils.showKeyboard(requireContext(), binding.getRoot(), false);
                    return true;
                }

            }
            return false;
        });
        binding.ivBackEdit.setOnClickListener(v -> dialogExit.show());
    }


    private Dialog createDialogExit() {
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 20);
        Dialog dialog = new Dialog(getContext(), R.style.blurDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.gravity = Gravity.CENTER;
        p.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        p.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        dialog.getWindow().setBackgroundDrawable(inset);
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.btn_exit).setOnClickListener(v -> {
            mainViewModel.setIsBack(true);
            dialog.dismiss();
        });
        return dialog;
    }

    private void save() {
        binding.btnSaveEdit.setOnClickListener(v -> {
            binding.sticker.setInEdit(false);
            if (dialogText != null) dialogText.setInEdit(false);
            Bitmap bitmap = binding.filter.getGPUImage().captureBitmap();
            Bitmap bmSticker = binding.sticker.createBitmap();
            Bitmap bmDraw = binding.draw.createBitmap();
            mainViewModel.saveBitmapToLocal(bitmap, bmSticker, bmDraw, requireContext());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.shareFragment);
        });
    }

    private void addFragment() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_place_edit_photo, filterFragment);
        ft.add(R.id.fragment_place_edit_photo, drawFragment);
        ft.add(R.id.fragment_place_edit_photo, addTextFragment);
        ft.add(R.id.fragment_place_edit_photo, stickerFragment);
        ft.commit();
        Log.d(TAG, "addFragment: ");
    }

    private void showFragment(int fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        for (int i = 0; i < fragmentList.size(); i++) {
            if (i == fragment) ft.show(fragmentList.get(fragment));
            else ft.hide(fragmentList.get(i));
        }
        ft.commit();
    }

    private void hideFragment(int fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragmentList.get(fragment).isAdded()) {
            ft.hide(fragmentList.get(fragment));
            ft.commit();
        }
    }

    private int heightFinal;

    private void onTranslateKeyboard(int height) {
//        if (checkShowKeyBoard) {
        if (height > 0) {
            if (heightFinal == 0)
                heightFinal = height;
            if (binding.edtInputText.getTranslationY() <= 0) {
                Log.d(TAG, "onTranslateKeyboard: " + heightFinal);
                binding.cslEnterText.setTranslationY(-heightFinal);
                binding.cslEnterText.setVisibility(View.VISIBLE);
            }

        } else {
            binding.cslEnterText.setVisibility(View.GONE);
            Log.d("TAG", "height<0: " + height);
        }
//        }
    }

    private void initKeyBoard() {

        heightProvider = new HeightProvider(requireActivity()).init().setHeightListener(new HeightProvider.HeightListener() {
            @Override
            public void onHeightChanged(int height) {

                onTranslateKeyboard(height);
            }
        });
    }

    private void setParam(Bitmap mBitmap) {
        Bitmap bitmap = BitmapUtils.resizeBitmap(mBitmap, getContext());
        int hB = bitmap.getHeight();
        int wB = bitmap.getWidth();
        int s = wR * hB - wB * hR;
        if (s > 0) {
            wRl = hR * wB / hB;
            hRl = hR;
        } else {
            hRl = wR * hB / wB;
            wRl = wR;
        }
        wTemp = wRl;
        hTemp = hRl;
        binding.filter.setImage(bitmap);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(wTemp, hTemp);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        binding.rlDetail1.setLayoutParams(params);
        new Handler().postDelayed(() -> dialog.dismiss(), Constant.TIME_DELAY_1000);

    }

    private void setSizeImage(Bitmap mBitmap) {
        filterFragment.addFilter(mBitmap);
        if (dialog == null)
            dialog = DialogUtils.getDiaLogLoading(getContext());
        dialog.show();
        binding.rlDetail.post(() -> {
            wR = binding.rlDetail.getWidth();
            hR = binding.rlDetail.getHeight();
            wR = binding.imgTemp.getWidth();
            hR = binding.imgTemp.getHeight();
            setParam(mBitmap);
            wTemp = wR;
            hTemp = hR;
            binding.rlDetail1.setVisibility(View.VISIBLE);
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (gpuImageFilter != null) {
            binding.filter.setFilter(gpuImageFilter);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("TAG", "onStop Home: ");
//        setUpCheckShow();
        binding.cslEnterText.setVisibility(View.GONE);
        DensityUtils.showKeyboard(requireContext(), binding.getRoot(), false);

    }

    @Override
    protected void onPermissionGranted() {

    }

}