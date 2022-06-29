/**
 * Created by Matthew Stewart on 10/30/2017 10:46:59 AM
 */
package com.filter.helper;


import android.content.Context;

import com.filter.R;
import com.filter.advanced.JSNormalFilter;
import com.filter.advanced.JSToneCurved;
import com.filter.base.GPUImageFilter;


public class FilterManager {

    private static FilterManager instance;
    private static Context context;

    private FilterManager() {
    }

    public static void init(Context ct) {
        context = ct;
        instance = new FilterManager();
    }

    public static FilterManager getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }

    public MagicFilterType[] types = new MagicFilterType[]{
            MagicFilterType.NONE,
            MagicFilterType.AFTERGLOW,
            MagicFilterType.ALICE_IN_WONDERLAND,
            MagicFilterType.AMBERS,
            MagicFilterType.AUGUST_MARCH,
            MagicFilterType.AURORA,
            MagicFilterType.BABY_FACE,
            MagicFilterType.BLOOD_ORANGE,
            MagicFilterType.BLUE_POPPIES,
            MagicFilterType.BLUE_YELLOW_FIELD,
            MagicFilterType.CAROUSEL,
            MagicFilterType.COLD_DESERT,
            MagicFilterType.COLD_HEART,
            MagicFilterType.COUNTRY,
            MagicFilterType.DIGITAL_FILM
    };

    public GPUImageFilter getFilter(MagicFilterType type) {
        JSToneCurved jsToneCurved = new JSToneCurved();
        switch (type) {
            case AFTERGLOW:
                jsToneCurved.setCurveFile(R.raw.afterglow);
                return jsToneCurved;
            case ALICE_IN_WONDERLAND:
                jsToneCurved.setCurveFile(R.raw.alice_in_wonderland);
                return jsToneCurved;
            case AMBERS:
                jsToneCurved.setCurveFile(R.raw.ambers);
                return jsToneCurved;
            case AUGUST_MARCH:
                jsToneCurved.setCurveFile(R.raw.august_march);
                return jsToneCurved;
            case AURORA:
                jsToneCurved.setCurveFile(R.raw.aurora);
                return jsToneCurved;
            case BABY_FACE:
                jsToneCurved.setCurveFile(R.raw.baby_face);
                return jsToneCurved;
            case BLOOD_ORANGE:
                jsToneCurved.setCurveFile(R.raw.blood_orange);
                return jsToneCurved;
            case BLUE_POPPIES:
                jsToneCurved.setCurveFile(R.raw.blue_poppies);
                return jsToneCurved;
            case BLUE_YELLOW_FIELD:
                jsToneCurved.setCurveFile(R.raw.blue_yellow_field);
                return jsToneCurved;
            case CAROUSEL:
                jsToneCurved.setCurveFile(R.raw.carousel);
                return jsToneCurved;
            case COLD_DESERT:
                jsToneCurved.setCurveFile(R.raw.cold_desert);
                return jsToneCurved;
            case COLD_HEART:
                jsToneCurved.setCurveFile(R.raw.cold_heart);
                return jsToneCurved;
            case COUNTRY:
                jsToneCurved.setCurveFile(R.raw.country);
                return jsToneCurved;
            case DIGITAL_FILM:
                jsToneCurved.setCurveFile(R.raw.digital_film);
                return jsToneCurved;

            default:
                return new JSNormalFilter();
        }
    }

    public int getFilterName(MagicFilterType filterType) {
        switch (filterType) {
            case AFTERGLOW:
                return R.string.afterglow;
            case ALICE_IN_WONDERLAND:
                return R.string.alice_in_wonderland;
            case AMBERS:
                return R.string.ambers;
            case AUGUST_MARCH:
                return R.string.august_march;
            case AURORA:
                return R.string.aurora;
            case BABY_FACE:
                return R.string.baby_face;
            case BLOOD_ORANGE:
                return R.string.blood_orange;
            case BLUE_POPPIES:
                return R.string.blue_poppies;
            case BLUE_YELLOW_FIELD:
                return R.string.blue_yellow_field;
            case CAROUSEL:
                return R.string.carousel;
            case COLD_DESERT:
                return R.string.cold_desert;
            case COLD_HEART:
                return R.string.cold_heart;
            case COUNTRY:
                return R.string.country;
            case DIGITAL_FILM:
                return R.string.digital_film;
            default:
                return R.string.filter_normal;
        }
    }
}
